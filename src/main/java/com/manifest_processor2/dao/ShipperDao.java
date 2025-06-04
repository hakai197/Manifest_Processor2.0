package com.manifest_processor2.dao;



import com.manifest_processor2.model.Shipper;

import java.util.List;

public interface ShipperDao {

    Shipper getShipperById(int id);
    List<Shipper> getShippers();
    Shipper createShipper(Shipper shipper);
    Shipper updateShipper(Shipper shipper);
    int deleteShipperById(int id);
    Shipper getByName(String name);

}
