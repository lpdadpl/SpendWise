import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import { TemplateService } from '../../services/template.service';
import { LucideAngularModule, Plus, Eye, Calendar, FileText } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-my-templates',
    standalone: true,
    imports: [CommonModule, NavbarComponent, LucideAngularModule, RouterLink],
    templateUrl: './my-templates.html',
    styleUrl: './my-templates.css'
})
export class MyTemplatesComponent implements OnInit {
    userTemplates: any[] = [];
    isLoading = true;

    constructor(
        private templateService: TemplateService,
        private router: Router,
        public authService: AuthService
    ) { }

    ngOnInit() {
        this.loadTemplates();
    }

    loadTemplates() {
        this.isLoading = true;
        this.templateService.getUserTemplates().subscribe({
            next: (data) => {
                console.log('Templates data received:', data);
                this.userTemplates = data;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading templates', err);
                this.isLoading = false;
            }
        });
    }

    navigate(path: string) {
        this.router.navigate([path]);
    }
}
