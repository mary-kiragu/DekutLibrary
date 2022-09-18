import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/book.service';
import { Book } from 'src/app/books/book.model';
import { User } from 'src/app/login/user.model';
import { CategoriesService } from '../../categories.service';
import { UserService } from '../../user.service';




@Component({
  selector: 'app-sections',
  templateUrl: './sections.component.html',
  styleUrls: ['./sections.component.css']
})
export class SectionsComponent implements OnInit {

  bookId!: number;
  bookForm = this.formBuilder.group({
    isbn: [],
    title: [],
    author: [],
    imageUrl: [],
    categoryId:[],
    accessionNumber:[]
  });
  books: any = [];
  category: any;
  bookCategory:any;
  book!: Book;
  loadingBooks = false;
  searchText: string = '';
  foundBooks:any;
  foundCategories:any;
  booksToRender: Book[]=[];
  booksFromDB: Book[] = [];
  isFiltered = false;
  user!:any;
  constructor(
    private bookService: BookService,
    private categoriesService:CategoriesService,
    private userService:UserService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.getCategory(id);
      this.getCurrentUser();
    }
  }

  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);

      });

  }


  getCategory(id: number) {

    this.categoriesService.getOne(id).subscribe(
      (res) => {
        this.category = res;


        this.category.parents?.forEach((parentCategory: any) => {
          this.bookCategory = parentCategory.id;
        });
        console.log('category : ', this.category);

         this.books=this.filterBooksByCategory(this.category.id);

         //console.log("subcats",this.getSubCategories(this.category.id));
      },
      (err) => {
        console.log(err);
      }
    );
  }
  addNewBook(): any {
    const bookDetails = this.extractBookDetails();
    console.log(bookDetails);

    this.bookService.createBook(bookDetails).subscribe(
      (res) => {
        console.log('created book', res);

      },
      (err) => {
        console.log('book not created');
      }
    );
  }


  extractBookDetails(): Book {
    return {
      title: this.bookForm.get('title')!.value,
      isbn: this.bookForm.get('isbn')!.value,
      author: this.bookForm.get('author')!.value,
      imageUrl: this.bookForm.get('imageUrl')!.value,
      categoryId: this.category.id,
      accessionNumber:this.bookForm.get('accessionNumber')!.value,
    };

  }

  setBook(): void {
    this.book = {
      id: undefined,
      title: '',
      author: '',
      imageUrl: '',
    };
  }

  resetBorrowBookForm(): void {
    this.bookForm.reset();
    this.bookId = undefined as any;
    this.setBook();
  }

  updateBook(): void {
    const bookDetails = this.extractBookDetails();
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

  save(): void {
    if (this.bookId) {
      this.updateBook();
    } else {
      this.addNewBook();
    }
  }

  filterBooksByCategory(id: number): void {
    this.loadingBooks = true;

    this.bookService.filterByParent(id).subscribe(
      (res) => {
        // console.log("Video Data", res);
        this.booksFromDB = res;
        console.log("books:",this.booksFromDB);

        this.loadingBooks = false;
        this.booksToRender=this.booksFromDB;


        // this.books.forEach((book: any) => {
        //   //video.duration = this.videoDuration(video.duration);
        // });

      },
      (err) => {
        this.loadingBooks = false;

        console.log("Error retrieving data", err);
      }
    );
  }

  SearchBooks(): void {

    this.bookService.searchBooks(this.searchText.trim()).subscribe(
      (result )=> {
        console.log(result);
        this.foundBooks=result;
        console.log("found",this.foundBooks)

   },

    (error) => {
        console.error('error getting books', error);
      }
    );
  }

  filter(): void {
    let filteredbooks = this.booksFromDB.filter((book)=>book.title?.includes(this.searchText))
    console.log(filteredbooks)
      this.booksToRender = filteredbooks;
      this.isFiltered = true;
    if(this.searchText == "") {
this.isFiltered = false
    }
  }
  unfilter(): void {
    this.booksToRender = this.booksFromDB;
    this.isFiltered = false
  }



}
