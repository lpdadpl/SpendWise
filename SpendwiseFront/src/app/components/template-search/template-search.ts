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

@Component({
    selector: 'app-template-search',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, DataViewModule, InputTextModule, ButtonModule, TagModule, IconFieldModule, InputIconModule, RouterLink],
    templateUrl: './template-search.html',
    styleUrl: './template-search.css'
})
export class TemplateSearchComponent {
    searchTerm: string = '';
    templates = [
        { name: 'Presupuesto Familiar', author: 'juanperez', category: 'Hogar', price: 0, rating: 4.5 },
        { name: 'Viaje a Europa', author: 'mariagarcia', category: 'Viajes', price: 5, rating: 4.8 },
        { name: 'Ahorro Universitario', author: 'carloslopez', category: 'Educación', price: 0, rating: 4.2 },
        { name: 'Control de Gastos Diarios', author: 'anarui', category: 'Personal', price: 2, rating: 4.7 },
        { name: 'Planificación de Boda', author: 'luisgonzalez', category: 'Eventos', price: 10, rating: 4.9 }
    ];

    getSeverity(category: string) {
        switch (category) {
            case 'Hogar': return 'success';
            case 'Viajes': return 'info';
            case 'Educación': return 'warn';
            case 'Personal': return 'secondary';
            case 'Eventos': return 'danger';
            default: return 'info';
        }
    }

    constructor() { }
}
