package net.alexburton.gangmod.event;

import net.alexburton.gangmod.GangMod;
import net.alexburton.gangmod.entity.ModEntities;
import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GangMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        //TODO: For each gang member, event.put()
        event.put(ModEntities.FISHY.get(), FishyEntity.createAttributes().build());
    }
}
