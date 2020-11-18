
# Bluetooth Temperature Sensor
This is the final project of the ESCOM-IPN Analog Electronic course, which objective is learn how to use all the analog electronic instruments and circuits and then find how can we mix this knowledge with the computer systems. 

## Overview
Using a temperature sensor *LM335*, we used a microcontroller *PIC16F886* in order to use the ADC *(analog to digital converver)*, with that we used a LCD screen to display the temperature, and also used a Bluetooth module to transmit to an Android application the digital signal of the temperature sensor. Finally in the app, we created a graphical representation to watch the temperature in °C, °F and °K.

## The circuit
We first designed the appropriated circuit to get the temperature and connect it to the PIC
![](https://enya.codes/gitassets/temperatureSensor/circuito.png)
Using this diagram, we recreated the circuit but in a protoboard. 
![](https://enya.codes/gitassets/temperatureSensor/circuitoArmado.png)
Then, we tested the circuit with the current temperature. 
![](https://enya.codes/gitassets/temperatureSensor/circuitoFuncionando.png)


## The app
For the app, we didn't created everything from scratch. We started with an already created app that helps with the Bluetooth devices detection, the pairing, displaying them and the most important part, it let us access to the terminal data. The original project is this [BluetoothTerminal](https://github.com/aproschenko-dev/BluetoothTerminal).
Based in that project, we just created a graphical screen to display the terminal data that comes from the circuit previously created. 

![](https://enya.codes/gitassets/temperatureSensor/appLoading.png)
![](https://enya.codes/gitassets/temperatureSensor/appTemperature.png)

## Team Members
The team was conformed by:
- Enya Quetzalli Gómez Rodríguez [(Github)](https://github.com/equetzal)
- Damián Alanís Ramírez [(Github)](https://github.com/d-alanis98)
- Victor Ángel Carmona Medina [(LinkedIn)](https://mx.linkedin.com/in/victor-angel-carmona-medina-411a20186)
