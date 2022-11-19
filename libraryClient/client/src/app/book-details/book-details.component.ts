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
extractedbookText:any;

comment={
  id:'',
  content:'',
  user:'',
  book:'',
  createdOn:'',
  createdBy:'',
  referencedCommentId:'',
  lastUpdatedOn:'',
  lastUpdatedBy:''
}
foundComment:any;

commentForm=this.formBuilder.group({
  content:[],
  book:[],
  referencedCommentId:[]

})

comments:any[]= [];
replies:any[]= [];
differenceInDays:any
differenceInSeconds:any
differenceInMinutes:any
differenceInHours:any

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
        this.getBookText()
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

          //window.open(this.src,'height=650,width=840');



        }

        this.getCommentByBook()




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
//comments

  getOneComment(id:number):void{
    this.bookService.findCommentbyId(id).subscribe(
      (res)=>{
        console.log("one comment",res)
        this.comment=res;
        console.log("one comment",this.comment.id)
        this.getReplies(this.comment.referencedCommentId)
      })
    }

  extractCommentDetails():any{
    return {
      content: this.commentForm.get('content')!.value,
      referencedCommentId: this.commentForm.get('referencedCommentId')!.value,
      book:this.book.id

    }


  }
  extractReplyDetails():any{
    return {
      content: this.commentForm.get('content')!.value,
      referencedCommentId:this.comment.id,
      book:this.book.id

    }


  }



  createComment():any{
   const commentdetail=this.extractCommentDetails()
   console.log("coment before",commentdetail)
   this.bookService.createComment(commentdetail).subscribe(
    (res)=>{
      console.log(res)
    },
    (err)=>{
      console.log(err)
    }
    )

  }


  createCommentReply():any{
    const commentdetail=this.extractReplyDetails()
    console.log("coment before",commentdetail)
    this.bookService.createComment(commentdetail).subscribe(
     (res)=>{
       console.log(res)
     },
     (err)=>{
       console.log(err)
     }
    )

   }

   getReplies(referencedCommentId:any):void{
    this.bookService.filterCommentsByReferencedId(referencedCommentId).subscribe(
      (res)=>{
        console.log("yeey",res)
      }
    )



   }
  getCommentByBook():any{
    console.log("about to get")
    this.bookService.getDiscussionByBook(this.book).subscribe(
      (res)=>{
        console.log("coments",res)
        this.comments=res;
        console.log("comments",this.comments)
        this.comments.forEach((comment:any)=>{
          let now= new Date(new Date().toJSON().slice(0, 10));
          var createdOn= new Date(comment.createdOn);
          var differenceInTime = now.getTime() -createdOn.getTime() ;
          console.log(differenceInTime)
          this.differenceInDays = differenceInTime / (1000 * 3600 * 24);
          console.log("Difference in dayS ", this.differenceInDays)

          this.differenceInSeconds = Math.floor(differenceInTime/1000);
         this.differenceInMinutes = Math.floor(this.differenceInSeconds/60);
         this.differenceInHours = Math.floor(this.differenceInMinutes/60);

         if (this.differenceInSeconds <1000){
          comment.createdOn=this.differenceInSeconds
          console.log("diff sec", comment.createdOn)

         }
         if (this.differenceInMinutes<60){
          comment.createdOn=this.differenceInMinutes
          console.log("diff mins", comment.createdOn)

         }
         if (this.differenceInHours<60){
          comment.createdOn=this.differenceInHours
          console.log("diff hours", comment.createdOn)

         }
         //comment.createdOn = this.differenceInDays;


        })


      },
      (err)=>
      {
        console.log("errorrr")

      }
    )
  }

  getBookText() {
    //this.loadingShortlisted = true;
    console.log("=========================");
    console.log("+++++", this.book.bookUrl);


    this.gettext(this.book.bookUrl).then((text: string) => {
     this.extractedbookText = text;
      console.log("Extracted book", this.extractedbookText)

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
    return pdf.promise.then(function (pdf: any) { // get all pages text
      var maxPages = pdf.numPages;
      var countPromises = []; // collecting all page promises
      for (var j = 1; j <= maxPages; j++) {
        var page = pdf.getPage(j);

        var txt = "";
        countPromises.push(page.then(function (page: any) { // add page promise
          var textContent = page.getTextContent();
          return textContent.then(function (text: any) { // return content promise
            return text.items.map(function (s: any) { return s.str; }).join(''); // value page text
          });
        }));
      }
      // Wait for all pages and join text
      return Promise.all(countPromises).then(function (texts) {
        return texts.join('');
      });
    });
  }
//   speak():any{

//     const synth = window.speechSynthesis;

//   const spokenbook = new SpeechSynthesisUtterance(this.extractedbookText);
//   const voices=synth.getVoices();
//   console.log("voices",voices)

//   const amISpeaking = synth.speaking;
//   console.log("jj",amISpeaking)
// if(amISpeaking){
//   synth.pause();
//   return{}


// }
// if(!amISpeaking){
//   synth.speak(spokenbook);
// }



//   console.log("jj",amISpeaking)


//   }
//    getVoices():any {

//     window.speechSynthesis.getVoices();
//   return window.speechSynthesis.getVoices();
// }



  // pauseAndResume(): any{
  //   const synth = window.speechSynthesis;
  //   const spokenbook = new SpeechSynthesisUtterance(this.extractedbookText);

  // const amISpeaking = synth.speaking;
  // synth.pause();


  // }

 speak(text:any, config:any):any {
    if (window.speechSynthesis) {
      var msg = new SpeechSynthesisUtterance();
    }

    function getVoices() {
      window.speechSynthesis.getVoices();
      return window.speechSynthesis.getVoices();
    }

    function sayIt() {
      var voices = getVoices();

      //choose voice. Fallback to default
      msg.voice =
        config && config.voiceIndex ? voices[config.voiceIndex] : voices[0];
      msg.volume = config && config.volume ? config.volume : 1;
      msg.rate = config && config.rate ? config.rate : 1;
      msg.pitch = config && config.pitch ? config.pitch : 1;

      //message for speech
      msg.text = text;

      speechSynthesis.speak(msg);
    }

    return {
      sayText: sayIt,
      getVoices: getVoices
    };
  };

// pitch:any
// rate:any
// volume:any



    function (scope:any, timeout:any, speech:any) {
        scope.support = false;
        if (window.speechSynthesis) {
          scope.support = true;

          timeout(function () {
            scope.voices = speech.getVoices();
          }, 500);
        }

        scope.this.pitch = 1;
        scope.this.rate = 1;
        scope.this.volume = 1;


        scope.submitEntry = function () {
          var voiceIdx = scope.voices.indexOf(scope.optionSelected),
            config = {
              voiceIndex: voiceIdx,
              rate: scope.rate,
              pitch: scope.pitch,
              volume: scope.volume
            };

          if (window.speechSynthesis) {
            speech.sayText(scope.msg, config);
          }
        };
      }








}
