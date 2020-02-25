package nz.carso.reveng.common.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import nz.carso.reveng.GeneralProxy;
import nz.carso.reveng.RevEng;

@Mod.EventBusSubscriber
public final class BlockRE {

    public final static Block analyzer = new BlockREAnalyzer();

    public final static Block[] allBlocks = {
            analyzer
    };

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        for (Block blk : allBlocks) {
            reg.register(blk);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();

        for (Block blk: allBlocks) {
            if (blk.getRegistryName() != null) {
                Item item = new ItemBlock(blk).setRegistryName(blk.getRegistryName());
                reg.register(item);
            }

        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        for (Block blk : allBlocks) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blk), 0,
                    new ModelResourceLocation(blk.getRegistryName(), "facing=up"));
        }
    }
}
