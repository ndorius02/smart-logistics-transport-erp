export interface SupplierCreateRequest {
  code: string;
  companyName: string;

  contactName: string | null;
  email: string | null;
  phoneNumber: string | null;

  address: string;
  city: string;
  postalCode: string | null;
  country: string;

  vatNumber: string | null;
}
