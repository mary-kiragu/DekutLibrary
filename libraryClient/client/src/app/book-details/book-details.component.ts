import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../books/book.model';
import { BookService } from '../book.service';
import { FormBuilder } from '@angular/forms';
import { UserService } from '../user.service';
import { User } from '../login/user.model';

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
  bookId!: number;
  bookForm = this.formBuilder.group({
    isbn: [],
    title: [],
    author: [],
    imageUrl: [],
    bookImageUrl:[],
    name:[],
    type:[],
    data:[],
    size:[],
    categoryId:[],
    accessionNumber:[],
    bookUrl:[],
    bookName:[],
    bookType:[],
    bookData:[],
    bookSize:[],
  });
  srcPDF ! :string;
  user!: User;

//book!:any;
borrowedBook:any;
id!:number;
  constructor(
    private bookService:BookService,
    private userService:UserService,
    private formBuilder: FormBuilder,
    private router:Router,
    private route:ActivatedRoute
  ) { }

  ngOnInit(): void {

    const id=Number(this.route.snapshot.paramMap.get("id"));
    if(id){
      this.getOne(id);

    }
  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);

      });

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

  extractBookDetails(): any {
    return {
      author: this.bookForm.get('author')!.value,
      isbn:this.bookForm.get('isbn')!.value,
      title: this.bookForm.get('title')!.value,
      imageUrl: this.bookForm.get('imageUrl')!.value,
      accessionNumber:this.bookForm.get('accessionNumber')!.value,
       bookImageUrl:this.book.bookImageUrl,
      name:this.book.name,
      type:this.book.type,
      data:this.book.data,
      size:this.book.size,
      bookUrl:this.book.bookUrl,
      bookName:this.book.bookName,
      bookType:this.book.bookType,
      bookData:this.book.bookData,
      bookSize:this.book.bookSize,
    };
  }


  updateBook(bookId:number): void {
    const bookDetails = this.extractBookDetails();
    bookDetails.id=bookId;
    console.log('book details', bookDetails);
    this.bookService.updateBook(bookDetails).subscribe(
      (res) => {
        console.log('updated book', res);

      },
      (err) => {
        console.log('book not updated book', err);
      }
    );
  }
  editBook(book: any): void {
    this.bookId = +book.id;
    this.bookForm = this.formBuilder.group({
      id: [book.id],
      title: [book.title],
      isbn: [book.isbn],
      accessionNumber: [book.accessionNumber],
      author: [book.author],
      imageUrl: [book.imageUrl],
    });
  }

  deleteBook(id: any): any {
    this.bookService.deleteBook(id).subscribe(
      (res) => {
        console.log('deleted book', res);

      },
      (error) => {
        console.log('book not deleted');
      }
    );
  }

}
