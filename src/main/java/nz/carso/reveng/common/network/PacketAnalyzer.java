package nz.carso.reveng.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nz.carso.reveng.common.block.TileEntityREAnalyzer;

public class PacketAnalyzer implements IMessage {
    public int selectedFace;
    public int selectedSlot;
    public int[] selectedSlots = {0, 0, 0, 0, 0, 0, 0};
    public int x, y, z;
    public PacketAnalyzer() {}

    public PacketAnalyzer(int face, int slot, int[] slots, int x, int y, int z) {
        this.selectedFace = face;
        this.selectedSlot = slot;
        this.selectedSlots = slots;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.selectedFace);
        buf.writeInt(this.selectedSlot);
        for (int i = 0; i < 7; i++)
            buf.writeInt(this.selectedSlots[i]);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.selectedFace = buf.readInt();
        this.selectedSlot = buf.readInt();
        for (int i = 0; i < 7; i++)
            this.selectedSlots[i] = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public static class PacketAnalyzerHandler implements IMessageHandler<PacketAnalyzer, IMessage> {
        @Override
        public IMessage onMessage(PacketAnalyzer message, MessageContext ctx) {
            ctx.getServerHandler().player.mcServer.addScheduledTask(() -> {
                World w = ctx.getServerHandler().player.getServerWorld();
                BlockPos p = new BlockPos(message.x, message.y, message.z);
                TileEntity te = w.getTileEntity(p);
                if (te != null && te instanceof TileEntityREAnalyzer) {
                    TileEntityREAnalyzer ana = (TileEntityREAnalyzer)te;
                    ana.selectedSlots = message.selectedSlots;
                    ana.setSelectedFace(message.selectedFace);
                    ana.setSelectedSlot(message.selectedSlot);
                    ana.tryInsert();
                    te.markDirty();
                    w.notifyBlockUpdate(p, w.getBlockState(p), w.getBlockState(p), 0);
                } else {
                    System.out.println("re analyzer: something's wrong");
                }
            });

            return null;
        }
    }
}
