package me.lorenzo0111.farms.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public abstract class SubCommand {
    @Getter private final FarmsCommand command;

    public abstract String[] getName();
    @Nullable public abstract String getPermission();
    public abstract void execute(Player player, String[] args);
}
