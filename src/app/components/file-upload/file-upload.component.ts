import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { Router } from '@angular/router';
import { CvDetails } from 'src/app/models/CvDetail';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})

export class FileUploadComponent implements OnInit {
  status: "initial" | "uploading" | "success" | "fail" = "initial";
  filesJd: File[] = [];
  filesCv: File[] = [];
  isJdUploaded:boolean=false;
  isCvUploaded:boolean=false;
  CvResult: CvDetails[]=[{resumeName: "xyz", percentageMatch : 23}];

  constructor(private http: HttpClient , private uploadService: FileUploadService,private router: Router) {}

  ngOnInit(): void {
  }

  onChangeJd(event: any) {
    this.CvResult=[];
    const files = event.target.files;

    if (files.length) {
      this.status = "initial";
      this.filesJd = files;
    }
    console.log(this.filesJd);
  }

  onChangeCv(event: any) {
    this.CvResult=[];
    const files = event.target.files;

    if (files.length) {
      this.status = "initial";
      this.filesCv = files;
    }
    console.log(this.filesCv)
  }

  onUpload() {
    if (this.filesJd.length&&this.filesCv.length) {

      const formDataJd = new FormData();
      const formDataCv = new FormData();


      [...this.filesJd].forEach((file) => {
        formDataJd.append("file", file, file.name);
      });

      [...this.filesCv].forEach((file) => {
        formDataCv.append("file", file, file.name);
      });

      const uploadJd$ = this.http.post("http://localhost:8080/api/Files/JobDescription", formDataJd);

      const uploadCv$ = this.http.post("http://localhost:8080/api/Files/Resume", formDataCv);


      this.status = "uploading";

      uploadJd$.subscribe({
        next: () => {
          this.status = "success";
        },
        error: (error: any) => {
          this.status = "fail";
          return throwError(() => error);
        },
      });

      uploadCv$.subscribe({
        next: () => {
          this.status = "success";
        },
        error: (error: any) => {
          this.status = "fail";
          return throwError(() => error);
        },
      });
    }
  }

  onProcess(){



//     const details$ = this.http.get("http://localhost:8080/api/details");
// details$.subscribe({

// });
//this.router.navigate(['/evaluation']);

//const detail$ = this.http.post("http://localhost:8080/api/ResumeMatch", "C:\\Users\\pcadmin\\HACKATHON");


//this.status = "uploading";

//  detail$.subscribe((data)=>{
// this.CvResult=data.resumeName; 
// })
// data.

// })

// ?



//const uploadJd$ = this.http.post("http://localhost:8080/api/Files/JobDescription", formDataJd);

// this.uploadService.readCv().subscribe((data)=>{
//   console.log(data);

// });
this.http.get<any>('http://localhost:8080/api/ResumeMatch?documentPath='+"C:/Users/pcadmin/HACKATHON" ,{
			//	headers: headers
			}).subscribe(data => {
				this.CvResult=data;
        console.log(this.CvResult);
			});;

//  this.uploadService.getCvDetail().subscribe((data)=>{
//   this.CvResult=data.type
     
// })
  }

  newPage(i:any){
    //console.log(this.CvResult[i].resumeName);
    //this.CvResult[i].resumeName
  //  this.router.navigate(['/evaluation'], { state: { resumeName: this.CvResult[i].resumeName } });
  this.http
  .get<any>('http://localhost:8080/compare/generateQuestion?'+this.CvResult[i].resumeName ,{
  //	headers: headers
  }).subscribe(data => {
    this.CvResult=data;
    console.log(this.CvResult);
  });;


  }
}