package crazypants.enderio.integration.immersiveengineering;

import javax.annotation.Nonnull;

import crazypants.enderio.farming.IFarmer;
import crazypants.enderio.farming.farmers.CustomSeedFarmer;
import crazypants.enderio.farming.farmers.IFarmerJoe;
import crazypants.enderio.farming.farmers.IHarvestResult;
import crazypants.enderio.farming.farmers.StemFarmer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HempFarmerIE extends IForgeRegistryEntry.Impl<IFarmerJoe> implements IFarmerJoe {

  public static HempFarmerIE create() {
    if (Block.REGISTRY.containsKey(new ResourceLocation("ImmersiveEngineering", "hemp"))) {
      Block hempBlock = Block.REGISTRY.getObject(new ResourceLocation("ImmersiveEngineering", "hemp"));
      Item hempSeedItem = Item.REGISTRY.getObject(new ResourceLocation("ImmersiveEngineering", "seed"));
      if (hempSeedItem != null) {
        ItemStack hempSeed = new ItemStack(hempSeedItem);
        return new HempFarmerIE(new CustomSeedFarmer(hempBlock, 4, hempSeed), new StemFarmer(hempBlock, hempSeed));
      }
    }
    return null;
  }

  private final @Nonnull IFarmerJoe seedFarmer;
  private final @Nonnull IFarmerJoe stemFarmer;

  public HempFarmerIE(@Nonnull IFarmerJoe seedFarmer, @Nonnull IFarmerJoe stemFarmer) {
    this.seedFarmer = seedFarmer;
    this.stemFarmer = stemFarmer;
  }

  @Override
  public boolean prepareBlock(@Nonnull IFarmer farm, @Nonnull BlockPos bc, @Nonnull Block block, @Nonnull IBlockState state) {
    return seedFarmer.prepareBlock(farm, bc, block, state);
  }

  @Override
  public boolean canHarvest(@Nonnull IFarmer farm, @Nonnull BlockPos bc, @Nonnull Block block, @Nonnull IBlockState state) {
    return seedFarmer.canHarvest(farm, bc, block, state);
  }

  @Override
  public boolean canPlant(@Nonnull ItemStack stack) {
    return seedFarmer.canPlant(stack);
  }

  @Override
  public IHarvestResult harvestBlock(@Nonnull IFarmer farm, @Nonnull BlockPos bc, @Nonnull Block block, @Nonnull IBlockState state) {
    return stemFarmer.harvestBlock(farm, bc, block, state);
  }

}
