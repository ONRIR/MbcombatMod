package com.bannerlordcombat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class CombatEventHandler {
    
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        CombatStateManager.tick();
    }
    
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        CombatStateManager.removeState(event.getEntity().getUUID());
    }
    
    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        
        if (source.getEntity() instanceof LivingEntity attacker) {
            LivingEntity target = event.getEntity();
            
            CombatStateManager.PlayerCombatState attackerState = CombatStateManager.getState(attacker);
            
            if (attackerState.isRecovering()) {
                event.setCanceled(true);
                return;
            }
            
            AttackDirection attackDir = attackerState.getCurrentAttackDirection();
            
            if (attackDir == AttackDirection.NONE && attacker instanceof Player) {
                event.setCanceled(true);
                return;
            }
        }
    }
    
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        
        if (source.getEntity() instanceof LivingEntity attacker) {
            LivingEntity target = event.getEntity();
            
            CombatStateManager.PlayerCombatState attackerState = CombatStateManager.getState(attacker);
            AttackDirection attackDir = attackerState.getCurrentAttackDirection();
            
            if (attackDir == AttackDirection.NONE && attacker instanceof Player) {
                event.setNewDamage(0);
                return;
            }
            
            float baseDamage = event.getOriginalDamage();
            float calculatedDamage = CombatCalculator.calculateDamage(attacker, target, attackDir, baseDamage);
            
            event.setNewDamage(calculatedDamage);
        }
    }
}
