import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Template } from '../models/template.model';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class TemplateService {
    templates = signal<Template[]>([
        {
            id: '1',
            name: 'Presupuesto Mensual Personal',
            description: 'Plantilla básica para gastos mensuales personales',
            categories: [
                { id: '1', name: 'Vivienda', budget: 500, color: '#5B7C99' },
                { id: '2', name: 'Alimentación', budget: 300, color: '#7FB685' },
                { id: '3', name: 'Transporte', budget: 150, color: '#9DB4C8' },
                { id: '4', name: 'Entretenimiento', budget: 100, color: '#A8C9AE' }
            ],
            totalBudget: 1050,
            createdBy: 'demo',
            createdAt: new Date().toISOString()
        }
    ]);

    constructor(
        private http: HttpClient,
        private authService: AuthService
    ) { }

    getUserTemplates() {
        return this.http.get<any[]>('/api/templates/mine');
    }

    getTemplateById(id: string) {
        return this.http.get<any>(`/api/templates/${id}`);
    }

    createTemplate(template: Omit<Template, 'id' | 'createdAt' | 'createdBy'>): { success: boolean; message?: string } {
        const currentUser = this.authService.user();
        if (!currentUser) return { success: false, message: 'No hay usuario autenticado' };

        const newTemplate: Template = {
            ...template,
            id: Date.now().toString(),
            createdBy: currentUser.username,
            createdAt: new Date().toISOString()
        };

        this.templates.update(templates => [...templates, newTemplate]);
        return { success: true, message: 'Plantilla creada exitosamente' };
    }

    updateTemplate(id: string, updates: Partial<Template>): { success: boolean; message?: string } {
        const template = this.templates().find(t => t.id === id);
        if (!template) return { success: false, message: 'Plantilla no encontrada' };

        const currentUser = this.authService.user();
        if (template.createdBy !== currentUser?.username) {
            return { success: false, message: 'No tienes permiso para editar esta plantilla' };
        }

        this.templates.update(templates =>
            templates.map(t => t.id === id ? { ...t, ...updates } : t)
        );
        return { success: true, message: 'Plantilla actualizada correctamente' };
    }

    deleteTemplate(id: string): { success: boolean; message?: string } {
        const template = this.templates().find(t => t.id === id);
        if (!template) return { success: false, message: 'Plantilla no encontrada' };

        const currentUser = this.authService.user();
        if (template.createdBy !== currentUser?.username) {
            return { success: false, message: 'No tienes permiso para eliminar esta plantilla' };
        }



        this.templates.update(templates => templates.filter(t => t.id !== id));
        return { success: true, message: 'Plantilla eliminada correctamente' };
    }
}
