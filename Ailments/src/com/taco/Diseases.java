package com.taco;



	import java.io.File;
	import java.util.logging.Logger;
import java.util.ArrayList;
	import java.util.HashMap;
	import org.bukkit.configuration.file.FileConfiguration;
	import org.bukkit.entity.Player;
	import org.bukkit.plugin.PluginDescriptionFile;
	import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;



	public class Diseases extends JavaPlugin {
		@SuppressWarnings("unused")
		private Logger log;
	    public ArrayList<Infection> infected = new ArrayList<Infection>();
	    public HashMap<Player, Integer> inDesert = new HashMap<Player, Integer>();
		protected static FileConfiguration Config;
		int[] blocks = {1,14,15,16,45,48,46,7,103,56,73};
		
		 public void loadConfig(){
			 try{
				File cfg = new File("plugins" + File.separator + "TheIslandGhost" + File.separator + "config.yml");
				cfg.mkdir();
	            Config = getConfig();	            
	            saveConfig();
			 } catch(Exception e){
				 
			 }
	        }
	    
	    public void onDisable() {
	    }
	    @Override
	    public void onEnable() {
	    	//loadConfig();
	    	PluginManager pm = this.getServer().getPluginManager();
	        PluginDescriptionFile pdfFile = this.getDescription();
	        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	        PListener pl = new PListener(this);
	        pm.registerEvents(pl,this);
	        cough();
	    }
	    public void cough()
	    {
    		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                	for(Infection i : infected)
                	{
                		if(i.getType().equals("cold"))
                		{
                			Player p = i.getPlayer();
                			int rand = (int) ((Math.random()*100)%11);
                			if(rand < 3)
                			{
                				if(p != null)
                					p.chat("/me coughs and hacks");
                			}
                		}
                	}
                }
            }, 20L, 3200L);
	    }
	    public boolean cure(Player p, String infection)
	    {
	    	boolean cured = false;
	    	for(Infection i : infected)
	    	{
		    	if(i.getPlayer().equals(p) && i.getType().equalsIgnoreCase(infection))
		    	{
		    		infected.remove(i);
		    		cured = true;
		    	}
	    	}
	    	return cured;
	    }
	    public Infection getInfection(Player p, String infection)
	    {
	    	for(Infection i : infected)
	    	{
		    	if(i.getPlayer().equals(p) && i.getType().equalsIgnoreCase(infection))
		    	{
		    		return i;
		    	}
	    	}
	    	return null;
	    }
	}




