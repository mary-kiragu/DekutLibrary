export enum Status{
  NEW="NEW",
  BORROWED="BORROWED",
  RETURNED="RETURNED"

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
  accessionNumber?:string,
  dueDate?:string
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
      this.dueDate=dueDate


  }

}
