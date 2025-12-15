export interface Category {
    id: string;
    name: string;
    budget: number;
    color: string;
}

export interface Template {
    id: string;
    name: string;
    description: string;
    categories: Category[];
    totalBudget: number;
    createdBy: string;
    createdAt: string;
}
