import { Component, OnInit } from '@angular/core';
import { UserService } from '../../user.service';
import { User } from '../user.model';
import { BookService } from '../../book.service';
import { ActivatedRoute } from '@angular/router';
import { BorrowHistory } from '../../books/book.model';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
 user!:User;
 history={
  id:'',
  user:'',
  book:'',
  createdOn:''
 }

 historyValues:BorrowHistory[]=[];

  constructor(private userService:UserService,private bookService:BookService,private route:ActivatedRoute) { }

  ngOnInit(): void {
    const id=Number(this.route.snapshot.paramMap.get("id"));
    if(id){
     // this.getOne(id);


    }
    this.getCurrentUser();


  }

  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);
        this.getMyHistory();

      });

  }

  getMyHistory():void{
    this.bookService.getHistoryByUser(this.user.id).subscribe(
      (res)=>{
        console.log("my hist",res);
        this.historyValues=res;

      },
      (err)=>{
        console.log("my failed",err);

      }
    )
  }

}
