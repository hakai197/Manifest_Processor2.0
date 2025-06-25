document.addEventListener('DOMContentLoaded', function() {


  
 
  setupEventListeners();
  updateAvailableDoorsList();
  updateAssignedDoorsList();
  updateAvailableTrailersList();
  updateAvailableUnloadersList();


function setupEventListeners() {
 
  document.getElementById('door-filter').addEventListener('input', filterAvailableDoors);
  document.getElementById('trailer-filter').addEventListener('input', filterAvailableTrailers);
  document.getElementById('unloader-filter').addEventListener('input', filterAvailableUnloaders);
}

function updateAvailableDoorsList() {
  const availableDoorsList = document.getElementById('available-doors-list');
  const availableDoors = DockManagementResources.getAvailableDoors();
  
  availableDoorsList.innerHTML = '';
  
  if (availableDoors.length === 0) {
    availableDoorsList.innerHTML = '<li class="no-data">No available doors</li>';
    return;
  }
  
  availableDoors.forEach(door => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${door.number}</span>
        <span class="trailer-info">${door.location} Dock</span>
      </div>
      <span class="status-available">Available</span>
    `;
    availableDoorsList.appendChild(li);
  });
}

function updateAssignedDoorsList() {
  const assignedDoorsList = document.getElementById('assigned-doors-list');
  const assignedDoors = DockManagementResources.getAssignedDoors();
  
  assignedDoorsList.innerHTML = '';
  
  if (assignedDoors.length === 0) {
    assignedDoorsList.innerHTML = '<li class="no-data">No assigned doors</li>';
    return;
  }
  
  assignedDoors.forEach(door => {
    const li = document.createElement('li');
    
    
    const trailer = DockManagementResources.getTrailers().find(t => t.assignedTo === door.number);
    const unloader = DockManagementResources.getUnloaders().find(u => u.assignedTo === door.number);
    
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${door.number}</span>
        <span class="trailer-info">${door.location} Dock</span>
        ${trailer ? `<span class="trailer-info">Trailer: ${trailer.number} (${trailer.trailerType})</span>` : ''}
        ${unloader ? `<span class="trailer-info">Unloader: ${unloader.name}</span>` : ''}
      </div>
      <span class="status-assigned">Assigned</span>
    `;
    
    assignedDoorsList.appendChild(li);
  });
}
function updateAvailableTrailersList() {
  const availableTrailersList = document.getElementById('available-trailers-list');
  const availableTrailers = DockManagementResources.getAvailableTrailers();
  
  availableTrailersList.innerHTML = '';
  
  if (availableTrailers.length === 0) {
    availableTrailersList.innerHTML = '<li class="no-data">No available trailers</li>';
    return;
  }
  
  availableTrailers.forEach(trailer => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${trailer.number}</span>
        <span class="trailer-info">${trailer.trailerType}</span>
      </div>
      <span class="status-available">Available</span>
    `;
    availableTrailersList.appendChild(li);
  });
}

  function updateAvailableUnloadersList() {
  const availableUnloadersList = document.getElementById('available-unloaders-list');
  const availableUnloaders = DockManagementResources.getAvailableUnloaders();
  
  availableUnloadersList.innerHTML = '';
  
  if (availableUnloaders.length === 0) {
    availableUnloadersList.innerHTML = '<li class="no-data">No available unloaders</li>';
    return;
  }
  
  availableUnloaders.forEach(unloader => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${unloader.name}</span>
      </div>
      <span class="status-available">Available</span>
    `;
    availableUnloadersList.appendChild(li);
  });
}
function filterAvailableDoors() {
  const filter = document.getElementById('door-filter').value.toLowerCase();
  const doors = DockManagementResources.getAvailableDoors();
  const filteredDoors = doors.filter(door => 
    door.number.toLowerCase().includes(filter) || 
    door.location.toLowerCase().includes(filter)
  );
  
  const list = document.getElementById('available-doors-list');
  list.innerHTML = '';
  
  if (filteredDoors.length === 0) {
    list.innerHTML = '<li class="no-data">No matching doors</li>';
    return;
  }
  
  filteredDoors.forEach(door => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${door.number}</span>
        <span class="trailer-info">${door.location} Dock</span>
      </div>
      <span class="status-available">Available</span>
    `;
    list.appendChild(li);
  });
}

function filterAvailableTrailers() {
  const filter = document.getElementById('trailer-filter').value.toLowerCase();
  const trailers = DockManagementResources.getAvailableTrailers();
  const filteredTrailers = trailers.filter(trailer => 
    trailer.number.toLowerCase().includes(filter) || 
    trailer.trailerType.toLowerCase().includes(filter)
  );
  
  const list = document.getElementById('available-trailers-list');
  list.innerHTML = '';
  
  if (filteredTrailers.length === 0) {
    list.innerHTML = '<li class="no-data">No matching trailers</li>';
    return;
  }
  
  filteredTrailers.forEach(trailer => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${trailer.number}</span>
        <span class="trailer-info">${trailer.trailerType}</span>
      </div>
      <span class="status-available">Available</span>
    `;
    list.appendChild(li);
  });
}

function filterAvailableUnloaders() {
  const filter = document.getElementById('unloader-filter').value.toLowerCase();
  const unloaders = DockManagementResources.getAvailableUnloaders();
  const filteredUnloaders = unloaders.filter(unloader => 
    unloader.name.toLowerCase().includes(filter) || 
    unloader.employeeNumber.toLowerCase().includes(filter)
  );
  
  const list = document.getElementById('available-unloaders-list');
  list.innerHTML = '';
  
  if (filteredUnloaders.length === 0) {
    list.innerHTML = '<li class="no-data">No matching unloaders</li>';
    return;
  }
  
  filteredUnloaders.forEach(unloader => {
    const li = document.createElement('li');
    li.innerHTML = `
      <div class="assignment-info">
        <span class="door-name">${unloader.name}</span>
      </div>
      <span class="status-available">Available</span>
    `;
    list.appendChild(li);
  });
}
});
