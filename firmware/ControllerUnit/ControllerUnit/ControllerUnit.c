/*
 * ControllerUnit.c
 * Project: DCS
 * Version: 0.2
 * Controller: ATmega8 
 * Author: Bc. Tomas Zátka
 */ 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <avr/io.h>
#include <avr/sleep.h>

#include <util/delay.h>

#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "lib/uart.h"
#include "lib/crc8.h"

#define F_CPU 16000000UL
//define F_CPU 7372800UL

#ifndef F_CPU
#error "F_CPU undefined, please define CPU frequency in Hz in Makefile"
#endif

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

// Function to convert integer to character array
char* convertIntegerToChar(int N)
{
	int m = N;
	int digit = 0;
	while (m) {
		digit++;
		m /= 10;
	}
	
	char* arr;
	char arr1[digit];
	arr = (char*)malloc(digit);
	int index = 0;
	
	while (N) {
		arr1[++index] = N % 10 + '0';
		N /= 10;
	}
	
	int i;
	for (i = 0; i < index; i++) {
		arr[i] = arr1[index - i];
	}
	arr[i] = '\0';
	return (char*)arr;
}

void SendMessageToCEU_uart(unsigned char UID, short input_NO, int value){
	char * message = (char* )calloc(CHUNK_LEN, sizeof(char));
	int mlen = 0; // 1B of stx + 2B of length
	
	//message[0] = 0; //STX
	//LENGTH (init)
	message[mlen] = 0;
	message[++mlen] = 0;
	
	//DATA PART (in ascii)	
	//Identifikátor kontroleru
	char temp[4];
	sprintf(temp,"%x",UID);
	message[++mlen] = temp[0];
	message[++mlen] = temp[1];
	//Input
	if(input_NO){
		message[++mlen] = 0x49; // I - input
		message[++mlen] = input_NO  + '0'; // We want charcode
	}
	//Data message (M only, ...)
	///Type
	message[++mlen] = 0x4D; // M - Measument
	///data
	char *cDataArr = calloc(20, sizeof(char));
	sprintf(cDataArr,"%d",value);
	
	for(int i = 0; i < strlen(cDataArr); i++){
		//Message length check
		if(mlen == CHUNK_LEN){
			message = realloc(message, mlen+CHUNK_LEN * sizeof(message));
		}
		message[++mlen] = cDataArr[i];
	}
	free(cDataArr);
	
	//Add length
	int packetLength = mlen - 1;
	message[0] = packetLength & 0xFF;
	message[1] = packetLength >> 8;	
	
	//Checksum
	int crc = crc8((uint8_t*)message, mlen+1);
	
	//Send message
	uart_putc(STX);
	for (int i = 0; i <= mlen; i++)
		uart_putc(message[i]);
	uart_putc(ETX);
	uart_putc(crc);
	
	free(message);
} 

/*
void SendMessageToCEU_zwave(int mData, char controller_ID, short input_NO){
	
}*/

#define TRIGPOINT 164

unsigned int ReadAnalog(unsigned char chnl){
	chnl = chnl & 0b00000111; // select adc channel between 0 to 7
	ADMUX = 0x40;        //channel A0 selected
	ADCSRA|=(1<<ADSC);   // start conversion
	while(!(ADCSRA & (1<<ADIF)));   // wait for ADIF conversion complete return
	ADCSRA|=(1<<ADIF);   // clear ADIF when conversion complete by writing 1
	return (ADC); //return calculated ADC value
}

/*
ISR (INT0_vect)          //External interrupt_zero ISR
{
	cnt_zero++;
}
*/

int main(void)
{	
	unsigned int c;
	char* message = NULL;
	unsigned short current_flag = NULL;
	unsigned int index = 0;
	
	unsigned char UID = 0xAA; // for test purposes
	
	//ADMUX=(1<<REFS0);      // Selecting internal reference voltage
	//ADCSRA=(1<<ADEN)|(1<<ADPS2)|(1<<ADPS1)|(1<<ADPS0);     // Enable ADC also set Prescaler as 128
	
	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));
	sei();
	uart_puts("ControllerUnit  Build v0.2 \r\n");
	
	//Debug
	_delay_ms(5000); 
	SendMessageToCEU_uart(UID,1,10); // for test purpose
	
	int analogValue; 
	
	char checksum;
	while(1){
		c = uart_getc();
		if (c & UART_NO_DATA){
			continue;
		}
	
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
		
			checksum = crc8((uint8_t*)message, len);
			if(c != checksum){
				uart_puts("Wrong Checksum \r\n");
				uart_putc(c);
				uart_putc(checksum);
			}
			else{
				uart_puts("ACK");
				//do stuff
				//
				//SendMessageToCEU_uart(UID,1,10); // for test purpose
			}
			current_flag = NULL;
			free(message);
		}
    }
}

