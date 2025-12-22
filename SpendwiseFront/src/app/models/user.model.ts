export interface User {
  username: string;
  email: string;
  password?: string;
  id?: number; // Optional because logic often uses it without id for login/register
  role?: string;
}
