export interface TransportCreateRequest {
  code: string;
  originWarehouseId: string;
  destinationWarehouseId: string;
  vehicleId: string;
  driverId: string;
  plannedDepartureAt: string;
  plannedArrivalAt: string;
}
