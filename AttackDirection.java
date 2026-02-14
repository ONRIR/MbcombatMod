package com.bannerlordcombat;

public enum AttackDirection {
    LEFT(0),
    RIGHT(1),
    UP(2),
    THRUST(3),
    NONE(4);
    
    private final int id;
    
    AttackDirection(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static AttackDirection fromId(int id) {
        for (AttackDirection dir : values()) {
            if (dir.id == id) return dir;
        }
        return NONE;
    }
    
    public boolean matches(AttackDirection other) {
        return this == other;
    }
}
