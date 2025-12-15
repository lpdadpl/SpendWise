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
        const result = this.authService.login(this.username, this.password);
        this.isLoading = false;
        if (result.success) {
            this.router.navigate(['/dashboard']);
        } else {
            this.error = result.message || 'Error';
        }
    }
}
