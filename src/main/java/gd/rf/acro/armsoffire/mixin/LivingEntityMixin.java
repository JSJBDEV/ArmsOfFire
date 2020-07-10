package gd.rf.acro.armsoffire.mixin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import gd.rf.acro.armsoffire.ArmsOfFire;
import gd.rf.acro.armsoffire.items.ModularGunItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin{

    @Inject(method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        LivingEntity livingEntity = ((LivingEntity)(Object)this);
        if(livingEntity.getOffHandStack().getItem()== ArmsOfFire.GREEN_PERK)
        {
            CreeperEntity creeperEntity = new CreeperEntity(EntityType.CREEPER,livingEntity.world);
            creeperEntity.teleport(livingEntity.getX(),livingEntity.getY(),livingEntity.getZ());
            livingEntity.getEntityWorld().spawnEntity(creeperEntity);
            livingEntity.getOffHandStack().decrement(1);
        }
        if(livingEntity.getOffHandStack().getItem()==ArmsOfFire.RED_PERK)
        {
            livingEntity.setHealth(10);
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,9999,2));
            if(livingEntity.getType()==EntityType.PLAYER)
            {
                ModularGunItem.lastStand((PlayerEntity) livingEntity);
            }
            info.setReturnValue(true);
            livingEntity.getOffHandStack().decrement(1);
        }
    }

}
