/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.api.objects.IFarm;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemoveAllCommand extends SubCommand {

    public RemoveAllCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"removeall","deleteall","clear"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.remove.all";
    }

    @Override
    public void execute(Player player, String[] args) {
        final int count = this.getCommand().getPlugin().getDataManager().getFarms().size();
        final List<IFarm> farms = this.getCommand().getPlugin().getDataManager().getFarms();

        for (IFarm farm : farms) {
            farm.safeDestroy();
        }

        this.getCommand().getPlugin().getDataManager().getFarms().clear();
        this.getCommand().getPlugin().getDataManager().save(false);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getCommand().getPlugin().getMessages().getString("prefix") + getCommand().getPlugin().getMessages().getString("commands.remove-all")).replace("%count%",String.valueOf(count)));
    }
}
