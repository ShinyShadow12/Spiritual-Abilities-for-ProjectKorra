package me.ShinyShadow_.SummoningRift;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class SummoningRift extends SpiritualAbility implements AddonAbility, ComboAbility {
	
	  private Location centerLoc = player.getLocation().add(0, 2, 0);
	  private Location pLocation = player.getLocation().add(0, 2, 0);	
	  private Location pLocation2 = player.getLocation().add(0, 1.5, 0);
	  
	  private double spawnTimer = 0D;	  
	  private double spawnInterval;	
	  private double currPoint;
	  private double angle;
	  private double nangle;
	  private double radius;
	  private double radius2;
	  private double riftDuration;
	  
	public SummoningRift(Player player) {
		super(player);
		
	    
	    this.riftDuration = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.RiftDuration");
	    this.radius = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Radius");
	    this.radius2 = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.Radius2");
	    this.spawnInterval = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SummoningRift.SpawnInterval");
		
		player = this.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
	if(bPlayer.canBendIgnoreBinds(this)) {
		start();
	}
	}

 public Location getCenterLocation() {
	 return this.centerLoc;
 }
	@Override
	public void progress() {
		if(!this.bPlayer.isOnline() || this.player.isDead()) {	
			remove();
			stop();
		}
		   
		  riftDuration -= 0.09D;
				if(riftDuration <= 0D) {		
					stop();
				}

	    if (!this.bPlayer.canBendIgnoreBindsCooldowns((CoreAbility)this)) {
	        remove();
	        return;
	      } 
		if(this.bPlayer.canBendIgnoreBindsCooldowns(this)) {
	
			this.bPlayer.addCooldown(this);
			player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, centerLoc, 90, 0.5, 0.5, 0.5, 0.0001);
			player.getWorld().spawnParticle(Particle.END_ROD, centerLoc, 1, 2, 0.3, 2, 0.05);
			Collection < Entity > nearbyEntities = player.getWorld().getNearbyEntities(centerLoc, 20, 3, 20);
		       for (Entity entity: nearbyEntities) {
		          if (entity instanceof LivingEntity) {       

		        	  if(entity.getUniqueId() == player.getUniqueId()) {        		  
		        		  player.getWorld().spawnParticle(Particle.SCRAPE, centerLoc, 1, 2, 0.3, 2, 0.05);
		        	  }
		        	  if(entity.getUniqueId() != player.getUniqueId()) {
		        		  player.getWorld().spawnParticle(Particle.REDSTONE, centerLoc, 1, 2, 0.3, 2, 0.05, new Particle.DustOptions(Color.RED, 1));		        		  
		        	  }
		          }
		        }
			player.getWorld().spawnParticle(Particle.SCRAPE, centerLoc, 1, 2, 0.3, 2, 0.05);
			
			
			for (int i = 0; i < 6; i++) {
	            this.currPoint += 360 / 300;
	            if (this.currPoint > 360) {
	                this.currPoint = 0;
	            }
	            angle = this.currPoint * Math.PI / 180.0D;
	            double x = radius * Math.cos(angle);
	          //double y = radius * Math.sin(angle);
	            double z = radius * Math.sin(angle);
	            pLocation.add(x, 0, z);
	            ParticleEffect.END_ROD.display(pLocation, 0, 0, 0, 0, 2);
	            pLocation.subtract(x, 0, z);
            
	            nangle = this.currPoint * Math.PI / -180.0D;
	            double nx = radius2 * Math.cos(nangle);
	            double nz = radius2 * Math.sin(nangle);
	            pLocation2.add(nx, 0, nz);
	            ParticleEffect.END_ROD.display(pLocation2, 0, 0, 0.044, 0, 2);
	            pLocation2.subtract(nx, 0, nz);
	        }
    
		   
			     spawnTimer -= 0.09D ;
			  if(spawnTimer <= 0D) { 	
				player.getWorld().playSound(centerLoc, Sound.BLOCK_PORTAL_AMBIENT, 1F, 2.8F);
				new SummoningRiftSpirits(player, centerLoc);
				new SummoningRiftSpirits(player, centerLoc);
				player.getWorld().playSound(centerLoc, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 0.8F, 0.25F);
				spawnTimer = spawnInterval;
				}
	}
	}
	  public ArrayList<ComboManager.AbilityInformation> getCombination() {
		    ArrayList<ComboManager.AbilityInformation> combo = new ArrayList<>();
		    combo.add(new ComboManager.AbilityInformation("Summon", ClickType.LEFT_CLICK));
		    combo.add(new ComboManager.AbilityInformation("EnergyRing", ClickType.LEFT_CLICK));
		    combo.add(new ComboManager.AbilityInformation("EnergyRing", ClickType.LEFT_CLICK));
		    return combo;
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
	public Object createNewComboInstance(Player player) {
		// TODO Auto-generated method stub
		return new SummoningRift(player);
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
	public String getDescription() {
		// TODO Auto-generated method stub
		return " Create a rift of pure energy to continuously summon 2 type "
				+ "of spirits to support you in your fight: Attackers and Healers. "
				+ "Attackers will tareget the nearest enemy and deal damage to them, "
				+ "while Healers will heal you. Damage is multiplied against mobs. ";
	}
	
	@Override
	public String getInstructions() {
		// TODO Auto-generated method stub
		return "Summon (Left Click) > Energy Ring (Left Click) x2";
	}


	@Override
	public void load() {
		 ProjectKorra.getCollisionInitializer().addComboAbility((CoreAbility)this);
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Cooldown", Integer.valueOf(25000));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Radius", Double.valueOf(3));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Radius2", Double.valueOf(1.5));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Damage", Double.valueOf(1));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Range", Double.valueOf(10));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.RiftDuration", Double.valueOf(25));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.SpawnInterval", Double.valueOf(2));
		 ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SummoningRift.Healing", Double.valueOf(1));
		 ConfigManager.defaultConfig.save();
		 ProjectKorra.log.info("Enabled " + getName() + " by " + getAuthor());
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		remove();
		
	}

}
