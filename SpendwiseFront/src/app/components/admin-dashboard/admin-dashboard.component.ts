import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth.service';

import { RouterModule } from '@angular/router';

import { NavbarComponent } from '../navbar/navbar';

@Component({
    selector: 'app-admin-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule, NavbarComponent],
    templateUrl: './admin-dashboard.component.html',
    styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
    private http = inject(HttpClient);
    private authService = inject(AuthService);

    users = signal<User[]>([]);
    errorMessage = signal<string>('');

    constructor() {
        this.loadUsers();
    }

    loadUsers() {
        this.http.get<User[]>('http://localhost:8080/api/admin/users').subscribe({
            next: (data) => this.users.set(data),
            error: (err) => this.errorMessage.set('Error al cargar usuarios')
        });
    }

    deleteUser(userId: number | undefined) {
        if (!userId) return;

        if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
            this.http.delete(`http://localhost:8080/api/admin/users/${userId}`, { responseType: 'text' }).subscribe({
                next: () => {
                    this.loadUsers(); // Reload list
                },
                error: (err) => {
                    this.errorMessage.set('Error al eliminar usuario');
                    console.error(err);
                }
            });
        }
    }
}
