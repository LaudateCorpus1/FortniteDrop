package squwid.cratedrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import squwid.util.MessageManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ben on 5/4/2018.
 */
public class CrateDrop {
    private static CrateDrop instance = new CrateDrop();
    public static CrateDrop getInstance(){return instance;}

    private Crate lastDrop;
    private int seconds = 1;

    public void onDrop(Player p){
        Crate drop = new Crate(p);
        Block current = drop.getDropLoc().getBlock();

        while (current.getType().equals(Material.AIR)){
            MessageManager.log(current.getType().name());
            // subtract 1 until it hits something that isnt air
            drop.getDropLoc().setY(drop.getDropLoc().getY()-1);
            current = drop.getDropLoc().getBlock();
        }

        drop.getDropLoc().setY(drop.getDropLoc().getY() + 1);
        drop.getDropLoc().getBlock().setType(Material.CHEST);
        lastDrop = drop;
        MessageManager.broadcast("AIRDROP AT " + drop.getX() + ", " + drop.getZ() + ".");
        MessageManager.msgAdmins("Type /dgo to teleport to the crate");
    }


    public void onTeleport(Player p){
        if (lastDrop == null){
            MessageManager.msg(p, "There currently is no drop");
            return;
        }
        Location tpLoc = lastDrop.getDropLoc();
        //tpLoc.setY(lastDrop.getDropLoc().getY() + 2);
        p.teleport(tpLoc);
        MessageManager.msg(p, "Teleported to the last drop!", true);
        return;
    }
    
    /* EVERYTHING BELOW THIS IS CURRENTLY TESTING */

    public void testDrop(Player p){

        if (lastDrop != null){
            if (!lastDrop.getLanded()){
                MessageManager.msg(p, "The last drop still has not landed!");
                return;
            }
        }

        Crate drop = new Crate(p);
        this.lastDrop = drop;
        this.lastDrop.getDropLoc().getBlock().setType(Material.CHEST);

        MessageManager.broadcast("AIRDROP AT " + drop.getX() + ", " + drop.getZ() + ".");
        MessageManager.msgAdmins("Type /dgo to teleport to the crate");

        timer();
    }

    public void timer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!lowerCrate()){
                    timer.cancel();
                    timer.purge();
                }
            }
        }, seconds*1000, seconds*1000);

    }

    private boolean lowerCrate(){
        Location temp = new Location(lastDrop.getDropLoc().getWorld(), lastDrop.getDropLoc().getX(), lastDrop.getDropLoc().getY(), lastDrop.getDropLoc().getZ());
        temp.setY(temp.getY() - 1);
        Block next = temp.getBlock();
        //MessageManager.log("y: " + lastDrop.getDropLoc().getY());
        // If the block went down one return true
        if (next.getType().equals(Material.AIR)){
            lastDrop.getDropLoc().getBlock().setType(Material.AIR);
            lastDrop.getDropLoc().setY(lastDrop.getDropLoc().getY()-1);
            lastDrop.getDropLoc().getBlock().setType(Material.CHEST);
            return true;
        } else {
            //TODO: If its underground set landed true and remove the crate
            MessageManager.log("Crate has landed at " + lastDrop.getDropLoc().getX() + ", " + lastDrop.getDropLoc().getY() + ", " + lastDrop.getDropLoc().getZ());
            lastDrop.setLanded(true);
            return false;
        }
    }
}
