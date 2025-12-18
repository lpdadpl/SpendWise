export interface Transaction {
    id: number;
    description: string;
    amount: number;
    transactionKind: 'INCOME' | 'EXPENSE';
    transactionTypeId: number;
    transactionTypeName: string;
}

export interface CreateTransactionDto {
    description: string;
    amount: number;
    transactionKind: 'INCOME' | 'EXPENSE';
    transactionTypeId: number;
}
