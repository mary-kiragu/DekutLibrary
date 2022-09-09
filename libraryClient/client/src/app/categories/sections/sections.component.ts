import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/book.service';
import { Book } from 'src/app/books/book.model';
import { CategoriesService } from '../../categories.service';

@Component({
  selector: 'app-sections',
  templateUrl: './sections.component.html',
  styleUrls: ['./sections.component.css']
})
export class SectionsComponent implements OnInit {

  bookId!: number;
  bookForm = this.formBuilder.group({
    title: [],
    author: [],
    imageUrl: [],
    categoryId:[],
  });
  books: any = [];
  category: any;
  bookCategory:any;
  book: Book = {} as Book;
  loadingBooks = false;
  constructor(
    private bookService: BookService,
    private categoriesService:CategoriesService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.getCategory(id);
    }
  }

  getCategory(id: number) {

    this.categoriesService.getOne(id).subscribe(
      (res) => {
        this.category = res;


        this.category.parents?.forEach((parentCategory: any) => {
          this.bookCategory = parentCategory.id;
        });
        console.log('category : ', this.category);

         this.filterBooksByCategory(this.category.id);

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
      author: this.bookForm.get('title')!.value,
      title: this.bookForm.get('author')!.value,
      imageUrl: this.bookForm.get('imageUrl')!.value,
      categoryId: this.category.id,
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
    if (this.book.id) {
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
        this.books = res;

        this.loadingBooks = false;


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



}
