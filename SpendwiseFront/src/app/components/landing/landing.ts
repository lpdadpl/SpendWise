import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { LucideAngularModule, Wallet, PieChart, Search, Users, TrendingUp, Shield, DollarSign, BarChart3 } from 'lucide-angular';
import { ChartModule } from 'primeng/chart';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
    selector: 'app-landing',
    standalone: true,
    imports: [CommonModule, RouterLink, LucideAngularModule, ChartModule],
    templateUrl: './landing.html',
    styleUrl: './landing.css'
})
export class LandingComponent {
    // Pie Chart Data
    public pieChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                callbacks: {
                    label: function (context) {
                        let label = context.label || '';
                        if (label) {
                            label += ': ';
                        }
                        if (context.parsed !== null) {
                            label += new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(context.parsed);
                        }
                        return label;
                    }
                }
            }
        }
    };

    public pieChartData: ChartData<'pie', number[], string | string[]> = {
        labels: ['Vivienda', 'Alimentación', 'Transporte', 'Entretenimiento'],
        datasets: [{
            data: [500, 300, 150, 100],
            backgroundColor: ['#5B7C99', '#7FB685', '#9DB4C8', '#A8C9AE'],
            hoverBackgroundColor: ['#5B7C99', '#7FB685', '#9DB4C8', '#A8C9AE']
        }]
    };

    // Bar Chart Data
    public barChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        scales: {
            x: {},
            y: {
                min: 0
            }
        },
        plugins: {
            legend: {
                display: true,
            }
        }
    };

    public barChartData: ChartData<'bar'> = {
        labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
        datasets: [
            { data: [1050, 1050, 1050, 1050, 1050, 1050], label: 'Presupuesto', backgroundColor: '#5B7C99' },
            { data: [920, 1020, 880, 950, 790, 1010], label: 'Gastado', backgroundColor: '#7FB685' }
        ]
    };
}
