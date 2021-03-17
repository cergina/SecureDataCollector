/*
 * ControllerUnit.c
 * Project: DCS
 * Version: 0.1
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

int main(void)
{
	unsigned int c;
	char* message = NULL;
	unsigned short current_flag = NULL;
	unsigned int index = 0;
	
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
		
			int y = len;
			/* for debug
			index = 2;
			for (int i = 2; i <= len; i++)
			uart_putc(message[i]);
			*/
			
			checksum = 0;
			index = 0;
			y = len + 2;
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
			}
			current_flag = NULL;
		}
    }	
}


void SendMessageToCEU_uart(int mData, char controller_ID, short input_NO){
	char * message;
	
	message = (char* )calloc(CHUNK_LEN, sizeof(char));
	
	message[0] = STX;
	
	//length
	message[1]
	message[2]
	
	//Data part (in ascii)
	
	//CE ID
	message[3]
	message[4]
	//input
	
	//data
	
	
	// crc
	int length // message length
	
	/* index = 0;
	while(y-- > 0){
		checksum ^= message[index++];
	}
	*/
	int crc = crc8(message, length);
} 


void SendMessageToCEU_zwave(int mData, char controller_ID, short input_NO){
	
}