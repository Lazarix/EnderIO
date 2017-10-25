package crazypants.enderio.machine.modes;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.NNList;

import crazypants.enderio.EnderIO;
import crazypants.util.CapturedMob;

public enum EntityAction { // TODO 1.12 implement on obelisks and spawner
  ATTRACT("blockAttractor.action"),
  AVERT("blockSpawnGuard.action"),
  RELOCATE("blockSpawnRelocator.action"),
  SPAWN("blockPoweredSpawner.action"),

  ;

  private final @Nonnull String langKey;

  private EntityAction(@Nonnull String langKey) {
    this.langKey = langKey;
  }

  public String getActionString() {
    return EnderIO.lang.localize(langKey);
  }

  public static interface Implementer {

    @Nonnull
    NNList<CapturedMob> getEntities();

    @Nonnull
    EntityAction getEntityAction();

  }

}
