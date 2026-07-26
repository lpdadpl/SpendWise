import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Transaction, CreateTransactionDto } from '../models/transaction.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TransactionService {
    private http = inject(HttpClient);
    private readonly API_URL = `${environment.apiUrl}/api/transactions`;

    getTransactions(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(this.API_URL);
    }

    createTransaction(dto: CreateTransactionDto): Observable<Transaction> {
        return this.http.post<Transaction>(this.API_URL, dto);
    }
}
