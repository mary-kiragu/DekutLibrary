export enum Status{
  ISSUED="ISSUED",
  BORROWED="BORROWED",
  AVAILABLE="AVAILABLE"

}

export class Book{
  id?:number;
  title?:string;
  author?:string;
  status?:Status;
  borrowedBy?:string;
  borrowedOn?:string;
  returnedOn?:string;
  issuedBy?:string;
  imageUrl?:string;
  categoryId?:number;
  isbn?:string;
  accessionNumber?:string;
  dueDate?:string;
  bookImageUrl?:string;
  name?:string;
  type?:string;
  data?:string;
  size?:string;
  fine?:number;
bookUrl: any;
  constructor(
    id?:number,
    title?:string,
    author?:string,
    status?:Status,
    borroweBy?:string,
    borrowedOn?:string,
    returnedOn?:string,
    issuedBy?:string,
    imageUrl?:string,
    categoryId?:number,
    isbn?:string,
    fine?:number,
  accessionNumber?:string,
  dueDate?:string,
  bookImageUrl?:string,
  name?:string,
  type?:string,
  data?:string,
  size?:string,
    ){

      this.id=id;
      this.title=title;
      this.author=author;
      this.borrowedBy=borroweBy;
      this.borrowedOn=borrowedOn;
      this.issuedBy=issuedBy;
      this.returnedOn=returnedOn;
      this.imageUrl=imageUrl;
      this.categoryId=categoryId;
      this.accessionNumber=accessionNumber;
      this.isbn=isbn;
      this.fine=fine;
      this.dueDate=dueDate;
      this.bookImageUrl=bookImageUrl;
      this.name=name;
      this.type=type;
      this.data=data;
      this.size=size;



  }

}
