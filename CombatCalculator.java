package com.bannerlordcombat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;

public class CombatCalculator {
    
    public static float calculateDamage(LivingEntity attacker, LivingEntity target, 
                                       AttackDirection attackDir, float baseDamage) {
        CombatStateManager.PlayerCombatState attackerState = CombatStateManager.getState(attacker);
        CombatStateManager.PlayerCombatState defenderState = CombatStateManager.getState(target);
        
        float finalDamage = baseDamage;
        
        Vec3 velocity = attacker.getDeltaMovement();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        attackerState.setMovementSpeed(speed);
        
        if (attacker.isPassenger()) {
            finalDamage = calculateMountedDamage(attacker, target, attackDir, baseDamage, speed);
        }
        
        if (defenderState.isBlocking()) {
            boolean hasShield = isHoldingShield(target);
            
            if (hasShield) {
                if (defenderState.getBlockDirection().matches(attackDir) || 
                    isAdjacentDirection(defenderState.getBlockDirection(), attackDir)) {
                    return 0;
                } else {
                    return finalDamage * 0.5f;
                }
            } else {
                if (defenderState.getBlockDirection().matches(attackDir)) {
                    long blockTime = System.currentTimeMillis() - defenderState.lastBlockTime;
                    if (blockTime < 200) {
                        return 0;
                    }
                }
                return finalDamage;
            }
        }
        
        int chainBonus = Math.min(attackerState.getChainCount() - 1, 3);
        finalDamage *= (1.0f + (chainBonus * 0.1f));
        
        return finalDamage;
    }
    
    private static float calculateMountedDamage(LivingEntity attacker, LivingEntity target,
                                                AttackDirection attackDir, float baseDamage, double speed) {
        float speedMultiplier = (float)(1.0 + Math.min(speed * 2.0, 2.0));
        
        Vec3 attackerPos = attacker.position();
        Vec3 targetPos = target.position();
        Vec3 toTarget = targetPos.subtract(attackerPos).normalize();
        Vec3 velocity = attacker.getDeltaMovement().normalize();
        
        double angle = toTarget.dot(velocity);
        
        if (attackDir == AttackDirection.THRUST) {
            if (angle > 0.7) {
                return baseDamage * speedMultiplier * 1.5f;
            } else {
                return baseDamage * speedMultiplier * 0.8f;
            }
        } else {
            double distance = attackerPos.distanceTo(targetPos);
            if (distance < 1.5 && speed > 0.3) {
                return baseDamage * speedMultiplier * 0.6f;
            } else {
                return baseDamage * speedMultiplier;
            }
        }
    }
    
    private static boolean isHoldingShield(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        ItemStack offHand = entity.getOffhandItem();
        return mainHand.getItem() instanceof ShieldItem || offHand.getItem() instanceof ShieldItem;
    }
    
    private static boolean isAdjacentDirection(AttackDirection block, AttackDirection attack) {
        if (block == AttackDirection.LEFT && attack == AttackDirection.UP) return true;
        if (block == AttackDirection.RIGHT && attack == AttackDirection.UP) return true;
        if (block == AttackDirection.UP && attack == AttackDirection.LEFT) return true;
        if (block == AttackDirection.UP && attack == AttackDirection.RIGHT) return true;
        return false;
    }
}
