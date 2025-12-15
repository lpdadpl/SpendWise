import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { LucideAngularModule, Wallet, PieChart, Search, Users, TrendingUp, Shield, DollarSign, BarChart3, Menu, X, Check, CheckCircle, AlertCircle, User, Mail, Lock, Plus, UserCircle, FileText, Trash2 } from 'lucide-angular';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideCharts(withDefaultRegisterables()),
    importProvidersFrom(LucideAngularModule.pick({ Wallet, PieChart, Search, Users, TrendingUp, Shield, DollarSign, BarChart3, Menu, X, Check, CheckCircle, AlertCircle, User, Mail, Lock, Plus, UserCircle, FileText, Trash2 }))
  ]
};
