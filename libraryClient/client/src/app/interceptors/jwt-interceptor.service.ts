import { HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TokenService } from '../token.service';

@Injectable({
  providedIn: 'root'
})
export class JwtInterceptorService {
  private apiServerUrl=environment.API_ENDPOINT;

  constructor(
    private tokenService:TokenService
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log(this.tokenService.getToken());
    if (request.url.startsWith(this.apiServerUrl) && this.tokenService.getToken() !== null) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.tokenService.getToken()}`
        }
      });
    }
    return next.handle(request);
  }
}
