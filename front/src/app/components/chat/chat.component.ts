import {Component, OnInit} from '@angular/core';
import {ChatService} from '../../services/chat.service';
import * as amqp from 'amqplib';
import {AuthService} from '../../services/auth.service';
import {User} from '../../models/user';
import {Conversation} from '../../models/conversation';

let amqpConn = null;


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  connection: any;
  exchange: any;
  queue: any;
  ch: any;
  users: User[] = [];
  profil: User;
  conv: Conversation;
  contact: User;


  constructor(private chat: ChatService) {
  }

  closeOnErr(err) {
    if (!err) {
      return false;
    }
    console.error('[AMQP] error', err);
    amqpConn.close();
    return true;
  }


  work(msg, cb) {
    console.log('PDF processing of ', msg.content.toString());
    cb(true);
  }

  selectConv(email) {
    this.chat.getContact(email).then((res) => {
      this.contact = (Object.assign(new User(), res.json()));
      this.conv = new Conversation(this.contact.id, this.contact, this.profil, '50');
    });
  }

  amqp() {
    amqp.connect('amqp://serveurnicoant.ddns.net:5672', function (err, conn) {
      if (err) {
        console.error('[AMQP]', err.message);
        return setTimeout(this, 1000);
      }
      conn.on('error', function (erra) {
        if (erra.message !== 'Connection closing') {
          console.error('[AMQP] conn error', erra.message);
        }
      });
      conn.on('close', function () {
        console.error('[AMQP] reconnecting');
        return setTimeout(this, 1000);
      });
      console.log('[AMQP] connected');
      amqpConn = conn;
      amqpConn.createChannel(function (erra, ch) {
        ch.on('error', function (erro) {
          console.error('[AMQP] channel error', erro.message);
        });
        ch.on('close', function () {
          console.log('[AMQP] channel closed');
        });

        ch.prefetch(10);
        ch.assertQueue('jobs', {durable: true}, function (erri, _ok) {
          ch.consume('jobs', console.log('toto'), {noAck: false});
          console.log('Worker is started');
        });
      });
    });


    //     this.connection = new Amqp.Connection('amqp://serveurnicoant.ddns.net');
//     this.exchange = this.connection.declareExchange('ExchangeName');
//     this.queue = this.connection.declareQueue('QueueName');
//     this.queue.bind(this.exchange);
//     this.queue.activateConsumer((message) => {
//       console.log('Message received: ' + message.getContent());
//     });
//
// // it is possible that the following message is not received because
// // it can be sent before the queue, binding or consumer exist
//     const msg = new Amqp.Message('Test');
//     this.exchange.send(msg);
//
//     this.connection.completeConfiguration().then(() => {
//       // the following message will be received because
//       // everything you defined earlier for this connection now exists
//       const msg2 = new Amqp.Message('Test2');
//       this.exchange.send(msg2);
//     });
  }

  ngOnInit() {
    this.chat.connectList();
    this.users = this.chat.userCo;
    if (localStorage.getItem('profile')) {
      this.profil = Object.assign(new User(), JSON.parse(localStorage.getItem('profile')));
    }
  }
}
