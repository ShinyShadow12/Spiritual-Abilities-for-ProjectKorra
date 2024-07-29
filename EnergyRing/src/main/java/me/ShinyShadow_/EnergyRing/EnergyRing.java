package me.ShinyShadow_.EnergyRing;

import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.MovementHandler;



public class EnergyRing extends SpiritualAbility implements AddonAbility {

	private Listener listener;
	
	private double chargesTimer = 0;
	private double areaTimer = 16;
	private double DAMAGE;
	private double damageArea;

	private Location particleLoc;
	private Location pLocation;
	
	
	private int chargeLevel = 1;
    private int playSoundPerLevel = 1;
    private double RANGE;
	
	  
    private boolean released = false;
    private boolean showMessage = true;
    private boolean ready = true;
    private boolean readyToRelease = false;


	public EnergyRing(Player player) {
		super(player);
		 this.RANGE = ConfigManager.getConfig().getDouble("ShinyShadow_.Air.Spiritual.EnergyRing.AdditiveRange");
		 this.DAMAGE = ConfigManager.getConfig().getDouble("ShinyShadow_.Air.Spiritual.EnergyRing.Damage");
		// TODO Auto-generated constructor stub
		
		 start();		
	}

	@Override
	public void progress() {	
		// TODO Auto-generated method stub

	if(this.bPlayer.canBendIgnoreBindsCooldowns(this)) {

		chargesTimer += 0.09D;
		pLocation = player.getLocation();	

	if(!released && ready) {
	
	   for (int d = 0; d <= 50; d += 1) {

	        particleLoc = new Location(player.getWorld(), pLocation.getX(), pLocation.getY(), pLocation.getZ());
	        particleLoc.setX(pLocation.getX() + Math.cos(d) * 1);
	        particleLoc.setZ(pLocation.getZ() + Math.sin(d) * 1);
	        ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, -6));
	        ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2, 1));
	        ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 4));
	        ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 4, -20));
            Vector vel = new Vector(player.getVelocity().getX(), -0.5, player.getVelocity().getZ());
            player.setVelocity(vel);

	        player.getWorld().spawnParticle(Particle.SCRAPE, pLocation, 1, 0.3, 0.3, 0.3, 1);
       
	   if(this.bPlayer.isOnCooldown(this)) {
		   stop();
	   }
	   if(!this.bPlayer.isOnCooldown(this)) {
		   
		 if(showMessage) {
 		  final MovementHandler mh = new MovementHandler((LivingEntity) player, SpiritualAbility.getAbility(EnergyRing.class));
 		  mh.stopWithDuration(0, "* Channeling *");
 		  showMessage = false;
		 }
	        if(chargesTimer >= 1.8D) {
	        	
	        	readyToRelease = true;
	        	if(playSoundPerLevel == 1) {
	        		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1F, 1F);
	        		damageArea = 2;
	        		playSoundPerLevel += 1;
	        	}
	        	player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc.add(0, 0.3, 0), 0, 0, 0, 0, 0.006);	
	        	
	        }
	        if(chargesTimer >= 5D) {
	        	
	        	if(playSoundPerLevel == 2) {
	        		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1F, 1F);
	        		DAMAGE = DAMAGE/2;
	        		damageArea = 4;
	        		playSoundPerLevel += 1;
	        	}
	        	
	        	chargeLevel = 2;
	        	player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc.add(0, 0.7, 0), 0, 0, 0, 0, 0.006);
	        	player.getWorld().spawnParticle(Particle.SCRAPE, pLocation, 1, 0.5, 0.4, 0.5, 1);
	        }
	        if(chargesTimer >= 8D) {
	        	
	        	if(playSoundPerLevel == 3) {
	        		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1F, 1F);
	        		DAMAGE = DAMAGE/2;
	        		damageArea = 7;
	        		playSoundPerLevel += 1;
	        	}
	        	
	        	chargeLevel = 3;
	        	player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc.add(0, 0.8, 0), 0, 0, 0, 0, 0.006);
	        	player.getWorld().spawnParticle(Particle.SCRAPE, pLocation, 1, 0.6, 0.5, 0.6, 2);
	        }
	   }
	   }
	}
	   if(player.isSneaking() && !released && readyToRelease) {	
		   ready = false;
		   player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.8F, 1.2F);
		   player.getWorld().playSound(player.getLocation().add(0, 2, 0), Sound.BLOCK_GLASS_BREAK, 0.2F, 0.03F);
		   player.getWorld().spawnParticle(Particle.SCRAPE, pLocation, 180 * chargeLevel, 4, 0.6, 4, 2.6);	
		   player.getWorld().spawnParticle(Particle.CRIT, pLocation, 180 * chargeLevel, 4, 0.6, 4, 2.6);	
		   player.getWorld().spawnParticle(Particle.REDSTONE, pLocation, 200 * chargeLevel, 3, 0.6, 3, 2.3, new DustOptions(Color.fromRGB(255, 255, 255), 1));
		   getCooldown();
		   this.bPlayer.addCooldown(this);
		   released = true;		
	   }

	
	 }

	
