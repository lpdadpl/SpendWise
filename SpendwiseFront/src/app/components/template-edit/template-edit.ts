import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TemplateService } from '../../services/template.service';
import { NavbarComponent } from '../navbar/navbar';
import { ForecastTemplate } from '../../models/template.model';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
    selector: 'app-template-edit',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        NavbarComponent,
        CardModule,
        InputTextModule,
        TextareaModule,
        CheckboxModule,
        ButtonModule,
        MessageModule,
        ProgressSpinnerModule
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

    // Form model
    name = '';
    description = '';
    isPublic = false;

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
                this.isLoading = false;
            },
            error: (err) => {
                this.error = 'Error al cargar la plantilla';
                this.isLoading = false;
            }
        });
    }

    onSubmit() {
        if (!this.id) return;

        this.isSaving = true;
        this.error = '';

        const updates: Partial<ForecastTemplate> = {
            name: this.name,
            description: this.description,
            isPublic: this.isPublic
        };

        this.templateService.updateTemplate(this.id, updates).subscribe({
            next: () => {
                this.isSaving = false;
                this.router.navigate(['/templates']);
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
