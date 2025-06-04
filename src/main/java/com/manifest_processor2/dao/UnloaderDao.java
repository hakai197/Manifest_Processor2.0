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

    Unloader getUnloaderByName(String name);
}
