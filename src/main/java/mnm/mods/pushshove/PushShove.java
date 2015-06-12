package mnm.mods.pushshove;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "PushShove", name = "Push and Shove", version = "@VERSION@")
public class PushShove {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMove(LivingUpdateEvent living) {
        Entity entity = living.entity;

        if (!entity.isDead) {

            final double radius = 0.20000000298023224D;
            AxisAlignedBB bounds = entity.getEntityBoundingBox().expand(radius, 0, radius);
            List<Entity> list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, bounds);

            if (list != null) {
                for (Entity entity1 : list) {
                    if (!entity1.isDead && entity1 instanceof EntityPlayer) {
                        entity.applyEntityCollision(entity1);
                    }
                }
            }
        }
    }
}
