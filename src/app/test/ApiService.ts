import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) { }

  sendMapToBackend(data: Map<string, string>) {
    const url = 'http://localhost:8080/rate/rate-code';
    return this.http.post(url, data);
  }
}
