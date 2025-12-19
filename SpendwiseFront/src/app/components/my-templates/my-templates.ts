import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, Plus, Eye, Calendar, FileText, LogIn } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ForecastTemplate } from '../../models/template.model';

@Component({
    selector: 'app-my-templates',
    standalone: true,
    imports: [
        CommonModule,
        NavbarComponent,
        LucideAngularModule,
        RouterLink,
        TableModule,
        InputTextModule,
        IconFieldModule,
        InputIconModule,
        ButtonModule,
        TagModule,
        TooltipModule
    ],
    templateUrl: './my-templates.html',
    styleUrl: './my-templates.css'
})
export class MyTemplatesComponent implements OnInit {
    userTemplates: ForecastTemplate[] = [];
    isLoading = true;

    constructor(
        private templateService: TemplateService,
        private router: Router,
        public authService: AuthService,
        private cd: ChangeDetectorRef
    ) { }

    ngOnInit() {
        this.loadTemplates();
    }

    loadTemplates() {
        this.isLoading = true;
        this.templateService.getUserTemplates().subscribe({
            next: (data) => {
                this.userTemplates = data;
                this.isLoading = false;
                this.cd.detectChanges();
            },
            error: (err) => {
                this.isLoading = false;
                this.cd.detectChanges();
            }
        });
    }

    navigate(path: string) {
        this.router.navigate([path]);
    }
}
