import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { RouterModule, Routes } from '@angular/router';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { TestComponent } from './test/test.component';
import { FormsModule } from '@angular/forms';

const routes: Routes=[
  {
    path:'evaluation',component:TestComponent
  },{
    path:'', component:FileUploadComponent
  }
  
]
@NgModule({
  declarations: [
    AppComponent,
    TestComponent,
    FileUploadComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    MonacoEditorModule.forRoot() ,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [RouterModule]

})
export class AppModule { }
