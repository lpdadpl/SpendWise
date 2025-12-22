export interface TemplateRow {
    name: string;
    transaction_ref_id: null;
    transaction_kind: 'INCOME' | 'EXPENSE';
    amount: number;
}

export interface TemplateStructure {
    type: string;
    rows: TemplateRow[];
}
