sbit LCD_RS at RC0_bit;
sbit LCD_EN at RC1_bit;
sbit LCD_D4 at RC2_bit;
sbit LCD_D5 at RC3_bit;
sbit LCD_D6 at RC4_bit;
sbit LCD_D7 at RC5_bit;
//Direccion de los bits de la LCD
sbit LCD_RS_Direction at TRISC0_bit;
sbit LCD_EN_Direction at TRISC1_bit;
sbit LCD_D4_Direction at TRISC2_bit;
sbit LCD_D5_Direction at TRISC3_bit;
sbit LCD_D6_Direction at TRISC4_bit;
sbit LCD_D7_Direction at TRISC5_bit;

void config();
float temperatura;
unsigned int lectura, referencia=1023;
float res;
char pantallaTemp[10];


void main(){
     config();
     while(1){
            lectura = ADC_Read(0);
            temperatura = lectura*5/referencia;
            FloatToStr(temperatura, pantallaTemp);
            pantallaTemp[9] = '°';
            pantallaTemp[10] = 'C';
            Lcd_Out(1,1, "Temperatura: ");
            Lcd_Out(2,3, pantallaTemp);
            Delay_ms(500);
     }
}

void config(){
     ANSEL = 1;
     TRISC = 0;
     TRISB = 0xFF;
     Lcd_Init();
     Lcd_Cmd(_LCD_CURSOR_OFF);
     Lcd_Cmd(_LCD_CLEAR);

}