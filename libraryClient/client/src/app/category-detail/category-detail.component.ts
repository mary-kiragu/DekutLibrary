import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriesService } from '../categories.service';
interface Category {
  id: number;
  name: string;
  parentCategoryId: number;
  description: string;
}

@Component({
  selector: 'app-category-detail',
  templateUrl: './category-detail.component.html',
  styleUrls: ['./category-detail.component.css'],
})
export class CategoryDetailComponent implements OnInit {
  constructor(
    private categoriesService: CategoriesService,
    private route: ActivatedRoute,
    private router: Router,
    protected fb: FormBuilder
  ) {}

  categoryForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    parentCategoryId: [],

  });
  category: any;
  user: any;

  subCategories!: any;
  loadingSubCats = false;
  loadingCats = false;
  loadingVideos = false;
  sortableSubCats: any;
  selectedCategoryId!: number;
  selectedCategory: Category | null = null

  bookCategory!: number;

  ngOnInit(): void {
    // window.scroll(0, 0);
    // this.getCurrentUser();
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
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

  getCategory(id: number) {
    this.loadingCats = true;
    this.categoriesService.getOne(id).subscribe(
      (res) => {
        this.category = res;
        this.loadingCats = false;

        this.category.parents?.forEach((parentCategory: any) => {
          this.bookCategory = parentCategory.id;
        });
        console.log('category : ', this.category);

        // // this.filterVideosByCategory(this.category.id);

         console.log("subcats",this.getSubCategories(this.category.id));
      },
      (err) => {
        console.log(err);
      }
    );
  }
  getSubCategories(id: number) {
    this.loadingSubCats = true;

    this.categoriesService.filterByParent(id).subscribe(
      (result) => {
        this.subCategories = result;
        this.sortableSubCats = result;
        this.loadingSubCats = false;
      },
      (error) => {
        this.loadingSubCats = false;
      }
    );
  }
  protected createcategoryFromForm(): any {
    return {
      id: this.categoryForm.get('id')!.value,
      name: this.categoryForm.get('name')!.value,
      description: this.categoryForm.get('description')!.value,
      parentCategoryId:this.category.id,
      categoryType: 'SECTION',
    };
  }

  saveSection(): void {
    console.log('saving section');
    let category = this.createcategoryFromForm();

    console.log('category : ', category);

    this.categoriesService.save(category).subscribe((result) => {
      this.getSubCategories(this.category.id);
      console.log('Successfully created Section : ', result);
      document.getElementById('closeCreateCategoryModal')?.click();
      this.reset();
    });
  }
  editSection(category: any): void {
    this.categoryForm = this.fb.group({
      id: [category.id],
      name: [category.name],
      description: [category.description],
      parentCategoryId: [category.parentCategoryId],
    });

    document.getElementById('editModal')?.click();
  }

  toCategoryDetail(id: number) {
     this.router.navigate(['/categories',this.category.id,id]);
    //this.router.navigate(['/categories', id]);
    //window.location.reload();
  }

  toLanding() {
    this.router.navigate(['/learn']);
  }

  reset(): void {
    this.categoryForm.reset();
  }
  setCategoryId(id: number): void {

    console.log(id);
    this.selectedCategoryId = id;

  }
  updateCategory(): void {
    const categoryDetails = this.createcategoryFromForm();
    categoryDetails.id=this.selectedCategory?.id;
    console.log('category details', categoryDetails);
    this.categoriesService.update(categoryDetails).subscribe(
      (res) => {
        console.log('updated category', res);
        this.getSubCategories(this.category?.id as number);
      },
      (err) => {
        console.log('category not updated book', err);
      }
    );
  }

deleteCategory(): any {
  this.categoriesService.deleteOne(this.selectedCategory?.id as number).subscribe(
    (res) => {
      console.log('deleted category', res);
      this.getSubCategories(this.category.id as number);
    },
    (error) => {
      console.log('category not deleted');
    }
  );
  this.selectedCategory = null;
}


  setSelectedCategory(category: Category){
    this.selectedCategory = category;
    this.categoryForm = this.fb.group({
      id: [category.id],
      name: [category.name],
      description: [category.description],
      parentCategoryId: [category.parentCategoryId],
    });
  }
}
