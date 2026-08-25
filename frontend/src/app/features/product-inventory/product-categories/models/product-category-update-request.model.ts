export interface ProductCategoryUpdateRequest {
  code: string;
  name: string;
  description: string | null;
  active: boolean;
}
