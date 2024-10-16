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
package quests.Q00409_PathOfTheElvenOracle;

import org.l2jmobius.gameserver.enums.ClassId;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.serverpackets.SocialAction;

public class Q00409_PathOfTheElvenOracle extends Quest
{
	// NPCs
	private static final int MANUEL = 30293;
	private static final int ALLANA = 30424;
	private static final int PERRIN = 30428;
	// Items
	private static final int CRYSTAL_MEDALLION = 1231;
	private static final int SWINDLER_MONEY = 1232;
	private static final int ALLANA_DIARY = 1233;
	private static final int LIZARD_CAPTAIN_ORDER = 1234;
	private static final int LEAF_OF_ORACLE = 1235;
	private static final int HALF_OF_DIARY = 1236;
	private static final int TAMIL_NECKLACE = 1275;
	
	public Q00409_PathOfTheElvenOracle()
	{
		super(409);
		registerQuestItems(CRYSTAL_MEDALLION, SWINDLER_MONEY, ALLANA_DIARY, LIZARD_CAPTAIN_ORDER, HALF_OF_DIARY, TAMIL_NECKLACE);
		addStartNpc(MANUEL);
		addTalkId(MANUEL, ALLANA, PERRIN);
		addKillId(27032, 27033, 27034, 27035);
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
			case "30293-05.htm":
			{
				st.startQuest();
				giveItems(player, CRYSTAL_MEDALLION, 1);
				break;
			}
			case "spawn_lizards":
			{
				st.setCond(2, true);
				addSpawn(27032, -92319, 154235, -3284, 2000, false, 0);
				addSpawn(27033, -92361, 154190, -3284, 2000, false, 0);
				addSpawn(27034, -92375, 154278, -3278, 2000, false, 0);
				return null;
			}
			case "30428-06.htm":
			{
				addSpawn(27035, -93194, 147587, -2672, 2000, false, 0);
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
				if (player.getClassId() != ClassId.ELVEN_MAGE)
				{
					htmltext = (player.getClassId() == ClassId.ORACLE) ? "30293-02a.htm" : "30293-02.htm";
				}
				else if (player.getLevel() < 19)
				{
					htmltext = "30293-03.htm";
				}
				else if (hasQuestItems(player, LEAF_OF_ORACLE))
				{
					htmltext = "30293-04.htm";
				}
				else
				{
					htmltext = "30293-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case MANUEL:
					{
						if (cond == 1)
						{
							htmltext = "30293-06.htm";
						}
						else if ((cond == 2) || (cond == 3))
						{
							htmltext = "30293-09.htm";
						}
						else if ((cond > 3) && (cond < 7))
						{
							htmltext = "30293-07.htm";
						}
						else if (cond == 7)
						{
							htmltext = "30293-08.htm";
							takeItems(player, ALLANA_DIARY, 1);
							takeItems(player, CRYSTAL_MEDALLION, 1);
							takeItems(player, LIZARD_CAPTAIN_ORDER, 1);
							takeItems(player, SWINDLER_MONEY, 1);
							giveItems(player, LEAF_OF_ORACLE, 1);
							addExpAndSp(player, 3200, 1130);
							player.broadcastPacket(new SocialAction(player.getObjectId(), 3));
							st.exitQuest(true, true);
						}
						break;
					}
					case ALLANA:
					{
						if (cond == 1)
						{
							htmltext = "30424-01.htm";
						}
						else if (cond == 3)
						{
							htmltext = "30424-02.htm";
							st.setCond(4, true);
							giveItems(player, HALF_OF_DIARY, 1);
						}
						else if (cond == 4)
						{
							htmltext = "30424-03.htm";
						}
						else if (cond == 5)
						{
							htmltext = "30424-06.htm";
						}
						else if (cond == 6)
						{
							htmltext = "30424-04.htm";
							st.setCond(7, true);
							takeItems(player, HALF_OF_DIARY, -1);
							giveItems(player, ALLANA_DIARY, 1);
						}
						else if (cond == 7)
						{
							htmltext = "30424-05.htm";
						}
						break;
					}
					case PERRIN:
					{
						if (cond == 4)
						{
							htmltext = "30428-01.htm";
						}
						else if (cond == 5)
						{
							htmltext = "30428-04.htm";
							st.setCond(6, true);
							takeItems(player, TAMIL_NECKLACE, -1);
							giveItems(player, SWINDLER_MONEY, 1);
						}
						else if (cond > 5)
						{
							htmltext = "30428-05.htm";
						}
						break;
					}
				}
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
		
		if (npc.getId() == 27035)
		{
			if (st.isCond(4))
			{
				st.setCond(5, true);
				giveItems(player, TAMIL_NECKLACE, 1);
			}
		}
		else if (st.isCond(2))
		{
			st.setCond(3, true);
			giveItems(player, LIZARD_CAPTAIN_ORDER, 1);
		}
		
		return null;
	}
}
