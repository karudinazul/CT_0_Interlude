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
package quests.Q00165_ShilensHunt;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00165_ShilensHunt extends Quest
{
	// NPCs
	private static final Map<Integer, Integer> MONSTERS = new HashMap<>();
	static
	{
		MONSTERS.put(20456, 3); // Ashen Wolf
		MONSTERS.put(20529, 1); // Young Brown Keltir
		MONSTERS.put(20532, 1); // Brown Keltir
		MONSTERS.put(20536, 2); // Elder Brown Keltir
	}
	// Items
	private static final int DARK_BEZOAR = 1160;
	private static final int LESSER_HEALING_POTION = 1060;
	
	public Q00165_ShilensHunt()
	{
		super(165);
		registerQuestItems(DARK_BEZOAR);
		addStartNpc(30348); // Nelsya
		addTalkId(30348);
		addKillId(MONSTERS.keySet());
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
		
		if (event.equals("30348-03.htm"))
		{
			st.startQuest();
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
				if (player.getRace() != Race.DARK_ELF)
				{
					htmltext = "30348-00.htm";
				}
				else if (player.getLevel() < 3)
				{
					htmltext = "30348-01.htm";
				}
				else
				{
					htmltext = "30348-02.htm";
				}
				break;
			}
			case State.STARTED:
			{
				if (getQuestItemsCount(player, DARK_BEZOAR) >= 13)
				{
					htmltext = "30348-05.htm";
					takeItems(player, DARK_BEZOAR, -1);
					rewardItems(player, LESSER_HEALING_POTION, 5);
					addExpAndSp(player, 1000, 0);
					st.exitQuest(false, true);
				}
				else
				{
					htmltext = "30348-04.htm";
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isCond(1) && (getRandom(3) < MONSTERS.get(npc.getId())))
		{
			giveItems(killer, DARK_BEZOAR, 1);
			if (getQuestItemsCount(killer, DARK_BEZOAR) < 13)
			{
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
			else
			{
				qs.setCond(2, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}
