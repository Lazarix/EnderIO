package crazypants.enderio.config.recipes;

import javax.annotation.Nonnull;

import crazypants.util.Prep;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class GenericUpgradeRecipeShapeless extends ShapelessOreRecipe {

  static {
    RecipeSorter.register("EnderIO:GenericUpgradeRecipeShapeless", GenericUpgradeRecipeShapeless.class, Category.SHAPED, "after:minecraft:shapeless");
  }

  public GenericUpgradeRecipeShapeless(ResourceLocation group, Block result, Object... recipe) {
    super(group, result, recipe);
  }

/*
  public GenericUpgradeRecipeShapeless(Block result, Object... recipe) {
    super(result, recipe);
  }

  public GenericUpgradeRecipeShapeless(Item result, Object... recipe) {
    super(result, recipe);
  }

  public GenericUpgradeRecipeShapeless(@Nonnull ItemStack result, Object... recipe) {
    super(result, recipe);
  }
*/ //todo: fix

  @SuppressWarnings("null")
  @Override
  public @Nonnull ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
    ItemStack result = super.getCraftingResult(inv);
    for (int x = 0; x < inv.getSizeInventory(); x++) {
      ItemStack slot = inv.getStackInSlot(x);
        if (Prep.isValid(slot) && result.getItem() == slot.getItem() && slot.hasTagCompound()) {
          result.setTagCompound(slot.getTagCompound().copy());
          return result;
        }
    }
    return result;
  }

}
