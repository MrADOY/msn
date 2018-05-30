#!/usr/bin/env node

 /* Appeler avec le service mail.*/

 var amqp = require('amqplib/callback_api');

 var args = process.argv.slice(2);
 console.log(args)

 amqp.connect('amqp://localhost', function(err, conn) {
   conn.createChannel(function(err, ch) {
     var ex = 'bus-messages';
     ch.assertExchange(ex, 'topic', {durable: false});

     ch.assertQueue('', {exclusive: true}, function(err, q) {
       console.log("J'attend un message je suis " + args);
       args.forEach(function(key) {
      ch.bindQueue(q.queue, ex, key);
         });
      ch.consume(q.queue, function(msg) {
         console.log(" Message reçu ! '%s' de '%s' ", msg.content.toString(),msg.fields.routingKey);
       }, {noAck: true});
     });
   });
 });
