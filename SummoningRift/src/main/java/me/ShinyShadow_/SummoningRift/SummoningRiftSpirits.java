package me.ShinyShadow_.SummoningRift;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;

public class SummoningRiftSpirits extends SpiritualAbility implements ComboAbility, AddonAbility {
	
	private Entity target;
	
	private Location spawnLocation;
	private Location targetLoc;
	
	private Vector direction;
	
	private String spiritType;
	
	private double distance;
	private double coveredDistance;
	private double DAMAGE;
	private double RANGE;
	private double HEALING;
	
	public SummoningRiftSpirits(Player player, Location centerLoc) {
		super(player);
		
	    this.RANGE = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Range");
	    this.DAMAGE = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Damage");
	    this.HEALING = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Healing");
	    	    
		setLocation(centerLoc);
		start();
		// TODO Auto-generated constructor stub
	}

	public Location setLocation(Location centerLoc) {
		spawnLocation = centerLoc;
		return spawnLocation;		
	}


	@Override
	public void progress() {
		
		if(!this.bPlayer.isOnline() || this.player.isDead()) {	
			remove();
			stop();
		}
		   		
		Collection < Entity > nearbyEntities = player.getWorld().getNearbyEntities(spawnLocation, RANGE, 3, RANGE);
		//player.sendMessage("s" + nearbyEntities.size());
       for (Entity entity: nearbyEntities) {
          if (entity instanceof LivingEntity) {       
        	
        	  if(targetLoc == null) {
        		double t = Math.random();
        		if(t <= 0.5) {
        			//target = nearbyEntities.iterator().next();
        			if(entity.getUniqueId() != player.getUniqueId() && entity.getLocation().distance(spawnLocation) <= 10) {
        			target = entity;
        			}
        			spiritType = "Damage";
        		}
        		if(t >= 0.5) {
        			target = player;
        			spiritType = "Heal";
        		}
        	  }     
        	  
        	 /* if(entity.getUniqueId() == player.getUniqueId()) {        		  
        		  spiritType = "Heal";
        	  }
        	  if(entity.getUniqueId() != player.getUniqueId()) {
        		  spiritType = "Damage";
        	  }*/
          }
        }
        if(target == null) {
        	stop();
        }
        if(target != null && target instanceof LivingEntity) {
        	targetLoc = target.getLocation();
        }
        
        if(targetLoc != null) {
        		distance = spawnLocation.distance(targetLoc);
        		direction = targetLoc.toVector().subtract(spawnLocation.toVector()).normalize();
        		coveredDistance += 0.35D;
        		
        		Location currentLocation = spawnLocation.clone().add(direction.clone().multiply(coveredDistance));
        		player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, currentLocation, 50, 0.10, 0.10, 0.10, 0.15D);
        		if(spiritType == "Damage") {
        			player.getWorld().spawnParticle(Particle.REDSTONE, currentLocation, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 1));
        			player.getWorld().spawnParticle(Particle.REDSTONE, currentLocation, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
        		}
        		if(spiritType == "Heal") {
        			player.getWorld().spawnParticle(Particle.SCRAPE, currentLocation, 1, 0.01, 0.01, 0.01, 1);
        		}
        		if(coveredDistance >= distance) {
        			target.getWorld().spawnParticle(Particle.CRIT, targetLoc, 30, 0.4, 0.4, 0.4, 1);
        			if(spiritType == "Damage") {
        				if(target.getUniqueId() != player.getUniqueId())      					
        					if(target instanceof Player) {
        					    DamageHandler.damageEntity(target, DAMAGE, this);      
        					}
        				if(!(target instanceof Player)) {
    					    DamageHandler.damageEntity(target, DAMAGE * 3, this);      
    					}
        			}
        			this.stop();
        			
        			if(spiritType == "Heal") {
        				if(player.getHealth() < 19) {  						
        						player.setHealth(player.getHealth() + HEALING);
        						((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5, 1));        						
        				}
        				this.stop();			
        			}
        		}
        }
 
	}
	
	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Cooldown");
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return player.getLocation();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SummoningRift";
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "ShinyShadow_";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		 ProjectKorra.getCollisionInitializer().addComboAbility((CoreAbility)this);
		 ProjectKorra.log.info("Enabled " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		remove();
	}

	@Override
	public Object createNewComboInstance(Player arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		// TODO Auto-generated method stub
		return null;
	}

}
