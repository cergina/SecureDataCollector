/*
 * CentralUnit.c
 * Project: DCS
 * Version: 0.8
 * Controller: ATmega128
 * Author: Bc. Tomas Zatka, Bc. Vladimir Bachan
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <avr/io.h>
#include <avr/sleep.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "lib/uart.h"
#include "lib/crc8.h"
#include "lib/eeprom_circular_buffer.h"
#include "lib/jsmn.h"

#define F_CPU 16000000UL

#ifndef F_CPU
#error "F_CPU undefined, please define CPU frequency in Hz in Makefile"
#endif

#include "util/delay.h"

//#define DEBUG_TEST
//#define DEBUG_TEST_TICK // careful with this debug mode, dont send messages on uart during this debug ticking, device dont work properly


#define UART_BAUD_RATE 9600

#define STX 0x02
#define ETX 0x03
#define FS 0x1C
#define ACK 0x06
#define NAK 0x15

#define F_STX 0
#define F_ID 1
#define F_LENGTH 2
#define F_DATA 3
#define F_ETX 4
#define F_CRC 5

#define CHUNK_LEN 50

#define PROTO_CCE 1
#define PROTO_CEQ 2

// Busy flag (this flag indicate that program is busy and we cant go to sleep mode)
uint8_t UART_Busy = 1;

#define QS_UNKNOWN		0	// unknown						
#define QS_READY		1	// RDY module ready
#define QS_CFUN			2	// +CFUN: 1 (full functionality)
#define QS_CPIN			3	// +CPIN: READY (pin OK)
#define QS_IDLE			4	// idle
#define QS_APSET		5	// setting AP
#define QS_REGISTERED	6	// registering APP
#define QS_ACTIVATED	7	// activating
#define QS_CTXSET		8	// setting context
#define QS_URLSET		9	// setting URL
#define QS_POSTED		10	// POSTing
#define QS_READING		11	// reading response 
#define QS_DEACTIVATING	12  // deactivating -> going to IDLE
uint8_t qState = QS_UNKNOWN;
uint8_t qOpPending = 0;
char *measurementToSend = NULL;

// #region string tools
void uart_putn(){
	uart_putc('\n');
}

void uart_println(char *msg){
	uart_puts(msg);
	uart_putn();
}

void uart1_putn(){
	uart1_putn();
}

void uart1_println(char *msg){
	uart1_puts(msg);
	uart1_putn();
}

void qSendForce(char *cmd){
	uart1_puts(cmd);
	uart1_putc('\n');
}
// #endregion

void qStateSet(uint8_t state){
	
	#ifdef DEBUG_TEST
	switch(state){
		case QS_UNKNOWN:{
			uart_println("Q => QS_UNKNOWN");
			break;
		}
		case QS_READY:{
			uart_println("Q => QS_READY");
			break;
		}
		case QS_CFUN:{
			uart_println("Q => QS_CFUN");
			break;
		}
		case QS_CPIN:{
			uart_println("Q => QS_CPIN");
			break;
		}
		case QS_IDLE:{
			uart_println("Q => QS_IDLE");
			break;
		}
		case QS_APSET:{
			uart_println("Q => QS_APSET");
			break;
		}
		case QS_REGISTERED:{
			uart_println("Q => QS_REGISTERED");
			break;
		}
		case QS_ACTIVATED:{
			uart_println("Q => QS_ACTIVATED");
			break;
		}
		case QS_CTXSET:{
			uart_println("Q => QS_CTXSET");
			break;
		}
		case QS_URLSET:{
			uart_println("Q => QS_URLSET");
			break;
		}
		case QS_POSTED:{
			uart_println("Q => QS_POSTED");
			break;
		}
		case QS_READING:{
			uart_println("Q => QS_READING");
			break;
		}
		case QS_DEACTIVATING:{
			uart_println("Q => QS_DEACTIVATING");
			break;
		}
	}
	#endif
	qState = state;
}


uint8_t qSend(char *cmd){
	if (qOpPending){
		uart_puts("AT failed\n");
		return 0;
	}
	
	qOpPending = 1;
	qSendForce(cmd);
	return 1;
}

void qConnect(){
	
	#ifdef DEBUG_TEST
	uart_puts("Q :: Connecting\n");
	#endif
	qSend("AT+QICSGP=1,\"internet\"");
}

void qDisconnect(){
	
	#ifdef DEBUG_TEST
	uart_puts("Q :: Disconnecting\n");
	#endif
	qStateSet(QS_DEACTIVATING);
	qSendForce("AT+QIDEACT");
}

void qMeasurementSend(char *m){
	
	#ifdef DEBUG_TEST
	uart_puts("Q :: Preparation\n");
	#endif
	
	uint8_t _len = strlen(m);
	char *_cmd = (char*)malloc(255*sizeof(char));
	sprintf(_cmd,"AT+QHTTPPOST=%d,60,60", _len);
	
	// Testy
	//uart_println(m);
	//uart_println(_cmd);
	
	_delay_ms(2000);
	qStateSet(QS_URLSET);
	qSendForce("AT+QHTTPURL=64,30");
	_delay_ms(1000);
	
	qSendForce("http://team14-20.studenti.fiit.stuba.sk/dcs/api/measurements-add");
	_delay_ms(5000);
	
	
	#ifdef DEBUG_TEST
	uart_puts("Q :: Posting\n");
	#endif
	qStateSet(QS_POSTED);
	qSendForce(_cmd);
	_delay_ms(2000);
	
	qSendForce(m);
	_delay_ms(3000);
	
	qSendForce("AT+QHTTPREAD=30");
	free(_cmd);
	free(m);
	measurementToSend = NULL;
	qStateSet(QS_ACTIVATED);
}

void qMeasurementCompose(int ceAddress, unsigned int reqNo, int cuAddress, int input, int measurement){
	measurementToSend = (char*) malloc(255 * sizeof(char));
	sprintf(measurementToSend, "{\"messageType\":\"measurements\",\"centralUnit\":\"%d\",\"requestNumber\":%d,\"controllers\":[{\"controllerUnit\":\"%d\",\"measurements\":[{\"sensorIO\":\"%d\",\"count\":%d}]}]}", ceAddress, reqNo, cuAddress, input, measurement);
	
	#ifdef DEBUG_TEST
	uart_println("Measurement preparation:");
	uart_println(measurementToSend);
	#endif
}

uint8_t ProcessQMessage(char *msg)
{
	if (0 == strcmp("RDY", msg))
	{
		qStateSet(QS_READY);
		return 1;
	}

	if (qState == QS_READY && 0 == strcmp("+CFUN: 1", msg))
	{
		qStateSet(QS_CPIN);
		return 1;
	}
	
	if (qState == QS_CPIN && 0 == strcmp("+CPIN: READY", msg))
	{
		qStateSet(QS_CFUN);
		return 1;
	}
	
	if (qState == QS_CFUN && 0 == strcmp("Call Ready", msg))
	{
		qStateSet(QS_IDLE);
		return 1;
	}
	
	if (qState == QS_CFUN && 0 == strcmp("Call Ready", msg))
	{
		qStateSet(QS_IDLE);
		return 1;
	}
	
	if (qOpPending && 0 == strcmp("CONNECT", msg)){
		
		#ifdef DEBUG_TEST
		uart_puts("Q :: GENERIC CONNECT\n");
		#endif
	}
	
	// react to response
	if (qOpPending && 0 == strcmp("OK", msg)){
		
		#ifdef DEBUG_TEST
		uart_puts("Q::OK\n");
		#endif
		qOpPending = 0; // disable pending Q state
		
		switch (qState) {
			case QS_IDLE: {
				qStateSet(QS_APSET);	
				qSend("AT+QIREGAPP");
				break;
			}
			case QS_APSET: {
				qStateSet(QS_REGISTERED);
				_delay_ms(2000); // QUECTEL important
				qSend("AT+QIACT");
				break;
			}
			case QS_REGISTERED: {
				qStateSet(QS_ACTIVATED);	
				qSend("AT+QIFGCNT=1");
				break;
			}
			default: {				
				#ifdef DEBUG_TEST
				uart_puts("Q :: GENERIC OK\n");
				#endif
			}
			
			// post reakcia
		}
		return 1;
	}
	
	if (qState == QS_DEACTIVATING && 0 == strcmp("DEACT OK", msg))
	{
		qStateSet(QS_IDLE);
		return 1;
	}
}

uint8_t readDipAddress()
{
	// set input mode (port C)			ATmega128	ATmega2560
	DDRC &= ~(1<<DDC0); //DIP1 LSB		phy_35		phy_53 ard_37
	DDRC &= ~(1<<DDC1); //DIP2			phy_36		phy_54 ard_36
	DDRC &= ~(1<<DDC2); //DIP3			phy_37		phy_55 ard_35
	DDRC &= ~(1<<DDC3); //DIP4			phy_38		phy_56 ard_34
	DDRC &= ~(1<<DDC4); //DIP5			phy_39		phy_57 ard_33
	DDRC &= ~(1<<DDC5); //DIP6			phy_40		phy_58 ard_32
	DDRC &= ~(1<<DDC6); //DIP7			phy_41		phy_59 ard_31
	DDRC &= ~(1<<DDC7); //DIP8 MSB		phy_42		phy_60 ard_30	
	
	uint8_t address = PINC;	//DIP1-8 
	address ^= 0xFF;		//to positive logic	
	return address;
}

typedef struct {
	char Address[3];
	char Input[2];
	char Time[13];
	char measurement[50];
} Measurement;

void AddDataToStruct(Measurement *data, char type, char *value){
	switch((char)type){
		case 'T':
		strcpy(data->Time, value);
		break;
		case 'I':
		strcpy(data->Input, value);
		break;
		case 'M':
		strcpy(data->measurement, value);
		break;
	}
}

//Parse Datova cast paketu a vytiahne z nej data pre spracovanie
Measurement *ParsePacket(char *message)
{	
	int len = message[1] << 8 | message[0];
	unsigned char parsedMessage[len];
	Measurement *data;
	data = (Measurement *)malloc(sizeof(Measurement));
	
	#ifdef DEBUG_TEST
		uart_puts("Arrived String:\r\n");
		for (int j = 0; j < len+2; j++)
			uart_putc(message[j]);
		uart_puts("\r\n");
	#endif
		
	for (int i = 0; i < len; i++)
	{	
		parsedMessage[i] = message[2+i];
	}
	
	#ifdef DEBUG_TEST
		uart_puts("Parsed String:\r\n");
		for (int j = 0; j < len; j++)
			uart_putc(parsedMessage[j]);
		uart_puts("\r\n");
	#endif
	
	memcpy(data->Address, parsedMessage, 2);
	
	char type = parsedMessage[2];
	int j = 0;
	char *value;
	value = (char *)calloc(CHUNK_LEN, sizeof(char));
	
	for (int i = 3; i < len; i++)
	{	
		if(parsedMessage[i] == FS){
			j=0;
			AddDataToStruct(data,type,value);
			free(value);
			value = (char *)calloc(CHUNK_LEN, sizeof(char));
			type= parsedMessage[i+1];
			i++;
			continue;
		}
		//if(j % CHUNK_LEN){
		//	int len = sizeof message / sizeof *message;
		//	message = realloc(message, len+CHUNK_LEN * sizeof(message));
		//}
		value[j] = parsedMessage[i];
		j++;
	}
	AddDataToStruct(data,type,value);
	free(value);
		
	//#ifdef DEBUG_TEST
	//	uart_puts("\r\n");
	//	uart_puts("Address:\r\n");
	//	uart_puts(data->Address);
	//	uart_puts("\r\n");
	//	uart_puts("Input:\r\n");
	//	uart_puts(data->Input);
	//	uart_puts("\r\n");
	//	uart_puts("measurement:\r\n");
	//	uart_puts(data->measurement);
	//	uart_puts("\r\n");
	//	uart_puts("Time:\r\n");
	//	uart_puts(data->Time);
	//	uart_puts("\r\n");
	//#endif
	
	//free(parsedMessage);
	return data;
}




int main(void)
{
	unsigned int c, c0;
	char *message = NULL;
	unsigned short current_flag = NULL;
	uint8_t current_proto = PROTO_CEQ;
	unsigned int index = 0;
	unsigned char UID = readDipAddress(); // only at startup!
	unsigned int reqNo = 0;

	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));
	uart1_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));

	//now enable interrupt, since UART library is interrupt controlled
	sei();

	#ifdef DEBUG_TEST
	uart_puts("CentralUnit  Build v0.8 \r\n");
	
	// #region DIP address print
	uart_puts("DIP address is: ");
	message = malloc(3);
	sprintf(message,"%02X\n",UID);
	uart_puts(message);
	free(message);
	// #endregion
	
	uart_puts("Current proto: ");
	uart_putc(current_proto + '0');
	uart_putn();
	#endif
	
	#ifdef DEBUG_TEST_TICK
		unsigned int tick= 0;
	#endif
	
	uint8_t checksum;
	while (1)
	{
		c0 = uart_getc();
		c = uart1_getc();
		
		if (!(c0 & UART_NO_DATA)){
			
			// generate test measurement
			if (c0 == '@'){
				if (measurementToSend == NULL){
					qMeasurementCompose(UID, ++reqNo, 1, 2, 1);
					//uart_putc(qOpPending);
					continue;
				}
			}
		}
		
		if (!(c & UART_NO_DATA)){
			//uart_putc(c);
			
			//#region STXETX 
			if (c == STX){
				current_flag = F_DATA;
				index = 0;
				message = (char* )calloc(CHUNK_LEN, sizeof(char));
				continue;
			}
			
			if (c == ETX){
				current_flag = F_CRC;
				continue;
			}
			
			if (current_flag == F_DATA)
			{
				message[index] = (char)c;
				index++;
				// extend message buffer if needed
				if (index % CHUNK_LEN == 0)
				{
					message = realloc(message, index + CHUNK_LEN * sizeof(message));
				}
				//if(index == CHUNK_LEN){
				//	int len = sizeof message / sizeof *message;
				//	message = realloc(message, len+CHUNK_LEN * sizeof(message));
				//}
			}
			
			if (current_flag == F_CRC) {
				int len = message[1] << 8 |  message[0];
				
				//#ifdef DEBUG_TEST
				//	for (int i = 0; i < len+2; i++)
				//		uart_putc(message[i]);
				//#endif 
				
				checksum = crc8((uint8_t*)message, len+2); // Need to add 2 what is count bytes of length.
				if((uint8_t)c != checksum){
					#ifdef DEBUG_TEST
					uart_puts("NAK\r\n");
					#endif
				}
				else{
					
					#ifdef DEBUG_TEST
					uart_puts("ACK\r\n");
					#endif
					//do stuff
					Measurement *data = ParsePacket(message);
					#ifdef DEBUG_TEST
						uart_puts("\r\n");
						uart_puts("Address:\r\n");
						uart_puts(data->Address);
						uart_puts("\r\n");
						uart_puts("Input:\r\n");
						uart_puts(data->Input);
						uart_puts("\r\n");
						uart_puts("measurement:\r\n");
						uart_puts(data->measurement);
						uart_puts("\r\n");
						uart_puts("Time:\r\n");
						uart_puts(data->Time);
						uart_puts("\r\n");
					#endif
					free(data);
				}
				current_flag = NULL;
				free(message);
			}
			//#endregion
		
			//#region Quectel
			if (current_proto == PROTO_CEQ)
			{
				// irelevantne
				if (c == 0x00 || c == 0x0D)
				{
					continue;
				}

				//uart_putc(c);
				//continue;

				// message end
				if (c == 0x0A)
				{
					uint8_t ret = ProcessQMessage(message);
					
					#ifdef DEBUG_TEST
					if (ret == 0){		
						uart_println(message);
					}
					#endif
					
					// clear buffer
					free(message);
					index = 0;
					message = (char *)calloc(CHUNK_LEN, sizeof(char));
					continue;
				}
				
				// store to buffer
				message[index] = (char)c;
				index++;

				// extend message buffer if needed
				if (index % CHUNK_LEN == 0)
				{
					message = realloc(message, index + CHUNK_LEN * sizeof(message));
				}
			}	
			//#endregion
			
			// remove flags
			c = c & 0xFF;
			c0 = c0 & 0xFF;
		}	
		
		if (current_proto == PROTO_CEQ && qOpPending == 0 && qState == QS_IDLE && measurementToSend != NULL){
			qConnect();
			continue;
		}
		
		if (current_proto == PROTO_CEQ && qOpPending == 0 && qState == QS_ACTIVATED && measurementToSend != NULL){
			qMeasurementSend(measurementToSend);
			continue;
		}
		
		continue; // bez deaktivácie
		if (current_proto == PROTO_CEQ && qOpPending == 0 && qState == QS_ACTIVATED && measurementToSend == NULL){
			qDisconnect();
			continue;
		}
		
		#ifdef DEBUG_TEST_TICK
			char result1[50];
			sprintf(result1, "%d", ++tick);
			uart_println(result1);
		#endif
	}
}
