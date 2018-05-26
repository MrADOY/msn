import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {User} from '../../models/user';
import {AuthService} from '../../services/auth.service';

// import * as data from './../config.json';
// const conf = (<any>data);

@Component({
  selector: 'app-sign',
  templateUrl: './sign.component.html',
  styleUrls: ['./sign.component.css']
})
export class SignComponent implements OnInit {
  constructor(private router: Router, private auth: AuthService) {
  }

  model: User = new User('', '', '', '', '');
  submitted = false;
  created = 0;

  ngOnInit() {
    this.model = {
      nom: '',
      prenom: '',
      email: '',
      password: '',
      confirmPassword: ''
    };
  }

  onSubmit() {
    this.submitted = true;
    this.auth.register(this.model).then((user) => {
      this.created = 1;
      localStorage.setItem('token', user.json().auth_token);
      localStorage.setItem('email', user.json().email);
      localStorage.setItem('id', user.json().id);
      // this.router.navigateByUrl('/status');
    })
      .catch((err) => {
        this.created = -1;
      });
  }

  newUser() {
    this.model = new User('', '', '', '', '');
  }
}
