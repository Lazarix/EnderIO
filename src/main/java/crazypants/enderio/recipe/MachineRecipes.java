package crazypants.enderio.recipe;

import crazypants.enderio.machine.ClearConfigRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MachineRecipes {

  public static void addRecipes() {
    ClearConfigRecipe inst = new ClearConfigRecipe();
    MinecraftForge.EVENT_BUS.register(inst);
    //GameRegistry.addRecipe(inst); //todo: fix
  }

}
