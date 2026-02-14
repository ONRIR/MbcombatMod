package com.bannerlordcombat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "bannerlordcombat", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInputHandler {
    
    public static KeyMapping attackLeftKey;
    public static KeyMapping attackRightKey;
    public static KeyMapping attackUpKey;
    public static KeyMapping attackThrustKey;
    public static KeyMapping blockModifierKey;
    
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        attackLeftKey = new KeyMapping(
            "key.bannerlordcombat.attack_left",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT,
            "key.categories.bannerlordcombat"
        );
        
        attackRightKey = new KeyMapping(
            "key.bannerlordcombat.attack_right",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT,
            "key.categories.bannerlordcombat"
        );
        
        attackUpKey = new KeyMapping(
            "key.bannerlordcombat.attack_up",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            "key.categories.bannerlordcombat"
        );
        
        attackThrustKey = new KeyMapping(
            "key.bannerlordcombat.attack_thrust",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_DOWN,
            "key.categories.bannerlordcombat"
        );
        
        blockModifierKey = new KeyMapping(
            "key.bannerlordcombat.block_modifier",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT,
            "key.categories.bannerlordcombat"
        );
        
        event.register(attackLeftKey);
        event.register(attackRightKey);
        event.register(attackUpKey);
        event.register(attackThrustKey);
        event.register(blockModifierKey);
    }
}
