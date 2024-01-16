package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WaveItem extends ItemStack {

    public WaveItem(int wave) {
        super();
        ItemMeta itemMeta = getItemMeta();
        switch (wave) {
            case 1 -> {
                setType(Material.WHITE_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 2 -> {
                setType(Material.LIME_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 3 -> {
                setType(Material.YELLOW_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 4 -> {
                setType(Material.ORANGE_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 5 -> {
                setType(Material.RED_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 6 -> {
                setType(Material.BROWN_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
            case 7 -> {
                setType(Material.BLACK_CONCRETE);
                setAmount(1);
                itemMeta = getItemMeta();
            }
        }
        itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Wave " + wave);
        setItemMeta(itemMeta);
    }

}
