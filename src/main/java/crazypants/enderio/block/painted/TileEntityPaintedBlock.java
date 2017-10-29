package crazypants.enderio.block.painted;

import javax.annotation.Nullable;

import crazypants.enderio.TileEntityEio;
import crazypants.enderio.paint.IPaintable;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.block.state.IBlockState;

@Storable
public class TileEntityPaintedBlock extends TileEntityEio implements IPaintable.IPaintableTileEntity {

  @Store
  private IBlockState paintSource = null;

  public TileEntityPaintedBlock() {
  }

  @Override
  public void onAfterDataPacket() {
    updateBlock();
  }

  @Override
  public void setPaintSource(@Nullable IBlockState paintSource) {
    this.paintSource = paintSource;
    markDirty();
    updateBlock();
  }

  @Override
  public IBlockState getPaintSource() {
    return paintSource;
  }

  @Override
  public void update() {
    
  }
}
