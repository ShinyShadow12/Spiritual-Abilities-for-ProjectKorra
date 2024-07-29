package me.ShinyShadow_.Summon;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.MovementHandler;



public class Summon extends SpiritualAbility implements AddonAbility {
  private Listener listener;
  private Entity target;

  private boolean released = false;
  private boolean ready = true;
  private boolean startTimer = false;
  private int test;
  
  private int summons = 0;

  private double summoningTimer = 7D;
  private double spiritExpirationTimer = 25D;
  private double damageTickTimer = 4D;


  private Location pLocation;
  private Location right;
  private Location left;
  private Location targetLoc = null;

  public Summon(Player player) {
    super(player);
    // TODO Auto-generated constructor stub
    
   
    start();
    
  }

  @Override
  public void progress() {
    // TODO Auto-generated method stub


        if(startTimer == true) {
        	spiritExpirationTimer -= 0.09;
        	if (spiritExpirationTimer <= 0) {  		
        		player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, right, 20, 0.13, 0.13, 0.13, 1);
        		player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, left, 20, 0.13, 0.13, 0.13, 1);
        		player.getWorld().spawnParticle(Particle.REDSTONE, left, 20, 0.3, 0.3, 0.3, 2, new Particle.DustOptions(Color.WHITE, 1));
        		player.getWorld().spawnParticle(Particle.REDSTONE, right, 20, 0.3, 0.3, 0.3, 2, new Particle.DustOptions(Color.WHITE, 1));
                getCooldown();
                this.bPlayer.addCooldown(this);
        		stop();
        }
        }

  if (!this.bPlayer.isOnCooldown(this)) {
        if (this.player.isSneaking() && !this.released) {
        	startTimer = true;
        }
        if (this.ready) {
          pLocation = player.getLocation();
          summoningTimer -= 0.09D;
    
            if (summons == 2) {
            	player.getWorld().spawnParticle(Particle.SCRAPE, pLocation.add(0, 0.6, 0), 1, 0.1, 0.1, 0.1, 0.1);
                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, pLocation.add(0, 0.6, 0), 15, 0.1, 0.1, 0.1, 0.2);
                ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 1));
                ((LivingEntity) player).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
            }
          
          if (!this.released) {
       	
            final MovementHandler mh = new MovementHandler((LivingEntity) player, SpiritualAbility.getAbility(Summon.class));
            mh.stopWithDuration(0, "* Summoning (" + summons + ") *");
            right = GeneralMethods.getRightSide(player.getLocation().add(0, 0, 1), 2).add(0, 1.2, 0);
            left = GeneralMethods.getLeftSide(player.getLocation().add(0, 0, 1), 2).add(0, 1.2, 0);

          }
          if (summoningTimer <= 5) {

            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, right, 40, 0.13, 0.13, 0.13, 0);
            player.getWorld().spawnParticle(Particle.SCRAPE, right, 1, 0.01, 0.01, 0.01, 1);
            player.getWorld().spawnParticle(Particle.REDSTONE, right, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
            player.getWorld().spawnParticle(Particle.REDSTONE, right, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
            if (summons == 0) {
            	player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, -1F);
              summons += 1;
            }

          }
          if (summoningTimer <= 0) {

            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, left, 40, 0.13, 0.13, 0.13, 0);
            player.getWorld().spawnParticle(Particle.SCRAPE, left, 1, 0.01, 0.01, 0.01, 1);
            player.getWorld().spawnParticle(Particle.REDSTONE, left, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
            player.getWorld().spawnParticle(Particle.REDSTONE, left, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));

            if (summons == 1) {
            	player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, -4F);
              summons += 1;
            }
          }
        }
      }


    

    if (this.player.isSneaking() && !released && summons == 2 && this.bPlayer.getBoundAbilityName().equals("Summon")) {
      Collection < Entity > nearbyEntities = player.getWorld().getNearbyEntities(player.getLocation(), 3.5, 3, 3.5);

      if(nearbyEntities.size() == 1) {
          getCooldown();
          this.bPlayer.addCooldown(this);
          stop();
      }
      for (Entity entity: nearbyEntities) {

        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
      
        	if (targetLoc == null) {
            target = entity;

            targetLoc = entity.getLocation();
          }
          if(targetLoc != null) {
              getCooldown();
              this.bPlayer.addCooldown(this);
              released = true;
          }
        }
        }
      }

    
    if (released == true) {

    	ready = false;

      if (targetLoc != null) {
        right = GeneralMethods.getRightSide(target.getLocation().add(0, 0, 1), 2).add(0, 1.2, 0);
        left = GeneralMethods.getLeftSide(target.getLocation().add(0, 0, 1), 2).add(0, 1.2, 0);
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, right, 40, 0.13, 0.13, 0.13, 0);
        player.getWorld().spawnParticle(Particle.REDSTONE, right, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 1));
        player.getWorld().spawnParticle(Particle.REDSTONE, right, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));

        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, left, 40, 0.13, 0.13, 0.13, 0);
        player.getWorld().spawnParticle(Particle.REDSTONE, left, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 1));
        player.getWorld().spawnParticle(Particle.REDSTONE, left, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));
        player.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, 0.6, 0), 1, 0.1, 0.1, 0.1, 0.1, new Particle.DustOptions(Color.RED, 1));
        ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
        damageTickTimer -= 0.09D;
        if(damageTickTimer <= 0) {
            Vector vel = new Vector(0, 0, 0);
            target.setVelocity(vel);
        	DamageHandler.damageEntity(target, 1, this);
        	target.setVelocity(vel);
        	damageTickTimer = 4D;
        }
    }
    }
    if(this.player.isSneaking() && summoningTimer < 6 && summons < 2 && !released && this.bPlayer.getBoundAbilityName().equals("Summon")) {
        getCooldown();
        this.bPlayer.addCooldown(this);
		stop();
    }
    
  }

  // Schedule the task to run every tick

  @Override
  public String getDescription() {
	  
	  return " Tap shift to start summoning little spirits of pure energy"
	  		+ "that will follow you around. "
	  		+ "Tap shift again to attach the spirits to a close entity. "
	  		+ "The spirits will damage and apply negative effects to the target. "
	  		+ "If you tap shift while on cooldown, the spirits will be automatically recalled once the cooldown ends.";
  }
  public long getCooldown() {
    // TODO Auto-generated method stub
    return 12000;
  }

  @Override
  public Location getLocation() {
    // TODO Auto-generated method stub
    return player.getLocation();
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return "Summon";
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
    this.listener = new SummonListener();
    ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this.listener, (Plugin) ProjectKorra.plugin);

  }

  @Override
  public void stop() {

    // TODO Auto-generated method stub
    HandlerList.unregisterAll(this.listener);
    remove();

  }

}