package crazypants.enderio.farming;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.stackable.Things;

import crazypants.enderio.EnderIO;
import crazypants.enderio.config.Config;
import crazypants.enderio.farming.farmers.ChorusFarmer;
import crazypants.enderio.farming.farmers.CocoaFarmer;
import crazypants.enderio.farming.farmers.CustomSeedFarmer;
import crazypants.enderio.farming.farmers.FlowerPicker;
import crazypants.enderio.farming.farmers.IFarmerJoe;
import crazypants.enderio.farming.farmers.MelonFarmer;
import crazypants.enderio.farming.farmers.PickableFarmer;
import crazypants.enderio.farming.farmers.PlantableFarmer;
import crazypants.enderio.farming.farmers.StemFarmer;
import crazypants.enderio.farming.farmers.TreeFarmer;
import crazypants.enderio.init.ModObject;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = EnderIO.MODID)
public final class FarmersRegistry {

  // TODO: Re-add Darksteel items
  public static final @Nonnull Things slotItemsAxeTools = new Things().add(Items.WOODEN_HOE).add(Items.STONE_HOE).add(Items.IRON_HOE).add(Items.GOLDEN_HOE)
      .add(Items.DIAMOND_HOE).add(Config.farmHoes);
  public static final @Nonnull Things slotItemsHoeTools = new Things().add(Items.WOODEN_AXE).add(Items.STONE_AXE).add(Items.IRON_AXE).add(Items.GOLDEN_AXE)
      .add(Items.DIAMOND_AXE); //.add(ModObject.itemDarkSteelAxe);
  public static final @Nonnull Things slotItemsExtraTools = new Things().add(Items.SHEARS); //.add(ModObject.itemDarkSteelShears);
  public static final @Nonnull Things slotItemsSeeds = new Things("treeSapling").add(Items.WHEAT_SEEDS).add(Items.CARROT).add(Items.POTATO)
      .add(Blocks.RED_MUSHROOM)
      .add(Blocks.BROWN_MUSHROOM).add(Items.NETHER_WART).add(Blocks.SAPLING).add(Items.REEDS).add(Items.MELON_SEEDS).add(Items.PUMPKIN_SEEDS);
  public static final @Nonnull Things slotItemsProduce = new Things("logWood").add(new ItemStack(Blocks.LOG, 1, 0)).add(Blocks.WHEAT)
      .add(new ItemStack(Blocks.LEAVES, 1, 0)).add(Items.APPLE).add(Items.MELON).add(Blocks.PUMPKIN).add(Blocks.YELLOW_FLOWER).add(Blocks.RED_FLOWER);
  public static final @Nonnull Things slotItemsFertilizer = new Things().add(new ItemStack(Items.DYE, 1, 15));

  // TODO 1.12: move those treetaps somewhere else
  // slotItemsStacks3.addAll(TileFarmStation.TREETAPS.getItemStacks());

