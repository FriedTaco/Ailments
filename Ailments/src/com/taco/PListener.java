package com.taco;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PListener implements Listener {
	
	Diseases plugin;
	
	public PListener(Diseases diseases) {
		plugin=diseases;
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Player && !event.isCancelled())
		{
			Player p = (Player) event.getEntity();
			if(event.getCause() == DamageCause.FALL)
			{
				if(p.getFallDistance() >= 12)
				{
					p.sendMessage(ChatColor.RED + "OUCH! That was quite some fall, and it seems your legs didn't make it.");
					plugin.infected.add(new Infection(p,"legs"));
				}
			}
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		//Later add slowness decrease for legs if holding a stick.
		Player p = event.getPlayer();
		Location loc = p.getLocation();
		Biome biome = loc.getWorld().getBiome(loc.getBlockX(),loc.getBlockZ());
		Infection heatstroke = plugin.getInfection(p, "heatstroke");
		if(biome.name().equalsIgnoreCase("desert"))
		{
			if(heatstroke != null)
			{
				p.removePotionEffect(PotionEffectType.HUNGER);
				p.removePotionEffect(PotionEffectType.WEAKNESS);
				p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 2));
				return;
			}
			int time = Math.abs((int) System.currentTimeMillis()/1000);
			if(!plugin.inDesert.containsKey(p))
			{
				plugin.inDesert.put(p, time);
			}
			else
			{
				if(loc.getWorld().getHighestBlockAt(loc).getY() < loc.getY())
				{
					if(Math.abs(plugin.inDesert.get(p)-time)>720)
					{
						p.sendMessage(ChatColor.RED + "You suddenly feel very cold and weak...");
						plugin.infected.add(new Infection(p,"heatstroke"));
					}
				}
				else
				{
					plugin.inDesert.put(p, time);
				}
			}
		}
		else
		{
			if(plugin.inDesert.containsKey(p))
			{
				plugin.inDesert.remove(p);
			}
		}
		if(p.isSprinting())
		{
			Infection brokenLegs = plugin.getInfection(p, "legs");
			if(brokenLegs != null)
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You try to sprint, but you can't because your legs are broken!");
			}
		}
	}
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		//Make soup and watermelon help with thirst/heatstroke.
		if(event.getEntity() instanceof Player)
		{
			Player p = (Player) event.getEntity();
			ItemStack i = p.getItemInHand();
			Infection heatstroke = plugin.getInfection(p, "heatstroke");
			Infection starving = plugin.getInfection(p, "starving");
			if(i != null)
			{
				int id = i.getTypeId();
				if(id == 319 || id == 365 || id == 363)
				{
					int chance = (int) (((Math.random()*100)%10)+1);
					if(chance <= 1)
					{
						p.sendMessage(ChatColor.RED + "You realize as you regurgitate your meal that eating raw food wasn't the best idea.");
						p.sendMessage(ChatColor.RED + "You aren't feeling to well right now...");
						plugin.infected.add(new Infection(p,"salmonella"));
					}
				}
				if(heatstroke != null)
				{
					if(id == 260 || id == 360 || id == 282 || id == 335)
					{
						p.sendMessage(ChatColor.AQUA + "Phew! You feel much better now!");
						plugin.cure(p, "heatstroke");
						p.removePotionEffect(PotionEffectType.HUNGER);
						p.removePotionEffect(PotionEffectType.WEAKNESS);
						p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 2));
						if(plugin.inDesert.containsKey(p))
						{
							plugin.inDesert.remove(p);
						}
					}
				}
				
			}
			if(event.getFoodLevel() <= 1)
			{
				if(starving == null)
				{
					plugin.infected.add(new Infection(p,"starving"));
				}
			}
			else
			{
				if(starving != null)
				{
					plugin.cure(p, "starving");
				}
			}
		}
	}
}
