var led = WoT.getLocalThing('basicLed');
var fancy = WoT.getLocalThing('fancyLed');

fancy.onInvoke('trafficLight', function(mayDrive) {
    print('changing light to  ' + mayDrive);

    if(mayDrive) {
        led.setProperty('rgbValueGreen',255);
        led.setProperty('rgbValueRed', 0);
        led.setProperty('rgbValueBlue', 0);
    } else {
        led.setProperty('rgbValueGreen',0);
        led.setProperty('rgbValueRed',255);
        led.setProperty('rgbValueBlue', 0);
    }
});