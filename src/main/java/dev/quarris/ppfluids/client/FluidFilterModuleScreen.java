package dev.quarris.ppfluids.client;

import de.ellpeck.prettypipes.pipe.containers.AbstractPipeGui;
import dev.quarris.ppfluids.container.FluidFilterModuleContainer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class FluidFilterModuleScreen extends AbstractPipeGui<FluidFilterModuleContainer> {

    public FluidFilterModuleScreen(FluidFilterModuleContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    protected void init() {
        super.init();
        List<Widget> buttons = this.container.getFilter().createScreenButtons(this, this.guiLeft + 7, this.guiTop + 17 + 32 + 18 * MathHelper.ceil((float)(this.container).getFilter().size() / 9.0F) + 2);

        for (Widget button : buttons) {
            this.addButton(button);
        }
    }
}
