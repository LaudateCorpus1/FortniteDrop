package squwid.cratedrop;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ben on 5/4/2018.
 */
public class Crate {

    private static int startHeight = 100;
    private int x;
    private int z;
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;
    private List<ItemStack> items;
    private boolean searched;
    private Location dropLoc;
    private boolean landed;

    public Crate(Player p){
        setMinMax();
        setZ();
        setX();
        setLocation(p);
        this.searched = false;
        this.landed = false;
    }

    public void setLanded(boolean landed){
        this.landed = landed;
    }

    public boolean getLanded(){
        return this.landed;
    }

    public void setLocation(Player p) {
        this.dropLoc = new Location(p.getWorld(), (double)x, startHeight, (double)z);
    }

    public Location getDropLoc(){
        return this.dropLoc;
    }

    public void setX(){
        this.x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
    }

    public void setZ() {
        this.z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
    }

    public void setMinMax(){
        //TODO: this will come from settings file
        this.minX = -15000;
        this.minZ = -15000;
        this.maxX = 15000;
        this.maxZ = 15000;
    }

    public void found() {
        this.searched = true;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public boolean getSearched(){
        return this.searched;
    }
}
