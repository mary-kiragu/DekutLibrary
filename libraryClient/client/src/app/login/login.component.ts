import { Component, OnInit } from '@angular/core';
import { User } from './user.model';
import { UserService } from '../user.service';
import { TokenService } from '../token.service';
import { Book } from '../books/book.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  ngOnInit(): void {}
  book = {
    id: '',
    title: '',
  };
  user = {
    email: '',
    password: '',
  };
  loggedInUser?: any;

  constructor(
    private userService: UserService,
    private tokenService: TokenService
  ) {}

  login(): void {
    this.userService.authenticate(this.user).subscribe(
      (res) => {
        console.log('loged in user', res);
        this.tokenService.saveToken(res.bearerToken);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
