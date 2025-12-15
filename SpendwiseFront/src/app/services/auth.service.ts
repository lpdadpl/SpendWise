import { Injectable, signal } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly STORAGE_KEY = 'spendwise_auth';

    currentUser = signal<User | null>(null);
    isAuthenticated = signal<boolean>(false);

    // Simulated database of users
    private users: User[] = [
        { username: 'demo', email: 'demo@spendwise.com', password: 'demo123' }
    ];

    constructor() {
        this.checkAuth();
    }

    private checkAuth() {
        const auth = localStorage.getItem(this.STORAGE_KEY);
        if (auth) {
            const user = JSON.parse(auth);
            this.currentUser.set(user);
            this.isAuthenticated.set(true);
        }
    }

    login(username: string, password: string): { success: boolean; message?: string } {
        const user = this.users.find(u => u.username === username && u.password === password);
        if (user) {
            this.setSession(user);
            return { success: true };
        }
        return { success: false, message: 'Usuario o contraseña incorrectos' };
    }

    register(username: string, email: string, password: string): { success: boolean; message?: string } {
        if (this.users.find(u => u.username === username)) {
            return { success: false, message: 'El usuario ya existe' };
        }
        if (this.users.find(u => u.email === email)) {
            return { success: false, message: 'El email ya está registrado' };
        }
        const newUser = { username, email, password };
        this.users.push(newUser);
        // Auto login after register? React app didn't seem to imply it, but we can return success.
        // React app line 88 just adds user.
        return { success: true };
    }

    logout() {
        this.currentUser.set(null);
        this.isAuthenticated.set(false);
        localStorage.removeItem(this.STORAGE_KEY);
    }

    updateProfile(updates: Partial<User>): { success: boolean; message?: string } {
        const current = this.currentUser();
        if (!current) return { success: false, message: 'No hay usuario autenticado' };

        const updatedUser = { ...current, ...updates };
        this.setSession(updatedUser);

        // Update in users array
        this.users = this.users.map(u => u.username === current.username ? updatedUser : u);

        return { success: true, message: 'Perfil actualizado correctamente' };
    }

    private setSession(user: User) {
        this.currentUser.set(user);
        this.isAuthenticated.set(true);
        localStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
    }

    // Helper for search user
    getUsers() {
        return this.users;
    }
}
