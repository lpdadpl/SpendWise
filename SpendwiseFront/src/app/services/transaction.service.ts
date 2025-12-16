import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Transaction, CreateTransactionDto } from '../models/transaction.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TransactionService {
    private http = inject(HttpClient);
    private readonly API_URL = 'http://localhost:8080/api/transactions';

    getTransactions(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(this.API_URL);
    }

    createTransaction(dto: CreateTransactionDto): Observable<Transaction> {
        return this.http.post<Transaction>(this.API_URL, dto);
    }
}
