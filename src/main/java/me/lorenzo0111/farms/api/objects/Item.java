package me.lorenzo0111.farms.api.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Data
public class Item {
    private final ItemStack item;
    private final int column;
    private final int row;
}
