package hhitt.fancyglow.utils;

import hhitt.fancyglow.FancyGlow;
import hhitt.fancyglow.managers.GlowManager;
import hhitt.fancyglow.managers.PlayerGlowManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FancyGlowPlaceholder extends PlaceholderExpansion {

    // PlaceholderAPI hook to create a placeholder (color one)
    private final GlowManager glowManager;
    private final PlayerGlowManager playerGlowManager;

    public FancyGlowPlaceholder(FancyGlow plugin) {
        this.glowManager = plugin.getGlowManager();
        this.playerGlowManager = plugin.getPlayerGlowManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "fancyglow";
    }

    @Override
    public @NotNull String getAuthor() {
        return "hhitt";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.3.1";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        String enabled = getPlaceholderAPI().getPlaceholderAPIConfig().booleanTrue();
        String disabled = getPlaceholderAPI().getPlaceholderAPIConfig().booleanFalse();

        switch (params) {
            case "color" -> {
                return playerGlowManager.getPlayerGlowColor(player);
            }
            case "status" -> {
                return (player.isGlowing() || glowManager.isFlashingTaskActive(player)) ? enabled : disabled;
            }
            case "status_formatted" -> {
                return playerGlowManager.getPlayerGlowingStatus(player);
            }
            case "color_name" -> {
                if (glowManager.isFlashingTaskActive(player)) {
                    return "FLASHING";
                }
                if (glowManager.isMulticolorTaskActive(player)) {
                    return "RAINBOW";
                }
                return playerGlowManager.getPlayerGlowColorName(player);
            }
        }
        return "";
    }
}
