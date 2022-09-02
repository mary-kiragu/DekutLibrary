import { Component, OnInit } from '@angular/core';
import { Book } from './book.model';
import { BookService } from '../book.service';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  constructor(private bookService:BookService) { }
  book!:Book;
  books:any;
  borrowedBook:any;


  ngOnInit(): void {
    this.getAllBooks();
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
