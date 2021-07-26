package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.guis.FarmsGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class GUICommand extends SubCommand {

    public GUICommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"gui", "list"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.gui";
    }

    @Override
    public void execute(Player player, String[] args) {
        new FarmsGUI(getCommand().getPlugin(), player).open(player);
    }


}
