import { PaymentPlan } from "../subscription/paymentPlanModel";

export enum Authority{
  SUBSCRIBER="SUBSCRIBER",
  ADMIN="ADMIN"
}

export enum AccountStatus{
  PAID="PAID",
  UNPAID="UNPAID"
}
export class User{

  id?:number;
  name?:string;
  email?:string;
  password?:string;
  authority?:Authority;
  plan?: PaymentPlan;
  accountStatus?: AccountStatus;
  nextBillingDate?: string;
  lastBillingDate?: string;



  constructor(id?:number,name?:string,email?:string ,password?:string,authority?:Authority, accountStatus?: AccountStatus,
    nextBillingDate?: string,lastBillingDate?: string){
    this.id=id;
    this.name=name;
    this.email=email;
    this.password=password;
    this.authority=authority;
    this.accountStatus=accountStatus;
    this.plan=this.plan;
    this.lastBillingDate=lastBillingDate;
    this.nextBillingDate=nextBillingDate;


  }


}
