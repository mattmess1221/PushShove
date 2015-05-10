package mnm.mods.pushshove;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "PushShove", name = "Push and Shove", version = "@VERSION@")
public class PushShove {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMove(LivingUpdateEvent living) {
        Entity entity = living.entity;

        if (!entity.isDead && entity.worldObj.isRemote) {
            AxisAlignedBB axisalignedbb = null;

            if (entity.ridingEntity != null && !entity.ridingEntity.isDead) {
                axisalignedbb = entity.boundingBox.union(entity.ridingEntity.boundingBox);
            } else {
                axisalignedbb = entity.boundingBox;
            }
            axisalignedbb = axisalignedbb.expand(.5, 0, .5);

            List<Entity> list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, axisalignedbb);

            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity1 = list.get(i);

                    if (!entity1.isDead && entity1 instanceof EntityPlayer) {
                        collideWithPlayer(entity, (EntityPlayer) entity1);
                    }
                }
            }
        }
    }

    private void collideWithPlayer(Entity entityIn, EntityPlayer entity) {
        if (entityIn.isSneaking() || entity.isSneaking()) {
            return;
        }
        if (entityIn.riddenByEntity != entity && entityIn.ridingEntity != entity) {
            // delta X
            double dx = entityIn.posX - entity.posX;
            // delta Z
            double dz = entityIn.posZ - entity.posZ;
            // delta abs max
            double dm = MathHelper.abs_max(dx, dz);

            if (dm >= 0.000099999776482582D) {
                dm = MathHelper.sqrt_double(dm);
                dx /= dm;
                dz /= dm;
                double d3 = 1.0D / dm;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                dx *= d3;
                dz *= d3;
                dx *= 0.05000000074505806D;
                dz *= 0.05000000074505806D;
                dx *= (1.0F - 0.75F);
                dz *= (1.0F - 0.75F);
                entity.addVelocity(-dx, 0.0D, -dz);
                entityIn.addVelocity(dx, 0.0D, dz);
            }
        }
    }

}
