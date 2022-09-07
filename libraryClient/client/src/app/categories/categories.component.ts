import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { CategoriesService } from '../categories.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

  constructor(
    private categoriesService:CategoriesService,
    protected fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getAll();

  }
  categoryForm = this.fb.group({
    id: [],
    name: [''],
    description: [''],
    parentCategoryId: [],
    rankInParent: [],
  })


// category = {
//     id: '',
//     name: '',
//     parentCategoryId:''
//   };

  categories:any;

  getAll():void{
    this.categoriesService.getAll().subscribe(
      (res) => {
      console.log('Array categories available', res);
      this.categories=res;
      console.log("yeeey",this.categories);
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

protected createcategoryFromForm(): any {
  return {
    id: this.categoryForm.get('id')!.value,
    name: this.categoryForm.get('name')!.value,
    description: this.categoryForm.get('description')!.value,
    parentCategoryId: this.categoryForm.get('parentCategoryId')!.value,
    rankInParent: this.categoryForm.get('rankInParent')!.value,
   // cpLogin: this.user ?.email,
    categoryType: 'CATEGORY'
  };
}

}


