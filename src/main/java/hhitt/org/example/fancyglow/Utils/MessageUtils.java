package hhitt.org.example.fancyglow.Utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    public static String getColoredMessages(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
