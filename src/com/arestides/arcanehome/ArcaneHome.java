package com.arestides.arcanehome;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcaneHome extends JavaPlugin{
	
	Logger logger = Bukkit.getLogger();
	
	HashMap<String, String[]> homes = new HashMap<>();
	
	public void onEnable(){
		logger.info("Enabling Arcane Home");
		this.saveDefaultConfig();
	}
	
	public void onDisable(){
		logger.info("Disabling Arcane Home");
		writeToConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args){
		if(cmd.getName().equalsIgnoreCase("sethome")){
			if(!(sender instanceof Player)){
				logger.info("This command must be run as a player!");
				return true;
			} else {
				//TODO Add teleport player home
				Player player = (Player) sender;
				String uuid = player.getUniqueId().toString();
				Location playerHome = player.getLocation();
				String[] home = {playerHome.getWorld().getName(), Integer.toString(playerHome.getBlockX()), 
				                 Integer.toString(playerHome.getBlockY()), Integer.toString(playerHome.getBlockZ())};
				homes.put(uuid, home);
				
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("home")){
			if(!(sender instanceof Player)){
				logger.info("This command must be run as a player!");
				return true;
			} else {
				//set players home
				Player player = (Player) sender;
				String uuid = player.getUniqueId().toString();
				String[] home = homes.get(uuid);
				Location playerHome;
				
				if (home != null){
					playerHome = new Location(Bukkit.getServer().getWorld(home[0]), Integer.parseInt(home[1]), 
							Integer.parseInt(home[2]), Integer.parseInt(home[3]));
				} else {
					playerHome = new Location(Bukkit.getServer().getWorld(this.getConfig().get(uuid + ".world").toString()), 
							Integer.parseInt(this.getConfig().get(uuid + ".x_coord").toString()),
							Integer.parseInt(this.getConfig().get(uuid + ".y_coord").toString()), 
							Integer.parseInt(this.getConfig().get(uuid + ".z_coord").toString()));
				}
				
				player.teleport(playerHome);
			}
		}
		
		return true;
	}
	
	private void writeToConfig(){
		
		for (String uuid : homes.keySet()){
			this.getConfig().set(uuid, "");
			this.getConfig().set(uuid +  ".world", homes.get(uuid)[0]);
			this.getConfig().set(uuid + ".x_coord", homes.get(uuid)[1]);
			this.getConfig().set(uuid + ".y_coord", homes.get(uuid)[2]);
			this.getConfig().set(uuid + ".z_coord", homes.get(uuid)[3]);
		}
		
		this.saveConfig();
	}
	
}
