import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../models/category.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoryService {
    private http = inject(HttpClient);
    private readonly API_URL = `${environment.apiUrl}/api/categories`;

    getCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(this.API_URL);
    }
}
