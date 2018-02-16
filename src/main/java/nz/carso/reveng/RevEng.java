package nz.carso.reveng;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nz.carso.reveng.common.block.TileEntityREAnalyzer;
import nz.carso.reveng.common.gui.REGuiHandler;
import nz.carso.reveng.common.network.REPacketHandler;
import org.apache.logging.log4j.Logger;

@Mod(modid = RevEng.MODID, name = RevEng.NAME, version = RevEng.VERSION)
public class RevEng
{
    public static final String MODID = "re_integration";
    public static final String NAME = "Reverse Engineering Integration";
    public static final String VERSION = "1.0";

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

    @Mod.Instance
    public static RevEng instance = new RevEng();

    @SidedProxy(clientSide="nz.carso.reveng.ClientProxy", serverSide="nz.carso.reveng.ServerProxy")
    public static nz.carso.reveng.GeneralProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new REGuiHandler());
        REPacketHandler.init();
        GameRegistry.registerTileEntity(TileEntityREAnalyzer.class, "re_integration:re_analyzer");

        proxy.preInit(event);
    }



    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) { proxy.postInit(event); }
}
