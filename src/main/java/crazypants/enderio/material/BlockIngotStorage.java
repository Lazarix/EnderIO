package crazypants.enderio.material;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.BlockEio;
import crazypants.enderio.ModObject;

public class BlockIngotStorage extends BlockEio {
  
  @SideOnly(Side.CLIENT)
  private IIcon[] icons = new IIcon[Alloy.values().length];

  public static BlockIngotStorage create() {
    BlockIngotStorage res = new BlockIngotStorage();
    res.init();
    return res;
  }
  
  private BlockIngotStorage() {
    super(ModObject.blockIngotStorage.unlocalisedName, null, Material.iron);
    setStepSound(soundTypeMetal);
  }
  
  @Override
  protected void init() {
    GameRegistry.registerBlock(this, BlockItemIngotStorage.class, ModObject.blockIngotStorage.unlocalisedName);
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register) {
    for (Alloy alloy : Alloy.values()) {
      icons[alloy.ordinal()] = register.registerIcon(alloy.iconKey + "Block");
    }
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    meta = MathHelper.clamp_int(meta, 0, Alloy.values().length - 1);
    return icons[meta];
  }
  
  @Override
  public int getDamageValue(World world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z);
  }
  
  @Override
  public int damageDropped(int meta) {
    return meta;
  }
  
  @Override
  public float getBlockHardness(World world, int x, int y, int z) {
    return Alloy.values()[world.getBlockMetadata(x, y, z)].getHardness();
  }
  
  @Override
  public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
    return getBlockHardness(world, x, y, z) * 2.0f; // vanilla default is / 5.0f, this means hardness*2 = resistance
  }
}