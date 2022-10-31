package me.kryniowesegryderiusz.kgenerators.generators.locations.handlers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.events.PostGeneratorRegenerationEvent;
import me.kryniowesegryderiusz.kgenerators.api.events.PreGeneratorRegenerationEvent;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.generators.locations.objects.GeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.xseries.XMaterial;

public class GeneratorLocationRegenerateHandler {
	
	static ItemStack pistonHead = XMaterial.PISTON_HEAD.parseItem();
	
	public void handle(GeneratorLocation gLocation) {
		
		PreGeneratorRegenerationEvent event = new PreGeneratorRegenerationEvent(gLocation);
		Main.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
	
		Location generatingLocation = gLocation.getGeneratedBlockLocation();
		Block generatingLocationBlock = generatingLocation.getBlock();
		
		if (generatingLocationBlock.getType().equals(pistonHead.getType())) {
			gLocation.scheduleGeneratorRegeneration();
			return;
		}
		
		if (!Main.getPlacedGenerators().isLoaded(gLocation)
				&& !Main.getMultiVersion().getBlocksUtils().isAir(generatingLocationBlock) 
				&& !Main.getMultiVersion().getBlocksUtils().isOnWhitelist(generatingLocationBlock)
				&& !gLocation.isBlockPossibleToMine(generatingLocation)) {
			gLocation.removeGenerator(true, null);
			return;
		}
		
		if (!gLocation.isReadyForRegeneration()) {
			gLocation.scheduleGeneratorRegeneration();
			return;
		}
		
		AbstractGeneratedObject ago = gLocation.getGenerator().drawGeneratedObject();
		gLocation.setLastGeneratedObject(ago);
		ago.regenerate(gLocation);
		  
		Main.getInstance().getServer().getPluginManager().callEvent(new PostGeneratorRegenerationEvent(gLocation));
	}
}
