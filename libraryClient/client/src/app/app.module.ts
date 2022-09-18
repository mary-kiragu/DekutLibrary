import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
// import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BooksComponent } from './books/books.component';
import { NavComponent } from './nav/nav.component';
import { BooksRecordComponent } from './books-record/books-record.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { JwtInterceptorService } from './interceptors/jwt-interceptor.service';
import { CategoriesComponent } from './categories/categories.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { CategoryDetailComponent } from './category-detail/category-detail.component';
import { FooterComponent } from './footer/footer.component';
import { SectionsComponent } from './categories/sections/sections.component';
import { AuthorityDirective } from './authority.directive';
import { FilterPipe } from './filter.pipe';
import { ProfileComponent } from './login/profile/profile.component';



@NgModule({
  declarations: [
    AppComponent,
    BooksComponent,
    NavComponent,
    BooksRecordComponent,
    BookDetailsComponent,
    LoginComponent,
    RegisterComponent,
    CategoriesComponent,
    SubscriptionComponent,
    CategoryDetailComponent,
    FooterComponent,
    SectionsComponent,
    AuthorityDirective,
    FilterPipe,
    ProfileComponent,

  ],
  imports: [
    BrowserModule,
    // BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
