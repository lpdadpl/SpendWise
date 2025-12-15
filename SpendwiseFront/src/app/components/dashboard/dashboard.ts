import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { LucideAngularModule, FileText, Search, Users, UserCircle, Plus, TrendingUp, Wallet } from 'lucide-angular';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent, LucideAngularModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class DashboardComponent {
    constructor(public authService: AuthService, private router: Router) { }

    navigate(path: string) {
        this.router.navigate([path]);
    }
}
