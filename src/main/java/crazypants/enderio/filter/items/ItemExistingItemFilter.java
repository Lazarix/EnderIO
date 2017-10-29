package crazypants.enderio.filter.items;

import java.util.List;

import javax.annotation.Nonnull;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;
import com.enderio.core.common.util.ChatUtil;

import crazypants.enderio.EnderIOTab;
import crazypants.enderio.Lang;
import crazypants.enderio.capability.ItemTools;
import crazypants.enderio.filter.FilterRegistry;
import crazypants.enderio.filter.IItemFilter;
import crazypants.enderio.filter.IItemFilterUpgrade;
import crazypants.enderio.filter.filters.ExistingItemFilter;
import crazypants.enderio.init.IModObject;
import crazypants.util.NbtValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public class ItemExistingItemFilter extends Item implements IItemFilterUpgrade, IResourceTooltipProvider {

  public static ItemExistingItemFilter create(@Nonnull IModObject modObject) {
    return new ItemExistingItemFilter(modObject);
  }

  protected ItemExistingItemFilter(@Nonnull IModObject modObject) {
    setCreativeTab(EnderIOTab.tabEnderIOItems);
    modObject.apply(this);
    setHasSubtypes(true);
    setMaxDamage(0);
    setMaxStackSize(64);
  }

  @Override
  public IItemFilter createFilterFromStack(@Nonnull ItemStack stack) {
    IItemFilter filter = new ExistingItemFilter();
    NBTTagCompound tag = NbtValue.FILTER.getTag(stack);
    if (tag != null) {
      filter.readFromNBT(tag);
    }
    return filter;
  }

  
  @Override
  public @Nonnull EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
      @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if(world.isRemote) {
      return EnumActionResult.SUCCESS;
    }    

    if(player.isSneaking()) {
      IItemHandler externalInventory = ItemTools.getExternalInventory(world, pos, side);
      if (externalInventory != null) {
        ItemStack heldItem = player.getHeldItem(hand);
        ExistingItemFilter filter = (ExistingItemFilter) createFilterFromStack(heldItem);
        ChatUtil.sendNoSpam(player, filter.mergeSnapshot(externalInventory) ? Lang.CONDUIT_FILTER_UPDATED.get() : Lang.CONDUIT_FILTER_NOTUPDATED.get());
        FilterRegistry.writeFilterToStack(filter, heldItem);
        return EnumActionResult.SUCCESS;
      }
    }

    return EnumActionResult.PASS;
  }

  @Override
  public @Nonnull String getUnlocalizedNameForTooltip(@Nonnull ItemStack stack) {
    return getUnlocalizedName();
  }

  //@Override
  @SideOnly(Side.CLIENT)
  public void addInformation(@Nonnull ItemStack par1ItemStack, @Nonnull EntityPlayer par2EntityPlayer, @Nonnull List<String> par3List, boolean par4) {
    if(FilterRegistry.isFilterSet(par1ItemStack)) {
      if(SpecialTooltipHandler.showAdvancedTooltips()) {
        par3List.add(Lang.CONDUIT_FILTER_CONFIGURED.get(TextFormatting.ITALIC));
        par3List.add(Lang.CONDUIT_FILTER_CLEAR.get(TextFormatting.ITALIC));
      }
    }
  }

}
