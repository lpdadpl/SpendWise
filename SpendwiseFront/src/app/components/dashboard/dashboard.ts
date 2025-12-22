import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, FileText, Search, Users, UserCircle, Plus, TrendingUp, Wallet, Eye, Settings, Edit } from 'lucide-angular';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent, LucideAngularModule, RouterModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class DashboardComponent {
    transactions: any[] = [];
    userTemplates: any[] = [];
    totalBalance: number = 0;
    isLoading = true;

    constructor(
        public authService: AuthService,
        private router: Router,
        private transactionService: TransactionService,
        private templateService: TemplateService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        // Cargar transacciones
        this.transactionService.getTransactions().subscribe({
            next: (data) => {
                this.transactions = data;
                this.calculateBalance();
            },
            error: (err) => {
                this.isLoading = false;
            }
        });

        // Cargar plantillas del usuario
        this.templateService.getUserTemplates().subscribe({
            next: (data) => {
                this.userTemplates = data;
                this.isLoading = false;
            },
            error: (err) => {
                this.isLoading = false;
            }
        });
    }

    calculateBalance() {
        let income = 0;
        let expense = 0;
        this.transactions.forEach(t => {
            if (t.transactionType === 'INCOME') income += t.amount;
            else expense += t.amount;
        });
        this.totalBalance = income - expense;
    }

    navigate(path: string) {
        this.router.navigate([path]);
    }
}
