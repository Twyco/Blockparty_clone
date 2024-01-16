package de.twyco.blockparty.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EmptySlotItem extends ItemStack {

    public EmptySlotItem() {
        super(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(" ");
        setItemMeta(itemMeta);
    }

}
