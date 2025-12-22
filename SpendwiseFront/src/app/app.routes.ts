import { Routes } from '@angular/router';
import { LandingComponent } from './components/landing/landing';
import { RegisterComponent } from './components/register/register';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { ProfileComponent } from './components/profile/profile';
import { CreateTemplateComponent } from './components/create-template/create-template';
import { UserSearchComponent } from './components/user-search/user-search';
import { TemplateSearchComponent } from './components/template-search/template-search';
import { MyTemplatesComponent } from './components/my-templates/my-templates';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
    { path: '', component: LandingComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
    { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
    { path: 'templates', component: MyTemplatesComponent, canActivate: [authGuard] },
    { path: 'templates/new', component: CreateTemplateComponent, canActivate: [authGuard] },
    { path: 'templates/search', component: TemplateSearchComponent, canActivate: [authGuard] },
    { path: 'users/search', component: UserSearchComponent, canActivate: [authGuard] },
    { path: 'admin', loadComponent: () => import('./components/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent), canActivate: [adminGuard] },
    { path: '**', redirectTo: '' }
];