if(this.bPlayer.canBendIgnoreBindsCooldowns(this)) {
	
		if (released) {
			areaTimer -= 0.09D;
			if(areaTimer <= 0) {
				chargeLevel = 0;
				stop();			
			}		

		   if(areaTimer >= 0) {
			   for (int d1 = 0; d1 <= 50; d1 += 1) {
				    particleLoc = new Location(player.getWorld(), pLocation.getX(), pLocation.getY(), pLocation.getZ());
			    	   
				        particleLoc.setX(pLocation.getX() + Math.cos(d1) * (chargeLevel * RANGE));
				        particleLoc.setZ(pLocation.getZ() + Math.sin(d1) * (chargeLevel * RANGE));
			    	   player.getWorld().spawnParticle(Particle.CRIT , particleLoc.add(0, 0.6, 0), 3, 0.1, 0.1, 0.1, 0.22);
			           player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc.add(0, 0.3, 0), 1, chargeLevel/10, 0.1, chargeLevel/10, 0.1);
			           player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, pLocation, 1, chargeLevel, 0, chargeLevel, 0.6);		       
		
			           ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, chargeLevel - 1));
			           ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, chargeLevel - 1));
			           ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE , 20, (int) (damageArea - (damageArea * 2))));

			       }
			        
			   }
			   	damageEntities();		        
		}
	}	
}


//}

	public void damageEntities() {
		
		
		Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(pLocation, damageArea, 3, damageArea);
		for (Entity entity : nearbyEntities) {
		
	        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {	            

	        		((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, chargeLevel * 20, chargeLevel));
	        		((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, chargeLevel * 20, chargeLevel));

	        	DamageHandler.damageEntity(entity, DAMAGE, this);	  	        	
	    }
	  }
	}


	
	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.EnergyRing.Cooldown");
	}

	public String getDescription() {
		return " Tap shift to start channeling your energy in the form of rings, "
				+ "and tap shift again to create a bigger ring around you that will increase "
				+ "its size depending on the amount of rings channeled. "
				+ "All entities in the area of the ring will have their energy disrupted "
				+ "causing them to be damaged and suffer negative effects "
				+ "(the bigger the ring, the lesser the damage but the "
				+ "stronger the negative effects)                                           "
				+ " "
				+ "Channeling so much energy causes your being to weaken, you will suffer some negative effects too.";
	}
	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return player.getLocation();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "EnergyRing";
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
		this.listener = new EnergyRingListener();
	    ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this.listener, (Plugin) ProjectKorra.plugin);
	    ProjectKorra.log.info("Succesfully enabled " + getName() + " by " + getAuthor());
	    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.EnergyRing.Cooldown", Integer.valueOf(26000));
	    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.EnergyRing.AdditiveRange", Double.valueOf(2));
	    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.EnergyRing.Damage", Integer.valueOf(2));
	    
	    ConfigManager.defaultConfig.save();
		
	}

	@Override
	public void stop() {

		   HandlerList.unregisterAll(this.listener);
		    remove();
		// TODO Auto-generated method stub
		
	}
}

