package com.bannerlordcombat;

import net.minecraft.world.entity.LivingEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatStateManager {
    
    private static final Map<UUID, PlayerCombatState> playerStates = new HashMap<>();
    
    public static PlayerCombatState getState(LivingEntity entity) {
        return playerStates.computeIfAbsent(entity.getUUID(), k -> new PlayerCombatState());
    }
    
    public static void removeState(UUID uuid) {
        playerStates.remove(uuid);
    }
    
    public static void tick() {
        playerStates.values().forEach(PlayerCombatState::tick);
    }
    
    public static class PlayerCombatState {
        private AttackDirection currentAttackDirection = AttackDirection.NONE;
        private AttackDirection blockDirection = AttackDirection.NONE;
        private long lastAttackTime = 0;
        private long attackStartTime = 0;
        private long lastBlockTime = 0;
        private boolean isBlocking = false;
        private boolean isRecovering = false;
        private int recoveryTicks = 0;
        private int attackChainCount = 0;
        private long lastChainTime = 0;
        private double lastMovementSpeed = 0;
        
        public void startAttack(AttackDirection direction) {
            long currentTime = System.currentTimeMillis();
            
            if (currentTime - lastChainTime > 1000) {
                attackChainCount = 0;
            }
            
            if (currentTime - lastAttackTime < 300) {
                isRecovering = true;
                recoveryTicks = 15;
                return;
            }
            
            this.currentAttackDirection = direction;
            this.attackStartTime = currentTime;
            this.lastAttackTime = currentTime;
            this.attackChainCount++;
            this.lastChainTime = currentTime;
        }
        
        public void startBlock(AttackDirection direction) {
            this.blockDirection = direction;
            this.isBlocking = true;
            this.lastBlockTime = System.currentTimeMillis();
        }
        
        public void stopBlock() {
            this.isBlocking = false;
            this.blockDirection = AttackDirection.NONE;
        }
        
        public boolean canAttack() {
            return !isRecovering && System.currentTimeMillis() - lastAttackTime >= getAttackCooldown();
        }
        
        public long getAttackCooldown() {
            return Math.max(200, 500 - (attackChainCount * 50));
        }
        
        public AttackDirection getCurrentAttackDirection() {
            return currentAttackDirection;
        }
        
        public AttackDirection getBlockDirection() {
            return blockDirection;
        }
        
        public boolean isBlocking() {
            return isBlocking;
        }
        
        public boolean isRecovering() {
            return isRecovering;
        }
        
        public int getChainCount() {
            return attackChainCount;
        }
        
        public void setMovementSpeed(double speed) {
            this.lastMovementSpeed = speed;
        }
        
        public double getMovementSpeed() {
            return lastMovementSpeed;
        }
        
        public void tick() {
            if (isRecovering) {
                recoveryTicks--;
                if (recoveryTicks <= 0) {
                    isRecovering = false;
                    recoveryTicks = 0;
                }
            }
            
            if (System.currentTimeMillis() - lastAttackTime > 500) {
                currentAttackDirection = AttackDirection.NONE;
            }
        }
        
        public void reset() {
            currentAttackDirection = AttackDirection.NONE;
            blockDirection = AttackDirection.NONE;
            isBlocking = false;
            isRecovering = false;
            recoveryTicks = 0;
            attackChainCount = 0;
        }
    }
}
