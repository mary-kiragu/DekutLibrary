import { Component, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { SubscriptionService } from "../subscription.service";
import {
  PaymentPlan,
  PaymentDuration,
  duration2LabelMapping,
} from "./paymentPlanModel";
import { UserService } from "../user.service";
import { AccountStatus } from "../login/user.model";

interface Plan {
  id?: number;
  name?: string;
  paymentAmount?: number;
  paymentDuration?: PaymentDuration;
  description: [];
}

@Component({
  selector: "app-subscription",
  templateUrl: "./subscription.component.html",
  styleUrls: ["./subscription.component.css"],
})
export class SubscriptionComponent implements OnInit {
  public duration2LabelMapping = duration2LabelMapping;

  public paymentDurations = Object.values(PaymentDuration);

  planId!: number;
  paymentRequest: any;

  selectedPlan: any;

  paymentRequestForm = this.fb.group({
    paymentPlan: [""],
    phoneNumber: [""],
    userId: [""],
  });

  paymentPlanForm = this.fb.group({
    id: [],
    name: [""],
    paymentAmount: [""],
    paymentDuration: [""],
    description: [""],
  });

  callback = {
    merchantRequestId: "",
    checkOutRequestId: "",
    resultCode: "",
    resultDescription: "",
  };
  paymentPlan: PaymentPlan = {} as PaymentPlan;
  paymentPlans: any = [];
  user: any;

  isProcessing = false;

  constructor(
    private subscriptionService: SubscriptionService,
    private userService: UserService,
    protected fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    if (id) {
      this.getOne(id);
    }
    this.getAll();
    this.getCurrentUser();
  }

  getCurrentUser(): void {
    this.userService.getProfile().subscribe((userProfile) => {
      this.user = userProfile;
      console.log("user profs", this.user);
    });
  }

  getAll(): void {
    this.subscriptionService.getAll().subscribe((res) => {
      console.log("Array of books available", res);

      this.paymentPlans = res;
    });
  }

  addPlan(): any {
    const planDetails = this.extractPaymentFormDetails();
    console.log(planDetails);

    this.subscriptionService.save(planDetails).subscribe(
      (res) => {
        console.log("created paymentPlan", res);
        this.getAll();
        this.reset();
      },
      (err) => {
        console.log("plan not created");
      }
    );
  }
  protected extractPaymentFormDetails(): any {
    return {
      id: this.paymentPlanForm.get("id")!.value,
      name: this.paymentPlanForm.get("name")!.value,
      paymentAmount: this.paymentPlanForm.get("paymentAmount")!.value,
      paymentDuration: this.paymentPlanForm.get("paymentDuration")!.value,
      description: this.paymentPlanForm.get("description")!.value,
    };
  }

  updatePlan(): void {
    const paymentPlanDetails = this.extractPaymentFormDetails();
    console.log("payment plan details", paymentPlanDetails);
    this.subscriptionService.update(paymentPlanDetails).subscribe(
      (res) => {
        console.log("updated plan", res);
        this.getAll();
      },
      (err) => {
        console.log("plan not updated book", err);
      }
    );
  }

  save(): void {
    if (this.paymentPlan.id) {
      this.updatePlan();
    } else {
      this.addPlan();
    }
  }

  getOne(id: number): void {
    this.subscriptionService.getOne(id).subscribe(
      (res) => {
        console.log(res);
        this.paymentPlan = res;
      },
      (err) => {
        console.log("plan not found");
      }
    );
  }

  setPaymentPlanId(paymentPlanID: number) {
    this.paymentPlan.id = paymentPlanID;
    console.log(this.paymentPlan.id);
  }

  setSelectedPlan(paymentPlan: PaymentPlan) {
    this.selectedPlan = paymentPlan;
    this.paymentPlanForm = this.fb.group(paymentPlan);
  }

  initiatePayment(): any {
    this.paymentRequest = this.extractPaymentRequestDetails();
    console.log("about to initiate payment", this.paymentRequest);
    this.subscriptionService.initiatePayment(this.paymentRequest).subscribe(
      (res) => {
        console.log("paymentrequest", res);

        this.wait(25000);
        console.log("wait");
        this.user = this.getCurrentUser();

        if (this.user.accountStatus === "PAID") {
          this.router.navigate(["/categories"]);
        }
      },
      (err) => {
        console.log("error", err);
      }
    );
  }

  wait(ms: number): any {
    var start = new Date().getTime();
    var end = start;
    while (end < start + ms) {
      end = new Date().getTime();
    }
  }

  extractPaymentRequestDetails(): any {
    return {
      phoneNumber: this.paymentRequestForm.get("phoneNumber")!.value,
      paymentPlan: this.paymentPlan.id,
      userId: this.user.id,
    };
  }

  setSelectedCategory(plan: Plan) {
    this.selectedPlan = plan;
  }

  reset(): void {
    this.paymentPlanForm.reset();
  }

  deletePlan(): any {
    this.subscriptionService
      .deleteOne(this.paymentPlan?.id as number)
      .subscribe(
        (res) => {
          console.log("deleted plan", res);
          this.getAll();
        },
        (error) => {
          console.log("plan not deleted");
        }
      );
  }
  removeSelectedPlan() {
    this.selectedPlan = null;
  }
}
