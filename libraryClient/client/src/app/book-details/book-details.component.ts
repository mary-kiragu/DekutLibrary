import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../books/book.model';
import { BookService } from '../book.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
book!:Book;
borrowedBook:any;
id!:number;
  constructor(
    private bookService:BookService,
    private router:Router,
    private route:ActivatedRoute
  ) { }

  ngOnInit(): void {

    const id=Number(this.route.snapshot.paramMap.get("id"));
    if(id){
      this.getOne(id);

    }
  }

  getOne(id:number):void{
    this.bookService.findbyId(id).subscribe(
      (res)=>{
        console.log(res);
        this.book=res;

      },
      (err)=>{
        console.log("book not found");
      }
      )




  }

  borrowBook():any{

    this.bookService.borrowBook(this.book).subscribe(
      (res)=>{
        this.borrowedBook=res;
        console.log(this.borrowedBook);
        const id=Number(this.route.snapshot.paramMap.get("id"));
        if(id){
          this.getOne(id);

        }

      },
      (err)=>{

      }
    )
  }

}
