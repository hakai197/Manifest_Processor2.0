package com.manifest_processor2.dao;


import com.manifest_processor2.model.Unloader;

import java.util.List;

public interface UnloaderDao {
    Unloader getUnloaderById(int id);
    Unloader createUnloader(Unloader unloader);
    Unloader updateUnloader(int id, Unloader unloader);
    int deleteUnloaderById(int id);
    List<Unloader> getAllUnloaders();

    List<Unloader> getAvailableUnloaders();

    List<Unloader> getUnloadersByStatus(String status);

    List<Unloader> getUnloadersByDoorId(int doorId);

    boolean assignUnloaderToDoor(int unloaderId, int doorId);

    boolean unassignUnloader(int unloaderId);

    Unloader getUnloaderByName(String name);
}
