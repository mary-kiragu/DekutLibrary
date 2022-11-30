import { Component, OnInit } from "@angular/core";
import { UserService } from "../user.service";
import {
  NavigationStart,
  Router,
  ActivatedRoute,
  NavigationEnd,
} from "@angular/router";
import { TokenService } from "../token.service";

@Component({
  selector: "app-nav",
  templateUrl: "./nav.component.html",
  styleUrls: ["./nav.component.css"],
})
export class NavComponent implements OnInit {
  user: any;
  isLoggedIn = true;
  currentURL = "/";
  r: Router | null = null;
  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private tokenService: TokenService
  ) {
    router.events.forEach((event) => {
      this.r = router;
      if (event instanceof NavigationStart) {
        this.user = this.tokenService.getUserFromStorage();
        this.getCurrentUser();
        console.log("The user is,  ", this.user);
        console.log("+++++++++++++++++");
        console.log(event.url);
        this.currentURL = event.url;
      }
    });
  }

  ngOnInit(): void {
    this.getCurrentUser();
    console.log("====================");
    console.log(this.router.url);
  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe((userProfile) => {
      this.user = userProfile;
      console.log("user profs", this.user);
      if (!this.user) {
        this.router.navigate(["/login"]);
      }
    });
  }
  goToProfileOrAdmin() {
    if (this.user.authority == "ADMIN") {
      this.router.navigate(["/book-records"]);
    } else {
      this.router.navigate(["/profile"]);
    }
  }
  reload() {
    window.location.reload();
  }
}
