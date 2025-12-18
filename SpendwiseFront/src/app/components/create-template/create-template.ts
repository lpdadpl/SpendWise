import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule } from 'lucide-angular';

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
    error = '';
    success = false;
    isLoading = false;

    constructor(
        private authService: AuthService,
        private templateService: TemplateService,
        private router: Router
    ) { }

    handleSubmit() {
        this.error = 'Funcionalidad de creación de plantillas en desarrollo';
        this.isLoading = false;
    }

    navigateBack() {
        this.router.navigate(['/dashboard']);
    }
}
