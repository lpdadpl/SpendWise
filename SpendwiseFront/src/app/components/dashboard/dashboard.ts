import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { LucideAngularModule, FileText, Search, Users, UserCircle, Plus, TrendingUp, Wallet } from 'lucide-angular';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, NavbarComponent, LucideAngularModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class DashboardComponent {
    transactions: any[] = [];
    totalBalance: number = 0;
    isLoading = true;

    constructor(
        public authService: AuthService,
        private router: Router,
        private transactionService: TransactionService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        this.transactionService.getTransactions().subscribe({
            next: (data) => {
                this.transactions = data;
                this.calculateBalance();
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading transactions', err);
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
