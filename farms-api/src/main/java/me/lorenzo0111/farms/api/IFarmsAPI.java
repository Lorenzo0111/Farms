package me.lorenzo0111.farms.api;

import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public interface IFarmsAPI {
    IFarm createFarm(Location location, Player owner);
    IFarm getFarm(Block block);
    List<IFarm> getFarms();
}
