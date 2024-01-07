package io.redspace.ironsspellbooks.api.item.weapons;

import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MagicSwordItem extends ExtendedSwordItem implements IPresetSpellContainer {

    List<SpellData> spellData = null;
    SpellDataRegistryHolder[] spellDataRegistryHolders;

    public MagicSwordItem(Tier tier, double attackDamage, double attackSpeed, SpellDataRegistryHolder[] spellDataRegistryHolders, Map<Attribute, AttributeModifier> additionalAttributes, Properties properties) {
        super(tier, attackDamage, attackSpeed, additionalAttributes, properties);
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public List<SpellData> getSpells() {
        if (spellData == null) {
            spellData = Arrays.stream(spellDataRegistryHolders).map(SpellDataRegistryHolder::getSpellData).toList();
            spellDataRegistryHolders = null;
        }
        return spellData;
    }


    @Override
    public ISpellContainer initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return new SpellContainer();
        }

        if (ISpellContainer.isSpellContainer(itemStack)) {
            return ISpellContainer.get(itemStack);
        } else {
            var spellContainer = ISpellContainer.create(spellDataRegistryHolders.length, true, false);
            getSpells().forEach(spellData -> spellContainer.addSpell(spellData.getSpell(), spellData.getLevel(), true, null));
            spellContainer.save(itemStack);
            return spellContainer;
        }
    }
}
