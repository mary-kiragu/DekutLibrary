import { CategoryDetailComponent } from './category-detail/category-detail.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BooksComponent } from './books/books.component';
import { BooksRecordComponent } from './books-record/books-record.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CategoriesComponent } from './categories/categories.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { SectionsComponent } from './categories/sections/sections.component';
import { ProfileComponent } from './login/profile/profile.component';

const routes: Routes = [
  {
    path: 'books',
    component: BooksComponent,
  },
  {
    path: 'book-records',
    component: BooksRecordComponent,
  },
  {
    path: 'book-records/:id',
    component: BookDetailsComponent,
  },
  {
    path: 'book/:id',
    component: BookDetailsComponent,
  },
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'profile',
    component: ProfileComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'subscribe',
    component: SubscriptionComponent,
  },
  {
    path: 'categories',
    component: CategoriesComponent,
  },
  {
    path: 'categories/:id/:id',
    component: SectionsComponent,
  },
  {
    path: 'categories/:id',
    component: CategoryDetailComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
