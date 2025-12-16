import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, LucideAngularModule],
    templateUrl: './register.html',
    styleUrl: './register.css'
})
export class RegisterComponent {
    username = '';
    email = '';
    password = '';
    error = '';
    success = false;
    isLoading = false;

    constructor(private authService: AuthService, private router: Router) { }

    onSubmit() {
        this.error = '';
        this.isLoading = true;
        this.authService.register(this.username, this.email, this.password).subscribe({
            next: (result) => {
                this.isLoading = false;
                if (result.success) {
                    this.success = true;
                    setTimeout(() => this.router.navigate(['/login']), 1500);
                } else {
                    this.error = result.message || 'Error';
                }
            },
            error: () => {
                this.isLoading = false;
                this.error = 'Error de conexión';
            }
        });
    }
}
