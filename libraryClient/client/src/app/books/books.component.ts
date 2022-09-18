import { Component, OnInit } from '@angular/core';
import { Book } from './book.model';
import { BookService } from '../book.service';
import { UserService } from '../user.service';
import { User } from '../login/user.model';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  constructor(
    private bookService:BookService,
    private userService:UserService
    ) { }
  book!:Book;
  books:any;
  borrowedBook:any;
  user!:User;


  ngOnInit(): void {
    this.getAllBooks();
    this.getCurrentUser();
  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);

      });

  }



  getAllBooks():void{
    this.bookService.getAllBooks().subscribe(
      (res)=>{


        console.log("Array of books available",res);

        this.books=res;

      }
    )
  }
  borrowBook():any{
    this.bookService.borrowBook(this.book).subscribe(
      (res)=>{
        this.borrowedBook=res;
        console.log(this.borrowedBook);

      },
      (err)=>{

      }
    )
  }


}
