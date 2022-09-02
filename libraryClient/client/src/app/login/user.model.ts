export enum Authority{
  SUBSCRIBER="SUBSCRIBER",
  ADMIN="ADMIN"
}
export class User{

  id?:number;
  name?:string;
  email?:string;
  password?:string;
  authority?:Authority;

  constructor(id?:number,name?:string,email?:string ,password?:string,authority?:Authority){
    this.id=id;
    this.name=name;
    this.email=email;
    this.password=password;
    this.authority=authority;

  }


}
