package mnm.mods.pushshove;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

            List<Entity> list = getCollidingEntities(entity);

            for (Entity entity1 : list) {
                if (!entity1.isDead && entity1 instanceof EntityPlayer) {
                    entity.applyEntityCollision(entity1);
                }
            }

        }
    }

    @SubscribeEvent
    public void onFall(LivingHurtEvent hurt) {
        if (hurt.source == DamageSource.fall) {
            List<Entity> list = getCollidingEntities(hurt.entity);
            boolean cancel = false;
            for (Entity entity : list) {
                if (entity instanceof EntityPlayer || entity instanceof EntityLivingBase) {
                    cancel = true;
                    entity.attackEntityFrom(DamageSource.causeMobDamage(hurt.entityLiving), hurt.ammount * 3);
                }

                hurt.setCanceled(cancel);
            }
        }
    }

    private List<Entity> getCollidingEntities(Entity entity) {
        final double radius = 0.20000000298023224D;
        AxisAlignedBB bounds = entity.getEntityBoundingBox().expand(radius, 0, radius);
        return entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, bounds);
    }
}
