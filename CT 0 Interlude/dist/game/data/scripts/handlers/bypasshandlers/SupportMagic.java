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
package handlers.bypasshandlers;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.enums.CategoryType;
import org.l2jmobius.gameserver.handler.IBypassHandler;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

/**
 * @author Mobius
 */
public class SupportMagic implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"supportmagic"
	};
	
	@Override
	public boolean useBypass(String command, Player player, Creature target)
	{
		if (!target.isNpc() || player.isCursedWeaponEquipped())
		{
			return false;
		}
		
		if (command.equalsIgnoreCase(COMMANDS[0]))
		{
			final int level = player.getLevel();
			final Npc npc = (Npc) target;
			if (!Config.ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE && !player.isNewbie())
			{
				npc.showChatWindow(player, "data/html/default/SupportMagicNovice.htm");
				return false;
			}
			else if (level > Config.MAX_NEWBIE_BUFFS_LEVEL)
			{
				npc.showChatWindow(player, "data/html/default/SupportMagicHighLevel.htm");
				return false;
			}
			else if (level < Config.MIN_NEWBIE_BUFFS_LEVEL)
			{
				npc.showChatWindow(player, "data/html/default/SupportMagicLowLevel.htm");
				return false;
			}
			else if (player.getClassId().level() == 3)
			{
				player.sendMessage("Only adventurers who have not completed their 3rd class transfer may receive these buffs."); // Custom message
				return false;
			}
			
			npc.setTarget(player);
			
			if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
			{
				npc.doCast(SkillData.getInstance().getSkill(4322, 1)); // WindWalk
			}
			if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
			{
				npc.doCast(SkillData.getInstance().getSkill(4323, 1)); // Shield
			}
			if (player.isInCategory(CategoryType.BEGINNER_MAGE))
			{
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4328, 1)); // Bless the Soul
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4329, 1)); // Acumen
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4330, 1)); // Concentration
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4331, 1)); // Empower
				}
			}
			else
			{
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4324, 1)); // Bless the Body
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4325, 1)); // Vampiric Rage
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4326, 1)); // Regeneration
				}
				if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
				{
					npc.doCast(SkillData.getInstance().getSkill(4327, 1)); // Haste
				}
			}
			if ((player.getLevel() >= Config.MIN_NEWBIE_BUFFS_LEVEL) && (player.getLevel() <= Config.MAX_NEWBIE_BUFFS_LEVEL))
			{
				player.doSimultaneousCast(SkillData.getInstance().getSkill(4338, 1)); // Life Cubic
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
