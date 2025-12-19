import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ForecastTemplate } from '../models/template.model';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class TemplateService {
    templates = signal<ForecastTemplate[]>([]);

    private readonly API_URL = 'http://localhost:8080/api/templates';

    constructor(
        private http: HttpClient,
        private authService: AuthService
    ) { }

    getUserTemplates() {
        return this.http.get<ForecastTemplate[]>(`${this.API_URL}/mine`);
    }

    getTemplateById(id: number) {
        return this.http.get<any>(`${this.API_URL}/${id}`);
    }

    updateTemplate(id: number, template: Partial<ForecastTemplate>) {
        return this.http.put<ForecastTemplate>(`${this.API_URL}/${id}`, template);
    }
}
