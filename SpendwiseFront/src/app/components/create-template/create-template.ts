import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, Trash2, Plus, AlertCircle, FileText, CheckCircle, Table } from 'lucide-angular';

import { TemplateRow, TemplateStructure } from '../../models/template-structure.model';
import { ForecastTemplate } from '../../models/template.model';

@Component({
    selector: 'app-create-template',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, LucideAngularModule, RouterLink],
    templateUrl: './create-template.html',
    styleUrl: './create-template.css'
})
export class CreateTemplateComponent {
    name = '';
    description = '';

    // Rows logic
    rows: TemplateRow[] = [
        { name: 'Sueldo', transaction_ref_id: null, transaction_kind: 'INCOME', amount: 0 },
        { name: 'Alquiler', transaction_ref_id: null, transaction_kind: 'EXPENSE', amount: 0 }
    ];

    isPublic = false;

    error = '';
    success = false;
    isLoading = false;

    constructor(
        private authService: AuthService,
        private templateService: TemplateService,
        private router: Router
    ) { }

    addItem() {
        this.rows.push({
            name: '',
            transaction_ref_id: null,
            transaction_kind: 'EXPENSE',
            amount: 0
        });
    }

    removeItem(index: number) {
        if (this.rows.length > 1) {
            this.rows.splice(index, 1);
        }
    }

    handleSubmit() {
        if (!this.name || !this.description) {
            this.error = 'Por favor completa todos los campos requeridos';
            return;
        }

        this.isLoading = true;
        this.error = '';

        // Construct JSON structure
        const structure: TemplateStructure = {
            type: 'table',
            rows: this.rows
        };

        const newTemplate: Partial<ForecastTemplate> = {
            name: this.name,
            description: this.description,
            structure: JSON.stringify(structure),
            isPublic: this.isPublic
        };

        this.templateService.createTemplate(newTemplate).subscribe({
            next: () => {
                this.success = true;
                this.isLoading = false;
                setTimeout(() => {
                    this.router.navigate(['/templates']);
                }, 1500);
            },
            error: (err) => {
                console.error(err);
                this.error = 'Error al crear la plantilla';
                this.isLoading = false;
            }
        });
    }

    navigateBack() {
        this.router.navigate(['/dashboard']);
    }
}
