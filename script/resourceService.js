const DockManagementResources = (function() {

  const allDockResources  = [
    // Dock doors
    { id: 1, type: 'door', number: 'N1', location: 'North', status: 'Assigned' },
    { id: 2, type: 'door', number: 'N2', location: 'North', status: 'Assigned' },
    { id: 3, type: 'door', number: 'N3', location: 'North', status: 'Assigned' },
    { id: 4, type: 'door', number: 'N4', location: 'North', status: 'Unassigned' },
    { id: 5, type: 'door', number: 'N5', location: 'North', status: 'Unassigned' },
    
    // Trailers
    { id: 1, type: 'trailer', number: 'OF1234', trailerType: 'Local/City', status: 'Assigned', assignedTo: 'N1' },
    { id: 2, type: 'trailer', number: 'LE4567', trailerType: 'Lift-Gate', status: 'Assigned', assignedTo: 'N2' },
    { id: 3, type: 'trailer', number: 'B2389', trailerType: 'Box Truck', status: 'Assigned', assignedTo: 'N3' },
    { id: 4, type: 'trailer', number: 'TR5678', trailerType: 'Flatbed', status: 'Unassigned' },
    { id: 5, type: 'trailer', number: 'CV7890', trailerType: 'Conestoga', status: 'Unassigned' },
    
    // Unloaders
    { id: 1, type: 'unloader', name: 'John Smith', shift: '2nd Shift', employeeNumber: '45782', status: 'Assigned', assignedTo: 'N1' },
    { id: 2, type: 'unloader', name: 'Emily Johnson', shift: '2nd Shift', employeeNumber: '39821', status: 'Assigned', assignedTo: 'N2' },
    { id: 3, type: 'unloader', name: 'Michael Williams', shift: '2nd Shift', employeeNumber: '61245', status: 'Assigned', assignedTo: 'N3' },
    { id: 4, type: 'unloader', name: 'Sarah Brown', shift: '2nd Shift', employeeNumber: '53467', status: 'Unassigned' },
    { id: 5, type: 'unloader', name: 'David Jones', shift: '2nd Shift', employeeNumber: '78934', status: 'Unassigned' },
    
    // Orders
    { id: 1, type: 'order', orderNumber: '123456789', trailer: 'OF1234', handlingUnit: 2, weight: 61, status: 'unloading' },
    { id: 2, type: 'order', orderNumber: '234567891', trailer: 'OF1234', handlingUnit: 4, weight: 113, status: 'unloading' },
    { id: 3, type: 'order', orderNumber: '345678911', trailer: 'OF1234', handlingUnit: 6, weight: 99, status: 'unloading' },
    { id: 4, type: 'order', orderNumber: '456789123', trailer: 'OF1234', handlingUnit: 5, weight: 65, status: 'unloading' },
    { id: 5, type: 'order', orderNumber: '567891234', trailer: 'OF1234', handlingUnit: 8, weight: 71, status: 'unloading' }
  ];

  
    function getResources() {
    return allDockResources;
  }

  function getDoors() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'door';
    });
  }

  function getAvailableDoors() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'door' && resource.status === 'Unassigned';
    });
  }

  function getAssignedDoors() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'door' && resource.status === 'Assigned';
    });
  }

  function getTrailers() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'trailer';
    });
  }

  function getAvailableTrailers() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'trailer' && resource.status === 'Unassigned';
    });
  }

  function getUnloaders() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'unloader';
    });
  }

  function getAvailableUnloaders() {
    return allDockResources.filter(function(resource) {
      return resource.type === 'unloader' && resource.status === 'Unassigned';
    });
  }

  function getOrdersForTrailer(trailerNumber) {
    return allDockResources.filter(function(resource) {
      return resource.type === 'order' && resource.trailer === trailerNumber;
    });
  }

  return {
    getResources: getResources,
    getDoors: getDoors,
    getAvailableDoors: getAvailableDoors,
    getAssignedDoors: getAssignedDoors,
    getTrailers: getTrailers,
    getAvailableTrailers: getAvailableTrailers,
    getUnloaders: getUnloaders,
    getAvailableUnloaders: getAvailableUnloaders,
    getOrdersForTrailer: getOrdersForTrailer
  };
})();