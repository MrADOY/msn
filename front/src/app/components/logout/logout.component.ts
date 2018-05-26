import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {User} from '../../models/user';
import {AuthService} from '../../services/auth.service';
// import * as data from './../config.json';
// const conf = (<any>data);

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private router: Router, private auth: AuthService) {
  }

  model: User;

  submitted = false;
  logged = 0;

  ngOnInit() {
    this.model = JSON.parse(localStorage.getItem('profile'));
    this.logged = 1;
    if (localStorage.getItem('jwt')) {
      this.logged = 1;
      this.auth.logout(this.model).then((user) => {
        console.log(user.json().accessToken);
        this.logged = -1;
        localStorage.removeItem('jwt');
        localStorage.removeItem('profile');
        this.auth.isLoggedIn = false;
      })
        .catch((err) => {
          this.logged = -1;
          this.auth.isLoggedIn = false;
        });
      // If the jwt key value exists, we’ll know the user is logged in, so we’ll get their profile.

    }
  }

  onSubmit() {
    this.submitted = true;
    this.auth.logout(this.model).then((user) => {
      console.log(user.json().accessToken);
      this.logged = -1;
      localStorage.removeItem('jwt');
      localStorage.removeItem('profile');
      this.auth.isLoggedIn = false;
    })
      .catch((err) => {
        this.logged = -1;
        this.auth.isLoggedIn = false;
      });
  }
}