  private static final @Nonnull Things SAPLINGS = new Things("treeSapling");
  private static final @Nonnull Things WOODS = new Things("logWood");
  private static final @Nonnull Things FLOWERS = new Things().add(Blocks.YELLOW_FLOWER).add(Blocks.RED_FLOWER);

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public static void registerFarmers(@Nonnull RegistryEvent.Register<IFarmerJoe> event) {
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void registerFarmersLow(@Nonnull RegistryEvent.Register<IFarmerJoe> event) {
    event.getRegistry().register(new FlowerPicker(FLOWERS).setRegistryName(EnderIO.DOMAIN, "flowers"));
    event.getRegistry().register(new StemFarmer(Blocks.REEDS, new ItemStack(Items.REEDS)).setRegistryName(EnderIO.DOMAIN, "reeds"));
    event.getRegistry().register(new StemFarmer(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)).setRegistryName(EnderIO.DOMAIN, "cactus"));
    event.getRegistry().register(new TreeFarmer(SAPLINGS, WOODS).setRegistryName(EnderIO.DOMAIN, "trees"));
    event.getRegistry().register(new TreeFarmer(true, Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK).setRegistryName(EnderIO.DOMAIN, "red_mushrooms"));
    event.getRegistry().register(new TreeFarmer(true, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK).setRegistryName(EnderIO.DOMAIN, "brown_mushrooms"));
    // special case of plantables to get spacing correct
    event.getRegistry()
        .register(new MelonFarmer(Blocks.MELON_STEM, Blocks.MELON_BLOCK, new ItemStack(Items.MELON_SEEDS)).setRegistryName(EnderIO.DOMAIN, "melons"));
    event.getRegistry()
        .register(new MelonFarmer(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)).setRegistryName(EnderIO.DOMAIN, "pumpkins"));
    // 'BlockNetherWart' is not an IGrowable
    event.getRegistry().register(
        new CustomSeedFarmer(Blocks.NETHER_WART, 3, new ItemStack(Items.NETHER_WART)).setRequiresTilling(false).setRegistryName(EnderIO.DOMAIN, "netherwart"));
    // Cocoa is odd
    event.getRegistry().register(new CocoaFarmer().setRegistryName(EnderIO.DOMAIN, "cocoa"));
    // Chorus plant is even odder
    event.getRegistry().register(new ChorusFarmer().setRegistryName(EnderIO.DOMAIN, "chorus"));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void registerFarmersLowest(@Nonnull RegistryEvent.Register<IFarmerJoe> event) {
    // Handles all 'vanilla' style crops
    event.getRegistry().register(new PlantableFarmer().setRegistryName(EnderIO.DOMAIN, "default"));
  }

  public static void addPickable(@Nonnull RegistryEvent.Register<IFarmerJoe> event, @Nonnull String mod, @Nonnull String blockName, @Nonnull String itemName) {
    Block cropBlock = findBlock(mod, blockName);
    Item seedItem = findItem(mod, itemName);
    if (cropBlock != null && seedItem != null) {
      event.getRegistry().register(new PickableFarmer(cropBlock, new ItemStack(seedItem)).setRegistryName(mod, blockName));
    }
  }

  public static CustomSeedFarmer addSeed(@Nonnull RegistryEvent.Register<IFarmerJoe> event, @Nonnull String mod, @Nonnull String blockName,
      @Nonnull String itemName, Block... extraFarmland) {
    Block cropBlock = findBlock(mod, blockName);
    Item seedItem = findItem(mod, itemName);
    if (cropBlock != null && seedItem != null) {
      CustomSeedFarmer farmer = new CustomSeedFarmer(cropBlock, new ItemStack(seedItem));
      if (extraFarmland != null) {
        for (Block farmland : extraFarmland) {
          if (farmland != null) {
            farmer.addTilledBlock(farmland);
          }
        }
      }
      event.getRegistry().register(farmer.setRegistryName(mod, blockName));
      return farmer;
    }
    return null;
  }

  public static Block findBlock(@Nonnull String mod, @Nonnull String blockName) {
    final ResourceLocation name = new ResourceLocation(mod, blockName);
    if (Block.REGISTRY.containsKey(name)) {
      return Block.REGISTRY.getObject(name);
    }
    return null;
  }

  public static Item findItem(@Nonnull String mod, @Nonnull String itemName) {
    final ResourceLocation name = new ResourceLocation(mod, itemName);
    if (Item.REGISTRY.containsKey(name)) {
      return Item.REGISTRY.getObject(name);
    }
    return null;
  }

  public static void registerFlower(String... names) {
    for (String name : names) {
      FLOWERS.add(name);
    }
  }

  public static void registerSaplings(String... names) {
    for (String name : names) {
      SAPLINGS.add(name);
    }
  }

  public static void registerLogs(String... names) {
    for (String name : names) {
      WOODS.add(name);
    }
  }

  private FarmersRegistry() {
  }

}
