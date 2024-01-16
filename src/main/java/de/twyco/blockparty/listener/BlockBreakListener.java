package de.twyco.blockparty.listener;

import de.twyco.blockparty.Blockparty;
import de.twyco.stegisagt.GameStatus;
import de.twyco.stegisagt.Stegisagt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(Stegisagt.getGameStatus().equals(GameStatus.PLAYING_BLOCKPARTY)){
            if(Blockparty.getInstance().isPlayingPlayer(event.getPlayer())){
                event.setCancelled(true);
            }
        }
    }

}
