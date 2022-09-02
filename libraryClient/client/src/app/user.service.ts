import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from './login/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiServerUrl=environment.API_ENDPOINT;


  constructor(private httpClient:HttpClient) { }

  register(user:User):Observable<User>{
    return this.httpClient.post<User>(this.apiServerUrl+"/api/register",user);
  }
  authenticate(user:User):Observable<any>{
    return this.httpClient.post<any>(this.apiServerUrl+"/api/authenticate",user);
  }
}
