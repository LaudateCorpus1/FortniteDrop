package squwid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import squwid.cratedrop.CrateDrop;
import squwid.util.ConfigManager;
import squwid.util.MessageManager;

/**
 * Created by Ben on 5/4/2018.
 */

public class FortniteDrop extends JavaPlugin {
    
    public void onEnable() {
        ConfigManager.getInstance().setup(this);
        MessageManager.source();
    }

    public void onDisable() {

    }

    CrateDrop drop = CrateDrop.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            MessageManager.error("you cannot send commands through console");
            return true;
        }

        Player p = (Player)sender;

        // Crate Drop
        if (label.equalsIgnoreCase("drop")){
            if (!p.hasPermission("fortnite.drop")){
                MessageManager.noPerm(p);
                return true;
            }
            drop.dropCrate(p);
            return true;
        }

        // Crate tp
        if(label.equalsIgnoreCase("dgo")){
            if (!p.hasPermission("fortnite.tp")){
                MessageManager.noPerm(p);
                return true;
            }
            drop.onTeleport(p);
            return true;
        }
        
        return true;
    }
    
}
