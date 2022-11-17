import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriesService } from '../categories.service';
import { User } from '../login/user.model';
import { UserService } from '../user.service';
interface Category {
  id: number;
  name: string;
  parentCategoryId: number;
  description: string;
  categoryType: string;
  imageUrl: string;
  categoryImageUrl: string;
  categoryName: string;
  type: string;
  data: any;
  size: string;

}

@Component({
  selector: 'app-category-detail',
  templateUrl: './category-detail.component.html',
  styleUrls: ['./category-detail.component.css'],
})
export class CategoryDetailComponent implements OnInit {

  categoryForm = this.fb.group({
    id: [],
    name: [''],
    description:[''],
    parentCategoryId: [],
    categoryType: [],
    imageUrl:[''],
    categoryImageUrl:[''],
    categoryName:[''],
    type:[''],
    data:[],
    size:[''],


  });
  category: any;
  uploadFile=true;
  showDescription=false;

  searchText: string = '';
  categoriesToRender: Category[] = []
  isFiltered = false;
  subCategories!: any;
  loadingSubCats = false;
  loadingCats = false;
  loadingVideos = false;
  sortableSubCats: any;
  selectedCategoryId!: number;
  selectedCategory: Category | null = null

  bookCategory!: number;

  user!:User;

  constructor(
    private categoriesService: CategoriesService,
    private userService:UserService,
    private route: ActivatedRoute,
    private router: Router,
    protected fb: FormBuilder
  ) {}


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
    this.getSubCategories(id);
    this.getCurrentUser();

  }
  getCurrentUser(): void {
    this.userService.getProfile().subscribe(
      userProfile => {
         this.user = userProfile;
        console.log("user profs",this.user);

      });

  }

  getCategory(id: number) {
    this.loadingCats = true;
    this.categoriesService.getOne(id).subscribe(
      (res) => {
        this.category = res;
        console.log("this category",this.category)
        this.loadingCats = false;

        this.category.parents?.forEach((parentCategory: any) => {
          this.bookCategory = parentCategory.id;
        });
        console.log('category : ', this.category);

        // // this.filterVideosByCategory(this.category.id);
        const subcats=this.getSubCategories(id);

         console.log("subcats",subcats);
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
        console.log("subcategories",this.subCategories);
        this.sortableSubCats = result;
        this.loadingSubCats = false;
      },
      (error) => {
        console.log("error getting subcats",error)
        this.loadingSubCats = false;
      }
    );
  }
  protected createcategoryFromForm(): any {

    return {
      id: this.categoryForm.get(['id'])!.value,
      name: this.categoryForm.get(['name'])!.value,
      description: this.categoryForm.get(['description'])!.value,
      parentCategoryId:this.category.id,
      //imageUrl: this.categoryForm.get(['imageUrl'])!.value || '',
      categoryType: 'SECTION',
      categoryImageUrl: this.category.categoryImageUrl,
      categoryName: this.category.categoryName,
      type: this.category.type,
      data: this.category.data,
      size: this.category.size,
    };
  }

  url: any;
msg = "";

selectFile(event: any) {
  if (!event.target.files[0] || event.target.files[0].length == 0) {
    this.msg = 'You must select an image';
    return;
  }

  console.log("Selected, ", event.target.files[0])
  var categoryImageName = event.target.files[0].name;
  var categoryImageType = event.target.files[0].type;
  var categoryImageSize = event.target.files[0].size;



  if (categoryImageType.match(/image\/*/) == null) {
    this.msg = "Only images are supported";
    return;
  }
  var reader = new FileReader();
  reader.readAsDataURL(event.target.files[0]);

  reader.onload = (_event) => {
    this.msg = "";
    this.url = reader.result;
    const categoryImageData = this.url.split('base64,')[1];
    this.category.categoryImageUrl = this.url;
    this.category.categoryName = categoryImageName;
    this.category.type = categoryImageType;
    this.category.data = categoryImageData;
    this.category.size = categoryImageSize;
    console.log("Selected image data, ", categoryImageData)
  }
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
  toggleDesc():void{
    this.showDescription=!this.showDescription;
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

  showInputUrl():any{
    this.uploadFile=false

  }
  showUploadFile():any{
    this.uploadFile=true

  }

  filter(): void {
    let filteredCaregories = this.subCategories.filter((subcategory : Category) =>
      subcategory.name.toLowerCase().includes(this.searchText.toLowerCase())
    );
    console.log(filteredCaregories);
    this.categoriesToRender = filteredCaregories;
    this.isFiltered = true;
    if (this.searchText == '') {
      this.isFiltered = false;
    }
  }

  unfilter(): void {
    this.categoriesToRender = this.subCategories;
    this.isFiltered = false;
  }



}
