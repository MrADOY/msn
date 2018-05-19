import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {User} from '../user';
import * as data from './../config.json';

const conf = (<any>data);

@Component({
  selector: 'app-sign',
  templateUrl: './sign.component.html',
  styleUrls: ['./sign.component.css']
})
export class SignComponent implements OnInit {
  // config: Config = require('../config.json');
  private apiInscription = 'http://' + conf.url + ':5000/api/auth/inscription';

  constructor(private http: HttpClient) {
  }

  public user: User;

  model = new User('', '', '', '', '');
  submitted = false;
  created = 0;

  ngOnInit() {
    this.user = {
      nom: '',
      prenom: '',
      email: '',
      password: '',
      confirmPassword: ''
    };
  }

  onSubmit() {
    this.submitted = true;
    this.http.post(this.apiInscription, JSON.stringify(this.model), {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
    })
      .subscribe(
        res => {
          console.log(res);
          this.created = 1;
        },
        err => {
          this.created = -1;
          if (err.error.message) {
            // Email déjà utilisé
            // console.log(err.error.message);
          }
        },
        () => {
          // No errors, route to new page
        }
      );
  }

  newUser() {
    this.model = new User('', '', '', '', '');
  }

  save(signForm: User, isValid: boolean) {
  }

}
