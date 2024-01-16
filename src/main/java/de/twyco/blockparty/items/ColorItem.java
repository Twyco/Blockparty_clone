package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ColorItem extends ItemStack {

    public ColorItem(Material color){
        super(color, 1);
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta == null){
            return;
        }
        switch (color){
            case BLACK_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.BLACK + ChatColor.BOLD.toString() + "SCHWARZ");
            }
            case ORANGE_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "ORANGE");
            }
            case BLUE_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.BLUE + ChatColor.BOLD.toString() + "BLAU");
            }
            case LIGHT_BLUE_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "HELL BLAU");
            }
            case GREEN_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "DUNKEL GRÜN");
            }
            case LIME_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "LIME");
            }
            case PURPLE_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "LILA");
            }
            case RED_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "ROT");
            }
            case YELLOW_CONCRETE -> {
                itemMeta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "GELB");
            }
        }
        setItemMeta(itemMeta);
    }

}
