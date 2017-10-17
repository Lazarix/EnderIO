package crazypants.enderio.filter.items;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;

import crazypants.enderio.EnderIOTab;
import crazypants.enderio.filter.IItemFilter;
import crazypants.enderio.filter.IItemFilterUpgrade;
import crazypants.enderio.filter.filters.PowerItemFilter;
import crazypants.enderio.init.IModObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

/**
 *
 * @author matthias
 */
public class ItemPowerItemFilter extends Item implements IItemFilterUpgrade, IResourceTooltipProvider {

  public static ItemPowerItemFilter create(@Nonnull IModObject modObject) {
    return new ItemPowerItemFilter(modObject);
  }

  protected ItemPowerItemFilter(@Nonnull IModObject modObject) {
    setCreativeTab(EnderIOTab.tabEnderIOItems);
  modObject.apply(this);
  setHasSubtypes(true);
    setMaxDamage(0);
    setMaxStackSize(64);
  }

  protected void init() {
    GameRegistry.register(this);
  }

  @Override
  public IItemFilter createFilterFromStack(@Nonnull ItemStack stack) {
    IItemFilter filter = new PowerItemFilter();
    if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("filter")) {
      filter.readFromNBT(stack.getTagCompound().getCompoundTag("filter"));
    }
    return filter;
  }

//  @Override
//  @SideOnly(Side.CLIENT)
//  public void registerIcons(IIconRegister IIconRegister) {
//    itemIcon = IIconRegister.registerIcon("enderio:filterUpgradePower");
//  }

  @Override
  public String getUnlocalizedName(@Nonnull ItemStack stack) {
    return getUnlocalizedName();
  }

  @Override
  public String getUnlocalizedNameForTooltip(@Nonnull ItemStack stack) {
    return getUnlocalizedName();
  }

}
