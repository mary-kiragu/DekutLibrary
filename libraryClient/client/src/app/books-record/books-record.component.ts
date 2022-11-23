import { Component, OnInit } from "@angular/core";
import { Book } from "../books/book.model";
import { BookService } from "../book.service";
import { FormBuilder } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { UserService } from "../user.service";
import { SubscriptionComponent } from "../subscription/subscription.component";
import { SubscriptionService } from "../subscription.service";

@Component({
  selector: "app-books-record",
  templateUrl: "./books-record.component.html",
  styleUrls: ["./books-record.component.css"],
})
export class BooksRecordComponent implements OnInit {
  book = {
    id: "",
    isbn: "",
    title: "",
    author: "",
    description: "",
    fine: "",
    imageUrl: "",
    bookImageUrl: "",
    name: "",
    type: "",
    data: "",
    size: "",
    categoryId: "",
    accessionNumber: "",
    bookUrl: "",
    bookName: "",
    bookType: "",
    bookData: "",
    bookSize: "",
  };
  // book: Book = {} as Book;
  bookId!: number;
  bookForm = this.formBuilder.group({
    isbn: [],
    title: [],
    author: [],
    description: [],
    imageUrl: [],
    fine: [],
    bookImageUrl: [],
    name: [],
    type: [],
    data: [],
    size: [],
    categoryId: [],
    accessionNumber: [],
    bookUrl: [],
    bookName: [],
    bookType: [],
    bookData: [],
    bookSize: [],
  });
  books: Book[] = [];
  user: any;
  uploadFile = true;
  paymentRequest: any;
  paymentRequestForm = this.formBuilder.group({
    phoneNumber: [""],
    userId: [""],
    bookId: [""],
  });

  issuedBook: any;
  booksToRender: Book[] = [];

  totalBorrowed = 0;
  totalIssued = 0;
  totalAvailable = 0;

  selectedBook: Book | null = null;
  activeFilter: "all" | "available" | "borrowed" | "issued" = "all";

  constructor(
    private bookService: BookService,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private subscriptionService: SubscriptionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.getAllBooks();
    const id = Number(this.route.snapshot.paramMap.get("id"));
    if (id) {
      this.getOne(id);
    }
    this.getCurrentUser();
  }

