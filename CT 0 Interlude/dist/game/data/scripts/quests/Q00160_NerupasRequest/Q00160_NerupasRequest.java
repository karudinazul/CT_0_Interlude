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
package quests.Q00160_NerupasRequest;

import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00160_NerupasRequest extends Quest
{
	// NPCs
	private static final int NERUPA = 30370;
	private static final int UNOREN = 30147;
	private static final int CREAMEES = 30149;
	private static final int JULIA = 30152;
	// Items
	private static final int SILVERY_SPIDERSILK = 1026;
	private static final int UNOREN_RECEIPT = 1027;
	private static final int CREAMEES_TICKET = 1028;
	private static final int NIGHTSHADE_LEAF = 1029;
	// Reward
	private static final int LESSER_HEALING_POTION = 1060;
	
	public Q00160_NerupasRequest()
	{
		super(160);
		registerQuestItems(SILVERY_SPIDERSILK, UNOREN_RECEIPT, CREAMEES_TICKET, NIGHTSHADE_LEAF);
		addStartNpc(NERUPA);
		addTalkId(NERUPA, UNOREN, CREAMEES, JULIA);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		final String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equals("30370-04.htm"))
		{
			st.startQuest();
			giveItems(player, SILVERY_SPIDERSILK, 1);
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
				if (player.getRace() != Race.ELF)
				{
					htmltext = "30370-00.htm";
				}
				else if (player.getLevel() < 3)
				{
					htmltext = "30370-02.htm";
				}
				else
				{
					htmltext = "30370-03.htm";
				}
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case NERUPA:
					{
						if (cond < 4)
						{
							htmltext = "30370-05.htm";
						}
						else if (cond == 4)
						{
							htmltext = "30370-06.htm";
							takeItems(player, NIGHTSHADE_LEAF, 1);
							rewardItems(player, LESSER_HEALING_POTION, 5);
							addExpAndSp(player, 1000, 0);
							st.exitQuest(false, true);
						}
						break;
					}
					case UNOREN:
					{
						if (cond == 1)
						{
							htmltext = "30147-01.htm";
							st.setCond(2, true);
							takeItems(player, SILVERY_SPIDERSILK, 1);
							giveItems(player, UNOREN_RECEIPT, 1);
						}
						else if (cond == 2)
						{
							htmltext = "30147-02.htm";
						}
						else if (cond == 4)
						{
							htmltext = "30147-03.htm";
						}
						break;
					}
					case CREAMEES:
					{
						if (cond == 2)
						{
							htmltext = "30149-01.htm";
							st.setCond(3, true);
							takeItems(player, UNOREN_RECEIPT, 1);
							giveItems(player, CREAMEES_TICKET, 1);
						}
						else if (cond == 3)
						{
							htmltext = "30149-02.htm";
						}
						else if (cond == 4)
						{
							htmltext = "30149-03.htm";
						}
						break;
					}
					case JULIA:
					{
						if (cond == 3)
						{
							htmltext = "30152-01.htm";
							st.setCond(4, true);
							takeItems(player, CREAMEES_TICKET, 1);
							giveItems(player, NIGHTSHADE_LEAF, 1);
						}
						else if (cond == 4)
						{
							htmltext = "30152-02.htm";
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
