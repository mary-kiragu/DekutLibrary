import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CategoriesService {
  CATEGORY_KEY = "category";
  private apiServerUrl=environment.API_ENDPOINT;
  constructor(private httpClient:HttpClient) {

  }
  save(data: any): Observable<any> {
    return this.httpClient.post<any>(this.apiServerUrl + "/api/categories", data);

  }
  update(data: any): Observable<any> {
    return this.httpClient.put(this.apiServerUrl + "/api/categories", data);
  }
  getAll(): Observable<any> {
    return this.httpClient.get(this.apiServerUrl + "/api/categories");
  }
  getOne(id: number): Observable<any> {
    return this.httpClient.get(this.apiServerUrl + "/api/categories/" + id);
  }
  filterByParent(parentId: number,adminView?:boolean): Observable<any> {
    if (!adminView) {
      adminView = false;
    }
    return this.httpClient.get(this.apiServerUrl + "/api/categories/filter-by-parent/" + parentId + '?adminView=' + adminView);
  }


}
