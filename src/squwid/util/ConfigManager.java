package squwid.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 5/7/2018.
 */
public class ConfigManager {
    
    Plugin p;
    FileConfiguration config;
    File configFile;
     
    private static int chestAmount = 9;
    private static ConfigManager instance = new ConfigManager();
    public static ConfigManager getInstance() { return instance; }
    
    public void setup(Plugin p) {
        this.p = p;
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        this.configFile = new File(p.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
                config.set("max_x", 1000);
                config.set("min_x", -1000);
                config.set("max_z", 1000);
                config.set("min_z",-1000);
                config.set("start_height", 100);
                // Items from a chest
                config.set("chest_slot_0", 0);
                config.set("chest_amount_0", 0);
                
                config.set("chest_slot_1", 0);
                config.set("chest_amount_1", 0);
                
                config.set("chest_slot_2", 0);
                config.set("chest_amount_2", 0);
                
                config.set("chest_slot_3", 0);
                config.set("chest_amount_3", 0);
                
                config.set("chest_slot_4", 0);
                config.set("chest_amount_4", 0);
                
                config.set("chest_slot_5", 0);
                config.set("chest_amount_5", 0);
                
                config.set("chest_slot_6", 0);
                config.set("chest_amount_6", 0);
                
                config.set("chest_slot_7", 0);
                config.set("chest_amount_7", 0);
                
                config.set("chest_slot_8", 0);
                config.set("chest_amount_8", 0);
                
                
                if (saveConfig()){
                    MessageManager.log("successfully created config file");
                }
            }
            catch (IOException e) {
                MessageManager.error("unable to create config file");
            }
        }
    }
    
    
    private boolean saveConfig(){
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e){
            MessageManager.error("could not save configuration file");
            return false;
        }
        return true;
    }
    
    public int getStartHeight(){
        int startHeight = config.getInt("start_height");
        if ((startHeight < 0) || (startHeight > 256)){
            startHeight = 100;
        }
        return startHeight;
    }
    
    public int getMinX(){
        int minX = config.getInt("min_x");
        return minX;
    }
    
    public int getMaxX() {
        int maxX = config.getInt("max_x");
        return maxX;
    }
    
    public int getMinZ() {
        int minZ = config.getInt("min_z");
        return minZ;
    }
    
    public int getMaxZ() {
        int maxZ = config.getInt("max_z");
        return maxZ;
    }
    
    
    public List<ItemStack> getConfigItems() {
        List<ItemStack> items = new ArrayList<>();
        
        String slotString = "chest_slot_";
        String amountString = "chest_amount_";
        
        for (int i = 0; i < chestAmount; i++){
            int tempItem = config.getInt(slotString + i);
            int tempAmount = config.getInt(amountString + i);
            
            if ((tempItem == 0) || (tempAmount == 0)){
                continue;
            }
            
            Material m = Material.getMaterial(tempItem);
            if (m == null){
                MessageManager.error("cannot find an item with id: " + tempItem);
                continue;
            }
            ItemStack itemStack = new ItemStack(m, tempAmount);
            items.add(itemStack);
        }
        return items;
        
    }
    
    //TODO: Add changing of config files in game.
    
    
    
}
