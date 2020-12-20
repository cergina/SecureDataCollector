/*
 * Atmega8_UART.c
 *
 * Created: 20. 12. 2020 17:53:04
 *  Author: Vlado
 */

#include <stdlib.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "lib/uart.h"

#define F_CPU 7372800UL

#ifndef F_CPU
#error "F_CPU undefined, please define CPU frequency in Hz in Makefile"
#endif

#define UART_BAUD_RATE 9600

int main(void)
{
    unsigned int c;
    char buffer[7];
    int num = 134;

    uart_init(UART_BAUD_SELECT(UART_BAUD_RATE, F_CPU));

    //now enable interrupt, since UART library is interrupt controlled
    sei();

    uart_puts("String stored in SRAM\n");

    uart_puts_P("String stored in FLASH\n");

    itoa(num, buffer, 10); // convert interger into string (decimal format)
    uart_puts(buffer);     // and transmit string to UART

    uart_putc('\n'); // Transmit single character to UART

    for (;;)
    {

        c = uart_getc();

        if (c & UART_NO_DATA)
        {
            continue;
        }

        if (c & UART_FRAME_ERROR)
        {
            uart_puts_P("UART Frame Error: ");
        }

        if (c & UART_OVERRUN_ERROR)
        {
            uart_puts_P("UART Overrun Error: ");
        }

        if (c & UART_BUFFER_OVERFLOW)
        {
            uart_puts_P("Buffer overflow error: ");
        }

        uart_putc((unsigned char)c);
    }
}