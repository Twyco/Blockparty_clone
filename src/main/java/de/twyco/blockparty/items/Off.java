package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Off extends ItemStack {

    public Off(){
        super();
        this.setType(Material.RED_DYE);
        this.setAmount(1);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Deaktiviert");
        this.setItemMeta(itemMeta);
    }

}
