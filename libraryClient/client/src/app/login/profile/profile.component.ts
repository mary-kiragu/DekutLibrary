import { Component, OnInit } from "@angular/core";
import { UserService } from "../../user.service";
import { User } from "../user.model";
import { BookService } from "../../book.service";
import { ActivatedRoute } from "@angular/router";
import { BorrowHistory } from "../../books/book.model";

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
  totalBorrowed = 0;
  totalReturned = 0;
  activeFilter: "all" | "borrow" | "return" = "all";

  constructor(
    private userService: UserService,
    private bookService: BookService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    if (id) {
      // this.getOne(id);
    }
    this.getCurrentUser();
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
        this.filter();
        this.filterReturned();
      },
      (err) => {
        console.log("my failed", err);
      }
    );
  }
}
