 //just an example script - to be moved into other repo

 WoT.newThing("counter")
    .then(function(thing) {
        console.log("created " + thing.name);

        thing
        .addProperty("count", { type: "integer" })
        .setProperty("count",0)
        .onUpdateProperty("count",
            function(newValue, oldValue) {
                console.log(oldValue + " -> " + newValue);
                var message = (oldValue < newValue)? "increased " : "decreased";
                console.log("counter " + message + " to " + newValue);
            }
         );

         thing
         .addAction("increment")
         .onInvokeAction("increment", function() {
            console.log("incrementing counter");
            var value = thing.getProperty("count") + 1;
            thing.setProperty("count", value);
            return value;
         });

        thing
        .addAction("decrement")
        .onInvokeAction("decrement", function() {
             console.log("decrementing counter");
             var value = thing.getProperty("count") - 1;
             thing.setProperty("count", value);
             return value;
        });
    });