package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayAreaSelectionTool extends ItemStack {

    public PlayAreaSelectionTool(){
        super(Material.WOODEN_SHOVEL, 1);
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta == null) {
            return;
        }
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.ITALIC.toString() + "Blockparty / Spielzone");
        setItemMeta(itemMeta);
    }

}
