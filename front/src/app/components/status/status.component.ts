import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {User} from '../../models/user';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  isLoggedIn = false;
  profile: User = new User('', '', '', '', '');
  constructor(private auth: AuthService) {
  }

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.auth.ensureAuthenticated(token)
        .then((user) => {
          this.isLoggedIn = true;
        })
        .catch((err) => {
          // console.log(JSON.parse(err));
        });
    }
    if (localStorage.getItem('profile')) {
      this.profile = Object.assign(new User(), JSON.parse(localStorage.getItem('profile')));
    }
  }
}
