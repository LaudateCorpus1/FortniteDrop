package squwid.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Ben on 5/4/2018.
 */
public class MessageManager {
    private static ChatColor mainColor = ChatColor.GOLD;
    private static ChatColor secondColor = ChatColor.GRAY;
    private static ChatColor errColor = ChatColor.RED;

    public static void broadcast(String message) {
        Bukkit.getServer().broadcastMessage(mainColor + message);}
    public static void msg(Player p, String message) {p.sendMessage(secondColor + message);}
    public static void msg(Player p, String message, boolean important){p.sendMessage(mainColor + message);}
    public static void log(String message) {Bukkit.getServer().getLogger().info(message);}
    public static void error(String message){Bukkit.getServer().getLogger().info(errColor + "ERROR: " + message);}

    public static void msgAdmins(String msg) {
        for (Object o : Bukkit.getServer().getOnlinePlayers()){
            if (!(o instanceof Player)){
                continue;
            }
            Player p = (Player)o;
            if (p.hasPermission("cratedrop.admin")){
                p.sendMessage(secondColor + msg);
            }
        }
    }
}
