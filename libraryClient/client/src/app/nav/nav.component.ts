import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { NavigationStart, Router } from '@angular/router';
import { TokenService } from '../token.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  user:any;
  isLoggedIn=true

  constructor(private userService:UserService,private router:Router,private tokenService:TokenService) {
    router.events.forEach((event) => {
      if (event instanceof NavigationStart) {
        this.user = this.tokenService.getUserFromStorage();
        console.log("The user is,  ",this.user);
      }
    });
  }

  ngOnInit(): void {
    this.getCurrentUser();

  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);

      });

  }
   logOut(): void{
    //delete token
    this.tokenService.clearToken();

    //delete user from storage
    this.tokenService.clearUser();
    this.isLoggedIn=false
    //route to login
    this.router.navigate(['/']);
   }


}
