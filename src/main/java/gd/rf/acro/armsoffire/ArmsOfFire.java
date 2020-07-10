package gd.rf.acro.armsoffire;

import com.sun.jna.platform.win32.WinBase;
import gd.rf.acro.armsoffire.items.ModularGunItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;


public class ArmsOfFire implements ModInitializer {
	public static final Identifier RELOAD_GUN = new Identifier("armsoffire","gun_reload");

	public static final ItemGroup TAB = FabricItemGroupBuilder.build(
			new Identifier("armsoffire", "tab"),
			() -> new ItemStack(ArmsOfFire.RIFLE));
	
	public static final EntityType<BulletEntity> BULLET_ENTITY_ENTITY_TYPE =register("bullet",SpawnGroup.MISC,EntityDimensions.changing(0.5f,0.5f),((type, world) -> new BulletEntity(world)));
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerItems();
		System.out.println("Hello Fabric world!");
		ServerSidePacketRegistry.INSTANCE.register(RELOAD_GUN, (packetContext, attachedData) -> {
			boolean shouldReload = attachedData.readBoolean();
			packetContext.getTaskQueue().execute(() -> {
				if(packetContext.getPlayer().getMainHandStack().getItem() instanceof ModularGunItem && shouldReload)
				{
					((ModularGunItem) packetContext.getPlayer().getMainHandStack().getItem()).reload(packetContext.getPlayer(), Hand.MAIN_HAND);
				}

			});
		});
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (LootTables.SIMPLE_DUNGEON_CHEST==id) {
				LootPool poolBuilder = FabricLootPoolBuilder.builder()
						.with(ItemEntry.builder(ArmsOfFire.RED_PERK).weight(1))
						.with(ItemEntry.builder(ArmsOfFire.BLUE_PERK).weight(1))
						.with(ItemEntry.builder(ArmsOfFire.YELLOW_PERK).weight(1))
						.with(ItemEntry.builder(ArmsOfFire.GREEN_PERK).weight(1))
						.with(ItemEntry.builder(Items.AIR).weight(10)).build();
				supplier.withPool(poolBuilder);
			}
		});

	}
	public static final ModularGunItem PISTOL = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(10),new GunBox(new Identifier("fire_charge"),3f));
	public static final ModularGunItem RIFLE = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(6),new GunBox(new Identifier("fire_charge"),10f).setScopeAmount(2).setRangeMod(5f));
	public static final ModularGunItem SNIPER = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(4),new GunBox(new Identifier("fire_charge"),20f).setRangeMod(10f).setScopeAmount(5).setCooldown(60));
	public static final ModularGunItem UZI = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(31),new GunBox(new Identifier("fire_charge"),1f).setCooldown(3));
	public static final ModularGunItem TOMMYGUN = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(51),new GunBox(new Identifier("fire_charge"),2f).setCooldown(4));
	public static final ModularGunItem SHOTGUN = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(9),new GunBox(new Identifier("fire_charge"),8f).setMultipleBullets(3,0.1f));
	public static final ModularGunItem SAWNOFF = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(9),new GunBox(new Identifier("fire_charge"),8f).setMultipleBullets(3,0.5f));
	public static final ModularGunItem COMBAT = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(21),new GunBox(new Identifier("fire_charge"),8f).setReloadTime(30).setMultipleBullets(5,0.2f));
	public static final ModularGunItem GRENADE = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(7),new GunBox(new Identifier("tnt"),10f).setCooldown(40).setExplosionSize(2));
	public static final ModularGunItem ROCKET = new ModularGunItem(new Item.Settings().group(TAB).maxDamage(5),new GunBox(new Identifier("tnt"),10f).setCooldown(60).setReloadTime(80).setExplosionSize(3));

	public static final PerkItem RED_PERK = new PerkItem(new Item.Settings().group(TAB),"last stand");
	public static final PerkItem GREEN_PERK = new PerkItem(new Item.Settings().group(TAB),"martyr");
	public static final PerkItem BLUE_PERK = new PerkItem(new Item.Settings().group(TAB),"calm");
	public static final PerkItem YELLOW_PERK = new PerkItem(new Item.Settings().group(TAB),"lock-on");
	private void registerItems()
	{
		Registry.register(Registry.ITEM,new Identifier("armsoffire","pistol"),PISTOL);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","uzi"),UZI);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","tommygun"),TOMMYGUN);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","shotgun"),SHOTGUN);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","sawnoff_shotgun"),SAWNOFF);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","combat_shotgun"),COMBAT);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","rifle"),RIFLE);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","sniper"),SNIPER);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","grenade_launcher"),GRENADE);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","rocket_launcher"),ROCKET);

		Registry.register(Registry.ITEM,new Identifier("armsoffire","red_perk"),RED_PERK);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","green_perk"),GREEN_PERK);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","blue_perk"),BLUE_PERK);
		Registry.register(Registry.ITEM,new Identifier("armsoffire","yellow_perk"),YELLOW_PERK);

	}



	public static <T extends Entity> EntityType<T> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<T> factory) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier("armsoffire", name), net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder.create(category, factory).size(size).build());
	}
}
