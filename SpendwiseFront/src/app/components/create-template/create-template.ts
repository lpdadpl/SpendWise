import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TemplateService } from '../../services/template.service';
import { Category, Template } from '../../models/template.model';
import { LucideAngularModule } from 'lucide-angular';

@Component({
    selector: 'app-create-template',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, LucideAngularModule, RouterLink],
    templateUrl: './create-template.html',
    styleUrl: './create-template.css'
})
export class CreateTemplateComponent {
    defaultColors = [
        '#5B7C99', '#7FB685', '#9DB4C8', '#A8C9AE', '#6B8CA3',
        '#8FC094', '#7A9DB0', '#9BC5A0', '#5B7C99', '#7FB685'
    ];

    name = '';
    description = '';
    Categories: Category[] = [
        { id: '1', name: '', budget: 0, color: this.defaultColors[0] }
    ];
    // Getter/setter or just use categories property
    categories = this.Categories;

    error = '';
    success = false;
    isLoading = false;

    constructor(
        private authService: AuthService,
        private templateService: TemplateService,
        private router: Router
    ) { }

    addCategory() {
        const newCategory: Category = {
            id: Date.now().toString(),
            name: '',
            budget: 0,
            color: this.defaultColors[this.categories.length % this.defaultColors.length]
        };
        this.categories.push(newCategory);
    }

    removeCategory(id: string) {
        if (this.categories.length > 1) {
            this.categories = this.categories.filter(c => c.id !== id);
        }
    }

    calculateTotal() {
        return this.categories.reduce((sum, cat) => sum + (Number(cat.budget) || 0), 0);
    }

    handleSubmit() {
        this.error = '';
        this.success = false;
        this.isLoading = true;

        // Validación
        if (!this.name.trim()) {
            this.error = 'El nombre de la plantilla es obligatorio';
            this.isLoading = false;
            return;
        }

        if (!this.description.trim()) {
            this.error = 'La descripción es obligatoria';
            this.isLoading = false;
            return;
        }

        const validCategories = this.categories.filter(c => c.name.trim() && c.budget > 0);
        if (validCategories.length === 0) {
            this.error = 'Debes agregar al menos una categoría válida con un presupuesto mayor a 0';
            this.isLoading = false;
            return;
        }

        const result = this.templateService.createTemplate({
            name: this.name,
            description: this.description,
            categories: validCategories,
            totalBudget: this.calculateTotal()
        });

        this.isLoading = false;

        if (result.success) {
            this.success = true;
            setTimeout(() => {
                this.router.navigate(['/dashboard']); // Redirect to dashboard or search
            }, 1500);
        } else {
            this.error = result.message || 'Error al crear la plantilla';
        }
    }

    navigateBack() {
        this.router.navigate(['/dashboard']);
    }
}
