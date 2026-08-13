export interface LoginResponse {
  token: string;
  tokenType: string;
  email: string;
  authorities: string[];
}
