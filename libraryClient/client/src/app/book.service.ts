import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from 'src/environments/environment';
import { Book } from './books/book.model';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private apiServerUrl=environment.API_ENDPOINT;

  constructor(private httpClient:HttpClient) {

   }

   createBook(book:Book):Observable<Book>{
    return this.httpClient.post<Book>(this.apiServerUrl+"/api/books",book);


  }
  updateBook(book:Book):Observable<Book>{
    return this.httpClient.put<Book>(this.apiServerUrl+"/api/books/update/"+book.id,book);
  }

  getAllBooks():Observable<Book>{
    return this.httpClient.get<Book>(this.apiServerUrl+"/api/books");
  }

  findbyId(id:number):Observable<any>{
    return this.httpClient.get<any>(this.apiServerUrl+"/api/books/"+id);
  }

   borrowBook(book:any):Observable<any>{
     return this.httpClient.put<any>(this.apiServerUrl+"/api/books/borrow/"+book.id,book);
   }


   returnBook(book:Book):Observable<Book>{
    return this.httpClient.put<Book>(this.apiServerUrl+"/api/books/return/"+book.id,book);
  }
  deleteBook(id:number):Observable<Book>{
    return this.httpClient.delete<Book>(this.apiServerUrl+"/api/books/"+id)
  }

  filterByParent(categoryId: number): Observable<any> {

    return this.httpClient.get(this.apiServerUrl + "/api/books/filter-by-category/" + categoryId);
  }
  searchBooks(text?: string): Observable<any> {
    return this.httpClient.get(this.apiServerUrl + '/api/books/search?text='  + text);
  }

}
