import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriesService } from '../categories.service';


@Component({
  selector: 'app-category-detail',
  templateUrl: './category-detail.component.html',
  styleUrls: ['./category-detail.component.css']
})
export class CategoryDetailComponent implements OnInit {

  constructor(
    private categoriesService:CategoriesService,
    private route: ActivatedRoute,
    private router: Router,
    protected fb: FormBuilder,

  ) {

   }

  categoryForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    parentCategoryId: [],
    rankInParent: [],
  })
  category:any;
   user: any;

  subCategories!: any;
  loadingSubCats = false;
  loadingCats = false;
  loadingVideos = false;
  sortableSubCats:any;

  bookCategory!: number;


  ngOnInit(): void {
    // window.scroll(0, 0);
    // this.getCurrentUser();
    const id=Number(this.route.snapshot.paramMap.get("id"));
    if(id){
      this.getCategory(id);

    }
    // this.route.params.subscribe((params) => {
    // this.getCategory(params['id']);
    // });






  }
  // getCurrentUser(): void {
  //   if (window.localStorage.getItem('nat-role') === 'SUPPORT') {
  //     // this.isAdmin = true;
  //   }
  // }

  getCategory(id:number){
    this.loadingCats=true;
    this.categoriesService.getOne(id).subscribe(
      (res)=>{
        this.category = res;
      this.loadingCats = false;

      this.category.parents ?.forEach((parentCategory: any) => {
        this.bookCategory = parentCategory.id;

      });
      console.log('category : ', this.category);

      // // this.filterVideosByCategory(this.category.id);
      // this.getSubCategories(this.category.id);

  },
  ( err)=>{
    console.log(err);
  }
);



}
getSubCategories(id: number) {
  this.loadingSubCats = true;

  this.categoriesService.filterByParent(id).subscribe(
    result => {
      this.subCategories = result;
      this.sortableSubCats = result;
      this.loadingSubCats = false;
    },
    error => {
      this.loadingSubCats = false;
    }
  )

}
protected createcategoryFromForm(): any {
  return {
    id: this.categoryForm.get('id')!.value,
    name: this.categoryForm.get('name')!.value,
    description: this.categoryForm.get('description')!.value,
    parentCategoryId: this.category.id,
    rankInParent: this.categoryForm.get('rankInParent')!.value,
    cpLogin: this.user ?.email,
    categoryType: 'SECTION'
  };
}

saveSection(): void {

  this.categoriesService.save(this.category).subscribe(
    result => {
      this.getSubCategories(this.category.id);
      console.log('Successfully created Category : ', result)
      document.getElementById("closeCreateCategoryModal") ?.click();
      this.reset();
    }
  )

}
toCategoryDetail(id: number) {
  this.router.navigate(['/categories', id]);
}

toLanding() {
  this.router.navigate(['/learn']);
}

reset(): void {
  this.categoryForm.reset();
}
}
