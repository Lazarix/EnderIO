package crazypants.enderio.enchantment;

import javax.annotation.Nonnull;

import com.enderio.core.api.common.enchant.IAdvancedEnchant;

import crazypants.enderio.Lang;
import crazypants.enderio.config.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EnchantmentSoulBound extends Enchantment implements IAdvancedEnchant {

  private static final @Nonnull String NAME = "soulBound";

  public static EnchantmentSoulBound create() {
    EnchantmentSoulBound res = new EnchantmentSoulBound();
    //GameRegistry.register(res); //todo: fix
    return res;
  }

  private EnchantmentSoulBound() {
    super(Config.enchantmentSoulBoundRarity, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
    setName(NAME);
    setRegistryName(NAME);
  }

  @Override
  public int getMaxEnchantability(int level) {
    return 60;
  }

  @Override
  public int getMinEnchantability(int level) {
    return 16;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public @Nonnull String[] getTooltipDetails(@Nonnull ItemStack stack) {
    return new String[] { Lang.ENCHANT_SOULBOUND.get() };
  }

}
