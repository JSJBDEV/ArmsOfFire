package gd.rf.acro.armsoffire;

import gd.rf.acro.armsoffire.items.ModularGunItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {
    public static KeyBinding reload=new KeyBinding("key.armsoffire.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R,"category.armsoffire.binds");
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ArmsOfFire.BULLET_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new BulletEntityRenderer(entityRenderDispatcher,context.getItemRenderer()));
        KeyBindingHelper.registerKeyBinding(reload);
    }
}
