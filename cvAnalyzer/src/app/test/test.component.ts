import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { ApiService } from './ApiService';
import { CodeResponse } from '../models/CodeResponse';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent {
  resumePassed:String="";
  question:String="";
  skill:String="";
  editorOptions = {theme: 'vs-dark', language: 'javascript'};
  code: string= 'function x() {\nconsole.log("Hello world!");\n}';
  codeResult:String="";
  result:boolean=false;
  parts: string[] = [];

  constructor(private http: HttpClient, private route: ActivatedRoute,private apiService: ApiService) {
    this.result=false;

    this.route.params.subscribe(params => {
      // Retrieve the productId from the route parameter
      this.resumePassed = params['data']
      console.log(this.resumePassed);
      // Assuming the route parameter is named 'id'

      this.http
      .get<any>('http://localhost:8080/compare/generateQuestion?resumeName='+this.resumePassed ,{
      //	headers: headers
      }).subscribe(data => {
        console.log(data);
        this.question=data[0].questions;
        this.skill=data[0].skill;
    });})
  }

  
  submitCode(){


    const data = new Map<String, String>();
    data.set("problemStatement", this.question);
    data.set("code", this.code);

    
    this.apiService.sendMapToBackend(Object.fromEntries(data)).subscribe(
      (response) => {
        console.log('Response from the backend:', response);
        const codeResponses = response as CodeResponse[];
        // Handle the response here
        this.codeResult=codeResponses[0].codeResponse;
        

        const regex = /(\d+)/g;
        this.parts = this.codeResult.split(regex);
      },
      (error) => {
        console.error('Error:', error);
        // Handle errors here
      }
    );
  


  //       console.log(this.code);
  //       const submitMap = new Map([
  //         [this.question, this.code],
  //       ]);

  //      // let headers = new HttpHeaders();
  //  // headers.append('Content-Type', 'application/json');
  //  console.log(submitMap);
  //   this.http
  //   .post('http://localhost:8080/rate/rate-code' ,submitMap,{
  //   	//headers : headers
  //   }).subscribe(data => {
  //     console.log(data);
   
  // });
   }
}
