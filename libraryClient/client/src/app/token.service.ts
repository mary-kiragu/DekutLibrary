import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  JWT_KEY = 'jwt';
  USER_KEY='user';

  public saveToken(token: string): void {
    localStorage.setItem(this.JWT_KEY, token);
  }

  public clearToken(): void {
    localStorage.removeItem(this.JWT_KEY);
  }

  public getToken(): any {
    return localStorage.getItem(this.JWT_KEY) || sessionStorage.getItem(this.JWT_KEY);
  }

  public clearUser():any{
    localStorage.removeItem(this.USER_KEY);
  }

  public getUserFromStorage():any{
    return localStorage.getItem(this.USER_KEY);

  }
}
