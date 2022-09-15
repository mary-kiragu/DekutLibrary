import { Injectable } from '@angular/core';
import { catchError, Observable, of, shareReplay } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from './login/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiServerUrl=environment.API_ENDPOINT;
  private userProfile!: Observable<any>;
  private accountCache$?: Observable<any | null>;


  constructor(private httpClient:HttpClient) { }

  getProfile(force?: boolean): Observable<any> {
    if (!this.userProfile || force) {
      this.userProfile = this.httpClient.get(this.apiServerUrl + "/api/user/profile");
    }
    return this.userProfile;
  }

  identity(force?: boolean): Observable<any | null> {
    if (!this.accountCache$ || force || !this.userProfile == null) {
      this.accountCache$ = this.getProfile().pipe(
        catchError(() => of(null)),
        shareReplay()
      );
    }
    return this.accountCache$;
  }


  register(user:User):Observable<User>{
    return this.httpClient.post<User>(this.apiServerUrl+"/api/register",user);
  }
  authenticate(user:User):Observable<any>{
    return this.httpClient.post<any>(this.apiServerUrl+"/api/authenticate",user);
  }
}
