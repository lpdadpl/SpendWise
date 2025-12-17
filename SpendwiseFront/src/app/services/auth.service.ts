import { Injectable, signal, inject, computed } from '@angular/core';
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

    private userSubject = signal<User | null>(null);
    readonly user = this.userSubject.asReadonly();
    readonly isAuthenticated = computed(() => !!this.userSubject());

    constructor() {
        this.checkAuth();
    }

    private checkAuth() {
        const token = localStorage.getItem(this.STORAGE_KEY);
        if (token) {
            try {
                // Decode token to get username (basic implementation)
                const payload = JSON.parse(atob(token.split('.')[1]));
                const user: User = { username: payload.sub, email: '', password: '' };
                this.userSubject.set(user);
            } catch (e) {
                console.warn("Invalid token structure", e);
                this.logout();
            }
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
        this.userSubject.set(null);
        localStorage.removeItem(this.STORAGE_KEY);
    }

    updateProfile(updates: Partial<User>): Observable<{ success: boolean; message?: string }> {
        // Implementation pending backend support
        return of({ success: false, message: 'Función no implementada en backend aún' });
    }

    private setSession(token: string, username: string) {
        localStorage.setItem(this.STORAGE_KEY, token);
        // Minimal user info
        this.userSubject.set({ username, email: '', password: '' });
    }
}
