import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from 'src/environments/environment';
import { PaymentPlan } from './subscription/paymentPlanModel';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiServerUrl=environment.API_ENDPOINT;

  constructor(private httpClient:HttpClient) { }

  save(paymentPlan: PaymentPlan): Observable<PaymentPlan> {
    return this.httpClient.post<PaymentPlan>(this.apiServerUrl + "/api/payment-plan", paymentPlan);

  }
  getAll(): Observable<PaymentPlan> {
    return this.httpClient.get<PaymentPlan>(this.apiServerUrl + "/api/payment-plan");
  }

  getOne(id: number): Observable<PaymentPlan> {
    return this.httpClient.get<PaymentPlan>(this.apiServerUrl + "/api/payment-plan/" + id);
  }

  update(paymentPlan:PaymentPlan):Observable<PaymentPlan>{
    return this.httpClient.put<PaymentPlan>(this.apiServerUrl+"/api/payment-plan",paymentPlan);
  }

  initiatePayment(data:any): Observable<any>{
    return this.httpClient.post<any>(this.apiServerUrl + "/api/payment",data);


  }

  processPayment(data:any): Observable<any>{
    return this.httpClient.post<any>(this.apiServerUrl + "/api/payment/callback",data);


  }

  deleteOne(id: number): Observable<any> {
    return this.httpClient.delete(this.apiServerUrl + "/api/payment-plan/" + id);

  }

}
