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
package quests.Q00659_IdRatherBeCollectingFairyBreath;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00659_IdRatherBeCollectingFairyBreath extends Quest
{
	// NPCs
	private static final int GALATEA = 30634;
	// Monsters
	private static final int SOBBING_WIND = 21023;
	private static final int BABBLING_WIND = 21024;
	private static final int GIGGLING_WIND = 21025;
	// Item
	private static final int FAIRY_BREATH = 8286;
	
	public Q00659_IdRatherBeCollectingFairyBreath()
	{
		super(659);
		registerQuestItems(FAIRY_BREATH);
		addStartNpc(GALATEA);
		addTalkId(GALATEA);
		addKillId(GIGGLING_WIND, BABBLING_WIND, SOBBING_WIND);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "30634-03.htm":
			{
				st.startQuest();
				break;
			}
			case "30634-06.htm":
			{
				final int count = getQuestItemsCount(player, FAIRY_BREATH);
				if (count > 0)
				{
					takeItems(player, FAIRY_BREATH, count);
					if (count < 10)
					{
						giveAdena(player, count * 50, true);
					}
					else
					{
						giveAdena(player, (count * 50) + 5365, true);
					}
				}
				break;
			}
			case "30634-08.htm":
			{
				st.exitQuest(true);
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() < 26) ? "30634-01.htm" : "30634-02.htm";
				break;
			}
			case State.STARTED:
			{
				htmltext = (!hasQuestItems(player, FAIRY_BREATH)) ? "30634-04.htm" : "30634-05.htm";
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isPet)
	{
		final QuestState st = getQuestState(player, false);
		if ((st == null) || !st.isStarted())
		{
			return null;
		}
		
		if (getRandom(10) < 9)
		{
			giveItems(player, FAIRY_BREATH, 1);
			playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
		}
		
		return null;
	}
}
