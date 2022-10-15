import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { CategoriesService } from '../categories.service';
import { User } from '../login/user.model';
import { UserService } from '../user.service';


interface Category {
  id: number;
  name: string;
  parentCategoryId: number;
  description: string;
  categoryType:string;
}

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  searchText: string = '';
  foundCategories:any;
  categoriesToRender: Category[]=[];
  categoriesFromDB: Category[] = [];
  isFiltered = false;
  selectedCategory: Category | null = null
  categoryForm = this.fb.group({
    id: [],
    name: [''],
    description: [''],
    parentCategoryId: [],
    categoryType: [],

  })

  category!: Category;
  user!:User;

  constructor(
    private categoriesService:CategoriesService,
    private userService:UserService,
    protected fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getAll();
    this.getCurrentUser();

  }

  notifyforPayment():void{

  }



  getAll():void{
    this.categoriesService.getAll().subscribe(
      (res) => {
      console.log('Array categories available', res);
      this.categoriesFromDB=res;
      console.log("yeeey",this.categoriesFromDB);
      this.categoriesToRender = this.categoriesFromDB
      // window.location.reload()
        },
        (err) => {
          console.log("no category found")
          console.log(err);
        }
        );

}

reset(): void {
  this.categoryForm.reset();
}

cancel(): void {
  document.getElementById("closeCreateCategoryModal") ?.click();
}

getCurrentUser(): void {
  // this.user = window.localStorage.getItem('user');
  // this.user = JSON.parse(this.user);
  this.userService.getProfile().subscribe(
    userProfile => {
       this.user = userProfile;
      console.log("user profs",this.user);

    });

}


// }
saveCategory(): void {
  // console.log('saving category')
  let category = this.createcategoryFromForm();

  // console.log('category : ', category);

  this.categoriesService.save(category).subscribe(
    result => {
       document.getElementById("closeCreateCategoryModal") ?.click();
       this.reset();
       this.getAll();
      console.log('Successfully created Course : ', result)

      // send notification
      // if (result.id)
      // this.notificationService.composeAndSendNotification(Entity.COURSE, result.id, 'New Course Created', 'COURSE_CREATE');

      // refresh state
      //this.getMyCourses();
    }
  )

}

updateCategory(): void {
  const categoryDetails = this.createcategoryFromForm();
  categoryDetails.id=this.selectedCategory?.id;
  console.log('category details', categoryDetails);
  this.categoriesService.update(categoryDetails).subscribe(
    (res) => {
      console.log('updated category', res);
      this.getAll();
    },
    (err) => {
      console.log('category not updated book', err);
    }
  );
}
editCategory(category: any): void {

  this.categoryForm = this.fb.group({
    id: [category.id],
    name: [category.name],
    description: [category.description],
    parentCategoryId: [category.parentCategoryId],
    rankInParent: [category.rankInParent],
  });

  document.getElementById("openTopicModal") ?.click();

}

setSelectedCategory(category: Category){
  this.selectedCategory = category;
}
removeSelectedCategory(){
  this.selectedCategory = null;
}
protected createcategoryFromForm(): any {
  return {
    id: this.categoryForm.get('id')!.value,
    name: this.categoryForm.get('name')!.value,
    description: this.categoryForm.get('description')!.value,
    parentCategoryId: this.categoryForm.get('parentCategoryId')!.value,
    // rankInParent: this.categoryForm.get('rankInParent')!.value,
   // cpLogin: this.user ?.email,
    categoryType:this.categoryForm.get('categoryType')!.value,
  };
}

filter(): void {
  let filteredCaregories = this.categoriesFromDB.filter((category)=>category.name.includes(this.searchText))
  console.log(filteredCaregories)
    this.categoriesToRender = filteredCaregories;
    this.isFiltered = true;
    if(this.searchText == "") {
      this.isFiltered = false
          }

}
unfilter(): void {
  this.categoriesToRender = this.categoriesFromDB;
  this.isFiltered = false
}

deleteCategory(): any {
  this.categoriesService.deleteOne(this.selectedCategory?.id as number).subscribe(
    (res) => {
      console.log('deleted category', res);
      this.getAll();
    },
    (error) => {
      console.log('category not deleted');
    }
  );
  this.selectedCategory = null;
}

save(): void {
  console.log(this.selectedCategory?.id)
   if (this.selectedCategory?.id) {
    this.updateCategory();
  } else {
    this.saveCategory();
  }
}





}


