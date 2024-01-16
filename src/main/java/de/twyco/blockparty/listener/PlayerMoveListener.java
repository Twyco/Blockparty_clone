package de.twyco.blockparty.listener;

import de.twyco.blockparty.Blockparty;
import de.twyco.blockparty.PlayArea;
import de.twyco.stegisagt.GameStatus;
import de.twyco.stegisagt.Stegisagt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final Blockparty instance;
    private final PlayArea playArea;

    public PlayerMoveListener() {
        instance = Blockparty.getInstance();
        playArea = instance.getPlayArea();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(Stegisagt.getGameStatus().equals(GameStatus.PLAYING_BLOCKPARTY)){
            Player player = event.getPlayer();
            if(instance.isPlayingPlayer(player)){
                if(event.getTo().getY() <= playArea.getY()){
                    Stegisagt.killPlayer(player);
                }
            }
        }
    }

}
