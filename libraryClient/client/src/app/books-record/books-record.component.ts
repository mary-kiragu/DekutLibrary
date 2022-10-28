import { Component, OnInit } from '@angular/core';
import { Book } from '../books/book.model';
import { BookService } from '../book.service';
import { FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-books-record',
  templateUrl: './books-record.component.html',
  styleUrls: ['./books-record.component.css'],
})
export class BooksRecordComponent implements OnInit {
  book={
    id:'',
    isbn: '',
    title: '',
    author: '',
    imageUrl: '',
    bookImageUrl:'',
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
  // book: Book = {} as Book;
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
  books: any = [];

  constructor(
    private bookService: BookService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.getAllBooks();
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.getOne(id);
    }
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
  getOne(id: number): void {
    this.bookService.findbyId(id).subscribe(
      (res) => {
        console.log(res);
        // this.book = res;
      },
      (err) => {
        console.log('book not found');
      }
    );
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

  getAllBooks(): void {
    this.bookService.getAllBooks().subscribe((res) => {
      console.log('Array of books available', res);

      this.books = res;
    });
  }

  addNewBook(): any {
    const bookDetails = this.extractBookDetails();
    console.log(bookDetails);

    this.bookService.createBook(bookDetails).subscribe(
      (res) => {
        console.log('created book', res);
        this.getAllBooks();
      },
      (err) => {
        console.log('book not created');
      }
    );
  }

  returnBook(book: Book): any {
    if (book.status === 'BORROWED') {
      console.log('book to be returned', book);
      this.bookService.returnBook(book).subscribe(
        (res) => {
          console.log('book returned: ', res);
          this.getAllBooks();
        },
        (error) => {
          console.log('Book return failed');
        }
      );
    } else {
      throw new Error('Book not borrowed');
    }
  }

  deleteBook(id: any): any {
    this.bookService.deleteBook(id).subscribe(
      (res) => {
        console.log('deleted book', res);
        this.getAllBooks();
      },
      (error) => {
        console.log('book not deleted');
      }
    );
  }

  // setBook(): void {
  //   this.book = {
  //     id: 'undefined',
  //     title: '',
  //     author: '',
  //     imageUrl: '',
  //   };
  // }

  resetBorrowBookForm(): void {
    this.bookForm.reset();
    this.bookId = undefined as any;
    //this.setBook();
  }

  updateBook(bookId:number): void {
    const bookDetails = this.extractBookDetails();
    bookDetails.id=bookId;
    console.log('book details', bookDetails);
    this.bookService.updateBook(bookDetails).subscribe(
      (res) => {
        console.log('updated book', res);
        this.getAllBooks();
      },
      (err) => {
        console.log('book not updated book', err);
      }
    );
  }

  save(): void {
    console.log(this.bookId)
     if (this.bookId) {
      this.updateBook(this.bookId);
    } else {
      this.addNewBook();
    }
  }
}
