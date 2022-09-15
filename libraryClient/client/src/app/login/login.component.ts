import { Component, OnInit } from '@angular/core';
import { User } from './user.model';
import { UserService } from '../user.service';
import { TokenService } from '../token.service';
import { Book } from '../books/book.model';
import { ActivatedRoute, Router } from '@angular/router';


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

  loginFailure=""

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  login(): void {
    this.userService.authenticate(this.user).subscribe(
      (res) => {
        console.log('loged in user', res);
        this.tokenService.saveToken(res.bearerToken);
        this.toLanding();

      },
      (err) => {
        console.log(err);

        this.loginFailure="Login failed! wrong username or password try again"
        setTimeout(()=>{
          this.loginFailure=""

        },3000)
      }
    );
  }

  toLanding() {
    this.router.navigate(['/categories']);
  }
}
