package net.alexburton.gangmod.event;

import net.alexburton.gangmod.GangMod;
import net.alexburton.gangmod.entity.client.ModModelLayers;
import net.alexburton.gangmod.entity.client.fishy_model;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GangMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        //TODO: For each gang member, event.registerLayerDefinition()
        event.registerLayerDefinition(ModModelLayers.FISHY_LAYER, fishy_model::createBodyLayer);
    }
}
