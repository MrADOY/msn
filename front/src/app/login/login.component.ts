import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import { User } from '../user';
import * as data from './../config.json';
const conf = (<any>data);

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // config: Config = require('../config.json');
  private apiConnection = 'http://' + conf.url + ':5000/api/auth/connexion';

  constructor(private http: HttpClient) {
  }

  model = new User('', '', '', '', '');

  submitted = false;
  logged = 0;

  onSubmit() {
    this.submitted = true;
    console.log(this.apiConnection);
    this.http.post(this.apiConnection, JSON.stringify(this.model), {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
    })
      .subscribe(
        res => {
          this.logged = 1;
        },
        err => {
          this.logged = -1;
            if (err.error.message) {
              // Email déjà utilisé
              // console.log(err.error.message);
            }
          },
          () => {
            // No errors, route to new page
          },
      );
  }
}
