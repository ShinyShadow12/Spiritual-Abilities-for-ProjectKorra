package me.ShinyShadow_.SoulTrap;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

//import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
//import com.projectkorra.projectkorra.util.MovementHandler;
import com.projectkorra.projectkorra.util.MovementHandler;

public class SoulTrap extends SpiritualAbility implements AddonAbility {

  private Listener listener;
  private double trapTimer = 6D;
  public boolean launched = false;
  private boolean playChargeSound = true;
  private boolean playLaunchSound = true;
  private boolean noEffects = true;
  private long EffectsDuration;
  private Location entityLocation;
  private Location eyeLoc;

  private Vector direction;
  private Entity target;

  private final double DAMAGE;
  private final double RANGE;
  private double chargeTime;
  private double holdTime;
  private double distance = 0;

  Random rand = new Random();

  public SoulTrap(Player player, Entity target) {
    super(player);
    this.RANGE = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.Range");
    this.DAMAGE = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.Damage");
    this.EffectsDuration = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.EffectsDuration");
    this.trapTimer = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.TrapDuration");
    this.chargeTime = ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.ChargeTime");
    // TODO Auto-generated constructor stub
    player.getWorld().spawnParticle(Particle.SCRAPE, player.getLocation(), 40, 0.8, 0.8, 0.8, 8);
    start();
  }

  @Override
  public void progress() {

   
if (bPlayer.canBend(this)) {
	  if(!this.bPlayer.isOnline() || this.player.isDead()) {	
			remove();
			stop();
		}
      if (this.player.isSneaking() && !this.launched) {

        if (playChargeSound) {

          player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 0.5F, 1.6F);
          playChargeSound = false;
        }

        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation().add(0D, 0.5D, 0D), 20, 0.2, 0.2, 0.2, 0.03D);

          holdTime += 0.09D;
        
        if (holdTime >= chargeTime) {
          player.getWorld().spawnParticle(Particle.SCRAPE, player.getLocation().add(0D, 0.5D, 0D), 6, 0.5, 0.5, 0.5, 0.04D);
          direction = player.getLocation().getDirection();
          direction.multiply(1.8);
          eyeLoc = player.getEyeLocation();
        }
      }
      if (!this.player.isSneaking() && holdTime >= chargeTime) {
        this.launched = true;
      }
      if (!this.player.isSneaking() && !(holdTime >= chargeTime)) {
        stop();
      }
}
      if (bPlayer.canBendIgnoreBinds(this)  && this.bPlayer.isOnline() && !this.player.isDead()) {
      if (this.launched) {

    	  if(playLaunchSound) {
    		  	player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ATTACK_TARGET, 2, 2);
    		  	playLaunchSound = false;
    	  }

        if (entityLocation == null) {
        	player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, eyeLoc, 80, 0.2, 0.2, 0.2, 0.06D);
        	player.getWorld().spawnParticle(Particle.SCRAPE, eyeLoc, 3, 0.1, 0.1, 0.1, 0.06D);
         	player.getWorld().spawnParticle(Particle.CRIT, eyeLoc, 3, 0.1, 0.1, 0.1, 0.06D);
         	eyeLoc.add(direction);
         	distance += direction.length();
        }
        
        Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(eyeLoc, 0.65, 0.65, 0.65);
        
        for (Entity entity : nearbyEntities) {

        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
            target = entity;
            entityLocation = entity.getLocation().add(0D, 1.35D, 0D).clone();
            player.getWorld().spawnParticle(Particle.CRIT, entityLocation, 30, 0.5, 0.5, 0.5, 2);
    		
    		//Collection<PotionEffect> activeEffects = ((LivingEntity) entity).getActivePotionEffects();
    		//player.sendMessage("sex " + activeEffects); 
    		
    		final MovementHandler mh = new MovementHandler((LivingEntity) entity, SpiritualAbility.getAbility(SoulTrap.class));
    		mh.stopWithDuration(8, "* Soul Blocked *");
            player.getWorld().playSound(entityLocation, Sound.BLOCK_CHAIN_BREAK, 6, 2);
            Vector vel = new Vector(0, 0.1, 0);
            entity.setVelocity(vel);
            DamageHandler.damageEntity(target, this.DAMAGE, this);
            entity.setVelocity(vel);
        	if(target.isDead() && target != null) {
        		stop();
        	}
            
            getCooldown();
            this.bPlayer.addCooldown(this);
          }
        }       
    }
}
    if (bPlayer.canBendIgnoreBindsCooldowns(this)  && this.bPlayer.isOnline() && !this.player.isDead()) {

      if (distance > this.RANGE && entityLocation == null) {
        stop();
      }
      if (entityLocation != null && trapTimer >= 0D) {
      	if(target.isDead() && target != null) {
    		stop();
    	}
      	
      	if(noEffects) {
            ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, (int) trapTimer * 10, 1));
            ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) EffectsDuration * 10, 7));
            ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) EffectsDuration * 10, 9));
            ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) EffectsDuration * 11, 9));
              noEffects = false;
      	}
      	
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entityLocation.add(0D, 0, 0D), 70, 0.55, 0.75, 0.55, 0.03D);
        player.getWorld().spawnParticle(Particle.SCRAPE, entityLocation.add(0D, 0.090D, 0D), 6, 0.15, 0.15, 0.15, 0.45D);
        trapTimer -= 0.09D;
        Vector vel = new Vector(0, 0.1, 0);
        target.setVelocity(vel);
      }
      if (trapTimer <= 0D) {
        player.getWorld().spawnParticle(Particle.CRIT, entityLocation, 30, 0.4, 0.4, 0.4, 1);
        player.getWorld().playSound(entityLocation, Sound.BLOCK_CHAIN_BREAK, 6, 2);
        DamageHandler.damageEntity(target, this.DAMAGE, this);
        stop();
      }
    }
  }
  @Override
  public long getCooldown() {
    // TODO Auto-generated method stub
    return ConfigManager.getConfig().getLong("ShinyShadow_.Air.Spiritual.SoulTrap.Cooldown");
  }

  @Override
  public Location getLocation() {
    // TODO Auto-generated method stub
    return player.getLocation();
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return "SoulTrap";
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
    this.listener = new SoulTrapListener();
    ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this.listener, (Plugin) ProjectKorra.plugin);
    ProjectKorra.log.info("Succesfully enabled " + getName() + " by " + getAuthor());
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.Cooldown", Integer.valueOf(15000));
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.EffectsDuration", Integer.valueOf(6));
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.TrapDuration", Double.valueOf(6));
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.Damage", Double.valueOf(2));
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.Range", Double.valueOf(20));
    ConfigManager.getConfig().addDefault("ShinyShadow_.Air.Spiritual.SoulTrap.ChargeTime", Double.valueOf(4));
    ConfigManager.defaultConfig.save();
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub
    HandlerList.unregisterAll(this.listener);
    remove();
  }

  public String getDescription() {
    return " Hold shift until you see green particles, " +
    	   "then release to throw a blast of spiritual energy " +
    	   "that will trap the soul of the entity hit, " +
    	   "making it unable to move and lifting it up.";
  }

  @Override
  public boolean isExplosiveAbility() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isIgniteAbility() {
    // TODO Auto-generated method stub
    return false;
  }

}
