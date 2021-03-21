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

#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "lib/uart.h"
#include "lib/crc8.h"

#define F_CPU 16000000UL

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
	
	message[0] = STX;
	//LENGTH (init)
	message[++mlen] = 0;
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
	int cDatalength= strlen(cDataArr);
	
	for(int i = 0; i < strlen(cDataArr); i++){
		//Message length check
		if(mlen == CHUNK_LEN){
			message = realloc(message, mlen+CHUNK_LEN * sizeof(message));
		}
		message[++mlen] = cDataArr[i];
	}
	free(cDataArr);
	
	//Add length
	int packetLength = mlen - 2;
	message[1] = packetLength & 0xFF;
	message[2] = packetLength >> 8;	
	
	//End of message
	message[++mlen] = ETX;
	
	//Checksum
	int crc = crc8((uint8_t*)message, mlen);
	message[++mlen] = crc;
	message[++mlen] = '\0';
	
	//debug
	for (int i = 0; i < mlen; i++)
		uart_putc(message[i]);
	
	free(message);
} 

/*
void SendMessageToCEU_zwave(int mData, char controller_ID, short input_NO){
	
}*/

int main(void)
{	
	unsigned int c;
	char* message = NULL;
	unsigned short current_flag = NULL;
	unsigned int index = 0;
	
	unsigned char UID = 0xFF; // for test purposes
	
	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));

	sei();
	
	uart_puts("ControllerUnit  Build v0.1 \r\n");
	
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
		
			int y = len + 2;
			checksum = 0;
			index = 0;
			while(y-- > 0){
				checksum ^= message[index++];
			}
		
			if(c != checksum){
				uart_puts("Wrong Checksum \r\n");
				uart_putc(c);
				uart_putc(checksum);
			}
			else{
				uart_puts("ACK");
				//do stuff
				SendMessageToCEU_uart(UID,1,0); // for test purpose
			}
			current_flag = NULL;
			free(message);
		}
    }	
}

