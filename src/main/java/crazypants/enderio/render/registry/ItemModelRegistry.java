package crazypants.enderio.render.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import crazypants.enderio.EnderIO;
import crazypants.enderio.render.model.FacadeSmartItemModel;
import crazypants.enderio.render.model.RotatingSmartItemModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemModelRegistry {

  private ItemModelRegistry() {
  }

  public static void create() {
    MinecraftForge.EVENT_BUS.register(new ItemModelRegistry());
  }

  public static interface Registry {
    @Nonnull
    IBakedModel wrap(@Nonnull IBakedModel model);
  }

  private static final Map<ModelResourceLocation, Registry> registries = new HashMap<ModelResourceLocation, Registry>();

  public static void register(@Nonnull String resource, @Nonnull Registry registry) {
    registries.put(new ModelResourceLocation(EnderIO.DOMAIN + ":" + resource + "#inventory"), registry);
  }

  public static void register(@Nonnull ModelResourceLocation resource, @Nonnull Registry registry) {
    registries.put(resource, registry);
  }

  public static void registerRotating(@Nonnull String resource, final int speed) {
    register(resource, new Registry() {
      @Override
      public @Nonnull IBakedModel wrap(@Nonnull IBakedModel model) {
        return new RotatingSmartItemModel((IBakedModel) model, speed);
      }
    });
  }

  public static void registerFacade(@Nonnull ModelResourceLocation resource) {
    register(resource, new Registry() {
      @Override
      public @Nonnull IBakedModel wrap(@Nonnull IBakedModel model) {
        return new FacadeSmartItemModel((IBakedModel) model);
      }
    });
  }

  @SubscribeEvent()
  public void bakeModels(@Nonnull ModelBakeEvent event) {
    for (Entry<ModelResourceLocation, Registry> entry : registries.entrySet()) {
      final ModelResourceLocation resource = entry.getKey();
      if (resource != null) {
        IBakedModel model = event.getModelRegistry().getObject(resource);
        final Registry registry = entry.getValue();
        if (registry != null && model != null) {
          event.getModelRegistry().putObject(resource, registry.wrap(model));
        }
      }
    }
  }

}
