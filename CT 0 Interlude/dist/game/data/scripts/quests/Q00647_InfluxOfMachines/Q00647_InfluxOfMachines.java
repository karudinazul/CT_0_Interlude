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
package quests.Q00647_InfluxOfMachines;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00647_InfluxOfMachines extends Quest
{
	// NPC
	private static final int GUTENHAGEN = 32069;
	// Item
	private static final int DESTROYED_GOLEM_SHARD = 8100;
	// Rewards
	private static final int GREAT_SWORD_RECIPE = 4963;
	private static final int KESHANBERK_RECIPE = 4966;
	private static final int KRIS_RECIPE = 4968;
	private static final int HELL_KNIFE_RECIPE = 4969;
	private static final int ARTHRO_NAIL_RECIPE = 4970;
	private static final int DARK_ELVEN_LONG_BOW_RECIPE = 4971;
	private static final int GREAT_AXE_RECIPE = 4972;
	private static final int STAFF_OF_EVIL_SPIRITS_RECIPE = 5004;
	
	public Q00647_InfluxOfMachines()
	{
		super(647);
		registerQuestItems(DESTROYED_GOLEM_SHARD);
		addStartNpc(GUTENHAGEN);
		addTalkId(GUTENHAGEN);
		for (int i = 22052; i < 22079; i++)
		{
			addKillId(i);
		}
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
		
		if (event.equals("32069-02.htm"))
		{
			st.startQuest();
		}
		else if (event.equals("32069-06.htm"))
		{
			takeItems(player, DESTROYED_GOLEM_SHARD, -1);
			final int reward = getRandom(8);
			switch (reward)
			{
				case 0:
				{
					giveItems(player, 4963, 1);
					break;
				}
				case 1:
				{
					giveItems(player, 4966, 1);
					break;
				}
				case 2:
				{
					giveItems(player, 4968, 1);
					break;
				}
				case 3:
				{
					giveItems(player, 4969, 1);
					break;
				}
				case 4:
				{
					giveItems(player, 4970, 1);
					break;
				}
				case 5:
				{
					giveItems(player, 4971, 1);
					break;
				}
				case 6:
				{
					giveItems(player, 4972, 1);
					break;
				}
				case 7:
				{
					giveItems(player, 5004, 1);
					break;
				}
			}
			//giveItems(player, getRandom(4963, 4972), 1);
			st.exitQuest(true, true);
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
				htmltext = (player.getLevel() < 46) ? "32069-03.htm" : "32069-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				if (cond == 1)
				{
					htmltext = "32069-04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "32069-05.htm";
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isPet)
	{
		final QuestState qs = getRandomPartyMemberState(player, 1, 3, npc);
		if (qs == null)
		{
			return null;
		}
		final Player partyMember = qs.getPlayer();
		
		final QuestState st = getQuestState(partyMember, false);
		if (st == null)
		{
			return null;
		}
		
		giveItemRandomly(partyMember, npc, DESTROYED_GOLEM_SHARD, 1, 500, 1, true);
		if (getQuestItemsCount(partyMember, DESTROYED_GOLEM_SHARD) >= 500)
		{
			st.setCond(2, true);
		}		
		return null;
	}
}