  getCurrentUser(): void {
    this.userService.getProfile().subscribe((userProfile) => {
      this.user = userProfile;
      console.log("user profs", this.user);
    });
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
        console.log("book not found");
      }
    );
  }
  url: any;
  msg = "";

  selectFile(event: any) {
    if (!event.target.files[0] || event.target.files[0].length == 0) {
      this.msg = "You must select an image";
      return;
    }

    console.log("Selected, ", event.target.files[0]);
    var bookImageName = event.target.files[0].name;
    var bookImageType = event.target.files[0].type;
    var bookImageSize = event.target.files[0].size;

    if (bookImageType.match(/image\/*/) == null) {
      this.msg = "Only images are supported";
      return;
    }
    var reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);

    reader.onload = (_event) => {
      this.msg = "";
      this.url = reader.result;
      const bookImageData = this.url.split("base64,")[1];
      this.book.bookImageUrl = this.url;
      this.book.name = bookImageName;
      this.book.type = bookImageType;
      this.book.data = bookImageData;
      this.book.size = bookImageSize;
      console.log("Selected image data, ", bookImageData);
    };
  }

  bookUrl: any;
  bookMsg = "";

  selectBook(event: any) {
    if (!event.target.files[0] || event.target.files[0].length == 0) {
      this.bookMsg = "You must select a pdf";
      return;
    }

    console.log("Selected, ", event.target.files[0]);
    var bookName = event.target.files[0].name;
    var bookType = event.target.files[0].type;
    var bookSize = event.target.files[0].size;

    // if (bookType.match(/image\/*/) == null) {
    //   this.msg = "Only images are supported";
    //   return;
    // }
    var reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);

    reader.onload = (_event) => {
      this.bookMsg = "";
      this.bookUrl = reader.result;
      const bookData = this.bookUrl.split("base64,")[1];
      this.book.bookUrl = this.bookUrl;
      this.book.bookName = bookName;
      this.book.bookType = bookType;
      this.book.bookData = bookData;
      this.book.bookSize = bookSize;
      console.log("Selected pdf data, ", bookData);
    };
  }


  extractBookDetails(): any {
    return {
      author: this.bookForm.get("author")!.value,
      isbn: this.bookForm.get("isbn")!.value,
      title: this.bookForm.get("title")!.value,
      description: this.bookForm.get("description")!.value,
      imageUrl: this.bookForm.get("imageUrl")!.value,
      accessionNumber: this.bookForm.get("accessionNumber")!.value,
      bookImageUrl: this.book.bookImageUrl,
      name: this.book.name,
      type: this.book.type,
      data: this.book.data,
      size: this.book.size,
      bookUrl: this.book.bookUrl,
      bookName: this.book.bookName,
      bookType: this.book.bookType,
      bookData: this.book.bookData,
      bookSize: this.book.bookSize,
    };
  }

  getAllBooks(): void {
    this.bookService.getAllBooks().subscribe((res: any) => {
      console.log("Array of books available", res);
      this.books = res;
      this.booksToRender = res as Book[];
      this.getSubtotalsByStatus(res);
    });
  }
  getSubtotalsByStatus(allBooks: Book[]): any {
    this.totalBorrowed = allBooks.filter(
      (book) => book.status === "BORROWED"
    ).length;
    this.totalAvailable = allBooks.filter(
      (book) => book.status === "AVAILABLE"
    ).length;
    this.totalIssued = allBooks.filter(
      (book) => book.status === "ISSUED"
    ).length;
  }
  addNewBook(): any {
    const bookDetails = this.extractBookDetails();
    console.log(bookDetails);

    this.bookService.createBook(bookDetails).subscribe(
      (res) => {
        console.log("created book", res);
        this.getAllBooks();
      },
      (err) => {
        console.log("book not created");
      }
    );
  }

  returnBook(book: Book): any {
    if (book.status === "ISSUED") {
      console.log("book to be returned", book);
      this.bookService.returnBook(book).subscribe(
        (res) => {
          console.log("book returned: ", res);
          this.getAllBooks();
        },
        (error) => {
          console.log("Book return failed");
        }
      );
    } else {
      throw new Error("Book not borrowed");
    }
  }

  deleteBook(id: any): any {
    this.bookService.deleteBook(id).subscribe(
      (res) => {
        console.log("deleted book", res);
        this.getAllBooks();
      },
      (error) => {
        console.log("book not deleted");
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

  updateBook(bookId: any): void {
    const bookDetails = this.extractBookDetails();
    bookDetails.id = bookId;
    console.log("book details", bookDetails);
    this.bookService.updateBook(bookDetails).subscribe(
      (res) => {
        console.log("updated book", res);
        this.getAllBooks();
      },
      (err) => {
        console.log("book not updated book", err);
      }
    );
  }
  showInputUrl(): any {
    this.uploadFile = false;
  }
  showUploadFile(): any {
    this.uploadFile = true;
  }

  setSelectedBook(book: Book) {
    this.selectedBook = book;
    this.bookForm = this.formBuilder.group({
      id: [book.id],
      title: [book.title],
      author: [book.author],
      isbn: [book.isbn],
      imageUrl: [book.imageUrl],
      bookImageUrl: [book.bookImageUrl],
      bookUrl: [book.bookImageUrl],

      accessionNumber: [book.accessionNumber],
      description: [book.description],
    });
  }

  save(): void {
    console.log(this.bookId);
    if (this.bookId) {
      this.updateBook(this.bookId);
    } else {
      this.addNewBook();
    }
  }

  extractPaymentRequestDetails(): any {
    return {
      phoneNumber: this.paymentRequestForm.get("phoneNumber")!.value,
      bookId: this.book.id,
      userId: this.user.id,
    };
  }

  filter(status: "all" | "available" | "borrowed" | "issued"): void {
    this.activeFilter = status;
    if (status === "all") {
      this.booksToRender = this.books;
      return;
    }
    this.booksToRender = this.books.filter(
      (book: Book) => book.status === status.toUpperCase()
    );
  }

  issueBook(book: Book): any {
    if (book.status === "BORROWED") {
      console.log("book to be issued", book);
    }
    this.bookService.issueBook(book).subscribe(
      (res) => {
        this.issuedBook = res;
        console.log("issued book", this.issuedBook);
        this.getAllBooks();
        // const id=Number(this.route.snapshot.paramMap.get("id"));
        // if(id){
        //   this.getOne(id);

        // }
      },
      (err) => {}
    );
  }
  initiateFPayment(): any {
    this.paymentRequest = this.extractPaymentRequestDetails();
    console.log("about to initiate payment", this.paymentRequest);
    this.subscriptionService.initiateFinePayment(this.paymentRequest).subscribe(
      (res) => {
        console.log("paymentrequest", res);

        this.wait(25000);
        console.log("wait");
        this.user = this.getCurrentUser();

        if (this.user.accountStatus === "PAID") {
          this.router.navigate(["/categories"]);
        }
      },
      (err) => {
        console.log("error", err);
      }
    );
  }
  wait(ms: number): any {
    var start = new Date().getTime();
    var end = start;
    while (end < start + ms) {
      end = new Date().getTime();
    }
  }

  setBookId(bookId: any) {
    this.book.id = bookId;
    console.log(this.book.id);
  }
}
