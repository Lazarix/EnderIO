package crazypants.enderio.item.darksteel.upgrade.energy;

import javax.annotation.Nonnull;

import crazypants.enderio.EnderIO;
import crazypants.enderio.config.recipes.xml.Item;
import crazypants.enderio.handler.darksteel.IDarkSteelItem;
import crazypants.enderio.power.tesla.ForgeToTeslaAdapter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnergyUpgradePowerAdapter {

  public static void init(@Nonnull FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(EnergyUpgradePowerAdapter.class);
  }

  private static final @Nonnull ResourceLocation KEY = new ResourceLocation(EnderIO.DOMAIN, "powerhandler");
  private static final @Nonnull ResourceLocation KEY_TESLA = new ResourceLocation(EnderIO.DOMAIN, "teslahandler");

  private static boolean addTesla = Loader.isModLoaded("tesla");

  @SubscribeEvent
  public static void attachCapabilities(AttachCapabilitiesEvent<Item> evt) {
    if (evt.getCapabilities().containsKey(KEY)) {
      return;
    }
    /*if (evt.getObject() instanceof IDarkSteelItem) {
      EnergyUpgrade cap = new EnergyUpgrade(evt.getObject());
      evt.addCapability(KEY, (ICapabilityProvider) cap);
      if (addTesla) {
        addTeslaWrapper(evt, cap);
      }
    }*/ //todo: fix

  }

  private static void addTeslaWrapper(@Nonnull AttachCapabilitiesEvent<Item> evt, @Nonnull EnergyUpgrade cap) {
    evt.addCapability(KEY_TESLA, new ForgeToTeslaAdapter((IEnergyStorage) cap));
  }

}
