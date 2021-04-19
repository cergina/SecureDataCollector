/*
 * CentralUnit.c
 * Project: DCS
 * Version: 0.3
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

#define DEBUG_TEST
#define DEBUG_TEST_TICK // careful with this debug mode, dont send messages on uart during this debug ticking, device dont work properly


#define UART_BAUD_RATE 9600

#define STX 0x02
#define ETX 0x03
#define FS 0x34
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

uint8_t QUEC_STATE = 0;
/*
 * 0 - unknown
 * 1 - RDY module ready
 * 2 - +CFUN: 1 (full functionality)
 * 3 - +CPIN: READY (pin OK)
 * 4 - idle
 * 5 - setting AP
 * 6 - registering APP
 * 7 - activating
 * 8 - setting context
 * 9 - setting URL
 * 10 - POSTing
 * 11 - reading response 
 * 12 - deactivating
 */

uint8_t ProcessQMessage(char *msg)
{
	if (QUEC_STATE == 0 && 0 == strcmp("RDY", msg))
	{
		QUEC_STATE = 1;
		uart_puts("Q -> RDY\n");
		return 1;
	}

	if (QUEC_STATE == 1 && 0 == strcmp("+CFUN: 1", msg))
	{
		QUEC_STATE = 2;
		uart_puts("Q -> FULL FUNC\n");
		return 1;
	}
	
	if (QUEC_STATE == 2 && 0 == strcmp("+CPIN: READY", msg))
	{
		QUEC_STATE = 3;
		uart_puts("Q -> PIN OK\n");
		return 1;
	}
	
	if (QUEC_STATE == 3 && 0 == strcmp("Call Ready", msg))
	{
		QUEC_STATE = 4;
		uart_puts("Q -> IDLE\n");
		return 1;
	}
	
	return 0;
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

//Parse Datov� ?as? paketu a vytiahne z nej d�ta pre spracovanie
void ParsePacket(char *message)
{
	int len = message[1] << 8 | message[0];
	for (int i = 2; i < len + 2; i++)
	{
		uart_putc(message[i]);
	}
}

int main(void)
{
	unsigned int c;
	char *message = NULL;
	unsigned short current_flag = NULL;
	uint8_t current_proto = PROTO_CEQ;
	unsigned int index = 0;
	unsigned char UID = readDipAddress(); // only at startup!

	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));
	uart1_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));

	//now enable interrupt, since UART library is interrupt controlled
	sei();

	uart_puts("CentralUnit  Build v0.7 \r\n");
	
	// #region DIP address print
	uart_puts("DIP address is: ");
	message = malloc(3);
	sprintf(message,"%02X\n",UID);
	uart_puts(message);
	free(message);
	// #endregion
	
	uart_putc(current_proto);
	uart_putc('\n');
	
	#ifdef DEBUG_TEST_TICK
		unsigned int tick= 0;
	#endif
	
	uint8_t checksum;
	while (1)
	{
		c = uart1_getc();
		if (!(c & UART_NO_DATA)){
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
				if(index == CHUNK_LEN){
					int len = sizeof message / sizeof *message;
					message = realloc(message, len+CHUNK_LEN * sizeof(message));
				}
			}
			
			if (current_flag == F_CRC) {
				int len = message[1] << 8 |  message[0];
				
				#ifdef DEBUG_TEST
					for (int i = 0; i < len+2; i++)
						uart_putc(message[i]);
				#endif 
				
				checksum = crc8((uint8_t*)message, len+2); // Need to add 2 what is count bytes of length.
				if((uint8_t)c != checksum){
					uart_puts("NAK\r\n");
				}
				else{
					uart_puts("ACK\r\n");
					//do stuff
				}
				current_flag = NULL;
				free(message);
			}
			continue;
		}
		
		// remove flags
		c = c & 0xFF;
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
				if (ret == 0){
					uart_puts(message);
					uart_putc('\n');
				}
				
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
			continue;			
		}
		
		#ifdef DEBUG_TEST_TICK
			char result1[50];
			sprintf(result1, "%d", ++tick);
			uart_puts(result1);
			uart_puts("\r\n");
			_delay_ms(1000);
		#endif
	}
}


/** EXPERIMENTAL
int uart_TX_busy(){
	int check = strlen(UART_TxBuf);
	if(check == 1){
		return 1;
	}
	else{
		return 0;
	}
}

int uart_RX_busy(){
	int check = strlen(UART_RxBuf);
	if(check == 1){
		return 1;
	}
	else{
		return 0;
	}
}
*/