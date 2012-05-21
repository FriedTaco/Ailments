package com.taco;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Infection {
	String type;
	Player player;
	public Infection(Player player, String type)
	{
		this.player = player;
		this.type = type;
		infect(player,type);
	}
	
	public String getType()
	{
		return this.type;
	}
	public Player getPlayer()
	{
		return this.player;
	}
	
	public void infect(Player p, String type)
	{
		//Ideas: Malaria, hypochondria, disorientation.
		if(type.equalsIgnoreCase("salmonella"))
		{
			if(p.getFoodLevel() >= 4)
				p.setFoodLevel(p.getFoodLevel()-4);
			else
				p.setFoodLevel(0);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 1));
		}
		else if(type.equalsIgnoreCase("arms"))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 12000, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 12000, 2));
		}
		else if(type.equalsIgnoreCase("legs"))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 12000, 2));
		}
		else if(type.equalsIgnoreCase("influenza"))
		{
			double duration = ((Math.random()*10)%2)*12000;
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) duration, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) duration, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) duration, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) duration, 1));
		}
		else if(type.equalsIgnoreCase("blacklung"))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 6000, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6000, 1));
		}
		else if(type.equalsIgnoreCase("heatstroke"))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 2));
		}
		else if(type.equalsIgnoreCase("starving"))
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 72000, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 72000, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 72000, 2));
		}
	}

}
