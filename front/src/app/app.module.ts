import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { ChatComponent } from './chat/chat.component';
import { LoginComponent } from './login/login.component';
import { SignComponent } from './sign/sign.component';
import { EqualValidator } from './equal-validator.directive';
import { FourOhFourComponent } from './four-oh-four/four-oh-four.component';

const appRoutes: Routes = [
  // { path: 'appareils', canActivate: [AuthGuard], component: AppareilViewComponent },
  // { path: 'appareils/:id', canActivate: [AuthGuard], component: SingleAppareilComponent },
  { path: 'login', component: LoginComponent },
  { path: 'sign', component: SignComponent },
  { path: '', component: LoginComponent },
  { path: 'not-found', component: FourOhFourComponent },
  { path: '**', redirectTo: 'not-found' }
];

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    LoginComponent,
    SignComponent,
    EqualValidator,
    FourOhFourComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
  ],
  providers: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
