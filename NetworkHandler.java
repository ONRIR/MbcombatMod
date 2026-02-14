package com.bannerlordcombat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkHandler {
    
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("bannerlordcombat").versioned("1.0.0");
        
        registrar.playToServer(
            AttackPacket.TYPE,
            AttackPacket.STREAM_CODEC,
            NetworkHandler::handleAttackPacket
        );
        
        registrar.playToServer(
            BlockPacket.TYPE,
            BlockPacket.STREAM_CODEC,
            NetworkHandler::handleBlockPacket
        );
        
        registrar.playToServer(
            StopBlockPacket.TYPE,
            StopBlockPacket.STREAM_CODEC,
            NetworkHandler::handleStopBlockPacket
        );
    }
    
    public static void sendAttackPacket(AttackDirection direction) {
        PacketDistributor.sendToServer(new AttackPacket(direction.getId()));
    }
    
    public static void sendBlockPacket(AttackDirection direction) {
        PacketDistributor.sendToServer(new BlockPacket(direction.getId()));
    }
    
    public static void sendStopBlockPacket() {
        PacketDistributor.sendToServer(new StopBlockPacket());
    }
    
    private static void handleAttackPacket(AttackPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                AttackDirection direction = AttackDirection.fromId(packet.directionId);
                CombatStateManager.PlayerCombatState state = CombatStateManager.getState(player);
                
                if (state.canAttack()) {
                    state.startAttack(direction);
                }
            }
        });
    }
    
    private static void handleBlockPacket(BlockPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                AttackDirection direction = AttackDirection.fromId(packet.directionId);
                CombatStateManager.PlayerCombatState state = CombatStateManager.getState(player);
                state.startBlock(direction);
            }
        });
    }
    
    private static void handleStopBlockPacket(StopBlockPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                CombatStateManager.PlayerCombatState state = CombatStateManager.getState(player);
                state.stopBlock();
            }
        });
    }
    
    public record AttackPacket(int directionId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AttackPacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("bannerlordcombat", "attack"));
        
        public static final StreamCodec<FriendlyByteBuf, AttackPacket> STREAM_CODEC = 
            StreamCodec.of(
                (buf, packet) -> buf.writeInt(packet.directionId),
                buf -> new AttackPacket(buf.readInt())
            );
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    
    public record BlockPacket(int directionId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<BlockPacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("bannerlordcombat", "block"));
        
        public static final StreamCodec<FriendlyByteBuf, BlockPacket> STREAM_CODEC = 
            StreamCodec.of(
                (buf, packet) -> buf.writeInt(packet.directionId),
                buf -> new BlockPacket(buf.readInt())
            );
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    
    public record StopBlockPacket() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<StopBlockPacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("bannerlordcombat", "stopblock"));
        
        public static final StreamCodec<FriendlyByteBuf, StopBlockPacket> STREAM_CODEC = 
            StreamCodec.of(
                (buf, packet) -> {},
                buf -> new StopBlockPacket()
            );
        
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
