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
  categoryType: string;
  imageUrl: string;
  categoryImageUrl: string;
  categoryName: string;
  type: string;
  data: any;
  size: string;
}

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css'],
})
export class CategoriesComponent implements OnInit {
  category = {
    id: '',
    name: '',
    parentCategoryId: '',
    description: '',
    categoryType: '',
    imageUrl: '',
    categoryImageUrl: '',
    categoryName: '',
    type: '',
    data: '',
    size: '',
  };

  searchText: string = '';
  foundCategories: any;
  categoriesToRender: Category[] = [];
  categoriesFromDB: Category[] = [];
  isFiltered = false;
  selectedCategory: any;
  uploadFile = true;
  categoryForm = this.fb.group({
    id: [],
    name: [''],
    description: [''],
    parentCategoryId: [],
    categoryType: [],
    imageUrl: [''],
    categoryImageUrl: [''],
    categoryName: [''],
    type: [''],
    data: [],
    size: [''],
  });

  user!: User;

  constructor(
    private categoriesService: CategoriesService,
    private userService: UserService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getAll();
    this.getCurrentUser();
  }

  notifyforPayment(): void {}

  getAll(): void {
    this.categoriesService.getAll().subscribe(
      (res) => {
        console.log('Array categories available', res);
        this.categoriesFromDB = res;
        console.log('yeeey', this.categoriesFromDB);
        this.categoriesToRender = this.categoriesFromDB;
        // window.location.reload()
      },
      (err) => {
        console.log('no category found');
        console.log(err);
      }
    );
  }

  reset(): void {
    this.categoryForm.reset();
  }

  cancel(): void {
    document.getElementById('closeCreateCategoryModal')?.click();
  }

  getCurrentUser(): void {
    // this.user = window.localStorage.getItem('user');
    // this.user = JSON.parse(this.user);
    this.userService.getProfile().subscribe((userProfile) => {
      this.user = userProfile;
      console.log('user profs', this.user);
    });
  }

  url: any;
  msg = '';

  selectFile(event: any) {
    if (!event.target.files[0] || event.target.files[0].length == 0) {
      this.msg = 'You must select an image';
      return;
    }

    console.log('Selected, ', event.target.files[0]);
    var categoryImageName = event.target.files[0].name;
    var categoryImageType = event.target.files[0].type;
    var categoryImageSize = event.target.files[0].size;

    if (categoryImageType.match(/image\/*/) == null) {
      this.msg = 'Only images are supported';
      return;
    }
    var reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);

    reader.onload = (_event) => {
      this.msg = '';
      this.url = reader.result;
      const categoryImageData = this.url.split('base64,')[1];
      this.category.categoryImageUrl = this.url;
      this.category.categoryName = categoryImageName;
      this.category.type = categoryImageType;
      this.category.data = categoryImageData;
      this.category.size = categoryImageSize;
      console.log('Selected image data, ', categoryImageData);
    };
  }
  selectUpdateFile(event: any) {
    if (!event.target.files[0] || event.target.files[0].length == 0) {
      this.msg = 'You must select an image';
      return;
    }

    console.log('Selected, ', event.target.files[0]);
    var categoryImageName = event.target.files[0].name;
    var categoryImageType = event.target.files[0].type;
    var categoryImageSize = event.target.files[0].size;

    if (categoryImageType.match(/image\/*/) == null) {
      this.msg = 'Only images are supported';
      return;
    }
    var reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);

    reader.onload = (_event) => {
      this.msg = '';
      this.url = reader.result;
      const categoryImageData = this.url.split('base64,')[1];
      this.selectedCategory.categoryImageUrl = this.url;
      this.selectedCategory.categoryName = categoryImageName;
      this.selectedCategory.type = categoryImageType;
      this.selectedCategory.data = categoryImageData;
      this.selectedCategory.size = categoryImageSize;
      console.log('Selected image data, ', categoryImageData);
    };
  }
  saveCategory(): void {
    // console.log('saving category')
    let category = this.createcategoryFromForm();

    // console.log('category : ', category);

    this.categoriesService.save(category).subscribe((result) => {
      document.getElementById('closeCreateCategoryModal')?.click();
      this.reset();
      this.getAll();
      console.log('Successfully created Course : ', result);

      // send notification
      // if (result.id)
      // this.notificationService.composeAndSendNotification(Entity.COURSE, result.id, 'New Course Created', 'COURSE_CREATE');

      // refresh state
      //this.getMyCourses();
    });
  }

  updateCategory(): void {
    const formData = this.createcategoryFromForm() as Category;
    console.log(formData);

    console.log(this.selectedCategory);
    const updatedCategory = this.selectedCategory as Category;
    updatedCategory.name = formData.name
    updatedCategory.description = formData.description
    console.log(formData.imageUrl);

    updatedCategory.imageUrl = formData.imageUrl
    // updatedCategory.cate = formData.name

    console.log('updated category details', updatedCategory);
    this.categoriesService.update(updatedCategory).subscribe(
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

    document.getElementById('openTopicModal')?.click();
  }

  setSelectedCategory(category: Category) {
    this.selectedCategory = category;
    this.categoryForm = this.fb.group(category)
  }
  removeSelectedCategory() {
    this.selectedCategory = null;
  }
  protected createcategoryFromForm(): any {
    return {
      id: this.categoryForm.get('id')!.value,
      name: this.categoryForm.get('name')!.value,
      description:this.categoryForm.get('description')!.value,
      parentCategoryId:this.categoryForm.get('parentCategoryId')!.value,
      categoryType: this.categoryForm.get('categoryType')!.value,
      imageUrl: this.categoryForm.get('imageUrl')!.value,
      categoryImageUrl: this.category.categoryImageUrl,
      categoryName: this.category.categoryName,
      type: this.category.type,
      data: this.category.data,
      size: this.category.size,
    };
  }

  filter(): void {
    let filteredCaregories = this.categoriesFromDB.filter((category) =>
      category.name.toLowerCase().includes(this.searchText.toLowerCase())
    );
    console.log(filteredCaregories);
    this.categoriesToRender = filteredCaregories;
    this.isFiltered = true;
    if (this.searchText == '') {
      this.isFiltered = false;
    }
  }
  unfilter(): void {
    this.categoriesToRender = this.categoriesFromDB;
    this.isFiltered = false;
  }

  deleteCategory(): any {
    this.categoriesService
      .deleteOne(this.selectedCategory?.id as number)
      .subscribe(
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
    console.log(this.selectedCategory?.id);
    if (this.selectedCategory?.id) {
      this.updateCategory();
    } else {
      this.saveCategory();
    }
  }
  categoryFilter(type: 'category' | 'section'): Category[] {
    if (type == 'category') {
      return this.categoriesToRender.filter(
        (cat) => cat.categoryType == 'CATEGORY'
      );
    } else {
      return this.categoriesToRender.filter(
        (cat) => cat.categoryType != 'CATEGORY'
      );
    }
  }
  showInputUrl(): any {
    this.uploadFile = false;
  }
  showUploadFile(): any {
    this.uploadFile = true;
  }
}
