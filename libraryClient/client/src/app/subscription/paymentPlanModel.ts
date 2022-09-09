export enum PaymentDuration{
  MONTH="MONTH",
  YEAR="YEAR",
  HOLIDAY="HOLIDAY"

}


export const duration2LabelMapping: Record<PaymentDuration, string> = {
  [PaymentDuration.MONTH]: "MONTH",
  [PaymentDuration.HOLIDAY]: "HOLIDAY",
  [PaymentDuration.YEAR]: "YEAR",
};

export class PaymentPlan{
  id?:number;
  name?:string;
  paymentAmount?:number;
  paymentDuration?:PaymentDuration;
  description :[];
  constructor(id:number,name:string,paymentAmount:number,paymentDuration:PaymentDuration,description:[]){
    this.id=id;
    this.name=name;
    this.paymentAmount=paymentAmount;
    this.paymentDuration=paymentDuration;
    this.description=description;

  }

}
