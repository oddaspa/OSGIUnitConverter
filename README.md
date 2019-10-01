# OSGIUnitConverter

Supports most arithmetic expressions. To add your own formulas/expression, add them to the units.txt under no.tdt4250.assignment2.conversionApp.no.

The formulas need to have 2 characters unique to the relationship e.g celsius to fahrenheit is cf, kilograms to pounds is kp etc.

Currently supported formulas:
* pk = x*0.45359237 (pounds to kilograms)
* kp = x/0.45359237 (kilograms to pounds)
* mk = x/1.609344   (miles to kilometers)
* km = x*1.609344   (kilometers to miles)
* cf = x*1.8+32     (celsius to fahrenheit)
* fc = (x-32)/1.8   (fahrenheit to celsius)
