package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.gui.inscription_table.network.ServerboundInscribeSpell;
import io.redspace.ironsspellbooks.gui.inscription_table.network.ServerboundInscriptionTableSelectSpell;
import io.redspace.ironsspellbooks.gui.overlays.network.ServerboundSelectSpell;
import io.redspace.ironsspellbooks.gui.scroll_forge.network.ServerboundScrollForgeSelectSpell;
import io.redspace.ironsspellbooks.network.*;
import io.redspace.ironsspellbooks.network.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(IronsSpellbooks.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ClientboundUpdateCastingState.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundUpdateCastingState::new)
                .encoder(ClientboundUpdateCastingState::toBytes)
                .consumer(ClientboundUpdateCastingState::handle)
                .add();

        net.messageBuilder(ClientboundAddMotionToPlayer.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundAddMotionToPlayer::new)
                .encoder(ClientboundAddMotionToPlayer::toBytes)
                .consumer(ClientboundAddMotionToPlayer::handle)
                .add();

        net.messageBuilder(ClientboundSyncMana.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncMana::new)
                .encoder(ClientboundSyncMana::toBytes)
                .consumer(ClientboundSyncMana::handle)
                .add();

        net.messageBuilder(ClientboundOnClientCast.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundOnClientCast::new)
                .encoder(ClientboundOnClientCast::toBytes)
                .consumer(ClientboundOnClientCast::handle)
                .add();

        net.messageBuilder(ClientboundSyncPlayerData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncPlayerData::new)
                .encoder(ClientboundSyncPlayerData::toBytes)
                .consumer(ClientboundSyncPlayerData::handle)
                .add();

        net.messageBuilder(ClientboundSyncEntityData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncEntityData::new)
                .encoder(ClientboundSyncEntityData::toBytes)
                .consumer(ClientboundSyncEntityData::handle)
                .add();

        net.messageBuilder(ServerboundInscribeSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundInscribeSpell::new)
                .encoder(ServerboundInscribeSpell::toBytes)
                .consumer(ServerboundInscribeSpell::handle)
                .add();

        net.messageBuilder(ClientboundSyncCooldown.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncCooldown::new)
                .encoder(ClientboundSyncCooldown::toBytes)
                .consumer(ClientboundSyncCooldown::handle)
                .add();

        net.messageBuilder(ClientboundSyncCooldowns.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncCooldowns::new)
                .encoder(ClientboundSyncCooldowns::toBytes)
                .consumer(ClientboundSyncCooldowns::handle)
                .add();

        net.messageBuilder(ClientboundSyncRecasts.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncRecasts::new)
                .encoder(ClientboundSyncRecasts::toBytes)
                .consumer(ClientboundSyncRecasts::handle)
                .add();

        net.messageBuilder(ClientBoundSyncRecast.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundSyncRecast::new)
                .encoder(ClientBoundSyncRecast::toBytes)
                .consumer(ClientBoundSyncRecast::handle)
                .add();

        net.messageBuilder(ClientBoundRemoveRecast.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundRemoveRecast::new)
                .encoder(ClientBoundRemoveRecast::toBytes)
                .consumer(ClientBoundRemoveRecast::handle)
                .add();

        net.messageBuilder(ClientboundTeleportParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundTeleportParticles::new)
                .encoder(ClientboundTeleportParticles::toBytes)
                .consumer(ClientboundTeleportParticles::handle)
                .add();

        net.messageBuilder(ClientboundFrostStepParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundFrostStepParticles::new)
                .encoder(ClientboundFrostStepParticles::toBytes)
                .consumer(ClientboundFrostStepParticles::handle)
                .add();

        net.messageBuilder(ServerboundScrollForgeSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundScrollForgeSelectSpell::new)
                .encoder(ServerboundScrollForgeSelectSpell::toBytes)
                .consumer(ServerboundScrollForgeSelectSpell::handle)
                .add();

        net.messageBuilder(ServerboundInscriptionTableSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundInscriptionTableSelectSpell::new)
                .encoder(ServerboundInscriptionTableSelectSpell::toBytes)
                .consumer(ServerboundInscriptionTableSelectSpell::handle)
                .add();

        net.messageBuilder(ServerboundCancelCast.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundCancelCast::new)
                .encoder(ServerboundCancelCast::toBytes)
                .consumer(ServerboundCancelCast::handle)
                .add();

        net.messageBuilder(ServerboundQuickCast.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundQuickCast::new)
                .encoder(ServerboundQuickCast::toBytes)
                .consumerNetworkThread(ServerboundQuickCast::handle)
                .add();

        net.messageBuilder(ClientboundHealParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundHealParticles::new)
                .encoder(ClientboundHealParticles::toBytes)
                .consumer(ClientboundHealParticles::handle)
                .add();

        net.messageBuilder(ClientboundBloodSiphonParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundBloodSiphonParticles::new)
                .encoder(ClientboundBloodSiphonParticles::toBytes)
                .consumer(ClientboundBloodSiphonParticles::handle)
                .add();

        net.messageBuilder(ClientboundRegenCloudParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundRegenCloudParticles::new)
                .encoder(ClientboundRegenCloudParticles::toBytes)
                .consumer(ClientboundRegenCloudParticles::handle)
                .add();

        net.messageBuilder(ClientboundOnCastStarted.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundOnCastStarted::new)
                .encoder(ClientboundOnCastStarted::toBytes)
                .consumer(ClientboundOnCastStarted::handle)
                .add();

        net.messageBuilder(ClientboundOnCastFinished.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundOnCastFinished::new)
                .encoder(ClientboundOnCastFinished::toBytes)
                .consumer(ClientboundOnCastFinished::handle)
                .add();

        net.messageBuilder(ClientboundAborptionParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundAborptionParticles::new)
                .encoder(ClientboundAborptionParticles::toBytes)
                .consumer(ClientboundAborptionParticles::handle)
                .add();

        net.messageBuilder(ClientboundFortifyAreaParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundFortifyAreaParticles::new)
                .encoder(ClientboundFortifyAreaParticles::toBytes)
                .consumer(ClientboundFortifyAreaParticles::handle)
                .add();

        net.messageBuilder(ClientboundSyncTargetingData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncTargetingData::new)
                .encoder(ClientboundSyncTargetingData::toBytes)
                .consumer(ClientboundSyncTargetingData::handle)
                .add();

        net.messageBuilder(ClientboundCastErrorMessage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundCastErrorMessage::new)
                .encoder(ClientboundCastErrorMessage::toBytes)
                .consumer(ClientboundCastErrorMessage::handle)
                .add();

        net.messageBuilder(ClientboundSyncAnimation.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncAnimation::new)
                .encoder(ClientboundSyncAnimation::toBytes)
                .consumerMainThread(ClientboundSyncAnimation::handle)
                .add();

        net.messageBuilder(ClientboundOakskinParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundOakskinParticles::new)
                .encoder(ClientboundOakskinParticles::toBytes)
                .consumerMainThread(ClientboundOakskinParticles::handle)
                .add();

        net.messageBuilder(ClientboundSyncCameraShake.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncCameraShake::new)
                .encoder(ClientboundSyncCameraShake::toBytes)
                .consumerMainThread(ClientboundSyncCameraShake::handle)
                .add();

        net.messageBuilder(ClientboundEquipmentChanged.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundEquipmentChanged::new)
                .encoder(ClientboundEquipmentChanged::toBytes)
                .consumerMainThread(ClientboundEquipmentChanged::handle)
                .add();

        net.messageBuilder(ServerboundLearnSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundLearnSpell::new)
                .encoder(ServerboundLearnSpell::toBytes)
                .consumer(ServerboundLearnSpell::handle)
                .add();

        net.messageBuilder(ServerboundSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundSelectSpell::new)
                .encoder(ServerboundSelectSpell::toBytes)
                .consumer(ServerboundSelectSpell::handle)
                .add();

        net.messageBuilder(ServerboundCast.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundCast::new)
                .encoder(ServerboundCast::toBytes)
                .consumer(ServerboundCast::handle)
                .add();

        net.messageBuilder(ClientboundOpenEldritchScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundOpenEldritchScreen::new)
                .encoder(ClientboundOpenEldritchScreen::toBytes)
                .consumerMainThread(ClientboundOpenEldritchScreen::handle)
                .add();

        net.messageBuilder(ClientboundFieryExplosionParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundFieryExplosionParticles::new)
                .encoder(ClientboundFieryExplosionParticles::toBytes)
                .consumerMainThread(ClientboundFieryExplosionParticles::handle)
                .add();

        net.messageBuilder(ClientboundEntityEvent.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundEntityEvent::new)
                .encoder(ClientboundEntityEvent::toBytes)
                .consumerMainThread(ClientboundEntityEvent::handle)
                .add();

        net.messageBuilder(ClientboundGuidingBoltManagerStartTracking.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundGuidingBoltManagerStartTracking::new)
                .encoder(ClientboundGuidingBoltManagerStartTracking::toBytes)
                .consumerMainThread(ClientboundGuidingBoltManagerStartTracking::handle)
                .add();

        net.messageBuilder(ClientboundGuidingBoltManagerStopTracking.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundGuidingBoltManagerStopTracking::new)
                .encoder(ClientboundGuidingBoltManagerStopTracking::toBytes)
                .consumerMainThread(ClientboundGuidingBoltManagerStopTracking::handle)
                .add();

        net.messageBuilder(ClientboundParticleShockwave.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundParticleShockwave::new)
                .encoder(ClientboundParticleShockwave::toBytes)
                .consumerMainThread(ClientboundParticleShockwave::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer)
            sendToPlayer(message, serverPlayer);
    }
}