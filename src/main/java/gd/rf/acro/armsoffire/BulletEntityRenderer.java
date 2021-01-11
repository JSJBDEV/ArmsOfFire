package gd.rf.acro.armsoffire;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BulletEntityRenderer extends FlyingItemEntityRenderer<BulletEntity> {
    public BulletEntityRenderer(EntityRenderDispatcher dispatcher,ItemRenderer renderer) {
        super(dispatcher,renderer);
    }


}
