package quarris.ppfluids.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import quarris.ppfluids.ModContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FluidItem extends ItemFluidContainer {

    public FluidItem() {
        super(new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1), Integer.MAX_VALUE);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        FluidStack fluidStack = getFluidCopyFromItem(stack);
        tooltip.add(new TranslationTextComponent(fluidStack.getTranslationKey())
                .appendString(": ").appendString(String.valueOf(fluidStack.getAmount())));
    }

    public static ItemStack createItemFromFluid(FluidStack fluid) {
        ItemStack item =  new ItemStack(ModContent.FLUID_ITEM);
        IFluidHandlerItem tank = FluidUtil.getFluidHandler(item).orElse(null);
        if (tank != null) {
            tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
        }
        return item;
    }

    public static FluidStack getFluidCopyFromItem(ItemStack item) {
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(item).orElse(null);
        if (handler != null) {
            return handler.getFluidInTank(0).copy();
        }
        return FluidStack.EMPTY;
    }

    public static ItemStack insertFluid(IFluidHandler handler, ItemStack fluidItem, boolean simulate) {
        FluidStack fluidStack = FluidItem.getFluidCopyFromItem(fluidItem);
        int filled = handler.fill(fluidStack, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        fluidStack.shrink(filled);

        if (fluidStack.isEmpty())
            return ItemStack.EMPTY;

        return FluidItem.createItemFromFluid(fluidStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidHandlerItemStack.Consumable(stack, capacity) {
            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return this.getFluid().isEmpty();
            }
        };
    }
}
