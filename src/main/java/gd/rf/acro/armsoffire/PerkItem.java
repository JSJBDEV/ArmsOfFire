package gd.rf.acro.armsoffire;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class PerkItem extends Item {
    private String perk;
    public PerkItem(Settings settings,String perkV) {
        super(settings);
        this.perk=perkV;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        switch (this.perk)
        {
            case "martyr":
                tooltip.add(new LiteralText("spawns a live creeper when you die"));
                break;
            case "lock-on":
                tooltip.add(new LiteralText("makes hit targets glow, more damage is done."));
                break;
            case "calm":
                tooltip.add(new LiteralText("weapons with scopes zoom further"));
                tooltip.add(new LiteralText("regeneration whilst zooming"));
                break;
            case "last stand":
                tooltip.add(new LiteralText("on a normal death, revived with 10 health"));
                tooltip.add(new LiteralText("but you can only use a pistol and you are withered"));
                break;
        }
    }
}
