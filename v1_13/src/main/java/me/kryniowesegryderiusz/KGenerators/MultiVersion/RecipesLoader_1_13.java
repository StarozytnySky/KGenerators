package me.kryniowesegryderiusz.KGenerators.MultiVersion;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.kryniowesegryderiusz.KGenerators.Main;
import me.kryniowesegryderiusz.KGenerators.Utils.Config;
import me.kryniowesegryderiusz.KGenerators.XSeries.XUtils;

public class RecipesLoader_1_13 implements RecipesLoader {

	@Override
	public void loadRecipes() {
		Config file = Main.getRecipesFile();
		
		if (!file.contains("recipes")) {
    		return;
    	}
		
    	ConfigurationSection recipesSection = file.getConfigurationSection("recipes");
    	
    	for(String generatorID: recipesSection.getKeys(false)){
    		
    		NamespacedKey key = new NamespacedKey(Main.getInstance(), "KGenerators_"+generatorID);
    		
    		if (!Main.generators.containsKey(generatorID)){
    			System.out.println("[KGenerators] !!! ERROR !!! There isnt any "+generatorID+" generator in config.yml! Recipe NOT LOADED!");
    			return;
    		}
    		
    		ItemStack generatorItem = Main.generators.get(generatorID).getGeneratorItem();
    		
    		ShapedRecipe recipe = new ShapedRecipe(key, generatorItem);
    		
    		ArrayList<String> shape = new ArrayList<String>();
    		shape = (ArrayList<String>) file.getList("recipes."+generatorID+".shape");
    		
    		recipe.shape(shape.get(0),shape.get(1),shape.get(2));
    		
    		ConfigurationSection ingredientsSection = file.getConfigurationSection("recipes."+generatorID+".ingredients");
        	for(String ingredientsString: ingredientsSection.getKeys(false)){
        		char ingredientsChar = ingredientsString.charAt(0);
        		ItemStack item = XUtils.parseItemStack(file.getString("recipes."+generatorID+".ingredients."+ingredientsString));
        		recipe.setIngredient(ingredientsChar, item.getType());
        	}
        	Bukkit.addRecipe(recipe);
        	System.out.println("[KGenerators] Loaded recipe for " + generatorID);
    	}
		
	}

}
