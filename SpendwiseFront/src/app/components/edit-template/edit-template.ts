import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, Trash2, Plus, AlertCircle, FileText, CheckCircle, Table } from 'lucide-angular';

import { TemplateRow, TemplateStructure } from '../../models/template-structure.model';
import { ForecastTemplate } from '../../models/template.model';

@Component({
    selector: 'app-edit-template',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, LucideAngularModule, RouterLink],
    templateUrl: './edit-template.html',
    styleUrl: './edit-template.css'
})
export class EditTemplateComponent implements OnInit {
    templateId: number | null = null;
    name = '';
    description = '';

    // Rows logic
    rows: TemplateRow[] = [
        { name: '', transaction_ref_id: null, transaction_kind: 'INCOME', amount: 0 }
    ];

    isPublic = false;

    error = '';
    success = false;
    isLoading = false;

    constructor(
        private authService: AuthService,
        private templateService: TemplateService,
        private router: Router,
        private route: ActivatedRoute,
        private cd: ChangeDetectorRef
    ) { }

    ngOnInit() {
        this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            if (id) {
                this.templateId = +id;
                this.loadTemplate(this.templateId);
            } else {
                this.error = 'No se proporcionó un ID de plantilla válido.';
                this.cd.detectChanges();
            }
        });
    }

    loadTemplate(id: number) {
        this.isLoading = true;
        this.templateService.getTemplateById(id).subscribe({
            next: (template: ForecastTemplate) => {
                this.name = template.name;
                this.description = template.description;
                this.isPublic = template.isPublic;

                if (template.structure) {
                    try {
                        let structure: TemplateStructure;
                        if (typeof template.structure === 'string') {
                            structure = JSON.parse(template.structure);
                        } else {
                            structure = template.structure;
                        }

                        if (structure && structure.rows) {
                            this.rows = structure.rows;
                        }
                    } catch (e) {
                        console.error('Error parsing template structure', e);
                        this.error = 'Error al cargar la estructura de la plantilla.';
                    }
                }
                this.isLoading = false;
                this.cd.detectChanges();
            },
            error: (err) => {
                console.error(err);
                this.error = 'Error al cargar la plantilla.';
                this.isLoading = false;
                this.cd.detectChanges();
            }
        });
    }

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

    validateNumberInput(event: any, index: number) {
        const input = event.target as HTMLInputElement;
        let value = input.value;

        // Remove any non-numeric and non-decimal characters
        value = value.replace(/[^0-9.]/g, '');

        // Ensure only one decimal point
        const parts = value.split('.');
        if (parts.length > 2) {
            value = parts[0] + '.' + parts.slice(1).join('');
        }

        input.value = value;
        this.rows[index].amount = parseFloat(value) || 0;
    }

    handleSubmit() {
        if (!this.name || !this.description) {
            this.error = 'Por favor completa todos los campos requeridos';
            return;
        }

        if (!this.templateId) {
            this.error = 'Error interno: ID de plantilla no encontrado';
            return;
        }

        this.isLoading = true;
        this.error = '';

        // Construct JSON structure
        const structure: TemplateStructure = {
            type: 'table',
            rows: this.rows
        };

        const updatedTemplate: Partial<ForecastTemplate> = {
            name: this.name,
            description: this.description,
            structure: JSON.stringify(structure),
            isPublic: this.isPublic
        };

        this.templateService.updateTemplate(this.templateId, updatedTemplate).subscribe({
            next: () => {
                this.success = true;
                this.isLoading = false;
                this.cd.detectChanges();
                setTimeout(() => {
                    this.router.navigate(['/templates']);
                }, 1500);
            },
            error: (err) => {
                console.error(err);
                this.error = 'Error al actualizar la plantilla';
                this.isLoading = false;
                this.cd.detectChanges();
            }
        });
    }

    navigateBack() {
        this.router.navigate(['/templates']);
    }
}
