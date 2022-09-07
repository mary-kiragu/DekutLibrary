export enum PayDuration{
  MONTH="MONTH",
  YEAR="YEAR",
  HOLIDAY="HOLIDAY"

}
export class PaymentPlan{
  id?:number;
  name?:string;
  paymetAmount?:number;
  paymentDuration?:PayDuration;
  constructor(id:number,name:string,paymetAmount:number,paymentDuration:PayDuration){
    this.id=id;
    this.name=name;
    this.paymetAmount=paymetAmount;
    this.paymentDuration=paymentDuration;

  }

}
