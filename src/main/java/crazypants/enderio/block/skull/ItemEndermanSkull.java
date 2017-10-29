package crazypants.enderio.block.skull;

import javax.annotation.Nonnull;

import crazypants.enderio.EnderIOTab;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEndermanSkull extends ItemBlock {

  public ItemEndermanSkull(@Nonnull Block block) {
    super(block);
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(EnderIOTab.tabEnderIO);
  }

  @Override
  public @Nonnull String getUnlocalizedName(@Nonnull ItemStack par1ItemStack) {
    int meta = par1ItemStack.getItemDamage();
    meta = MathHelper.clamp(meta, 0, SkullType.values().length - 1);
    return getUnlocalizedName() + "." + SkullType.values()[meta].getName();
  }

  //@Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(@Nonnull Item par1, @Nonnull CreativeTabs par2CreativeTabs, @Nonnull NonNullList<ItemStack> par3List) {
    for (int j = 0; j < SkullType.values().length; ++j) {
      if (!SkullType.values()[j].showEyes()) {
        par3List.add(new ItemStack(par1, 1, j));
      }
    }
  }

}
