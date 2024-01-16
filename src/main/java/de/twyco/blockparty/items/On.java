package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class On extends ItemStack {

    public On(){
        super();
        this.setType(Material.LIME_DYE);
        this.setAmount(1);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Aktiviert");
        this.setItemMeta(itemMeta);
    }

}
