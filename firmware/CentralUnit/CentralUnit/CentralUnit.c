/*
 * CentralUnit.cpp
 *
 * Created: 26. 2. 2021 18:55:53
 * Author: Tomas
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

#define PROTO_CCE 1
#define PROTO_CEQ 2

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

	uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));
	uart1_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));

	//now enable interrupt, since UART library is interrupt controlled
	sei();

	uart_puts("CentralUnit  Build v0.2 \r\n");
	uart_putc(current_proto);
	char checksum;
	while (1)
	{
		c = uart1_getc();
		if (c & UART_NO_DATA)
		{
			continue;
		}
		
		// remove flags
		c = c & 0xFF;

		if (current_proto == PROTO_CCE)
		{
			//uart_putc(c);
			if (c == STX)
			{
				current_flag = F_DATA;
				index = 0;
				message = (char *)calloc(CHUNK_LEN, sizeof(char));
				continue;
			}

			if (c == ETX)
			{
				current_flag = F_CRC;
				continue;
			}

			if (current_flag == F_DATA)
			{
				message[index] = (char)c;
				index++;
				if (index == CHUNK_LEN)
				{
					int len = sizeof message / sizeof *message;
					message = realloc(message, len + CHUNK_LEN * sizeof(message));
				}
			}

			if (current_flag == F_CRC)
			{
				int len = message[1] << 8 | message[0];

				checksum = crc8((uint8_t *)message, len + 2);
				
				//int y = len;
				//index = 0;
				//while (y-- > 0)
				//{
				//	uart_putc(message[index++]);
				//}
				
				int l = message[1] << 8 | message[0];
				for (int i = 0; i < l + 2; i++)
				{
					uart_putc(message[i]);
				}

				if (c != checksum)
				{
					uart_puts("Wrong Checksum \r\n");
					uart_putc(c);
					uart_putc(checksum);
					checksum = 0; // vynulujeme
				}
				else
				{
					uart_puts("ACK");
					checksum = 0; // vynulujeme
								  //ParsePacket(message);
				}

				current_flag = NULL;
			}
		}

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
		}
	}
}