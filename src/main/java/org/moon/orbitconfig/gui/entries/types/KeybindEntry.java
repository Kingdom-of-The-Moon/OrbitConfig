package org.moon.orbitconfig.gui.entries.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.moon.orbitconfig.config.ConfigObject;
import org.moon.orbitconfig.gui.ConfigScreen;
import org.moon.orbitconfig.gui.entries.TypedEntry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class KeybindEntry extends TypedEntry<KeyBinding> {

    private final ButtonWidget toggle;
    private KeyBinding focusedBinding;

    public KeybindEntry(ConfigScreen parent, ConfigObject config, Text display, Text tooltip, Field f) {
        super(parent, config, f, display, tooltip);

        this.toggle = new ButtonWidget(0, 0, 75, 20, this.display, (button -> focusedBinding = this.initialValue));
    }

    @Override
    public List<? extends Element> children() {
        return Arrays.asList(this.toggle, this.reset);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Arrays.asList(this.toggle, this.reset);
    }


    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //toggle button
        this.toggle.x = x + 165;
        this.toggle.y = y;
        this.toggle.setMessage(this.entry.<KeyBinding>get().getBoundKeyLocalizedText());

        if (focusedBinding == this.entry.<KeyBinding>get()) {
            this.toggle.setMessage((new LiteralText("> ")).append(this.toggle.getMessage().shallowCopy().formatted(Formatting.AQUA)).append(" <").formatted(Formatting.AQUA));
        }
        else if (!this.entry.<KeyBinding>get().isUnbound()) {
            for (KeyBinding key : MinecraftClient.getInstance().options.keysAll) {
                if (key != this.entry.<KeyBinding>get() && this.entry.<KeyBinding>get().equals(key)) {
                    this.toggle.setMessage(this.toggle.getMessage().shallowCopy().formatted(Formatting.RED));

                    break;
                }
            }
        }

        this.toggle.render(matrices, mouseX, mouseY, tickDelta);
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
    }
}
