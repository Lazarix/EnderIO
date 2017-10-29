package crazypants.enderio.gui;

import javax.annotation.Nonnull;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.client.gui.button.CycleButton;

import crazypants.enderio.Lang;
import crazypants.enderio.machine.interfaces.IRedstoneModeControlable;
import crazypants.enderio.machine.modes.PacketRedstoneMode;
import crazypants.enderio.machine.modes.RedstoneControlMode;
import crazypants.enderio.network.PacketHandler;
import net.minecraft.tileentity.TileEntity;

public class RedstoneModeButton<T extends TileEntity & IRedstoneModeControlable> extends CycleButton<RedstoneControlMode.IconHolder> {

  private IRedstoneModeControlable model;
  private T te;

  public RedstoneModeButton(@Nonnull IGuiScreen gui, int id, int x, int y, @Nonnull IRedstoneModeControlable model) {
    super(gui, id, x, y, RedstoneControlMode.IconHolder.class);
    this.model = model;
    setModeRaw(RedstoneControlMode.IconHolder.getFromMode(model.getRedstoneControlMode()));
  }

  public RedstoneModeButton(@Nonnull IGuiScreen gui, int id, int x, int y, @Nonnull T te) {
    this(gui, id, x, y, (IRedstoneModeControlable) te);
    this.te = te;
  }

  public void setModeRaw(@Nonnull RedstoneControlMode.IconHolder newMode) {
    if (model == null) {
      return;
    }
    super.setMode(newMode);
    setToolTip(Lang.GUI_REDSTONE_MODE.get(), getMode().getTooltip()); // forces our behavior
  }

  @Override
  public void setMode(@Nonnull RedstoneControlMode.IconHolder newMode) {
    if (model == null) {
      return;
    }
    setModeRaw(newMode);
    model.setRedstoneControlMode(getMode().getMode());
    if (te != null) {
      PacketHandler.INSTANCE.sendToServer(new PacketRedstoneMode(te));
    }
  }

  public void onGuiInit() {
  }
}
