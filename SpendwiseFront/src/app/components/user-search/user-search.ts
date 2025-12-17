import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';

import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-user-search',
    standalone: true,
    imports: [CommonModule, FormsModule, TableModule, InputTextModule, ButtonModule, IconFieldModule, InputIconModule, RouterLink],
    templateUrl: './user-search.html',
    styleUrl: './user-search.css'
})
export class UserSearchComponent {
    searchTerm: string = '';
    users = [
        { username: 'juanperez', email: 'juan@example.com', followers: 120 },
        { username: 'mariagarcia', email: 'maria@example.com', followers: 350 },
        { username: 'carloslopez', email: 'carlos@example.com', followers: 85 },
        { username: 'anarui', email: 'ana@example.com', followers: 900 },
        { username: 'luisgonzalez', email: 'luis@example.com', followers: 45 }
    ];

    constructor() { }
}
