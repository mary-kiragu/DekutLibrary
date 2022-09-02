import { Component, OnInit } from '@angular/core';
import { CategoriesService } from '../categories.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

  constructor(
    private categoriesService:CategoriesService
  ) { }

  ngOnInit(): void {
    this.getAll();

  }
category = {
    id: '',
    name: '',
    parentCategoryId:''
  };

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
createCategory():void{
  this.categoriesService.save(this.category).subscribe(
    (res)=>{
      console.log("created a new category",res);

    },
    (err)=>{
      console.log("category not created");
    }
  )


}
updateCategory():void{
  this.categoriesService.update(this.category).subscribe(
    (res)=>{
      console.log("updated category",res);

    },
    (err)=>{
      console.log("category not updated");
    }
  )


}

}
