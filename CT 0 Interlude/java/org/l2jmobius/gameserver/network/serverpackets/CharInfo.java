/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Decoy;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.skill.AbnormalVisualEffect;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

public class CharInfo extends ServerPacket
{
	private final Player _player;
	private int _objId;
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private final int _mAtkSpd;
	private final int _pAtkSpd;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	private int _vehicleId = 0;
	private final boolean _gmSeeInvis;
	
	public CharInfo(Player player, boolean gmSeeInvis)
	{
		_player = player;
		_objId = player.getObjectId();
		if ((_player.getVehicle() != null) && (_player.getInVehiclePosition() != null))
		{
			_x = _player.getInVehiclePosition().getX();
			_y = _player.getInVehiclePosition().getY();
			_z = _player.getInVehiclePosition().getZ();
			_vehicleId = _player.getVehicle().getObjectId();
		}
		else
		{
			_x = _player.getX();
			_y = _player.getY();
			_z = _player.getZ();
		}
		_heading = _player.getHeading();
		_mAtkSpd = _player.getMAtkSpd();
		_pAtkSpd = (int) _player.getPAtkSpd();
		_moveMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = player.isFlying() ? _runSpd : 0;
		_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
		_gmSeeInvis = gmSeeInvis;
	}
	
	public CharInfo(Decoy decoy, boolean gmSeeInvis)
	{
		this(decoy.getActingPlayer(), gmSeeInvis); // init
		_objId = decoy.getObjectId();
		_x = decoy.getX();
		_y = decoy.getY();
		_z = decoy.getZ();
		_heading = decoy.getHeading();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.CHAR_INFO.writeId(this, buffer);
		buffer.writeInt(_x);
		buffer.writeInt(_y);
		buffer.writeInt(_z);
		buffer.writeInt(_vehicleId);
		buffer.writeInt(_objId);
		buffer.writeString(_player.getAppearance().getVisibleName());
		buffer.writeInt(_player.getRace().ordinal());
		buffer.writeInt(_player.getAppearance().isFemale());
		buffer.writeInt(_player.getBaseClass());
		
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_UNDER));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HEAD));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LHAND));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_GLOVES));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CHEST));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEGS));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_FEET));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_CLOAK));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR));
		buffer.writeInt(_player.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR2));
		// c6 new h's
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeInt(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeInt(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeShort(0);
		buffer.writeInt(_player.getPvpFlag());
		buffer.writeInt(_player.getKarma());
		buffer.writeInt(_mAtkSpd);
		buffer.writeInt(_pAtkSpd);
		buffer.writeInt(_player.getPvpFlag());
		buffer.writeInt(_player.getKarma());
		buffer.writeInt(_runSpd);
		buffer.writeInt(_walkSpd);
		buffer.writeInt(_swimRunSpd);
		buffer.writeInt(_swimWalkSpd);
		buffer.writeInt(_flyRunSpd);
		buffer.writeInt(_flyWalkSpd);
		buffer.writeInt(_flyRunSpd);
		buffer.writeInt(_flyWalkSpd);
		buffer.writeDouble(_moveMultiplier);
		buffer.writeDouble(_player.getAttackSpeedMultiplier());
		buffer.writeDouble(_player.getCollisionRadius());
		buffer.writeDouble(_player.getCollisionHeight());
		buffer.writeInt(_player.getAppearance().getHairStyle());
		buffer.writeInt(_player.getAppearance().getHairColor());
		buffer.writeInt(_player.getAppearance().getFace());
		buffer.writeString(_gmSeeInvis ? "Invisible" : _player.getAppearance().getVisibleTitle());
		if (!_player.isCursedWeaponEquipped())
		{
			buffer.writeInt(_player.getClanId());
			buffer.writeInt(_player.getClanCrestId());
			buffer.writeInt(_player.getAllyId());
			buffer.writeInt(_player.getAllyCrestId());
		}
		else
		{
			buffer.writeInt(0);
			buffer.writeInt(0);
			buffer.writeInt(0);
			buffer.writeInt(0);
		}
		// In UserInfo leader rights and siege flags, but here found nothing??
		// Therefore RelationChanged packet with that info is required
		buffer.writeInt(0);
		buffer.writeByte(!_player.isSitting()); // standing = 1 sitting = 0
		buffer.writeByte(_player.isRunning()); // running = 1 walking = 0
		buffer.writeByte(_player.isInCombat());
		buffer.writeByte(!_player.isInOlympiadMode() && _player.isAlikeDead());
		buffer.writeByte(!_gmSeeInvis && _player.isInvisible()); // invisible = 1 visible =0
		buffer.writeByte(_player.getMountType().ordinal()); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		buffer.writeByte(_player.getPrivateStoreType().getId());
		
		buffer.writeShort(_player.getCubics().size());
		for (int cubicId : _player.getCubics().keySet())
		{
			buffer.writeShort(cubicId);
		}
		
		buffer.writeByte(_player.isInPartyMatchRoom());
		buffer.writeInt(_gmSeeInvis ? (_player.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask()) : _player.getAbnormalVisualEffects());
		buffer.writeByte(_player.getRecomLeft());
		buffer.writeShort(_player.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
		buffer.writeInt(_player.getClassId().getId());
		buffer.writeInt(_player.getMaxCp());
		buffer.writeInt((int) _player.getCurrentCp());
		buffer.writeByte(_player.isMounted() ? 0 : _player.getEnchantEffect());
		buffer.writeByte(_player.getTeam().getId());
		buffer.writeInt(_player.getClanCrestLargeId());
		buffer.writeByte(_player.isNoble()); // Symbol on char menu ctrl+I
		buffer.writeByte(_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA)); // Hero Aura
		
		buffer.writeByte(_player.isFishing()); // 1: Fishing Mode (Cannot be undone by setting back to 0)
		buffer.writeInt(_player.getFishX());
		buffer.writeInt(_player.getFishY());
		buffer.writeInt(_player.getFishZ());
		
		buffer.writeInt(_player.getAppearance().getNameColor());
		buffer.writeInt(_heading);
		buffer.writeInt(_player.getPledgeClass());
		buffer.writeInt(_player.getPledgeType());
		buffer.writeInt(_player.getAppearance().getTitleColor());
		buffer.writeInt(_player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_player.getCursedWeaponEquippedId()) : 0);
	}
}
