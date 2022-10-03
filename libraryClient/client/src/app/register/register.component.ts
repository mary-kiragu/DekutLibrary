import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../login/user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {

  entry = {
    email: '',
    password: '',
    name:'',
    phoneNumber:''
  };

  registerFailure=""

   user!:User;

  constructor(private userService: UserService,private router: Router) {}

  ngOnInit(): void {}

  errorFound(){
    this.registerFailure = "  "
    //email: empty | proper
    const validateEmail = (email: string) => {
      return String(email)
        .toLowerCase()
        .match(
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
    };
    if(!validateEmail(this.entry.email)){
      this.registerFailure = "Enter a valid email address"
      return true;
    }
    if(this.entry.name == "" || this.entry.password=="" || this.entry.phoneNumber==""){
      console.log("Something is empty")
      this.registerFailure = "Please fill in all the fields"
      return true;
    }
    return false
    //Empty fields
  }

  register(): void {
    if(this.errorFound()){
      return
    }
    this.userService.register(this.entry).subscribe(
      (res) => {
        console.log(' user registered', res);
        this.router.navigate(['/']);
      },
      (err) => {
        console.log('User not registered in');
        this.registerFailure="Registraction failed! User already exist!"
        setTimeout(()=>{
          this.registerFailure=""
          this.router.navigate(['/']);

        },3000)


      }
    );
  }

  toCategories() {
    this.router.navigate(['/categories']);

 }
}
