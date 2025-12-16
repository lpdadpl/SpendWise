export interface Transaction {
    id: number;
    description: string;
    amount: number;
    transactionType: 'INCOME' | 'EXPENSE';
    categoryId: number;
    categoryName: string;
}

export interface CreateTransactionDto {
    description: string;
    amount: number;
    transactionType: 'INCOME' | 'EXPENSE';
    categoryId: number;
}
