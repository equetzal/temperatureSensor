
_main:

;ACDPIC.c,27 :: 		void main(){
;ACDPIC.c,28 :: 		config();
	CALL       _config+0
;ACDPIC.c,29 :: 		UART1_Init(9600);
	MOVLW      25
	MOVWF      SPBRG+0
	BSF        TXSTA+0, 2
	CALL       _UART1_Init+0
;ACDPIC.c,30 :: 		delay_ms(100);
	MOVLW      130
	MOVWF      R12+0
	MOVLW      221
	MOVWF      R13+0
L_main0:
	DECFSZ     R13+0, 1
	GOTO       L_main0
	DECFSZ     R12+0, 1
	GOTO       L_main0
	NOP
	NOP
;ACDPIC.c,31 :: 		lectura = 1;
	MOVLW      1
	MOVWF      _lectura+0
	MOVLW      0
	MOVWF      _lectura+1
;ACDPIC.c,32 :: 		while(1){
L_main1:
;ACDPIC.c,33 :: 		limpia();
	CALL       _limpia+0
;ACDPIC.c,36 :: 		lectura = ADC_Read(2);
	MOVLW      2
	MOVWF      FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _lectura+0
	MOVF       R0+1, 0
	MOVWF      _lectura+1
;ACDPIC.c,44 :: 		temperatura = (int)lectura;
	CALL       _int2double+0
	MOVF       R0+0, 0
	MOVWF      _temperatura+0
	MOVF       R0+1, 0
	MOVWF      _temperatura+1
	MOVF       R0+2, 0
	MOVWF      _temperatura+2
	MOVF       R0+3, 0
	MOVWF      _temperatura+3
;ACDPIC.c,45 :: 		resultado = 50.00/1024.00*temperatura;
	MOVLW      0
	MOVWF      R4+0
	MOVLW      0
	MOVWF      R4+1
	MOVLW      72
	MOVWF      R4+2
	MOVLW      122
	MOVWF      R4+3
	CALL       _Mul_32x32_FP+0
	MOVF       R0+0, 0
	MOVWF      _resultado+0
	MOVF       R0+1, 0
	MOVWF      _resultado+1
	MOVF       R0+2, 0
	MOVWF      _resultado+2
	MOVF       R0+3, 0
	MOVWF      _resultado+3
;ACDPIC.c,46 :: 		FloatToStr(resultado, pantallaLCD);
	MOVF       R0+0, 0
	MOVWF      FARG_FloatToStr_fnum+0
	MOVF       R0+1, 0
	MOVWF      FARG_FloatToStr_fnum+1
	MOVF       R0+2, 0
	MOVWF      FARG_FloatToStr_fnum+2
	MOVF       R0+3, 0
	MOVWF      FARG_FloatToStr_fnum+3
	MOVLW      _pantallaLCD+0
	MOVWF      FARG_FloatToStr_str+0
	CALL       _FloatToStr+0
;ACDPIC.c,48 :: 		IntToStr(lectura, pantallaBT);
	MOVF       _lectura+0, 0
	MOVWF      FARG_IntToStr_input+0
	MOVF       _lectura+1, 0
	MOVWF      FARG_IntToStr_input+1
	MOVLW      _pantallaBT+0
	MOVWF      FARG_IntToStr_output+0
	CALL       _IntToStr+0
;ACDPIC.c,49 :: 		UART1_Write_Text(pantallaBT);
	MOVLW      _pantallaBT+0
	MOVWF      FARG_UART1_Write_Text_uart_text+0
	CALL       _UART1_Write_Text+0
;ACDPIC.c,50 :: 		Lcd_Out(1,1, "Temperatura: °C");
	MOVLW      1
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      ?lstr1_ACDPIC+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;ACDPIC.c,51 :: 		Lcd_Out(2,3, pantallaLCD);
	MOVLW      2
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      3
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      _pantallaLCD+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;ACDPIC.c,52 :: 		Delay_ms(800);
	MOVLW      5
	MOVWF      R11+0
	MOVLW      15
	MOVWF      R12+0
	MOVLW      241
	MOVWF      R13+0
L_main3:
	DECFSZ     R13+0, 1
	GOTO       L_main3
	DECFSZ     R12+0, 1
	GOTO       L_main3
	DECFSZ     R11+0, 1
	GOTO       L_main3
;ACDPIC.c,53 :: 		}
	GOTO       L_main1
;ACDPIC.c,54 :: 		}
L_end_main:
	GOTO       $+0
