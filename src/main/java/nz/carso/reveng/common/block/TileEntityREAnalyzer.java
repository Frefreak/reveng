package nz.carso.reveng.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import javax.annotation.Nullable;

import static com.google.common.primitives.Ints.min;

public class TileEntityREAnalyzer extends TileEntity implements ITickable {

    ItemStackHandler inv = new ItemStackHandler(1);
    public IItemHandler[] targetCap = {null, null, null, null, null, null, null};
    public int selectedFace = 0;
    public int selectedSlot = -1;
    public int[] selectedSlots = {0, 0, 0, 0, 0, 0, 0};

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inv.deserializeNBT(compound.getCompoundTag("inv"));
        selectedFace = compound.getInteger("face");
        selectedSlot = compound.getInteger("slot");
        selectedSlots = compound.getIntArray("slots");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        System.out.println("write");
        compound.setTag("inv", inv.serializeNBT());
        compound.setInteger("face", selectedFace);
        compound.setInteger("slot", selectedSlot);
        compound.setIntArray("slots", selectedSlots);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY? (T)inv : super.getCapability(capability, facing);
    }


    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos.offset(state.getValue(BlockREAnalyzer.FACING)));
        if (te != null) {
            for (int i = 0; i < 6; i++) {
                EnumFacing facing = EnumFacing.getFront(i);
                if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                    targetCap[i] = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                } else
                    targetCap[i] = null;
            }
            if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                targetCap[6] = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            else
                targetCap[6] = null;
        } else {
            for (int i = 0; i < 6; i++) {
                targetCap[i] = null;
            }
            targetCap[6] = null;
        }
        if (targetCap[selectedFace] != null) {
            selectedSlot = min(selectedSlots[selectedFace], targetCap[selectedFace].getSlots() - 1);
            selectedSlots[selectedFace] = selectedSlot;
        }
        else
            selectedSlot = -1;

    }

    public void setSelectedFace(int face) {
        if (targetCap[face] != null) {
            this.selectedFace = face;
            this.selectedSlot = this.selectedSlots[face];
        }
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
        this.selectedSlots[this.selectedFace] = slot;
    }

    public void tryInsert() {
        if (this.targetCap[this.selectedFace] != null) {
            ItemStack rem = this.targetCap[this.selectedFace].insertItem(this.selectedSlot, inv.getStackInSlot(0), false);
            inv.setStackInSlot(0, rem);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        readFromNBT(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }
}
