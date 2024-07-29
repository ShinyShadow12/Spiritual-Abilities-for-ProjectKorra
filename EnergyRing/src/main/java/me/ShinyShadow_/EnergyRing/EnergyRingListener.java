package me.ShinyShadow_.EnergyRing;


import com.projectkorra.projectkorra.BendingPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.entity.Entity;



public class EnergyRingListener implements Listener {
	

	//private boolean instanceExists = false;
	public Entity target; 
		@EventHandler
		public void onClick(PlayerToggleSneakEvent event) {
			
		
			Player player = event.getPlayer();
		    BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		    if (event.isCancelled() || bPlayer == null)
			      return; 
			    if (!event.isSneaking())
			      return; 
			    if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null))
			      return; 
			    if (bPlayer.getBoundAbilityName().equalsIgnoreCase("EnergyRing"))
			    
			       new EnergyRing(player);
			   
		    	
		    	
			}
		   
		}
		
