package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemoveAll extends SubCommand {

    public RemoveAll(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"removeall","deleteall"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.remove.all";
    }

    @Override
    public void execute(Player player, String[] args) {
        final int count = this.getCommand().getPlugin().getDataManager().getFarms().size();
        final List<Farm> farms = this.getCommand().getPlugin().getDataManager().getFarms();

        for (Farm farm : farms) {
            farm.safeDestroy();
        }

        this.getCommand().getPlugin().getDataManager().getFarms().clear();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getCommand().getPlugin().getMessages().getString("prefix") + getCommand().getPlugin().getMessages().getString("commands.remove-all")).replace("%count%",String.valueOf(count)));
    }
}
