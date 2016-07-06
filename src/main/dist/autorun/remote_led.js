//just an example script - to be moved into other repo

// this script reacts to changes in the votes and changes the color of the LED accordingly

var basicLed = null;
var voter = null;

console.log("looking for voter locally");

 WoT.discover('local', { 'name' : 'voter'})
 .then(function(things) {
    console.log("discover returned: " + things);
    voter = things[0];

    console.log("first element: " + voter);

    //should be only one thing
    console.log("found " + voter.name);

    WoT.consumeDescriptionUri("http://thingweb-demo:8080/things/basicLed").then(function(basicLed) {
        console.log("discover of basicLed returned: " + basicLed);

         voter.onUpdateProperty('votes', function(votes) {
             if(votes < 0) {
                 // make led blue
                 console.log("setting remote LED blue");
                 basicLed.setProperty("rgbValueRed",0);
                 basicLed.setProperty("rgbValueGreen",0);
                 basicLed.setProperty("rgbValueBlue",120);
                 basicLed.setProperty("brightness",40);
             } else if(votes > 0) {
                 // make led red
                 console.log("setting remote LED red");
                 basicLed.setProperty("rgbValueRed",255);
                 basicLed.setProperty("rgbValueGreen",0);
                 basicLed.setProperty("rgbValueBlue",0);
             }  else  { // (votes == 0)
                 // make led white
                 console.log("setting remote LED off");
                 basicLed.setProperty("rgbValueRed",60);
                 basicLed.setProperty("rgbValueGreen",60);
                 basicLed.setProperty("rgbValueBlue",60);
             };
         });
    })
 })
._catch(function(err){
    console.error(err);
});

console.log("client script running");