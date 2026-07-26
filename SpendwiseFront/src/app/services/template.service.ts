import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ForecastTemplate } from '../models/template.model';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class TemplateService {
    templates = signal<ForecastTemplate[]>([]);

    private readonly API_URL = `${environment.apiUrl}/api/templates`;

    constructor(
        private http: HttpClient,
        private authService: AuthService
    ) { }

    getUserTemplates() {
        return this.http.get<ForecastTemplate[]>(`${this.API_URL}/mine`);
    }

    getPublicTemplates(search = '') {
        return this.http.get<ForecastTemplate[]>(`${this.API_URL}/public`, {
            params: search ? { search } : {}
        });
    }

    copyTemplate(id: number) {
        return this.http.post<ForecastTemplate>(`${this.API_URL}/${id}/copy`, {});
    }

    createTemplate(template: Partial<ForecastTemplate>) {
        return this.http.post<ForecastTemplate>(this.API_URL, template);
    }

    getTemplateById(id: number) {
        return this.http.get<ForecastTemplate>(`${this.API_URL}/${id}`);
    }

    updateTemplate(id: number, template: Partial<ForecastTemplate>) {
        return this.http.put<ForecastTemplate>(`${this.API_URL}/${id}`, template);
    }

    deleteTemplate(id: number) {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}
