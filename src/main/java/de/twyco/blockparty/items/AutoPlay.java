package de.twyco.blockparty.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class AutoPlay extends ItemStack {

    public AutoPlay(){
        super(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) getItemMeta();
        skullMeta.setOwner("MHF_ArrowRight");
        skullMeta.setDisplayName(ChatColor.GOLD + "Autoplay nächste Wave");
        setItemMeta(skullMeta);
    }

}
