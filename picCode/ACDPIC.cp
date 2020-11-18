#line 1 "C:/Users/gomez/odrive/OneDrive IPN/ESCOM/3°/Electronica Analogica/Pic/ACDPIC.c"
sbit LCD_RS at RC0_bit;
sbit LCD_EN at RC1_bit;
sbit LCD_D4 at RC2_bit;
sbit LCD_D5 at RC3_bit;
sbit LCD_D6 at RC4_bit;
sbit LCD_D7 at RC5_bit;

sbit LCD_RS_Direction at TRISC0_bit;
sbit LCD_EN_Direction at TRISC1_bit;
sbit LCD_D4_Direction at TRISC2_bit;
sbit LCD_D5_Direction at TRISC3_bit;
sbit LCD_D6_Direction at TRISC4_bit;
sbit LCD_D7_Direction at TRISC5_bit;

void config();
void limpia();

float temperatura, resultado = 0.00;
unsigned int lectura;
float res;
char pantallaLCD[14];
char pantallaBT[14];




void main(){
 config();
 UART1_Init(9600);
 delay_ms(100);
 lectura = 1;
 while(1){
 limpia();


 lectura = ADC_Read(2);
#line 44 "C:/Users/gomez/odrive/OneDrive IPN/ESCOM/3°/Electronica Analogica/Pic/ACDPIC.c"
 temperatura = (int)lectura;
 resultado = 50.00/1024.00*temperatura;
 FloatToStr(resultado, pantallaLCD);

 IntToStr(lectura, pantallaBT);
 UART1_Write_Text(pantallaBT);
 Lcd_Out(1,1, "Temperatura: °C");
 Lcd_Out(2,3, pantallaLCD);
 Delay_ms(800);
 }
}

void limpia(){
 pantallaLCD[0] = ' ';
 pantallaLCD[1] = ' ';
 pantallaLCD[2] = ' ';
 pantallaLCD[3] = ' ';
 pantallaLCD[4] = ' ';
 pantallaLCD[5] = ' ';
 pantallaLCD[6] = ' ';
 pantallaLCD[7] = ' ';
 pantallaLCD[8] = ' ';
 pantallaLCD[9] = ' ';
 pantallaLCD[10] = ' ';
 pantallaLCD[11] = ' ';
 pantallaLCD[12] = ' ';
 pantallaLCD[13] = ' ';
 pantallaBT[0] = ' ';
 pantallaBT[1] = ' ';
 pantallaBT[2] = ' ';
 pantallaBT[3] = ' ';
 pantallaBT[4] = ' ';
 pantallaBT[5] = ' ';
 pantallaBT[6] = ' ';
 pantallaBT[7] = ' ';
 pantallaBT[8] = ' ';
 pantallaBT[9] = ' ';
 pantallaBT[10] = ' ';
 pantallaBT[11] = ' ';
 pantallaBT[12] = ' ';
 pantallaBT[13] = ' ';
 Lcd_Cmd(_LCD_CLEAR);
}

void config(){
 ANSEL = 0x04;
 ANSELH = 0;
 C1ON_bit = 0;
 C2ON_bit = 0;

 TRISA = 0xFF;
 TRISC = 0;
 TRISB = 0;
 Lcd_Init();
 ADC_Init();
 Lcd_Cmd(_LCD_CURSOR_OFF);
 Lcd_Cmd(_LCD_CLEAR);

}
