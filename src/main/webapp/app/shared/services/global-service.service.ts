import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SqueaksDTO } from '../model/squeaksDTO.model';

@Injectable({
  providedIn: 'root',
})
export class GlobalServiceService {
  // Save a reference to the main API url, as this will be used by every api call method you write here
  apiUrl: string = 'http://localhost:8080/';

  constructor(public http: HttpClient) {}

  // Example API call made from UI -> Backend
  // Sending a GET request to http://localhost:8080/api/squeaks/id

  // Also included is a definition for the type of the return (just because you can ignore types with 'any' does not mean you should)
  // This allows you to access properties correctly on the returned object over in the home component
  public apiCall(id: number): Observable<SqueaksDTO> {
    return this.http.get<SqueaksDTO>(`${this.apiUrl}api/squeaks/${id}`);
  }

  // Example POST call to http://localhost:8080/fakeRoute
  // Sending through with the body, an object with 1 string property
  public postCall(obj: any): Observable<any> {
    return this.http.post(`${this.apiUrl}fakeRoute`, { fake: 'data' });
  }
}
