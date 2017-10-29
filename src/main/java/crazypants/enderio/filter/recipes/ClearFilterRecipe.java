package crazypants.enderio.filter.recipes;

import com.enderio.core.common.util.NNList;
import crazypants.enderio.EnderIO;
import crazypants.enderio.filter.FilterRegistry;
import crazypants.enderio.filter.IItemFilterUpgrade;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import javax.annotation.Nullable;

public class ClearFilterRecipe implements IRecipe{

  static {
    RecipeSorter.register("EnderIO:clearFilter", ClearFilterRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
  }
  
  private ItemStack output;
  
  @Override
  public boolean matches(InventoryCrafting inv, World world) {
    int count = 0;
    ItemStack input = null;
    
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack checkStack = inv.getStackInSlot(i);
      if (checkStack != null && checkStack.getItem() instanceof IItemFilterUpgrade) {
        count++;
      }
      input = (count == 1 && checkStack != null) ? checkStack : input;
    }
    
    if (count == 1 && FilterRegistry.isFilterSet(input)) {
      ItemStack out = input.copy();
      out.setCount(1);
      out.setTagCompound(null);
      this.output = out;
    } else {
      this.output = null;
    }
    
    return count == 1 && output != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return output.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return false;
  }

  //@Override
  public int getRecipeSize() {
    return 1;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return output;
  }
  
  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {
    if (this.output != null && ItemStack.areItemStacksEqual(output, event.getItemStack())) {
      event.getToolTip().add(TextFormatting.RED.toString() + TextFormatting.ITALIC + EnderIO.lang.localize("itemConduitFilterUpgrade.clearConfigWarning"));
    }
  }
  
  @Override
  public NNList<ItemStack> getRemainingItems(InventoryCrafting inv) {
    return new NNList<>();
  }

  @Override
  public IRecipe setRegistryName(ResourceLocation name) {
    return null;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return null;
  }

  @Override
  public Class<IRecipe> getRegistryType() {
    return null;
  }
}
