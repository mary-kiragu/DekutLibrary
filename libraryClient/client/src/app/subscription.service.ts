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
}
