package nz.carso.reveng.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class REPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("re_integration");

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(PacketAnalyzer.PacketAnalyzerHandler.class, PacketAnalyzer.class, id++, Side.SERVER);
    }
}
