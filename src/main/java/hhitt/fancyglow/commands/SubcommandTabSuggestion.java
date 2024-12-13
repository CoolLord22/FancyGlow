package hhitt.fancyglow.commands;

import hhitt.fancyglow.FancyGlow;
import hhitt.fancyglow.managers.GlowManager;
import hhitt.fancyglow.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubcommandTabSuggestion implements TabCompleter {

    private final GlowManager glowManager;

    public SubcommandTabSuggestion(FancyGlow plugin) {
        this.glowManager = plugin.getGlowManager();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        boolean hasAdminPermission = sender.hasPermission("fancyglow.admin");
        boolean canDisable = sender.hasPermission("fancyglow.command.disable");
        boolean canDisableOthers = sender.hasPermission("fancyglow.command.disable.others");
        boolean canDisableEveryone = sender.hasPermission("fancyglow.command.disable.everyone");
        boolean canReload = sender.hasPermission("fancyglow.command.reload");
        boolean canColor = sender.hasPermission("fancyglow.command.color");

        // Suggest for disable and color
        if (args.length == 1) {
            // Add permission required arguments
            if (hasAdminPermission) {
                completions.addAll(Arrays.asList("disable", "reload", "color"));
            } else {
                if (canDisable) completions.add("disable");
                if (canReload) completions.add("reload");
                if (canColor) completions.add("color");
            }

            // Returns starting string argument completion
            return filter(completions, args[0]);
        }

        // Suggest all colors and rainbow
        if (args.length == 2) {
            // Returns empty list to console
            if (!(sender instanceof Player)) return Collections.emptyList();

            if (args[0].equalsIgnoreCase("color") && (canColor || hasAdminPermission)) {

                completions = ColorUtils.getChatColorValues()
                        .stream()
                        .filter(name -> glowManager.hasGlowPermission(((Player) sender), name))
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

                boolean canRainbow = sender.hasPermission("fancyglow.rainbow");
                // Add rainbow completion if can
                if (canRainbow) completions.add("rainbow");

                // Returns starting string argument completion
                return filter(completions, args[1]);
            }

            if (args[0].equalsIgnoreCase("disable") && (canDisableOthers || hasAdminPermission)) {

                completions = Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                if (canDisableEveryone) {
                    completions.add("all");
                }

                return completions;
            }
        }

        return Collections.emptyList();
    }

    public List<String> filter(List<String> tabCompletions, String argument) {
        return tabCompletions.stream()
                .filter(string -> string.regionMatches(true, 0, argument, 0, argument.length()))
                .sorted()
                .collect(Collectors.toList());
    }

}
