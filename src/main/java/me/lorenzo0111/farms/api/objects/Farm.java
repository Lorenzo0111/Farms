package me.lorenzo0111.farms.api.objects;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
import me.lorenzo0111.farms.data.DataManager;
import me.lorenzo0111.farms.utils.BlockUtils;
import me.lorenzo0111.farms.utils.StandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@NoArgsConstructor
@Data
public class Farm implements ConfigurationSerializable {
    private Location location;
    private UUID uuid;
    private UUID owner;
    private int level;
    private int radius;
    private FarmType type;
    private Material block;
    private Material before;
    private transient int task;
    private List<ItemStack> items = new ArrayList<>();

    /**
     * @param location Location of the center
     * @param uuid UUID of the farm
     * @param owner Owner of the farm
     * @param level Level of the farm
     * @param radius Radius of the farm
     * @param type Type of the farm
     * @param block Material that will be generated
     * @param before The block before the gold block
     */
    public Farm(Location location, UUID uuid, UUID owner, int level, int radius, FarmType type, Material block, Material before) {
        this.location = location;
        this.uuid = uuid;
        this.owner = owner;
        this.level = level;
        this.radius = radius;
        this.type = type;
        this.block = block;
        this.before = before;
    }

    public Farm(Map<String,Object> data) {
        this(
                (Location) data.get("location"),
                UUID.fromString((String) data.get("uuid")),
                UUID.fromString((String) data.get("owner")),
                (int) data.get("level"),
                (int) data.get("radius"),
                FarmType.valueOf((String) data.get("type")),
                Material.getMaterial((String) data.get("block")),
                Material.getMaterial((String) data.get("before"))
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("location",location);
        map.put("uuid",uuid.toString());
        map.put("owner",owner.toString());
        map.put("radius",radius);
        map.put("level",level);
        map.put("type",type.toString());
        map.put("block",block.toString());
        map.put("before",before.toString());
        return map;
    }

    public void destroy(DataManager data) {
        data.getFarms().remove(this);
        this.safeDestroy();
    }

    public void safeDestroy() {
        location.getBlock().setType(Material.AIR);
        BlockUtils.near(location.clone().subtract(0,1,0).getBlock(), radius).forEach((block) -> {
            if (block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.GOLD_BLOCK))
                block.setType(Material.AIR);
        });
        if (task > 0)
            Bukkit.getScheduler().cancelTask(this.getTask());
        for (Entity entity : location.getChunk().getEntities()) {
            UUID uuid = StandUtils.farm(entity);
            if (this.uuid.equals(uuid)) entity.remove();
        }
    }

    public void pickup(HumanEntity player) {
        ItemStack item = CreateCommand.getItem();
        item = ItemBuilder.from(item)
                .setNbt("farm_level", String.valueOf(this.getLevel()))
                .build();
        player.getInventory().addItem(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Farm farm = (Farm) o;
        return Objects.equals(uuid, farm.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
