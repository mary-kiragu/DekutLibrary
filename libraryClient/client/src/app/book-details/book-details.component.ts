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
  book={
    id:'',
    isbn: '',
    title: '',
    author: '',
    imageUrl: '',
    bookImageUrl:'',
    status:'',
    name:'',
    type:'',
    data:'',
    size:'',
    categoryId:'',
    accessionNumber:'',
    bookUrl:'',
    bookName:'',
    bookType:'',
    bookData:'',
    bookSize:'',
  }
  srcPDF ! :string;

//book!:any;
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
        this.book = res;
        console.log("finding book ", this.book);
        if (res.bookType == 'application/pdf') {
          var arrrayBuffer = base64ToArrayBuffer(res.bookData); //data is the base64 encoded string
          function base64ToArrayBuffer(base64: string) {
            var binaryString = window.atob(base64);
            var binaryLen = binaryString.length;
            var bytes = new Uint8Array(binaryLen);
            for (var i = 0; i < binaryLen; i++) {
              var ascii = binaryString.charCodeAt(i);
              bytes[i] = ascii;
            }
            return bytes;
          }
          var blob = new Blob([arrrayBuffer], { type: "application/pdf" });
          this.srcPDF = window.URL.createObjectURL(blob);
          console.log("Testing pdf *****", this.srcPDF)

          // window.open(this.src,'height=650,width=840');

        }

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
