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
package quests.Q00648_AnIceMerchantsDream;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

import quests.Q00115_TheOtherSideOfTruth.Q00115_TheOtherSideOfTruth;

public class Q00648_AnIceMerchantsDream extends Quest
{
	// NPCs
	private static final int RAFFORTY = 32020;
	private static final int ICE_SHELF = 32023;
	// Items
	private static final int SILVER_HEMOCYTE = 8057;
	private static final int SILVER_ICE_CRYSTAL = 8077;
	private static final int BLACK_ICE_CRYSTAL = 8078;
	// Rewards
	private static final Map<String, int[]> REWARDS = new HashMap<>();
	static
	{
		// @formatter:off
		REWARDS.put("a", new int[]{SILVER_ICE_CRYSTAL, 23, 1894}); // Crafted Leather
		REWARDS.put("b", new int[]{SILVER_ICE_CRYSTAL, 6, 1881}); // Coarse Bone Powder
		REWARDS.put("c", new int[]{SILVER_ICE_CRYSTAL, 8, 1880}); // Steel
		REWARDS.put("d", new int[]{BLACK_ICE_CRYSTAL, 1800, 729}); // Scroll: Enchant Weapon (A-Grade)
		REWARDS.put("e", new int[]{BLACK_ICE_CRYSTAL, 240, 730}); // Scroll: Enchant Armor (A-Grade)
		REWARDS.put("f", new int[]{BLACK_ICE_CRYSTAL, 500, 947}); // Scroll: Enchant Weapon (B-Grade)
		REWARDS.put("g", new int[]{BLACK_ICE_CRYSTAL, 80, 948}); // Scroll: Enchant Armor (B-Grade)
	}
	
	public Q00648_AnIceMerchantsDream()
	{
		super(648);
		addStartNpc(RAFFORTY, ICE_SHELF);
		addTalkId(RAFFORTY, ICE_SHELF);
		addKillId(22080,22081,22082,22083,22084,22085,22086,22087,22088,22089,22090,22092,22093,22094,22096,22097,22098);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "32020-04.htm":
			{
				st.startQuest();
				break;
			}
			case "32020-05.htm":
			{
				st.setState(State.STARTED);
				st.setCond(2);
				playSound(player, QuestSound.ITEMSOUND_QUEST_ACCEPT);
				break;
			}
			default:
			{
				int exCond;
				int val;
				if (!event.equals("32020-14.htm") && !event.equals("32020-15.htm"))
				{
					if (event.startsWith("32020-17"))
					{
						
						int[] reward = REWARDS.get(event.substring(8, 9));
						if (getQuestItemsCount(player, reward[0]) >= reward[1])
						{
							takeItems(player, reward[0], reward[1]);
							rewardItems(player, reward[2], 1);
						}
						else
						{
							htmltext = "32020-15a.htm";
						}
					}
					else if (!event.equals("32020-20.htm") && !event.equals("32020-22.htm"))
					{
						if (event.equals("32023-05.htm"))
						{
							if (st.getInt("exCond") == 0)
							{
								st.set("exCond", String.valueOf((getRandom(4) + 1) * 10));
							}
						}
						else if (event.startsWith("32023-06-"))
						{
							exCond = st.getInt("exCond");
							if (exCond > 0)
							{
								htmltext = "32023-06.htm";
								st.set("exCond", String.valueOf(exCond + (event.endsWith("chisel") ? 1 : 2)));
								playSound(player, QuestSound.ITEMSOUND_BROKEN_KEY);
								takeItems(player, 8077, 1);
							}
						}
						else if (event.startsWith("32023-07-"))
						{
							exCond = st.getInt("exCond");
							if (exCond > 0)
							{
								val = exCond / 10;
								if (val == ((exCond - (val * 10)) + (event.endsWith("knife") ? 0 : 2)))
								{
									htmltext = "32023-07.htm";
									playSound(player, QuestSound.ITEMSOUND_ENCHANT_SUCCESS);
									rewardItems(player, 8078, 1);
								}
								else
								{
									htmltext = "32023-08.htm";
									playSound(player, QuestSound.ITEMSOUND_ENCHANT_FAILED);
								}
								
								st.set("exCond", "0");
							}
						}
					}
					else
					{
						st.exitQuest(true, true);
					}
				}
				else
				{
					exCond = getQuestItemsCount(player, 8078);
					val = getQuestItemsCount(player, 8077);
					if ((val + exCond) > 0)
					{
						takeItems(player, 8078, -1);
						takeItems(player, 8077, -1);
						giveAdena(player, (val * 300) + (exCond * 1200), true);
					}
					else
					{
						htmltext = "32020-16a.htm";
					}
				}
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
				if (npc.getId() == RAFFORTY)
				{
					if (player.getLevel() < 53)
					{
						htmltext = "32020-01.htm";
					}
					else
					{
						QuestState st2 = player.getQuestState(Q00115_TheOtherSideOfTruth.class.getSimpleName());
						htmltext = ((st2 != null) && st2.isCompleted()) ? "32020-02.htm" : "32020-03.htm";
					}
				}
				else
				{
					htmltext = "32023-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				if (npc.getId() == RAFFORTY)
				{
					final boolean hasItem = (hasAtLeastOneQuestItem(player, SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL));
					QuestState st2 = player.getQuestState(Q00115_TheOtherSideOfTruth.class.getSimpleName());
					if ((st2 != null) && st2.isCompleted())
					{
						htmltext = (hasItem) ? "32020-11.htm" : "32020-09.htm";
						if (st.isCond(1))
						{
							st.setCond(2, true);
						}
					}
					else
					{
						htmltext = (hasItem) ? "32020-10.htm" : "32020-08.htm";
					}
				}
				else
				{
					if (!hasQuestItems(player, SILVER_ICE_CRYSTAL))
					{
						htmltext = "32023-02.htm";
					}
					else
					{
						if ((st.getInt("exCond") % 10) == 0)
						{
							htmltext = "32023-03.htm";
							st.set("exCond", "0");
						}
						else
						{
							htmltext = "32023-04.htm";
						}
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
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if ((st == null) || !st.isStarted())
		{
			return null;
		}
		final Player partyMember = st.getPlayer();
		
		giveItemRandomly(partyMember, npc, SILVER_ICE_CRYSTAL, 1, 0, 0.2, true);
		
		if (st.isCond(2) && (getRandom(100) < 5))
		{
			giveItemRandomly(partyMember, npc, SILVER_HEMOCYTE, 1, 0, 0.2, true);
		}
		
		return null;
	}
}
