import { Component, OnInit } from "@angular/core";
import { UserService } from "../../user.service";
import { User } from "../user.model";
import { BookService } from "../../book.service";
import { ActivatedRoute, Router } from "@angular/router";
import { BorrowHistory } from "../../books/book.model";
import { TokenService } from "src/app/token.service";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit {
  user!: User;
  history = {
    id: "",
    user: "",
    book: "",
    dueDate: "",
    createdOn: "",
  };
  borrowedBooks: BorrowHistory[] = [];
  returnedBooks: BorrowHistory[] = [];
  historyValues: BorrowHistory[] = [];
  payments: any[] = [];
  totalBorrowed = 0;
  totalReturned = 0;
  activeFilter: "all" | "borrow" | "return" = "all";

  constructor(
    private userService: UserService,
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    console.log(this.route);

    const id = Number(this.route.snapshot.paramMap.get("id"));
    if (id) {
      // this.getOne(id);
    }
    this.getCurrentUser();
  }
  logOut(): void {
    //delete token
    this.tokenService.clearToken();
    //delete user from storage
    this.tokenService.clearUser();
    //route to login
    this.router.navigate(["/"]);
  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe((userProfile) => {
      this.user = userProfile;
      console.log("user profs", this.user);
      this.getMyHistory();
    });
  }
  getSubtotalsByStatus(allBooks: BorrowHistory[]): any {
    this.totalBorrowed = allBooks.filter(
      (book) => book.action === "BORROW"
    ).length;
    this.totalReturned = allBooks.filter(
      (book) => book.action === "RETURN"
    ).length;
  }

  filter(): void {
    console.log("--------------", this.historyValues);

    this.borrowedBooks = this.historyValues.filter(
      (history: BorrowHistory) => history.action === "BORROW"
    );
    console.log("--------------", this.borrowedBooks);
  }
  filterReturned(): void {
    console.log("--------------", this.historyValues);

    this.returnedBooks = this.historyValues.filter(
      (history: BorrowHistory) => history.action === "RETURN"
    );
    console.log("--------------", this.returnedBooks);
  }
  getMyHistory(): void {
    this.bookService.getHistoryByUser(this.user.id).subscribe(
      (res) => {
        console.log("my hist", res);
        this.historyValues = res;
        this.historyValues.forEach((history: BorrowHistory) => {
          const arr = history.createdOn || ([] as number[]);
          const pd = `${arr[0]}/${arr[1]}/${arr[2]}`;
          const date = new Date(pd);
          history.createdOn = date.toLocaleDateString();
        });
        this.filter();
        this.filterReturned();
        this.getPayments();
      },
      (err) => {
        console.log("my failed", err);
      }
    );
  }

  getPayments() {
    this.userService.getPaymentsByUser(this.user.id).subscribe((res) => {
      console.log("payments", res);
      this.payments = res;
      this.payments.forEach((payment) => {
        payment.initiatedOn = new Date(
          payment.initiatedOn
        ).toLocaleDateString();
      });
    });
  }
}
