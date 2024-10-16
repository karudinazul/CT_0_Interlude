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
package quests.Q00117_TheOceanOfDistantStars;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.util.Util;

public class Q00117_TheOceanOfDistantStars extends Quest
{
	// NPCs
	private static final int ABEY = 32053;
	private static final int GHOST = 32054;
	private static final int ANCIENT_GHOST = 32055;
	private static final int OBI = 32052;
	private static final int BOX = 32076;
	// Monsters
	private static final int BANDIT_WARRIOR = 22023;
	private static final int BANDIT_INSPECTOR = 22024;
	// Items
	private static final int GREY_STAR = 8495;
	private static final int ENGRAVED_HAMMER = 8488;
	
	public Q00117_TheOceanOfDistantStars()
	{
		super(117);
		registerQuestItems(GREY_STAR, ENGRAVED_HAMMER);
		addStartNpc(ABEY);
		addTalkId(ABEY, ANCIENT_GHOST, GHOST, OBI, BOX);
		addKillId(BANDIT_WARRIOR, BANDIT_INSPECTOR);
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
			case "32053-02.htm":
			{
				st.startQuest();
				break;
			}
			case "32055-02.htm":
			{
				st.setCond(2, true);
				break;
			}
			case "32052-02.htm":
			{
				st.setCond(3, true);
				break;
			}
			case "32053-04.htm":
			{
				st.setCond(4, true);
				break;
			}
			case "32076-02.htm":
			{
				st.setCond(5, true);
				giveItems(player, ENGRAVED_HAMMER, 1);
				break;
			}
			case "32053-06.htm":
			{
				st.setCond(6, true);
				break;
			}
			case "32052-04.htm":
			{
				st.setCond(7, true);
				break;
			}
			case "32052-06.htm":
			{
				st.setCond(9, true);
				takeItems(player, GREY_STAR, 1);
				break;
			}
			case "32055-04.htm":
			{
				st.setCond(10, true);
				takeItems(player, ENGRAVED_HAMMER, 1);
				break;
			}
			case "32054-03.htm":
			{
				addExpAndSp(player, 63591, 0);
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
				htmltext = (player.getLevel() < 39) ? "32053-00.htm" : "32053-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case ANCIENT_GHOST:
					{
						if (cond == 1)
						{
							htmltext = "32055-01.htm";
						}
						else if ((cond > 1) && (cond < 9))
						{
							htmltext = "32055-02.htm";
						}
						else if (cond == 9)
						{
							htmltext = "32055-03.htm";
						}
						else if (cond > 9)
						{
							htmltext = "32055-05.htm";
						}
						break;
					}
					case OBI:
					{
						if (cond == 2)
						{
							htmltext = "32052-01.htm";
						}
						else if ((cond > 2) && (cond < 6))
						{
							htmltext = "32052-02.htm";
						}
						else if (cond == 6)
						{
							htmltext = "32052-03.htm";
						}
						else if (cond == 7)
						{
							htmltext = "32052-04.htm";
						}
						else if (cond == 8)
						{
							htmltext = "32052-05.htm";
						}
						else if (cond > 8)
						{
							htmltext = "32052-06.htm";
						}
						break;
					}
					case ABEY:
					{
						if ((cond == 1) || (cond == 2))
						{
							htmltext = "32053-02.htm";
						}
						else if (cond == 3)
						{
							htmltext = "32053-03.htm";
						}
						else if (cond == 4)
						{
							htmltext = "32053-04.htm";
						}
						else if (cond == 5)
						{
							htmltext = "32053-05.htm";
						}
						else if (cond > 5)
						{
							htmltext = "32053-06.htm";
						}
						break;
					}
					case BOX:
					{
						if (cond == 4)
						{
							htmltext = "32076-01.htm";
						}
						else if (cond > 4)
						{
							htmltext = "32076-03.htm";
						}
						break;
					}
					case GHOST:
					{
						if (cond == 10)
						{
							htmltext = "32054-01.htm";
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
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final QuestState qs = getRandomPartyMemberState(killer, 7, 3, npc);
		if ((qs == null) || !Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, true))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		if (giveItemRandomly(killer, npc, GREY_STAR, 1, 1, 0.2, true))
		{
			qs.setCond(8);
		}
		
		return super.onKill(npc, killer, isSummon);
	}
}
