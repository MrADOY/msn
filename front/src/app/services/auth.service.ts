import {Injectable, Input} from '@angular/core';
import {Headers, Http} from '@angular/http';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../models/user';
import 'rxjs/add/operator/toPromise';

import * as data from './../config.json';

const conf = (<any>data);

@Injectable()
export class AuthService {
  loggedIn: Boolean = false;
  admin: Boolean = false;
  profile: Object;

  @Input() set isLoggedIn(etat: Boolean) {
    this.loggedIn = etat;
  }

  @Input() set setProfile(profil: Object) {
    this.profile = profil;
  }

  get getProfile() {
    return this.profile;
  }

  get isLoggedIn() {
    return this.loggedIn;
  }

  get isAdmin() {
    return this.admin;
  }

  private apiAuth = 'http://' + conf.url + ':5000/api/auth';
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) {
  }

  login(user: User): Promise<any> {
    const url = `${this.apiAuth}/connexion`;
    return this.http.post(url, user, {headers: this.headers}).toPromise();
  }

  logout(user: User): Promise<any> {
    const url = `${this.apiAuth}/deconnexion`;
    return this.http.post(url, user, {headers: this.headers}).toPromise().then(
      res => { // Success
        console.log(res);
      }
    );
  }

  register(user: User): Promise<any> {
    const url = `${this.apiAuth}/inscription`;
    return this.http.post(url, user, {headers: this.headers}).toPromise();
  }

  ensureAuthenticated(token): Promise<any> {
    const url = `${this.apiAuth}/status`;
    const headers: Headers = new Headers({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    // console.log(headers);
    return this.http.get(url, {headers: headers}).toPromise();
  }
}
