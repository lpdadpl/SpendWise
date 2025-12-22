import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { LucideAngularModule, Wallet, PieChart, Search, Users, TrendingUp, TrendingDown, Shield, DollarSign, BarChart3, Menu, X, Check, CheckCircle, AlertCircle, User, Mail, Lock, Plus, UserCircle, FileText, Trash2, Sheet, Table, ArrowLeft, Edit, Settings, Save, Globe, Calendar, Layout } from 'lucide-angular';
import { authInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: '.my-app-dark'
        }
      }
    }),
    importProvidersFrom(LucideAngularModule.pick({ Wallet, PieChart, Search, Users, TrendingUp, TrendingDown, Shield, DollarSign, BarChart3, Menu, X, Check, CheckCircle, AlertCircle, User, Mail, Lock, Plus, UserCircle, FileText, Trash2, Sheet, Table, ArrowLeft, Edit, Settings, Save, Globe, Calendar, Layout }))
  ]
};
