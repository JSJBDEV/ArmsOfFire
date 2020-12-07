package gd.rf.acro.armsoffire.items;

import gd.rf.acro.armsoffire.ArmsOfFire;
import gd.rf.acro.armsoffire.BulletEntity;
import gd.rf.acro.armsoffire.ClientInit;
import gd.rf.acro.armsoffire.GunBox;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class ModularGunItem extends Item {
    GunBox gunBox;
    public ModularGunItem(Settings settings,GunBox gunBox) {
        super(settings);
        this.gunBox=gunBox;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getStackInHand(hand).getDamage()<user.getStackInHand(hand).getMaxDamage()-1 && user.getStackInHand(hand).getCooldown()==0)
        {
            user.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
            BulletEntity bulletEntity = new BulletEntity(ArmsOfFire.BULLET_ENTITY_ENTITY_TYPE,world,user,this.gunBox);
            bulletEntity.teleport(user.getX(),user.getY()+1,user.getZ());
            bulletEntity.setVelocity(user.getRotationVector().multiply(this.gunBox.getRangeMod()));
            world.spawnEntity(bulletEntity);
            user.getStackInHand(hand).damage(1,user,s->user.sendToolBreakStatus(hand));

            if(this.gunBox.isMultishot())
            {
                for (int i = 1; i < this.gunBox.getBulletsPerShot(); i++) {
                    if(user.getStackInHand(hand).getDamage()<user.getStackInHand(hand).getMaxDamage()-1)
                    {
                        BulletEntity bulletEntityM = new BulletEntity(ArmsOfFire.BULLET_ENTITY_ENTITY_TYPE,world,user,this.gunBox);
                        bulletEntityM.teleport(user.getX(),user.getY()+1,user.getZ());
                        float angle = this.gunBox.getBulletAngle()*i-(this.gunBox.getBulletsPerShot()/2.0f)*this.gunBox.getBulletAngle();
                        bulletEntityM.setVelocity(user.getRotationVector().rotateY(angle).multiply(this.gunBox.getRangeMod()));
                        world.spawnEntity(bulletEntityM);
                        user.getStackInHand(hand).damage(1,user,s->user.sendToolBreakStatus(hand));
                    }
                }
            }
            user.getStackInHand(hand).setCooldown(this.gunBox.getCooldown());

        }
        return super.use(world, user, hand);
    }
    public void reload(PlayerEntity user, Hand hand)
    {
        if(user.getStackInHand(hand).getItem() instanceof ModularGunItem)
        {
            while (user.getStackInHand(hand).getDamage()!=0 && user.inventory.count(Registry.ITEM.get(this.gunBox.getAmmo()))>0)
            {
                removeAmmo(this.gunBox.getAmmo(),user);
                user.getStackInHand(hand).damage(-1,user,s->user.sendToolBreakStatus(hand));
                user.getStackInHand(hand).setCooldown(this.gunBox.getReloadTime());
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(selected && this.gunBox.getScopeAmount()>0 && entity instanceof PlayerEntity && entity.isSneaking())
        {
            PlayerEntity player = (PlayerEntity) entity;
            int extra = 0;
            if(player.getOffHandStack().getItem()==ArmsOfFire.BLUE_PERK)
            {
                extra=5;
               player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,10));
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,10,this.gunBox.getScopeAmount()+extra));
        }
        if(ClientInit.reload.isPressed() && selected)
        {
            entity.playSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE,1,1);
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(true);
            ClientSidePacketRegistry.INSTANCE.sendToServer(ArmsOfFire.RELOAD_GUN,passedData);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText("Clip: "+ (stack.getMaxDamage()-stack.getDamage()-1)+"/"+(stack.getMaxDamage()-1)));
        tooltip.add(new LiteralText("Ammo: "+ Registry.ITEM.get(this.gunBox.getAmmo())));
        tooltip.add(new LiteralText("Damage: "+ this.gunBox.getDamage()));
    }

    private void removeAmmo(Identifier ammo, PlayerEntity playerEntity)
    {
        if(!playerEntity.isCreative())
        {
            for (ItemStack item : playerEntity.inventory.main) {
                if (item.getItem() == Registry.ITEM.get(ammo)) {
                    item.decrement(1);
                    break;
                }
            }
        }
    }

    public static void lastStand(PlayerEntity entity)
    {
        entity.inventory.main.forEach(item->
        {
            if(item.getItem() instanceof ModularGunItem && item.getItem() != ArmsOfFire.PISTOL)
            {
                entity.getItemCooldownManager().set(item.getItem(),1000);
            }
        });
    }
}
