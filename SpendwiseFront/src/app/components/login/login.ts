import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, LucideAngularModule],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class LoginComponent {
    username = '';
    password = '';
    error = '';
    isLoading = false;

    constructor(private authService: AuthService, private router: Router) { }

    onSubmit() {
        this.error = '';
        this.isLoading = true;
        this.authService.login(this.username, this.password).subscribe({
            next: (result) => {
                this.isLoading = false;
                // Si el login es exitoso, el backend devuelve un objeto con el token.
                // Si hay error, salta al bloque 'error'.
                this.router.navigate(['/dashboard']);
            },
            error: (err) => {
                console.error("Login fallido:", err);
                this.isLoading = false;
                if (err.status === 401) {
                    this.error = 'Credenciales incorrectas.';
                } else if (err.status === 400) {
                    this.error = err.error || 'Datos inválidos.';
                } else {
                    this.error = 'Error de conexión. Inténtelo más tarde.';
                }
            }
        });
    }
}
