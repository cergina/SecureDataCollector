/*
 * ControllerUnit.c
 * Project: DCS
 * Version: 0.47
 * Controller: ATmega8 
 * Author: Bc. Tomas Zatka, Bc. Vladimir Bachan
 */ 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <avr/io.h>
#include <stdint.h>
#include <avr/sleep.h>

#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "lib/uart.h"
#include "lib/crc8.h"
#include "lib/atmega-adc.h"

#define F_CPU 16000000UL
//define F_CPU 7372800UL

#ifndef F_CPU
#error "F_CPU undefined, please define CPU frequency in Hz in Makefile"
#endif

#include "util/delay.h"	

#define DEBUG

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

#define LOW_DOWN 0
#define LOW_UP 164
#define HIGH_DOWN 410
#define HIGH_UP 1023

// Control flags for ADC5 (PCINT13)
uint16_t ADC5_lastValue;
volatile short int ADC5_readFlag = 0; // True/false

ISR(PCINT1_vect){
	ADC5_readFlag = 1;
}

uint8_t readDipAddress()
{
	// set input mode
	DDRD &= ~(1<<DDD2); //DIP1 LSB
	DDRD &= ~(1<<DDD3); //DIP2
	DDRD &= ~(1<<DDD4); //DIP3
	DDRD &= ~(1<<DDD5); //DIP4
	DDRD &= ~(1<<DDD6); //DIP5
	DDRD &= ~(1<<DDD7); //DIP6
	DDRB &= ~(1<<DDB0); //DIP7
	DDRB &= ~(1<<DDB1); //DIP8 MSB
	
	uint8_t address = PIND>>2;		//DIP1-6
	address |= (PINB & 0x03)<<6;	//DIP7-8
	address ^= 0xFF;				//to positive logic
	
	return address;
}

void SendMessageToCEU_uart(unsigned char UID, short input_NO, int value){
	char * message = (char* )calloc(CHUNK_LEN, sizeof(char));
	int mlen = 0; // 1B of stx + 2B of length
	
	//message[0] = 0; //STX
	//LENGTH (init)
	message[mlen] = 0;
	message[++mlen] = 0;
	
	//DATA PART (in ascii)	
	//Identifik�tor kontroleru
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

int main(void)
{	
	unsigned int c;
	char* message = NULL;
	unsigned short current_flag = NULL;
	unsigned int index = 0;
	char checksum;
	unsigned char UID = readDipAddress(); // only at startup!
	
	PCICR |= (1 << PCIE1);     // set PCIE1 to enable PCMSK1 scan
	PCMSK1 |= (1 << PCINT13);   // set PCINT13 to trigger an interrupt on state change

	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));
	sei();
	uart_puts("ControllerUnit  Build v0.47 \r\n");
	
	// #region DIP address print
	uart_puts("DIP address is: ");
	message = malloc(3);
	sprintf(message,"%02X\n",UID);
	uart_puts(message);
	free(message);
	// #endregion
	
	//Init value ADC5 - PCINT13
	ADC5_lastValue = adc_read(ADC_PRESCALER_128, ADC_VREF_AVCC, 5);
	#ifdef DEBUG
		char result[50];
		sprintf(result, "%d", ADC5_lastValue);
		uart_puts(result);
		uart_puts("\r\n");
	#endif
	ADC5_lastValue = 1; // We begin with logical 1 bcs on device its means idle state
	
	int tick= 0;
	
	while(1) {
		//Process code for ADC5 (PCINT13)
		if(ADC5_readFlag == 1){
			uint16_t adc_value = adc_read(ADC_PRESCALER_128, ADC_VREF_AVCC, 5);
			uart_puts("Measure\r\n");
			uint8_t bValue = adc_value >= HIGH_DOWN ? 1 : (adc_value <= LOW_UP ? 0 : -1);
			
			// RISE UP
			if (bValue != ADC5_lastValue && bValue == 1 ){
				#ifdef DEBUG
					uart_puts("Rising edge\r\n");
				#endif
				//ignore
			}
			
			if (bValue != ADC5_lastValue && bValue == 0 ){
				#ifdef DEBUG
					uart_puts("Falling edge\r\n");
				#endif
				//We send 1 as measured value because for now its trigger every time
				SendMessageToCEU_uart(UID,1,1); // for test purpose
			}
			
			ADC5_lastValue = bValue;
			ADC5_readFlag = 0;
		}
		
		#ifdef DEBUG
			char result1[50];
			sprintf(result1, "%d", ++tick);
			uart_puts(result1);
			uart_puts("\r\n");
		#endif
		_delay_ms(1000);
		/*
		c = uart_getc();
		if (c & UART_NO_DATA){
			continue;
		}
		else{		
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
					//uart_putc(c);
					//uart_putc(checksum);
				}
				else{
					uart_puts("ACK");
					//do stuff
				}
				current_flag = NULL;
				free(message);
			}
		}*/
	}
}
		


