package nz.carso.reveng;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nz.carso.reveng.common.block.BlockRE;
import nz.carso.reveng.common.block.TileEntityREAnalyzer;
import nz.carso.reveng.common.gui.REGuiHandler;
import org.apache.logging.log4j.Logger;

public class GeneralProxy {
    public static Logger logger;
    public final static CreativeTabs reTab = new CreativeTabs("RE integration") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.DIAMOND, 1);
        }

        @Override
        public String getTranslatedTabLabel() {
            return getTabLabel();
        }
    };

    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        GameRegistry.registerTileEntity(TileEntityREAnalyzer.class, "re_analyzer");
    }


    public void init(FMLInitializationEvent event)
    {
    }

    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
