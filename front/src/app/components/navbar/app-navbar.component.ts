import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {User} from '../../models/user';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {
  logged = false;
  profile: User = new User('', '', '', '', '');

  constructor(private auth: AuthService) {
  }

  ngOnInit() {
    if (localStorage.getItem('jwt')) {
      this.logged = true;
    }
    if (localStorage.getItem('profile')) {
      this.profile = Object.assign(new User(), JSON.parse(localStorage.getItem('profile')));
    }
  }
}
