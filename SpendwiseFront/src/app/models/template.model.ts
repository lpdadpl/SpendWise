export interface ForecastTemplate {
    id: number;
    name: string;
    description: string;
    structure: string;
    isPublic: boolean;
    createdAt: string;
    ownerName?: string;
}

export interface TemplateRow {
    name: string;
    transaction_type: 'INCOME' | 'EXPENSE';
    amount: number;
}

export interface TemplateStructure {
    type: string;
    rows: TemplateRow[];
}
