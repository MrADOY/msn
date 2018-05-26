import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {User} from '../../models/user';
import {AuthService} from '../../services/auth.service';
// import * as data from './../config.json';
// const conf = (<any>data);

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, private auth: AuthService) {
  }

  model: User = new User('', '', '', '', '');
  obj = new User('', '', '', '', '');
  submitted = false;
  logged = 0;
  loggedOut = 0;

  ngOnInit() {
    if (localStorage.getItem('jwt')) {
      this.logged = 1;
      this.auth.isLoggedIn = true;
      // If the jwt key value exists, we’ll know the user is logged in, so we’ll get their profile.
    } else {
      this.auth.isLoggedIn = false;
    }
  }

  logout() {
    this.logged = 0;
    this.loggedOut = 1;
    localStorage.removeItem('jwt');
    localStorage.removeItem('profile');
    this.auth.isLoggedIn = false;
    window.location.reload();
    this.auth.logout(this.model).then((user) => {
      this.logged = 0;

    })
      .catch((err) => {
        this.logged = 0;
      });
  }

  login() {
    this.auth.login(this.model).then((user) => {
      this.logged = 1;
      localStorage.setItem('jwt', user.json().token);
      localStorage.setItem('profile', JSON.stringify(user.json().utilisateur));
      this.obj = Object.assign(new User(), JSON.stringify(user.json().utilisateur));
      this.auth.profile = this.obj;
      this.auth.isLoggedIn = true;
      window.location.reload();
      this.router.navigateByUrl('/chat');
    })
      .catch((err) => {
        this.logged = -1;
      });
  }

  onLogout() {
    this.submitted = true;
    this.logout();
  }

    onSubmit() {
    this.submitted = true;
    this.login();
    /* V1 */
    //  private apiConnection = 'http://' + conf.url + ':5000/api/auth/connexion';
    //
    //   constructor(private http: HttpClient, private auth: AuthService) {
    //   }
    // onSubmit() {
    // this.http.post(this.apiConnection, JSON.stringify(this.model), {
    //   headers: new HttpHeaders()
    //     .set('Content-Type', 'application/json')
    // })
    //   .subscribe(
    //     res => {
    //       console.log(res);
    //       this.logged = 1;
    //     },
    //     err => {
    //       this.logged = -1;
    //       if (err.error.message) {
    //         // Email déjà utilisé
    //         // console.log(err.error.message);
    //       }
    //     },
    //     () => {
    //       // No errors, route to new page
    //     },
    //   );
    // }
  }
}
