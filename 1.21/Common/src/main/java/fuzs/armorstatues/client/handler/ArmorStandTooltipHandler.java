package fuzs.armorstatues.client.handler;

import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.statuemenus.api.v1.helper.ArmorStandInteractHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArmorStandTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> lines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (itemStack.is(Items.ARMOR_STAND)) {
            List<Component> components = Proxy.INSTANCE.splitTooltipLines(ArmorStandInteractHelper.getArmorStandHoverText());
            if (tooltipFlag.isAdvanced()) {
                lines.addAll(lines.size() - (!itemStack.getComponents().isEmpty() ? 2 : 1), components);
            } else {
                lines.addAll(components);
            }
        }
    }
}
