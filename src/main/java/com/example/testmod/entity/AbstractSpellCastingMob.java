package com.example.testmod.entity;

import com.example.testmod.TestMod;
import com.example.testmod.capabilities.magic.PlayerMagicData;
import com.example.testmod.spells.AbstractSpell;
import com.example.testmod.spells.CastType;
import com.example.testmod.spells.SpellType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumMap;

import static com.example.testmod.entity.SpellCastSyncedData.SPELL_SYNCED_DATA;

public abstract class AbstractSpellCastingMob extends PathfinderMob {
    //TODO: probably need a way to control the spell level dynamically.
    // I'm not going to add this until we have an idea of what we want

//    private static final EntityDataAccessor<Integer> DATA_CASTING_SPELL_ID = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Integer> DATA_CASTING_SPELL_LEVEL = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Optional<BlockPos>> DATA_CASTING_TELEPORT_LOC = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<SpellCastSyncedData> DATA_CASTING = SynchedEntityData.defineId(AbstractSpellCastingMob.class, SPELL_SYNCED_DATA);

    private final EnumMap<SpellType, AbstractSpell> spells = new EnumMap<>(SpellType.class);
    private final PlayerMagicData playerMagicData = new PlayerMagicData();
    private AbstractSpell castingSpell;
    private SpellCastSyncedData emptySyncedData = new SpellCastSyncedData();

    protected AbstractSpellCastingMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CASTING, emptySyncedData);
    }


    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);

        if (level.isClientSide && pKey == DATA_CASTING) {
            var castingData = entityData.get(DATA_CASTING);

            if (castingData == null || castingData.spellId == 0) {
                return;
            }

            TestMod.LOGGER.debug("ASCM.onSyncedDataUpdated {}", castingData);

            var spellType = SpellType.getTypeFromValue(castingData.spellId);


            if (castingData.usePosition) {
                playerMagicData.setTeleportTargetPosition(new Vec3(castingData.x, castingData.y, castingData.z));
            }

            castSpell(spellType, castingData.spellLevel);

        }
    }

    private void castComplete() {
        TestMod.LOGGER.debug("ASCM.castComplete isClientSide:{}", level.isClientSide);
        if (!level.isClientSide) {
            castingSpell.onCastComplete(level, this, playerMagicData);
        }

        playerMagicData.resetCastingState();
        castingSpell = null;

        if (!level.isClientSide) {
            entityData.set(DATA_CASTING, emptySyncedData);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level.isClientSide || castingSpell == null) {
            return;
        }

        TestMod.LOGGER.debug("aiStep: {}, level:{}, duration:{}", castingSpell.getSpellType(), castingSpell.getLevel(), playerMagicData.getCastDurationRemaining());

        if (playerMagicData.getCastDurationRemaining() <= 0) {
            if (castingSpell.getCastType() == CastType.INSTANT) {
                castingSpell.onClientPreCast(level, this, InteractionHand.MAIN_HAND, playerMagicData);
                castComplete();
            }
        } else { //Actively casting a long cast or continuous cast
            if (castingSpell.getSpellType() == SpellType.FIREBALL_SPELL) {
                //TODO: this needs to be handled by abstract spell in some way with an onClientCastTick event
                addClientSideParticles();
            }
        }

        //TODO:  playerMagicData.handleCastDuration(); <-- This may need to be called here?
    }

    @Override
    protected void customServerAiStep() {

        super.customServerAiStep();

        if (castingSpell == null || entityData.isDirty()) {
            return;
        }

        playerMagicData.handleCastDuration();

        if (playerMagicData.getCastDurationRemaining() <= 0) {
            if (castingSpell.getCastType() == CastType.LONG || castingSpell.getCastType() == CastType.INSTANT) {
                forceLookAtTarget(getTarget());
                castingSpell.onCast(level, this, playerMagicData);
            }
            castComplete();
        } else if (castingSpell.getCastType() == CastType.CONTINUOUS) {
            if ((playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                forceLookAtTarget(getTarget());
                castingSpell.onCast(level, this, playerMagicData);
            }
        }
    }

    public void castSpell(SpellType spellType, int spellLevel) {
        TestMod.LOGGER.debug("ASCM.castSpell spellType:{} spellLevel:{} isClient:{}", spellType, spellLevel, level.isClientSide);
        setCastingSpell(spellType, spellLevel);
        startCasting();
    }

    private void setCastingSpell(SpellType spellType, int spellLevel) {
        TestMod.LOGGER.debug("ASCM.setCastingSpell:spellType:{} spellLevel:{} isClient:{}}", spellType, spellLevel, level.isClientSide);
        if (spellType == SpellType.NONE_SPELL) {
            castingSpell = null;
        } else {
            castingSpell = spells.computeIfAbsent(spellType, key -> AbstractSpell.getSpell(spellType, spellLevel));
        }
    }

    private void startCasting() {
        if (!level.isClientSide) {

            var data = new SpellCastSyncedData();
            data.spellId = castingSpell.getID();
            data.spellLevel = castingSpell.getLevel();
            if (playerMagicData.getTeleportTargetPosition() != null) {
                data.usePosition = true;
                data.x = (int) playerMagicData.getTeleportTargetPosition().x;
                data.y = (int) playerMagicData.getTeleportTargetPosition().y;
                data.z = (int) playerMagicData.getTeleportTargetPosition().z;
            }

            entityData.set(DATA_CASTING, data);
        }

        playerMagicData.initiateCast(castingSpell.getID(), castingSpell.getLevel(), castingSpell.getCastTime());

        //TODO: this may be in the wrong spot.. i don't think this works for all cast types here
        if (!level.isClientSide) {
            castingSpell.onServerPreCast(level, this, playerMagicData);
        }
    }

    public boolean isCasting() {
        return playerMagicData != null && playerMagicData.isCasting();
    }

    public void setTeleportLocationBehindTarget(int distance) {
        var target = getTarget();
        if (target != null) {
            var rotation = target.getLookAngle().normalize().scale(-distance);
            var pos = target.position();
            playerMagicData.setTeleportTargetPosition(rotation.add(pos));
        }
    }

    public boolean isValidTarget(@Nullable LivingEntity livingEntity) {
        if (livingEntity != null && livingEntity.isAlive() && livingEntity instanceof Player) {
            return true;
        }
        return false;
    }

    private void forceLookAtTarget(LivingEntity target) {
        lookAt(target, 180, 180);
    }

    private void addClientSideParticles() {
        double d0 = .4d;
        double d1 = .3d;
        double d2 = .35d;
        float f = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, d0, d1, d2);
        this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double) f2 * 0.6D, d0, d1, d2);

    }
}
