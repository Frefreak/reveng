package nz.carso.reveng.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import nz.carso.reveng.common.block.ContainerREAnalyzer;
import nz.carso.reveng.common.block.TileEntityREAnalyzer;

import javax.annotation.Nullable;

public class REGuiHandler implements IGuiHandler {

    public final static int ANALYZER = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ANALYZER:
                TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                if (te != null)
                    return new ContainerREAnalyzer(player.inventory, (TileEntityREAnalyzer) te);
                else
                    return null;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ANALYZER:
                TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                if (te != null)
                    return new GuiREAnalyzer(new ContainerREAnalyzer(player.inventory, (TileEntityREAnalyzer) te),
                            (TileEntityREAnalyzer) te);
            default:
                return null;
        }
    }
}