; end of _main

_limpia:

;ACDPIC.c,56 :: 		void limpia(){
;ACDPIC.c,57 :: 		pantallaLCD[0] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+0
;ACDPIC.c,58 :: 		pantallaLCD[1] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+1
;ACDPIC.c,59 :: 		pantallaLCD[2] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+2
;ACDPIC.c,60 :: 		pantallaLCD[3] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+3
;ACDPIC.c,61 :: 		pantallaLCD[4] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+4
;ACDPIC.c,62 :: 		pantallaLCD[5] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+5
;ACDPIC.c,63 :: 		pantallaLCD[6] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+6
;ACDPIC.c,64 :: 		pantallaLCD[7] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+7
;ACDPIC.c,65 :: 		pantallaLCD[8] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+8
;ACDPIC.c,66 :: 		pantallaLCD[9] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+9
;ACDPIC.c,67 :: 		pantallaLCD[10] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+10
;ACDPIC.c,68 :: 		pantallaLCD[11] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+11
;ACDPIC.c,69 :: 		pantallaLCD[12] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+12
;ACDPIC.c,70 :: 		pantallaLCD[13] = ' ';
	MOVLW      32
	MOVWF      _pantallaLCD+13
;ACDPIC.c,71 :: 		pantallaBT[0] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+0
;ACDPIC.c,72 :: 		pantallaBT[1] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+1
;ACDPIC.c,73 :: 		pantallaBT[2] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+2
;ACDPIC.c,74 :: 		pantallaBT[3] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+3
;ACDPIC.c,75 :: 		pantallaBT[4] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+4
;ACDPIC.c,76 :: 		pantallaBT[5] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+5
;ACDPIC.c,77 :: 		pantallaBT[6] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+6
;ACDPIC.c,78 :: 		pantallaBT[7] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+7
;ACDPIC.c,79 :: 		pantallaBT[8] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+8
;ACDPIC.c,80 :: 		pantallaBT[9] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+9
;ACDPIC.c,81 :: 		pantallaBT[10] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+10
;ACDPIC.c,82 :: 		pantallaBT[11] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+11
;ACDPIC.c,83 :: 		pantallaBT[12] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+12
;ACDPIC.c,84 :: 		pantallaBT[13] = ' ';
	MOVLW      32
	MOVWF      _pantallaBT+13
;ACDPIC.c,85 :: 		Lcd_Cmd(_LCD_CLEAR);
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;ACDPIC.c,86 :: 		}
L_end_limpia:
	RETURN
; end of _limpia

_config:

;ACDPIC.c,88 :: 		void config(){
;ACDPIC.c,89 :: 		ANSEL  = 0x04;              // Configure AN2 pin as analog
	MOVLW      4
	MOVWF      ANSEL+0
;ACDPIC.c,90 :: 		ANSELH = 0;                 // Configure other AN pins as digital I/O
	CLRF       ANSELH+0
;ACDPIC.c,91 :: 		C1ON_bit = 0;               // Disable comparators
	BCF        C1ON_bit+0, BitPos(C1ON_bit+0)
;ACDPIC.c,92 :: 		C2ON_bit = 0;
	BCF        C2ON_bit+0, BitPos(C2ON_bit+0)
;ACDPIC.c,94 :: 		TRISA  = 0xFF;              // PORTA is input
	MOVLW      255
	MOVWF      TRISA+0
;ACDPIC.c,95 :: 		TRISC  = 0;                 // PORTC is output
	CLRF       TRISC+0
;ACDPIC.c,96 :: 		TRISB  = 0;
	CLRF       TRISB+0
;ACDPIC.c,97 :: 		Lcd_Init();
	CALL       _Lcd_Init+0
;ACDPIC.c,98 :: 		ADC_Init();
	CALL       _ADC_Init+0
;ACDPIC.c,99 :: 		Lcd_Cmd(_LCD_CURSOR_OFF);
	MOVLW      12
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;ACDPIC.c,100 :: 		Lcd_Cmd(_LCD_CLEAR);
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;ACDPIC.c,102 :: 		}
L_end_config:
	RETURN
; end of _config
