package squwid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import squwid.cratedrop.CrateDrop;
import squwid.util.MessageManager;

/**
 * Created by Ben on 5/4/2018.
 */
public class FortniteDrop extends JavaPlugin {
    public void onEnable() {

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
            drop.testDrop(p);
            //drop.onDrop(p);
            return true;
        }

        // Crate tp
        if(label.equalsIgnoreCase("dgo")){
            drop.onTeleport(p);
            return true;
        }


        return true;
    }
    
}
