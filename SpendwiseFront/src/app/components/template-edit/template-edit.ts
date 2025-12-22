import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TemplateService } from '../../services/template.service';
import { NavbarComponent } from '../navbar/navbar';
import { ForecastTemplate } from '../../models/template.model';
import { TemplateRow, TemplateStructure } from '../../models/template-structure.model';
import { LucideAngularModule, Trash2, Plus, AlertCircle, FileText, CheckCircle, Table, ArrowLeft, Save } from 'lucide-angular';

@Component({
    selector: 'app-template-edit',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        NavbarComponent,
        LucideAngularModule
    ],
    templateUrl: './template-edit.html',
    styleUrl: './template-edit.css'
})
export class TemplateEditComponent implements OnInit {
    id: number | null = null;
    template: ForecastTemplate | null = null;
    isLoading = true;
    isSaving = false;
    error = '';
    success = false;

    // Form model
    name = '';
    description = '';
    isPublic = false;
    rows: TemplateRow[] = [];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private templateService: TemplateService
    ) { }

    ngOnInit() {
        this.route.params.subscribe(params => {
            const id = params['id'];
            if (id) {
                this.id = Number(id);
                this.loadTemplate(this.id);
            } else {
                this.router.navigate(['/templates']);
            }
        });
    }

    loadTemplate(id: number) {
        this.isLoading = true;
        this.templateService.getTemplateById(id).subscribe({
            next: (data) => {
                this.template = data;
                this.name = data.name;
                this.description = data.description;
                this.isPublic = data.isPublic;

                // Parse structure
                try {
                    if (data.structure) {
                        const structure: TemplateStructure = JSON.parse(data.structure);
                        this.rows = structure.rows || [];
                    }
                } catch (e) {
                    console.error('Error parsing structure', e);
                    this.rows = [];
                }

                if (this.rows.length === 0) {
                    this.addItem(); // Ensure at least one row
                }

                this.isLoading = false;
            },
            error: (err) => {
                this.error = 'Error al cargar la plantilla';
                this.isLoading = false;
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

    onSubmit() {
        if (!this.id) return;
        if (!this.name || !this.description) {
            this.error = 'Completa los campos requeridos';
            return;
        }

        this.isSaving = true;
        this.error = '';

        const structure: TemplateStructure = {
            type: 'table',
            rows: this.rows
        };

        const updates: Partial<ForecastTemplate> = {
            name: this.name,
            description: this.description,
            isPublic: this.isPublic,
            structure: JSON.stringify(structure)
        };

        this.templateService.updateTemplate(this.id, updates).subscribe({
            next: () => {
                this.success = true;
                this.isSaving = false;
                setTimeout(() => {
                    this.router.navigate(['/templates']);
                }, 1500);
            },
            error: (err) => {
                this.error = 'Error al actualizar la plantilla';
                this.isSaving = false;
            }
        });
    }

    onCancel() {
        this.router.navigate(['/templates']);
    }
}
