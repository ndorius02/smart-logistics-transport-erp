export interface Customer {
  id: string;

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

  active: boolean;

  createdAt: string;
  updatedAt: string | null;
}
