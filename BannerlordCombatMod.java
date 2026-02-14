package com.bannerlordcombat;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod("bannerlordcombat")
public class BannerlordCombatMod {
    
    public BannerlordCombatMod(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(new CombatEventHandler());
        NeoForge.EVENT_BUS.register(new NetworkHandler());
        modEventBus.register(new NetworkHandler());
    }
}
