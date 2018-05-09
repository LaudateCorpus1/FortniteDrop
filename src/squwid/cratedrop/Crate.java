package squwid.cratedrop;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import squwid.util.ConfigManager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ben on 5/4/2018.
 */
public class Crate {

    private int startHeight;
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
        this.startHeight = ConfigManager.getInstance().getStartHeight();
        setLocation(p);
        this.searched = false;
        this.landed = false;
        this.items = ConfigManager.getInstance().getConfigItems();
    }


    public void setLanded(boolean landed){
        this.landed = landed;
    }

    public boolean getLanded(){
        return this.landed;
    }

    public void setLocation(Player p) {
        this.dropLoc = new Location(p.getWorld(), (double)x, (double)startHeight, (double)z);
    }

    public Location getDropLoc(){
        return this.dropLoc;
    }

    public void setX(){
        // min then max
        this.x = ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    }

    public void setZ() {
        this.z = ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
    }

    public void setMinMax(){
        //TODO: this will come from settings file
        this.minX = ConfigManager.getInstance().getMinX();
        this.minZ = ConfigManager.getInstance().getMinZ();
        this.maxX = ConfigManager.getInstance().getMaxX();
        this.maxZ = ConfigManager.getInstance().getMaxZ();
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
