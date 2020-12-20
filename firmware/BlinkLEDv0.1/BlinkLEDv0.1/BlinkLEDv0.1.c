/*
 * BlinkLEDv0.c
 *
 * Created: 20. 12. 2020 16:10:59
 *  Author: Vlado
 */ 

#define F_CPU 7372000UL

#include <avr/io.h>
#include <util/delay.h>

int main(void)
{
		DDRC |= (1<<DDC4);
		while (1)
		{
			PORTC |= (1<<PORTC4);
			_delay_ms(1000);
			PORTC &= ~ (1<<PORTC4);
			_delay_ms(1000);
		}
}