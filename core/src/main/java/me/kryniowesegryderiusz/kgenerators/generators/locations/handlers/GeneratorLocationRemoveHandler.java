package me.kryniowesegryderiusz.kgenerators.generators.locations.handlers;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.ItemsAdderHook;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.Generator;
import me.kryniowesegryderiusz.kgenerators.generators.locations.objects.GeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.utils.PlayerUtils;
import me.kryniowesegryderiusz.kgenerators.xseries.XMaterial;

public class GeneratorLocationRemoveHandler {
	
	public void handle(GeneratorLocation gLocation, boolean drop, @Nullable Player toWho) {
		final ItemStack air = XMaterial.AIR.parseItem();
		Location location = gLocation.getLocation();
		Generator generator = gLocation.getGenerator();
		
		Main.getSchedules().remove(gLocation);
		Main.getDatabases().getDb().removeSchedule(gLocation);
		
		Main.getPlacedGenerators().removeLoaded(gLocation);
		Main.getDatabases().getDb().removeGenerator(location);
		
		gLocation.getOwner().removeGeneratorFromPlayer(gLocation.getGenerator());
		
		if (drop) {
			PlayerUtils.dropToInventory(toWho, location, generator.getGeneratorItem());
		}
		
		Main.getMultiVersion().getBlocksUtils().setBlock(location, air);
		Main.getMultiVersion().getBlocksUtils().setBlock(gLocation.getGeneratedBlockLocation(), air);
		
		ItemsAdderHook.handleGeneratorLocationRemove(gLocation);
	}
}
