import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { UserService } from './user.service';

@Directive({
  selector: '[appAuthority]'
})
export class AuthorityDirective {

  constructor(private templateRef: TemplateRef<any>,
    private viewContainerRef: ViewContainerRef,
    private authService: UserService) { }
    user: any;
  referenceAuthorities: string[] = [];

  @Input()
    set hasAuthority(value: string | string[]) {

      this.authService.identity().subscribe(
        userProfile => {
          this.user = userProfile;

          if (Array.isArray(value)) {
            value.forEach((authority) => {
              this.referenceAuthorities.push(authority);
            })
          } else {
            this.referenceAuthorities.push(value);
          }

          // console.log(this.user);

        },
        err => {
          console.log('Error getting user : ', err);
          this.user = {
            authority: 'ANONYMOUS'
          };
        },);
    }

}
