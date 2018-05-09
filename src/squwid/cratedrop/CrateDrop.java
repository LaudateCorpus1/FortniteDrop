package squwid.cratedrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
    
    // onTeleport is the command when a player wants to teleport to the last crate
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

    // dropCrate starts the drop of the crate from the command of Player p
    public void dropCrate(Player p){
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
        MessageManager.log("Crate has started descent at " + lastDrop.getDropLoc().getX() + ", " + lastDrop.getDropLoc().getY() + ", " + lastDrop.getDropLoc().getZ());
        timer();
    }

    
    /* Private stuff that the timer and lower crate need to deal with */
    
    // When a crate actually hits the ground
    private void onCrateTouchDown() {
        Block b = this.lastDrop.getDropLoc().getBlock();
        if (!b.getType().equals(Material.CHEST)){
            MessageManager.error("items were unable to be added to the chest");
            return;
        }
        Chest chest = (Chest) b.getState();
        Inventory inv = chest.getInventory();
        for (ItemStack i : this.lastDrop.getItems()){
            inv.addItem(i);
        }
    }


    private void buildCrate() {
        lastDrop.getDropLoc().getBlock().setType(Material.CHEST);
        return;
    }


    private void deleteCrate() {
        lastDrop.getDropLoc().getBlock().setType(Material.AIR);
        return;
    }
    
    private void timer() {
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
        
        // if the block was able to drop one return true
        if (next.getType().equals(Material.AIR)){
            deleteCrate();
            removeBalloon();
            lastDrop.getDropLoc().setY(lastDrop.getDropLoc().getY()-1);
            buildCrate();
            buildBalloon();
            return true;
        } else {
            //TODO: If its underground set landed true and remove the crate
            
            // When the crate touches the ground
            onCrateTouchDown();
            
            MessageManager.log("Crate has landed at " + lastDrop.getDropLoc().getX() + ", " + lastDrop.getDropLoc().getY() + ", " + lastDrop.getDropLoc().getZ());
            lastDrop.setLanded(true);
            return false;
        }
    }
    
    
    /* KRAFT'S STUFF */

    public void removeBalloon(){
        Location l = new Location(this.lastDrop.getDropLoc().getWorld(), this.lastDrop.getDropLoc().getX(), this.lastDrop.getDropLoc().getY(), this.lastDrop.getDropLoc().getZ());
        l.setY(l.getY() + 14);
        
        l.setX(l.getX() + 2);
        l.setZ(l.getZ() + 2);
        for(int x = 0; x < 5; x++) {
            l.setX(l.getX() - x);
            for (int z = 0; z < 5; z++) {
                l.setZ(l.getZ() - z);
                l.getBlock().setType(Material.AIR);
                l.setZ(l.getZ() + z);
            }
            l.setX(l.getX() + x);
        }
        //shape top layer for decent
        for(int y = 0; y < 4; y++){
            if(y != 0){
                l.setY(l.getY() - 1);
            }
            switch(y){
                case 0:
                    delOuterToAir(l);
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    l.getBlock().setType(Material.AIR);
                    l.setX(l.getX() + 2);
                    l.getBlock().setType(Material.AIR);
                    l.setZ(l.getZ() + 2);
                    l.getBlock().setType(Material.AIR);
                    l.setX(l.getX() - 2);
                    l.getBlock().setType(Material.AIR);
                    continue;
                case 1:
                    l.setX(l.getX() + 3);
                    l.setZ(l.getZ() + 1);
                    l.getBlock().setType(Material.STONE);
                    delOuterToAir(l);
                    continue;
                case 2:
                case 3:
                    l.getBlock().setType(Material.AIR);
                    l.setX(l.getX() + 4);
                    l.getBlock().setType(Material.AIR);
                    l.setZ(l.getZ() + 4);
                    l.getBlock().setType(Material.AIR);
                    l.setX(l.getX() - 4);
                    l.getBlock().setType(Material.AIR);
                    l.setZ(l.getZ() - 4);
            }
        }
    }
    
    private void delOuterToAir(Location l){
        for(int x = 0; x < 5 ; x++){
            l.setX(l.getX() - x);
            l.getBlock().setType(Material.AIR);
            l.setX(l.getX() + x);
        }
        for(int z = 1; z < 5; z++){
            l.setZ(l.getZ() - z);
            l.getBlock().setType(Material.AIR);
            l.setZ(l.getZ() + z);
        }
        l.setX(l.getX() - 4);
        l.setZ(l.getZ() - 4);
        for(int x = 0; x < 5 ; x++){
            l.setX(l.getX() + x);
            l.getBlock().setType(Material.AIR);
            l.setX(l.getX() - x);
        }
        for(int z = 1; z < 5; z++){
            l.setZ(l.getZ() + z);
            l.getBlock().setType(Material.AIR);
            l.setZ(l.getZ() - z);
        }
    }
    
    private void buildBalloon(){
        for(int i = 1; i < 15; i++){
            Location l = new Location(this.lastDrop.getDropLoc().getWorld(), this.lastDrop.getDropLoc().getX(), this.lastDrop.getDropLoc().getY() + i, this.lastDrop.getDropLoc().getZ());
            switch(i){
                case 1:
                    l.getBlock().setType(Material.IRON_FENCE);
                    continue;
                case 2:
                    l.getBlock().setType(Material.DOUBLE_STEP);
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    l.getBlock().setType(Material.DOUBLE_STEP); //was step
                    l.setX(l.getX() - 2);
                    l.getBlock().setType(Material.DOUBLE_STEP); //was step
                    l.setZ(l.getZ() - 2);
                    l.getBlock().setType(Material.DOUBLE_STEP); //was step
                    l.setX(l.getX() + 2);
                    l.getBlock().setType(Material.DOUBLE_STEP); //was step
                    continue;
                case 3:
                    l.getBlock().setType(Material.IRON_FENCE);
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    l.getBlock().setType(Material.IRON_FENCE);
                    l.setX(l.getX() - 2);
                    l.getBlock().setType(Material.IRON_FENCE);
                    l.setZ(l.getZ() - 2);
                    l.getBlock().setType(Material.IRON_FENCE);
                    l.setX(l.getX() + 2);
                    l.getBlock().setType(Material.IRON_FENCE);
                    continue;
                case 4:
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    for(int x = 0; x < 3; x++) {
                        l.setX(l.getX() - x);
                        for (int z = 0; z < 3; z++) {
                            l.setZ(l.getZ() - z);
                            l.getBlock().setType(Material.IRON_FENCE);
                            l.setZ(l.getZ() + z);
                        }
                        l.setX(l.getX() + x);
                    }
                    continue;
                case 5:
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    for(int x = 0; x < 3; x++) {
                        l.setX(l.getX() - x);
                        for (int z = 0; z < 3; z++) {
                            l.setZ(l.getZ() - z);
                            l.getBlock().setType(Material.WOOL);
                            l.getBlock().setData((byte)4);
                            l.setZ(l.getZ() + z);
                        }
                        l.setX(l.getX() + x);
                    }
                    continue;
                case 6:
                    buildHLayer(l);
                    continue;
                case 7:
                    buildLayerNoCorners(l);
                    continue;
                case 8:
                    buildLayerNoCorners(l);
                    continue;
                case 9:
                    buildGlowStoneLayer(l);
                    continue;
                case 10:
                    buildGlowStoneLayer(l);
                    continue;
                case 11:
                    buildLayerNoCorners(l);
                    continue;
                case 12:
                    buildLayerNoCorners(l);
                    continue;
                case 13:
                    buildHLayer(l);
                    continue;
                case 14:
                    l.getBlock().setType(Material.WOOL);
                    l.getBlock().setData((byte)4);
                    l.setX(l.getX() + 1);
                    l.getBlock().setType(Material.WOOL);
                    l.getBlock().setData((byte)4);
                    l.setX(l.getX() - 2);
                    l.getBlock().setType(Material.WOOL);
                    l.getBlock().setData((byte)4);
                    l.setX(l.getX() + 1);
                    l.setZ(l.getZ() + 1);
                    l.getBlock().setType(Material.WOOL);
                    l.getBlock().setData((byte)4);
                    l.setZ(l.getZ() - 2);
                    l.getBlock().setType(Material.WOOL);
                    l.getBlock().setData((byte)4);
                    continue;

            }
        }
        return;
    }

    private void buildLayerNoCorners(Location l){
        l.setX(l.getX() + 2);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() + 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() - 2);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);

        l.setX(l.getX() - 1);
        l.setZ(l.getZ() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() + 4);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);

        l.setX(l.getX() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() - 4);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);

        l.setX(l.getX() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() + 4);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);

        l.setX(l.getX() - 1);
        l.setZ(l.getZ() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
        l.setZ(l.getZ() - 1);
        l.getBlock().setType(Material.WOOL);
        l.getBlock().setData((byte)4);
    }

    private void buildGlowStoneLayer(Location l){
        l.setX(l.getX() + 2);
        l.setZ(l.getZ() + 2);
        for(int x = 0; x < 5 ; x++){
            l.setX(l.getX() - x);
            if(x == 2){
                l.getBlock().setType(Material.GLOWSTONE);
            }
            else {
                l.getBlock().setType(Material.WOOL);
                l.getBlock().setData((byte) 4);
            }
            l.setX(l.getX() + x);
        }
        for(int z = 1; z < 5; z++){
            l.setZ(l.getZ() - z);
            if(z == 2){
                l.getBlock().setType(Material.GLOWSTONE);
            }
            else {
                l.getBlock().setType(Material.WOOL);
                l.getBlock().setData((byte) 4);
            }
            l.setZ(l.getZ() + z);
        }
        l.setX(l.getX() - 4);
        l.setZ(l.getZ() - 4);
        for(int x = 0; x < 5 ; x++){
            l.setX(l.getX() + x);
            if(x == 2){
                l.getBlock().setType(Material.GLOWSTONE);
            }
            else {
                l.getBlock().setType(Material.WOOL);
                l.getBlock().setData((byte) 4);
            }
            l.setX(l.getX() - x);
        }
        for(int z = 1; z < 5; z++){
            l.setZ(l.getZ() + z);
            if(z == 2){
                l.getBlock().setType(Material.GLOWSTONE);
            }
            else {
                l.getBlock().setType(Material.WOOL);
                l.getBlock().setData((byte) 4);
            }
            l.setZ(l.getZ() - z);
        }
    }
    
    private void buildHLayer(Location l){
        l.setX(l.getX() + 1);
        l.setZ(l.getZ() + 1);
        for(int x = 0; x < 3; x++) {
            l.setX(l.getX() - x);
            for (int z = 0; z < 3; z++) {
                if(x == 1 && z == 1)
                    continue;
                l.setZ(l.getZ() - z);
                l.getBlock().setType(Material.WOOL);
                l.getBlock().setData((byte)4);
                l.setZ(l.getZ() + z);
            }
            l.setX(l.getX() + x);
        }
    }
}
