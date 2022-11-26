import { User } from "../login/user.model";

export enum Status {
  ISSUED = "ISSUED",
  BORROWED = "BORROWED",
  AVAILABLE = "AVAILABLE",
}
export enum Action {
  BORROW = "BORROW",
  RETURN = "RETURN",
}

export class Book {
  id?: number;
  title?: string;
  author?: string;
  description?: string;
  status?: Status;
  borrowedBy?: string;
  borrowedOn?: string;
  returnedOn?: string;
  issuedBy?: string;
  issuedOn?: string;
  imageUrl?: string;
  categoryId?: number;
  isbn?: string;
  accessionNumber?: string;
  dueDate?: string;
  bookImageUrl?: string;
  name?: string;
  type?: string;
  data?: string;
  size?: string;
  fine: number;
  bookUrl: any;
  constructor(
    id?: number,
    title?: string,
    author?: string,
    status?: Status,
    borroweBy?: string,
    borrowedOn?: string,
    returnedOn?: string,
    issuedBy?: string,
    imageUrl?: string,
    categoryId?: number,
    isbn?: string,
    fine: number = 0,
    issuedOn?: string,
    description?: string,
    accessionNumber?: string,
    dueDate?: string,
    bookImageUrl?: string,
    name?: string,
    type?: string,
    data?: string,
    size?: string
  ) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.description = description;
    this.borrowedBy = borroweBy;
    this.borrowedOn = borrowedOn;
    this.issuedOn = issuedOn;
    this.issuedBy = issuedBy;
    this.returnedOn = returnedOn;
    this.imageUrl = imageUrl;
    this.categoryId = categoryId;
    this.accessionNumber = accessionNumber;
    this.isbn = isbn;
    this.fine = fine || 0;
    this.dueDate = dueDate;
    this.bookImageUrl = bookImageUrl;
    this.name = name;
    this.type = type;
    this.data = data;
    this.size = size;
  }
}

export class BorrowHistory {
  id?: number;
  user?: User;
  book?: Book;
  action?: Action;
  createdOn?: string;
  dueDate?: string;

  constructor(
    id?: number,
    user?: User,
    dueDate?: string,
    book?: Book,
    action?: Action,
    createdOn?: string
  ) {
    this.id = id;
    this.user = user;
    this.book = book;
    this, (action = action);
    this.createdOn = createdOn;
    this.dueDate = dueDate;
  }
}
