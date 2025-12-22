import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TemplateService } from '../../services/template.service';
import { NavbarComponent } from '../navbar/navbar';
import { TemplateRow, TemplateStructure } from '../../models/template.model';
import { TableModule } from 'primeng/table';
import { LucideAngularModule, ArrowLeft, Calendar, Globe, Lock, FileText, Layout } from 'lucide-angular';
import { TagModule } from 'primeng/tag';

@Component({
    selector: 'app-view-template',
    standalone: true,
    imports: [CommonModule, NavbarComponent, TableModule, LucideAngularModule, TagModule],
    templateUrl: './view-template.html'
})
export class ViewTemplateComponent implements OnInit {
    template: any;
    templateRows: TemplateRow[] = [];
    isLoading = true;

    constructor(
        private route: ActivatedRoute,
        private templateService: TemplateService,
        private router: Router,
        private cd: ChangeDetectorRef
    ) { }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.templateService.getTemplateById(+id).subscribe({
                next: (data) => {
                    this.template = data;
                    if (this.template.structure) {
                        try {
                            const parsedStructure: TemplateStructure = JSON.parse(this.template.structure);
                            this.templateRows = parsedStructure.rows || [];
                        } catch (e) {
                            console.error('Error parsing structure', e);
                        }
                    }
                    this.isLoading = false;
                    this.cd.detectChanges();
                },
                error: (err) => {
                    console.error('Error loading template', err);
                    this.isLoading = false;
                    this.cd.detectChanges();
                }
            });
        }
    }

    goBack() {
        this.router.navigate(['/templates']);
    }
}
