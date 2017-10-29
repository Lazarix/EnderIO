package crazypants.enderio.teleport;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.Util;
import com.enderio.core.common.vecmath.Vector3d;

import crazypants.enderio.Log;
import crazypants.enderio.api.teleport.TeleportEntityEvent;
import crazypants.enderio.api.teleport.TravelSource;
import crazypants.enderio.sound.SoundHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportUtil {

  public static boolean doTeleport(@Nonnull Entity entityLiving, @Nonnull BlockPos pos, int targetDim, boolean conserveMotion, @Nonnull TravelSource source) {
    if (entityLiving.world.isRemote) {
      return checkClientTeleport(entityLiving, pos, targetDim, source);
    }
    return serverTeleport(entityLiving, pos, targetDim, conserveMotion, source);
  }

  public static boolean checkClientTeleport(@Nonnull Entity entityLiving, @Nonnull BlockPos pos, int targetDim, @Nonnull TravelSource source) {
    TeleportEntityEvent evt = new TeleportEntityEvent(entityLiving, source, pos, targetDim);
    if (MinecraftForge.EVENT_BUS.post(evt)) {
      return false;
    }
    return true;
  }

  public static boolean serverTeleport(@Nonnull Entity entity, @Nonnull BlockPos pos, int targetDim, boolean conserveMotion, @Nonnull TravelSource source) {

    TeleportEntityEvent evt = new TeleportEntityEvent(entity, source, pos, targetDim);
    if (MinecraftForge.EVENT_BUS.post(evt)) {
      return false;
    }

    EntityPlayerMP player = null;
    if (entity instanceof EntityPlayerMP) {
      player = (EntityPlayerMP) entity;
    }
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();

    int from = entity.dimension;
    /*if (from != targetDim) {
      MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
      WorldServer fromDim = server.worldServerForDimension(from);
      WorldServer toDim = server.worldServerForDimension(targetDim);
      Teleporter teleporter = new TeleporterEIO(toDim);
      // play sound at the dimension we are leaving for others to hear
      SoundHelper.playSound(server.worldServerForDimension(entity.dimension), entity, source.sound, 1.0F, 1.0F);
      if (player != null) {
        server.getPlayerList().transferPlayerToDimension(player, targetDim, teleporter);
        if (from == 1 && entity.isEntityAlive()) { // get around vanilla End
                                                   // hacks
          toDim.spawnEntity(entity);
          toDim.updateEntityWithOptionalForce(entity, false);
        }
      } else {
        NBTTagCompound tagCompound = new NBTTagCompound();
        float rotationYaw = entity.rotationYaw;
        float rotationPitch = entity.rotationPitch;
        entity.writeToNBT(tagCompound);
        Class<? extends Entity> entityClass = entity.getClass();
        fromDim.removeEntity(entity);

        try {
          Entity newEntity = entityClass.getConstructor(World.class).newInstance(toDim);
          newEntity.readFromNBT(tagCompound);
          newEntity.setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
          newEntity.forceSpawn = true;
          toDim.spawnEntity(newEntity);
          newEntity.forceSpawn = false; // necessary?
        } catch (Exception e) {
          // Throwables.propagate(e);
          Log.error("serverTeleport: Error creating a entity to be created in new dimension.");
          return false;
        }
      }
    }*/ //todo: fix

    // Force the chunk to load
    if (!entity.world.isBlockLoaded(pos)) {
      entity.world.getChunkFromBlockCoords(pos);
    }

    if (player != null) {
      player.connection.setPlayerLocation(x + 0.5, y + 1.1, z + 0.5, player.rotationYaw, player.rotationPitch);
    } else {
      entity.setPositionAndUpdate(x + 0.5, y + 1.1, z + 0.5);
    }

    entity.fallDistance = 0;
    SoundHelper.playSound(entity.world, entity, source.sound, 1.0F, 1.0F);

    if (player != null) {
      if (conserveMotion) {
        Vector3d velocityVex = Util.getLookVecEio(player);
        SPacketEntityVelocity p = new SPacketEntityVelocity(entity.getEntityId(), velocityVex.x, velocityVex.y, velocityVex.z);
        player.connection.sendPacket(p);
      }
    }

    return true;
  }

  private static class TeleporterEIO extends Teleporter {

    public TeleporterEIO(WorldServer p_i1963_1_) {
      super(p_i1963_1_);
    }

    @Override
    public boolean makePortal(@Nonnull Entity p_makePortal_1_) {
      return true;
    }

    @Override
    public boolean placeInExistingPortal(@Nonnull Entity entityIn, float rotationYaw) {
      return true;
    }

    @Override
    public void placeInPortal(@Nonnull Entity entity, float rotationYaw) {
      int x = MathHelper.floor(entity.posX);
      int y = MathHelper.floor(entity.posY) - 1;
      int z = MathHelper.floor(entity.posZ);

      entity.setLocationAndAngles(x, y, z, entity.rotationPitch, entity.rotationYaw);
      entity.motionX = 0;
      entity.motionY = 0;
      entity.motionZ = 0;
    }

    @Override
    public void removeStalePortalLocations(long p_removeStalePortalLocations_1_) {
    }

  }

}
