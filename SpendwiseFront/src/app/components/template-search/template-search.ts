import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataViewModule } from 'primeng/dataview';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';

import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { TemplateService } from '../../services/template.service';
import { ForecastTemplate } from '../../models/template.model';

@Component({
    selector: 'app-template-search',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, DataViewModule, InputTextModule, ButtonModule, TagModule, IconFieldModule, InputIconModule, RouterLink],
    templateUrl: './template-search.html',
    styleUrl: './template-search.css'
})
export class TemplateSearchComponent {
    searchTerm: string = '';
    templates: ForecastTemplate[] = [];
    message = '';

    search() {
        this.templateService.getPublicTemplates(this.searchTerm).subscribe({
            next: templates => this.templates = templates,
            error: () => this.message = 'No se pudieron cargar las plantillas públicas.'
        });
    }

    copy(template: ForecastTemplate) {
        this.templateService.copyTemplate(template.id).subscribe({
            next: () => this.message = `Plantilla "${template.name}" copiada a tu biblioteca.`,
            error: () => this.message = 'No se pudo copiar la plantilla.'
        });
    }

    constructor(private templateService: TemplateService) {
        this.search();
    }
}
