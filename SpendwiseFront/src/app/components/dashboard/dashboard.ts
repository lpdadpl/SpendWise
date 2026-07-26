import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, FileText, Search, Users, UserCircle, Plus, TrendingUp, Wallet, Eye, Settings, Edit } from 'lucide-angular';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, FormsModule, NavbarComponent, LucideAngularModule, RouterModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class DashboardComponent {
    transactions: import('../../models/transaction.model').Transaction[] = [];
    userTemplates: import('../../models/template.model').ForecastTemplate[] = [];
    totalBalance: number = 0;
    plannedIncome = 0;
    plannedExpense = 0;
    actualIncome = 0;
    actualExpense = 0;
    budgetDelta = 0;
    primaryInsight = 'Crea una plantilla para comparar tu previsión con tus movimientos reales.';
    goalName = 'Fondo de emergencia';
    goalTarget = 1000;
    goalSaved = 0;
    isLoading = true;

    constructor(
        public authService: AuthService,
        private router: Router,
        private transactionService: TransactionService,
        private templateService: TemplateService
    ) { }

    ngOnInit() {
        this.loadGoal();
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
                this.calculateForecastInsights();
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
            if (t.transactionKind === 'INCOME') income += t.amount;
            else expense += t.amount;
        });
        this.actualIncome = income;
        this.actualExpense = expense;
        this.totalBalance = income - expense;
        this.goalSaved = Math.max(this.totalBalance, 0);
        this.calculateForecastInsights();
    }

    get goalProgress() {
        if (this.goalTarget <= 0) return 0;
        return Math.min(100, Math.round((this.goalSaved / this.goalTarget) * 100));
    }

    saveGoal() {
        localStorage.setItem('spendwise_goal', JSON.stringify({
            name: this.goalName,
            target: this.goalTarget
        }));
    }

    private loadGoal() {
        const rawGoal = localStorage.getItem('spendwise_goal');
        if (!rawGoal) return;
        try {
            const goal = JSON.parse(rawGoal);
            this.goalName = goal.name || this.goalName;
            this.goalTarget = Number(goal.target) || this.goalTarget;
        } catch {
            localStorage.removeItem('spendwise_goal');
        }
    }

    calculateForecastInsights() {
        this.plannedIncome = 0;
        this.plannedExpense = 0;

        for (const template of this.userTemplates) {
            if (!template.structure) continue;
            try {
                const structure = JSON.parse(template.structure);
                for (const row of structure.rows || []) {
                    const amount = Number(row.amount) || 0;
                    if (row.transaction_kind === 'INCOME') this.plannedIncome += amount;
                    if (row.transaction_kind === 'EXPENSE') this.plannedExpense += amount;
                }
            } catch {
                continue;
            }
        }

        this.budgetDelta = this.plannedExpense - this.actualExpense;
        if (this.plannedExpense > 0) {
            const percentage = Math.round((this.actualExpense / this.plannedExpense) * 100);
            this.primaryInsight = percentage > 100
                ? `Vas ${percentage - 100}% por encima de tu gasto previsto.`
                : `Has usado el ${percentage}% de tu gasto previsto.`;
        }
    }

    navigate(path: string) {
        this.router.navigate([path]);
    }
}
