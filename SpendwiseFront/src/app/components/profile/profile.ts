import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { LucideAngularModule } from 'lucide-angular';
import { User } from '../../models/user.model';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, LucideAngularModule],
    templateUrl: './profile.html',
    styleUrl: './profile.css'
})
export class ProfileComponent {
    isEditing = false;
    username = '';
    email = '';
    currentPassword = '';
    newPassword = '';
    confirmPassword = '';
    error = '';
    success = '';
    isLoading = false;

    constructor(public authService: AuthService) {
        const user = this.authService.currentUser();
        this.username = user?.username || '';
        this.email = user?.email || '';
    }

    handleCancel() {
        const user = this.authService.currentUser();
        this.username = user?.username || '';
        this.email = user?.email || '';
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';
        this.error = '';
        this.success = '';
        this.isEditing = false;
    }

    handleSubmit() {
        this.error = '';
        this.success = '';
        this.isLoading = true;

        // Validación
        if (!this.username.trim()) {
            this.error = 'El nombre de usuario es obligatorio';
            this.isLoading = false;
            return;
        }

        if (!this.email.trim() || !this.email.includes('@')) {
            this.error = 'Por favor ingresa un email válido';
            this.isLoading = false;
            return;
        }

        const updates: Partial<User> = {
            username: this.username.trim(),
            email: this.email.trim()
        };

        const user = this.authService.currentUser();

        // Si está cambiando la contraseña
        if (this.currentPassword || this.newPassword || this.confirmPassword) {
            if (!this.currentPassword) {
                this.error = 'Ingresa tu contraseña actual';
                this.isLoading = false;
                return;
            }

            if (!this.newPassword) {
                this.error = 'Ingresa una nueva contraseña';
                this.isLoading = false;
                return;
            }

            if (this.newPassword.length < 6) {
                this.error = 'La nueva contraseña debe tener al menos 6 caracteres';
                this.isLoading = false;
                return;
            }

            if (this.newPassword !== this.confirmPassword) {
                this.error = 'Las contraseñas no coinciden';
                this.isLoading = false;
                return;
            }

            updates.password = this.newPassword;
        }

        this.authService.updateProfile(updates).subscribe({
            next: (result) => {
                this.isLoading = false;
                if (result.success) {
                    this.success = result.message || 'Perfil actualizado correctamente';
                    this.currentPassword = '';
                    this.newPassword = '';
                    this.confirmPassword = '';
                    this.isEditing = false;
                } else {
                    this.error = result.message || 'Error al actualizar el perfil';
                }
            },
            error: () => {
                this.isLoading = false;
                this.error = 'Error de conexión';
            }
        });
    }
}
