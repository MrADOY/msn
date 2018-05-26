import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HttpModule} from '@angular/http';
import {Routes, RouterModule} from '@angular/router';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AvatarModule } from 'ng2-avatar';

import {EqualValidator} from './equal-validator.directive';
import {AppComponent} from './app.component';
import {ChatComponent} from './components/chat/chat.component';
import {LoginComponent} from './components/login/login.component';
import {SignComponent} from './components/sign/sign.component';
import {FourOhFourComponent} from './components/four-oh-four/four-oh-four.component';
import {AppNavbarComponent} from './components/navbar/app-navbar.component';
import {StatusComponent} from './components/status/status.component';
import {UserComponent} from './components/user/user.component';
import {LogoutComponent} from './components/logout/logout.component';

import {AuthService} from './services/auth.service';
import {ChatService} from './services/chat.service';
import {AuthGuard} from './guards/auth.guard'; // Redirige vers [/login] pas authentifié

const appRoutes: Routes = [
  // { path: 'appareils', canActivate: [AuthGuard], component: AppareilViewComponent },
  // { path: 'appareils/:id', canActivate: [AuthGuard], component: SingleAppareilComponent },
  {path: 'account', loadChildren: 'app/modules/account/account.module#AccountModule'},
  // {path: 'dashboard', loadChildren: 'app/dashboard/dashboard.module#DashboardModule'},
  {path: 'login', component: LoginComponent},
  {path: 'chat', component: ChatComponent},
  {path: 'sign', component: SignComponent},
  {path: 'status', component: StatusComponent},
  {path: 'users/:username', component: UserComponent},
  {path: '', component: LoginComponent},
  {path: 'not-found', component: FourOhFourComponent},
  {path: '**', redirectTo: 'not-found'}
];

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    LoginComponent,
    SignComponent,
    EqualValidator,
    FourOhFourComponent,
    AppNavbarComponent,
    StatusComponent,
    UserComponent,
    LogoutComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
    NgbModule.forRoot(),
    HttpModule,
    AvatarModule.forRoot()
  ],
  providers: [
    AuthService,
    AuthGuard,
    ChatService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
