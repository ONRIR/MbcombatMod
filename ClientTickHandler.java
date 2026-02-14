package com.bannerlordcombat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = "bannerlordcombat", value = Dist.CLIENT)
public class ClientTickHandler {
    
    private static boolean wasLeftPressed = false;
    private static boolean wasRightPressed = false;
    private static boolean wasUpPressed = false;
    private static boolean wasThrustPressed = false;
    
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        
        if (player == null) return;
        
        boolean isBlockModifierPressed = ClientInputHandler.blockModifierKey.isDown();
        
        boolean leftPressed = ClientInputHandler.attackLeftKey.isDown();
        boolean rightPressed = ClientInputHandler.attackRightKey.isDown();
        boolean upPressed = ClientInputHandler.attackUpKey.isDown();
        boolean thrustPressed = ClientInputHandler.attackThrustKey.isDown();
        
        if (leftPressed && !wasLeftPressed) {
            if (isBlockModifierPressed) {
                NetworkHandler.sendBlockPacket(AttackDirection.LEFT);
            } else {
                NetworkHandler.sendAttackPacket(AttackDirection.LEFT);
            }
        }
        
        if (rightPressed && !wasRightPressed) {
            if (isBlockModifierPressed) {
                NetworkHandler.sendBlockPacket(AttackDirection.RIGHT);
            } else {
                NetworkHandler.sendAttackPacket(AttackDirection.RIGHT);
            }
        }
        
        if (upPressed && !wasUpPressed) {
            if (isBlockModifierPressed) {
                NetworkHandler.sendBlockPacket(AttackDirection.UP);
            } else {
                NetworkHandler.sendAttackPacket(AttackDirection.UP);
            }
        }
        
        if (thrustPressed && !wasThrustPressed) {
            if (isBlockModifierPressed) {
                NetworkHandler.sendBlockPacket(AttackDirection.THRUST);
            } else {
                NetworkHandler.sendAttackPacket(AttackDirection.THRUST);
            }
        }
        
        if (!isBlockModifierPressed) {
            NetworkHandler.sendStopBlockPacket();
        }
        
        wasLeftPressed = leftPressed;
        wasRightPressed = rightPressed;
        wasUpPressed = upPressed;
        wasThrustPressed = thrustPressed;
    }
}
