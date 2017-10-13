package crazypants.enderio.init;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public interface IModObject
    {

    // Since it was removed from endercore, I have added this
    default @Nullable
    Block getBlock()
        {
        return null;
        }


    default @Nullable
    Item getItem()
        {
        return null;
        }


    @Nonnull
    String getUnlocalisedName();

    @Nonnull
    ResourceLocation getRegistryName();

    @Nonnull
    <B extends Block> B apply(@Nonnull B block);

    @Nonnull
    <I extends Item> I apply(@Nonnull I item);

    public static interface Registerable extends IModObject
        {

        @Nonnull
        Class<?> getClazz();

        @Nullable
        String getBlockMethodName();

        @Nullable
        String getItemMethodName();

        @Nullable
        Class<? extends TileEntity> getTileClass();

        void setItem(@Nullable Item obj);

        void setBlock(@Nullable Block obj);

        }

    /**
     * Interface to be implemented on blocks that are created from modObjects. It will be called at the right time to create and register the blockItem. Note that
     * the method shall NOT do the registering itself.
     */
    public static interface WithBlockItem
        {

        default Item createBlockItem(@Nonnull IModObject modObject)
            {
            return modObject.apply(new ItemBlock((Block) this));
            }

        ;

        }

    public static interface LifecycleInit
        {

        void init(@Nonnull IModObject modObject, @Nonnull FMLInitializationEvent event);

        }

    public static interface LifecyclePostInit
        {

        void init(@Nonnull IModObject modObject, @Nonnull FMLPostInitializationEvent event);

        }

    }