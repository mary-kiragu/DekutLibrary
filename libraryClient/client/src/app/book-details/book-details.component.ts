import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Book } from "../books/book.model";
import { BookService } from "../book.service";
import { FormBuilder } from "@angular/forms";
import { UserService } from "../user.service";
import { User } from "../login/user.model";

@Component({
  selector: "app-book-details",
  templateUrl: "./book-details.component.html",
  styleUrls: ["./book-details.component.css"],
})
export class BookDetailsComponent implements OnInit {
  book = {
    id: "",
    isbn: "",
    title: "",
    author: "",
    imageUrl: "",
    description: "",
    bookImageUrl: "",
    status: "",
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
  bookId!: number;
  bookForm = this.formBuilder.group({
    isbn: [],
    title: [],
    author: [],
    description: [],
    imageUrl: [],
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
  srcPDF!: string;
  user!: User;

  //book!:any;
  borrowedBook: any;
  id!: number;
  extractedbookText: any;

  comment = {
    id: "",
    content: "",
    user: "",
    book: "",
    createdOn: "",
    createdBy: "",
    referencedCommentId: "",
    lastUpdatedOn: "",
    lastUpdatedBy: "",
  };
  foundComment: any;

  commentForm = this.formBuilder.group({
    content: [],
    book: [],
    referencedCommentId: [],
  });

  comments: any[] = [];
  replies: any[] = [];
  differenceInDays: any;
  differenceInSeconds: any;
  differenceInMinutes: any;
  differenceInHours: any;

  showPdfModal = false;

  speechState: "off" | "speaking" | "paused" = "off";

  synth = window.speechSynthesis;

  constructor(
    private bookService: BookService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
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

  getOne(id: number): void {
    this.bookService.findbyId(id).subscribe(
      (res) => {
        console.log(res);
        this.book = res;
        this.getBookText();
        console.log("finding book ", this.book);
        if (res.bookType == "application/pdf") {
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
          console.log("Testing pdf *****", this.srcPDF);

          //window.open(this.src,'height=650,width=840');
        }
        this.comments.sort();
        this.getCommentByBook();
      },
      (err) => {
        console.log("book not found");
      }
    );
  }

  borrowBook(): any {
    this.bookService.borrowBook(this.book).subscribe(
      (res) => {
        this.borrowedBook = res;
        console.log(this.borrowedBook);
        const id = Number(this.route.snapshot.paramMap.get("id"));
        if (id) {
          this.getOne(id);
        }
      },
      (err) => {}
    );
  }

  extractBookDetails(): any {
    return {
      author: this.bookForm.get("author")!.value,
      isbn: this.bookForm.get("isbn")!.value,
      title: this.bookForm.get("title")!.value,
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

  updateBook(bookId: number): void {
    const bookDetails = this.extractBookDetails();
    bookDetails.id = bookId;
    console.log("book details", bookDetails);
    this.bookService.updateBook(bookDetails).subscribe(
      (res) => {
        console.log("updated book", res);
      },
      (err) => {
        console.log("book not updated book", err);
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
        console.log("deleted book", res);
      },
      (error) => {
        console.log("book not deleted");
      }
    );
  }
  //comments

  getOneComment(id: number): void {
    this.bookService.findCommentbyId(id).subscribe((res) => {
      console.log("one comment", res);
      this.comment = res;
      console.log("one comment", this.comment.id);
      this.getReplies(this.comment.referencedCommentId);
    });
  }

  extractCommentDetails(): any {
    return {
      content: this.commentForm.get("content")!.value,
      referencedCommentId: this.commentForm.get("referencedCommentId")!.value,
      book: this.book.id,
    };
  }
  extractReplyDetails(): any {
    return {
      content: this.commentForm.get("content")!.value,
      referencedCommentId: this.comment.id,
      book: this.book.id,
    };
  }

  createComment(): any {
    const commentdetail = this.extractCommentDetails();
    console.log("coment before", commentdetail);
    this.bookService.createComment(commentdetail).subscribe(
      (res) => {
        console.log(res);
        res.createdOn = this.timeSince(new Date(res.createdOn));
        // res.createdBy = "You";
        this.comments.unshift(res);
        this.commentForm.reset();
      },
      (err) => {
        console.log(err);
      }
    );
  }
  updateComment(commentId: number): void {
    const commentDets = this.extractCommentDetails();
    commentDets.id = commentId;
    console.log("comment details", commentDets);
    this.bookService.updateComment(commentDets).subscribe(
      (res) => {
        console.log("updated comment", res);
      },
      (err) => {
        console.log("comment not updated book", err);
      }
    );
  }
  createCommentReply(): any {
    const commentdetail = this.extractReplyDetails();
    console.log("coment before", commentdetail);
    this.bookService.createComment(commentdetail).subscribe(
      (res) => {
        console.log(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }
  deleteComment(id: any): any {
    this.bookService.deleteComment(id).subscribe(
      (res) => {
        console.log("deleted comment", res);
        this.getCommentByBook();
      },
      (error) => {
        console.log("comment not deleted");
      }
    );
  }

  getReplies(referencedCommentId: any): void {
    this.bookService
      .filterCommentsByReferencedId(referencedCommentId)
      .subscribe((res) => {
        console.log("yeey", res);
      });
  }
  getCommentByBook(): any {
    console.log("about to get");
    this.bookService.getDiscussionByBook(this.book).subscribe(
      (res) => {
        console.log("coments", res);
        this.comments = res.reverse();
        console.log("comments", this.comments);
        this.comments.forEach((comment: any) => {
          console.log("--createdOn", comment.createdOn);
          comment.createdOn = this.timeSince(new Date(comment.createdOn));
        });
      },
      (err) => {
        console.log("errorrr");
      }
    );
  }
  timeSince(date: Date) {
    //@ts-ignore
    let seconds = Math.floor((new Date() - date) / 1000);

    let interval = seconds / 31536000;

    if (interval > 1) {
      return Math.floor(interval) + " years";
    }
    interval = seconds / 2592000;
    if (interval > 1) {
      return Math.floor(interval) + " months";
    }
    interval = seconds / 86400;
    if (interval > 1) {
      return Math.floor(interval) + " days";
    }
    interval = seconds / 3600;
    if (interval > 1) {
      return Math.floor(interval) + " hours";
    }
    interval = seconds / 60;
    if (interval > 1) {
      return Math.floor(interval) + " minutes";
    }
    return Math.floor(seconds) + " seconds";
  }
  getBookText() {
    //this.loadingShortlisted = true;
    console.log("=========================");
    console.log("+++++", this.book.bookUrl);

    this.gettext(this.book.bookUrl).then(
      (text: string) => {
        this.extractedbookText = text;
        console.log("Extracted book", this.extractedbookText);

        // this.extractResume();
      },
      function (reason: string) {
        console.error(reason);
      }
    );
  }

  gettext(pdfUrl: string) {
    //this.loadingShortlisted = true;
    // @ts-ignore
    var pdf = window.pdfjsLib.getDocument(pdfUrl);
    return pdf.promise.then(function (pdf: any) {
      // get all pages text
      var maxPages = pdf.numPages;
      var countPromises = []; // collecting all page promises
      for (var j = 1; j <= maxPages; j++) {
        var page = pdf.getPage(j);

        var txt = "";
        countPromises.push(
          page.then(function (page: any) {
            // add page promise
            var textContent = page.getTextContent();
            return textContent.then(function (text: any) {
              // return content promise
              return text.items
                .map(function (s: any) {
                  return s.str;
                })
                .join(""); // value page text
            });
          })
        );
      }
      // Wait for all pages and join text
      return Promise.all(countPromises).then(function (texts) {
        return texts.join("");
      });
    });
  }
  speak(): void {
    console.log(this.synth.speaking);

    let voices = this.synth.getVoices();
    voices = this.synth.getVoices();
    console.log(voices);

    console.log(voices[8]);
    console.log("text to read", this.extractedbookText);

    const spokenbook = new SpeechSynthesisUtterance(this.extractedbookText);
    spokenbook.voice = voices[8];
    spokenbook.rate = 1;
    spokenbook.pitch = 1;
    this.synth.speak(spokenbook);
    this.speechState = "speaking";
    console.log(this.synth.speaking);
  }
  stop() {
    this.synth.cancel();
    this.speechState = "off";
  }
  resume() {
    this.synth.resume();
    this.speechState = "speaking";
  }
  getVoices(): any {
    window.speechSynthesis.getVoices();
    return window.speechSynthesis.getVoices();
  }
}
