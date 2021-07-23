package me.lorenzo0111.farms.api.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public interface IFarm {
    Location getLocation();
    UUID getUuid();
    UUID getOwner();
    int getLevel();
    int getRadius();
    FarmType getType();
    Material getBlock();

    void safeDestroy();
    void pickup(HumanEntity entity);
}
