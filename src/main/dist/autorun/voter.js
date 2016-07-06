 //just an example script - to be moved into other repo

console.log("attempting to initialize voter");

WoT.discover('local', { 'name' : 'voter'})
    .then(function(things) {
     console.log("discover returned: " + things);
     var voter = things[0];

     console.log("first element: " + voter);

     //should be only one thing
     console.log("found " + voter.name);

        voter.setProperty("votes",0);

        voter
        .onUpdateProperty("votes",
            function(newValue, oldValue) {
                console.log(oldValue + " -> " + newValue);
                var message = (oldValue < newValue)? "increased " : "decreased";
                console.log("votes " + message + " to " + newValue);
            }
         );

        voter
        .onInvokeAction("tooCold", function() {
            console.log("vote for too cold, incrementing votes");
            var value = voter.getProperty("votes") + 1;
            voter.setProperty("votes", value);
            return value;
        });

        voter
        .onInvokeAction("tooHot", function() {
            console.log("vote for too hot, decrementing votes");
            var value = voter.getProperty("votes") - 1;
            voter.setProperty("votes", value);
            return value;
        });
    });