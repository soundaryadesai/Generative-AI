import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CvDetails } from '../models/CvDetail';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  readCv(): Observable<HttpEvent<CvDetails[]>> {
    const formData: FormData = new FormData();

   // formData.append('files', file);
   // debugger;
    //console.log(file);

    const req = new HttpRequest('GET', `${this.baseUrl}/api/ResumeMatch`, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  // getFiles(): Observable<CvDetails> {
  //   return this.http.get(`${this.baseUrl}/files`);
  // }
}