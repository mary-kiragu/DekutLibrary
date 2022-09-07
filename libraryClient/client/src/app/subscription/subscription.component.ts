import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { SubscriptionService } from '../subscription.service';
import { PaymentPlan } from './paymentPlanModel';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements OnInit {

  constructor(private subscriptionService:SubscriptionService,
    protected fb: FormBuilder,) { }

  ngOnInit(): void {
    this.getAll();
  }

  paymentPlanForm = this.fb.group({
    id: [],
    name: [''],
    paymentAmount: [''],
    paymentDuration: [''],
    description: [''],

  })
  paymentPlan: PaymentPlan = {} as PaymentPlan;
  paymentPlans: any = [];

  protected extractPaymentFormDetails(): any {
    return {
      id: this.paymentPlanForm.get('id')!.value,
      name: this.paymentPlanForm.get('name')!.value,
      paymetAmount:this.paymentPlanForm.get('paymentAmount')!.value,
      paymetDuration:this.paymentPlanForm.get('paymentDuration')!.value,
      description: this.paymentPlanForm.get('description')!.value,

    };
  }

  getAll(): void {
    this.subscriptionService.getAll().subscribe((res) => {
      console.log('Array of books available', res);

      this.paymentPlan = res;
    });
  }

  addPlan(): any {
    const planDetails = this.extractPaymentFormDetails();
    console.log(planDetails);

    this.subscriptionService.save(planDetails).subscribe(
      (res) => {
        console.log('created paymentPlan', res);
        this.getAll();
      },
      (err) => {
        console.log('plan not created');
      }
    );
  }

}
