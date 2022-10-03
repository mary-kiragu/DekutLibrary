import { Component, OnInit } from '@angular/core';
import { User, AccountStatus } from './user.model';
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
  book = {
    id: '',
    title: '',
  };
  entry = {
    email: '',
    password: '',
  };
  user: any;
  loggedInUser?: any;

  loginFailure = '';
  isLoggedIn=false;

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  ngOnInit(): void {

  }



  login(): void {
    console.log('about');
    this.userService.authenticate(this.entry).subscribe(
      (res) => {
        console.log('loged in user');
        console.log(res);
        // console.log(res.email)
        this.tokenService.saveToken(res.bearerToken);

        this.userService.getProfile().subscribe((userProfile) => {

          localStorage.setItem("user",JSON.stringify(userProfile))

          if (userProfile.authority === 'SUBSCRIBER' && userProfile.accountStatus==="UNPAID" ) {
            this.toPlan();
          }
        });
        this.isLoggedIn=true;

        this.toLanding();
      },
      (err) => {
        console.log(err);

        this.loginFailure =
          'Login failed! wrong username or password try again';
        setTimeout(() => {
          this.loginFailure = '';
        }, 3000);
      }
    );
  }

  toLanding() {
    this.router.navigate(['/categories']);
  }

  toPlan() {
    this.router.navigate(['/subscribe']);
  }
}
