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
package quests.Q00018_MeetingWithTheGoldenRam;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00018_MeetingWithTheGoldenRam extends Quest
{
	// NPCs
	private static final int DONAL = 31314;
	private static final int DAISY = 31315;
	private static final int ABERCROMBIE = 31555;
	// Items
	private static final int SUPPLY_BOX = 7245;
	
	public Q00018_MeetingWithTheGoldenRam()
	{
		super(18);
		
		registerQuestItems(SUPPLY_BOX);
		addStartNpc(DONAL);
		addTalkId(DONAL, DAISY, ABERCROMBIE);
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
			case "31314-03.htm":
			{
				st.startQuest();
				break;
			}
			case "31315-02.htm":
			{
				st.setCond(2, true);
				giveItems(player, SUPPLY_BOX, 1);
				break;
			}
			case "31555-02.htm":
			{
				takeItems(player, SUPPLY_BOX, 1);
				giveAdena(player, 15000, true);
				addExpAndSp(player, 50000, 0);
				st.exitQuest(false, true);
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
				htmltext = (player.getLevel() < 66) ? "31314-02.htm" : "31314-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case DONAL:
					{
						htmltext = "31314-04.htm";
						break;
					}
					case DAISY:
					{
						if (cond == 1)
						{
							htmltext = "31315-01.htm";
						}
						else if (cond == 2)
						{
							htmltext = "31315-03.htm";
						}
						break;
					}
					case ABERCROMBIE:
					{
						if (cond == 2)
						{
							htmltext = "31555-01.htm";
						}
						break;
					}
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
}