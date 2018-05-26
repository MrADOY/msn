#!/usr/bin/env node

var amqp = require('amqplib/callback_api');

var args = process.argv[2];
console.log(args)

amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var ex = 'bus-messages';
    var key = args + ".*";
    ch.assertExchange(ex, 'topic', {durable: false});

    ch.assertQueue('', {exclusive: true}, function(err, q) {
      console.log("J'attend un message je suis " + args);
      ch.bindQueue(q.queue, ex, key);
      ch.consume(q.queue, function(msg) {
        console.log(" Message reçu ! '%s'", msg.content.toString());
      }, {noAck: true});
    });
  });
});
