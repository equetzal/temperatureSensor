
_main:

;ACD.c,22 :: 		void main(){
;ACD.c,23 :: 		config();
	CALL       _config+0
;ACD.c,24 :: 		while(1){
L_main0:
;ACD.c,25 :: 		lectura = ADC_Read(0);
	CLRF       FARG_ADC_Read_channel+0
	CALL       _ADC_Read+0
	MOVF       R0+0, 0
	MOVWF      _lectura+0
	MOVF       R0+1, 0
	MOVWF      _lectura+1
;ACD.c,26 :: 		temperatura = lectura*5/referencia;
	MOVLW      5
	MOVWF      R4+0
	MOVLW      0
	MOVWF      R4+1
	CALL       _Mul_16X16_U+0
	MOVF       _referencia+0, 0
	MOVWF      R4+0
	MOVF       _referencia+1, 0
	MOVWF      R4+1
	CALL       _Div_16X16_U+0
	CALL       _word2double+0
	MOVF       R0+0, 0
	MOVWF      _temperatura+0
	MOVF       R0+1, 0
	MOVWF      _temperatura+1
	MOVF       R0+2, 0
	MOVWF      _temperatura+2
	MOVF       R0+3, 0
	MOVWF      _temperatura+3
;ACD.c,27 :: 		FloatToStr(temperatura, pantallaTemp);
	MOVF       R0+0, 0
	MOVWF      FARG_FloatToStr_fnum+0
	MOVF       R0+1, 0
	MOVWF      FARG_FloatToStr_fnum+1
	MOVF       R0+2, 0
	MOVWF      FARG_FloatToStr_fnum+2
	MOVF       R0+3, 0
	MOVWF      FARG_FloatToStr_fnum+3
	MOVLW      _pantallaTemp+0
	MOVWF      FARG_FloatToStr_str+0
	CALL       _FloatToStr+0
;ACD.c,28 :: 		pantallaTemp[9] = '°';
	MOVLW      176
	MOVWF      _pantallaTemp+9
;ACD.c,29 :: 		pantallaTemp[10] = 'C';
	MOVLW      67
	MOVWF      _pantallaTemp+10
;ACD.c,30 :: 		Lcd_Out(1,1, "Temperatura: ");
	MOVLW      1
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      1
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      ?lstr1_ACD+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;ACD.c,31 :: 		Lcd_Out(2,3, pantallaTemp);
	MOVLW      2
	MOVWF      FARG_Lcd_Out_row+0
	MOVLW      3
	MOVWF      FARG_Lcd_Out_column+0
	MOVLW      _pantallaTemp+0
	MOVWF      FARG_Lcd_Out_text+0
	CALL       _Lcd_Out+0
;ACD.c,32 :: 		Delay_ms(500);
	MOVLW      3
	MOVWF      R11+0
	MOVLW      138
	MOVWF      R12+0
	MOVLW      85
	MOVWF      R13+0
L_main2:
	DECFSZ     R13+0, 1
	GOTO       L_main2
	DECFSZ     R12+0, 1
	GOTO       L_main2
	DECFSZ     R11+0, 1
	GOTO       L_main2
	NOP
	NOP
;ACD.c,33 :: 		}
	GOTO       L_main0
;ACD.c,34 :: 		}
L_end_main:
	GOTO       $+0
; end of _main

_config:

;ACD.c,36 :: 		void config(){
;ACD.c,37 :: 		ANSEL = 1;
	MOVLW      1
	MOVWF      ANSEL+0
;ACD.c,38 :: 		TRISC = 0;
	CLRF       TRISC+0
;ACD.c,39 :: 		TRISB = 0xFF;
	MOVLW      255
	MOVWF      TRISB+0
;ACD.c,40 :: 		Lcd_Init();
	CALL       _Lcd_Init+0
;ACD.c,41 :: 		Lcd_Cmd(_LCD_CURSOR_OFF);
	MOVLW      12
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;ACD.c,42 :: 		Lcd_Cmd(_LCD_CLEAR);
	MOVLW      1
	MOVWF      FARG_Lcd_Cmd_out_char+0
	CALL       _Lcd_Cmd+0
;ACD.c,44 :: 		}
L_end_config:
	RETURN
; end of _config
