package com.manifest_processor2.dao;

import com.manifest_processor2.model.DockDoor;

import java.util.List;


public interface DockDoorDao {
    DockDoor getDockDoorById(int id);
    List<DockDoor> getDockDoors();
    List<DockDoor> getDockDoorbyStatus(String status);
    DockDoor updateDockDoor(DockDoor dockDoor);
    DockDoor createDockDoor(DockDoor dockDoor);
}