package com.example.testmod.spells.cold;


import com.example.testmod.TestMod;
import com.example.testmod.capabilities.magic.data.PlayerMagicData;
import com.example.testmod.entity.cone_of_cold.ConeOfColdProjectile;
import com.example.testmod.spells.AbstractSpell;
import com.example.testmod.spells.SpellType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ConeOfColdSpell extends AbstractSpell {
    public ConeOfColdSpell() {
        this(1);
    }

    public ConeOfColdSpell(int level) {
        super(SpellType.CONE_OF_COLD_SPELL);
        this.level = level;
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 5;
        this.cooldown = 100;
    }

    @Override
    public void onCastComplete(Level world, Player player, PlayerMagicData playerMagicData) {
        TestMod.LOGGER.debug("ConeOfColdSpell.onCast: {}, {}, {}", (playerMagicData.cone == null), playerMagicData.isCasting(), playerMagicData.getCastDurationRemaining());
        if (playerMagicData.cone != null) {
            playerMagicData.cone.discard();
            playerMagicData.cone = null;
        }
    }

    @Override
    protected void onCast(Level world, Player player, PlayerMagicData playerMagicData) {
        if (playerMagicData.isCasting() && playerMagicData.getCastingSpellId() == this.getID() && playerMagicData.cone != null) {
            playerMagicData.cone.setDealDamageActive();
        } else {
            ConeOfColdProjectile coneOfColdProjectile = new ConeOfColdProjectile(world, player);
            coneOfColdProjectile.setPos(player.position().add(0, player.getEyeHeight() * .7, 0));
            coneOfColdProjectile.setDamage(getSpellPower());
            world.addFreshEntity(coneOfColdProjectile);
            playerMagicData.cone = coneOfColdProjectile;
        }
    }
}
