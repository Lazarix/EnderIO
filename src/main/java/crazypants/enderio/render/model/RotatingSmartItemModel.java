package crazypants.enderio.render.model;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import crazypants.enderio.EnderIO;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class RotatingSmartItemModel implements IBakedModel {

  private final @Nonnull IBakedModel parent;
  private final int speed;

  public RotatingSmartItemModel(@Nonnull IBakedModel parent, int speed) {
    this.parent = parent;
    this.speed = speed;
  }

  @Override
  public @Nonnull List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
    return parent.getQuads(state, side, rand);
  }

  @Override
  public boolean isAmbientOcclusion() {
    return parent.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return parent.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public @Nonnull TextureAtlasSprite getParticleTexture() {
    return parent.getParticleTexture();
  }

  @SuppressWarnings("deprecation")
  @Override
  public @Nonnull ItemCameraTransforms getItemCameraTransforms() {
    return parent.getItemCameraTransforms();
  }

  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
    Pair<? extends IBakedModel, Matrix4f> perspective = parent.handlePerspective(cameraTransformType);

    double r = (EnderIO.proxy.getTickCount() % 360) + Minecraft.getMinecraft().getRenderPartialTicks();

    TRSRTransformation transformOrig = new TRSRTransformation(perspective.getRight());
    Quat4f leftRot = transformOrig.getLeftRot();
    Quat4f yRotation = new Quat4f();
    yRotation.set(new AxisAngle4d(0, 1, 0, Math.toRadians(r * speed)));
    leftRot.mul(yRotation);
    TRSRTransformation transformNew = new TRSRTransformation(transformOrig.getTranslation(), leftRot, transformOrig.getScale(), transformOrig.getRightRot());

    return Pair.of(perspective.getLeft(), transformNew.getMatrix());
  }

  private final @Nonnull ItemOverrideList overrides = new ItemOverrideList(Lists.<ItemOverride> newArrayList()) {
    @Override
    public @Nonnull IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world,
        @Nullable EntityLivingBase entity) {
      if (originalModel != RotatingSmartItemModel.this) {
        return originalModel;
      }

      IBakedModel newBase = parent.getOverrides().handleItemState(parent, stack, world, entity);
      if (parent != newBase && newBase instanceof IBakedModel) {
        return new RotatingSmartItemModel((IBakedModel) newBase, speed);
      }
      return RotatingSmartItemModel.this;
    }
  };

  @Override
  public @Nonnull ItemOverrideList getOverrides() {
    return overrides;
  }

}
