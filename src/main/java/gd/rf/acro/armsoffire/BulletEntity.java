package gd.rf.acro.armsoffire;

import com.ibm.icu.text.MessagePattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BulletEntity extends SnowballEntity {
    private GunBox gunBox;
    private PlayerEntity attack;


    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world,PlayerEntity attacker,GunBox gunBoxV) {
        super(entityType, world);
        this.attack=attacker;
        this.setOwner(attacker);
        this.gunBox=gunBoxV;
    }
    public BulletEntity(World world)
    {
        super(ArmsOfFire.BULLET_ENTITY_ENTITY_TYPE, world);
    }
    public BulletEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }



    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if(gunBox!=null)
        {
            int extra = 0;
            if(this.attack.getOffHandStack().getItem()==ArmsOfFire.YELLOW_PERK)
            {
                entityHitResult.getEntity().setGlowing(true);
                extra=3;
            }
            entityHitResult.getEntity().damage(DamageSource.mob(this.attack),this.gunBox.getDamage()+extra);
            if(gunBox.getExplosionSize()>0)
            {
                world.createExplosion(this,this.getX(),this.getY(),this.getZ(),gunBox.getExplosionSize(), Explosion.DestructionType.DESTROY);
            }

        }
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if(gunBox!=null)
        {
            if(gunBox.getExplosionSize()>0)
            {
                world.createExplosion(this,this.getX(),this.getY(),this.getZ(),gunBox.getExplosionSize(), Explosion.DestructionType.DESTROY);
            }
        }
    }

    @Override
    protected ItemStack getItem() {
        return new ItemStack(Items.JUKEBOX);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.JUKEBOX;
    }

    public Packet<?> createSpawnPacket() {
        Entity entity = this.attack;
        return new EntitySpawnS2CPacket(this, entity == null ? 0 : entity.getEntityId());
    }
}
