package fuzs.armorstatues.neoforge.client;

import fuzs.armorstatues.ArmorStatues;
import fuzs.armorstatues.client.ArmorStatuesClient;
import fuzs.armorstatues.handler.ArmorStandInteractHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod.EventBusSubscriber(modid = ArmorStatues.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArmorStatuesNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(ArmorStatues.MOD_ID, ArmorStatuesClient::new);
        registerHandlers();
    }

    private static void registerHandlers() {
        NeoForge.EVENT_BUS.addListener((final InputEvent.InteractionKeyMappingTriggered evt) -> {

            Minecraft minecraft = Minecraft.getInstance();
            if (evt.isUseItem() && minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.ENTITY) {

                EntityHitResult hitResult = (EntityHitResult) minecraft.hitResult;
                Entity entity = hitResult.getEntity();
                if (minecraft.level.getWorldBorder().isWithinBounds(entity.blockPosition())) {

                    Vec3 hitVector = hitResult.getLocation().subtract(entity.getX(), entity.getY(), entity.getZ());
                    EventResultHolder<InteractionResult> result = ArmorStandInteractHandler.onUseEntityAt(minecraft.player, minecraft.level, evt.getHand(), entity, hitVector);
                    // if InteractionResult.FAIL is returned the mod is missing server-side, and we open the menu client-side without sending a packet to the server, so the server does not try to interact
                    // only Fabric sending the packet is simple prevented by returning InteractionResult.FAIL from ArmorStandInteractHandler::onUseEntityAt, on Forge this approach seems to work
                    if (result.filter(interactionResult -> interactionResult == InteractionResult.FAIL).isInterrupt()) {
                        evt.setSwingHand(false);
                        evt.setCanceled(true);
                    }
                }
            }
        });
    }
}