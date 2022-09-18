import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  user:any;

  constructor(private userService:UserService) { }

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


}
