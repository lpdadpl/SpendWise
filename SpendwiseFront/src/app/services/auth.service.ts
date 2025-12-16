import { Injectable, signal, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { User } from '../models/user.model';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

interface AuthResponse {
    accessToken: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly STORAGE_KEY = 'spendwise_auth';
    private readonly API_URL = 'http://localhost:8080/auth';
    private http = inject(HttpClient);

    currentUser = signal<User | null>(null);
    isAuthenticated = signal<boolean>(false);

    constructor() {
        this.checkAuth();
    }

    private checkAuth() {
        // Simple check: if token exists, we are authenticated.
        // In a real app, we might validate the token or fetch the profile.
        const token = localStorage.getItem(this.STORAGE_KEY);
        if (token) {
            this.isAuthenticated.set(true);
            // Optionally decode token to get username if needed,
            // or just set a placeholder since we don't have the user details persisted except token.
            // For now, we leave user null or reconstruct if we stored 'user' separately.
            // Let's assume we proceed with just knowing we are auth.
        }
    }

    login(username: string, password: string): Observable<{ success: boolean; message?: string }> {
        return this.http.post<AuthResponse>(`${this.API_URL}/login`, { username, password }).pipe(
            tap(response => {
                this.setSession(response.accessToken, username);
            }),
            map(() => ({ success: true })),
            catchError((error: HttpErrorResponse) => {
                let message = 'Error en el servidor';
                if (error.status === 401 || error.status === 403) {
                    message = 'Usuario o contraseña incorrectos';
                }
                return of({ success: false, message });
            })
        );
    }

    register(username: string, email: string, password: string): Observable<{ success: boolean; message?: string }> {
        return this.http.post(`${this.API_URL}/register`, { username, email, password }, { responseType: 'text' }).pipe(
            map(() => ({ success: true })),
            catchError((error: HttpErrorResponse) => {
                return of({ success: false, message: error.error || 'Error al registrar usuario' });
            })
        );
    }

    logout() {
        this.currentUser.set(null);
        this.isAuthenticated.set(false);
        localStorage.removeItem(this.STORAGE_KEY);
    }

    updateProfile(updates: Partial<User>): Observable<{ success: boolean; message?: string }> {
        // Implementation pending backend support
        return of({ success: false, message: 'Función no implementada en backend aún' });
    }

    private setSession(token: string, username: string) {
        localStorage.setItem(this.STORAGE_KEY, token);
        this.isAuthenticated.set(true);
        // Minimal user info
        this.currentUser.set({ username, email: '', password: '' });
    }
}
