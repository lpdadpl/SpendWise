import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TemplateService } from '../../services/template.service';
import { NavbarComponent } from '../navbar/navbar';
import { LucideAngularModule, ArrowLeft, Calendar, FileText } from 'lucide-angular';
import { ForecastTemplate, TemplateStructure } from '../../models/template.model';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
    selector: 'app-template-view',
    standalone: true,
    imports: [
        CommonModule,
        NavbarComponent,
        RouterLink,
        LucideAngularModule,
        CardModule,
        TagModule,
        ButtonModule,
        ProgressSpinnerModule
    ],
    templateUrl: './template-view.html',
    styleUrl: './template-view.css'
})
export class TemplateViewComponent implements OnInit {
    template: ForecastTemplate | null = null;
    parsedStructure: TemplateStructure | null = null;
    isLoading = true;
    error = '';

    constructor(
        private route: ActivatedRoute,
        private templateService: TemplateService
    ) { }

    ngOnInit() {
        this.route.params.subscribe(params => {
            const id = params['id'];
            if (id) {
                this.loadTemplate(id);
            }
        });
    }

    loadTemplate(id: string) {
        this.isLoading = true;
        this.templateService.getTemplateById(Number(id)).subscribe({
            next: (data: ForecastTemplate) => {
                this.template = data;
                try {
                    this.parsedStructure = JSON.parse(data.structure);
                } catch (e) {
                    console.error('Error parsing structure JSON', e);
                }
                this.isLoading = false;
            },
            error: (err) => {
                this.error = 'Error al cargar la plantilla';
                this.isLoading = false;
            }
        });
    }
}
