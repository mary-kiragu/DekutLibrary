import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  user = {
    email: '',
    password: '',
    name:''
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {}

  register(): void {
    this.userService.register(this.user).subscribe(
      (res) => {
        console.log(' user registered', res);
      },
      (err) => {
        console.log('User not registered in');
      }
    );
  }
}
