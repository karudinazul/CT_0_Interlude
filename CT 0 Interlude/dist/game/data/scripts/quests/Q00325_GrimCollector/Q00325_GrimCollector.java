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
package quests.Q00325_GrimCollector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00325_GrimCollector extends Quest
{
	// NPCs
	private static final int CURTIS = 30336;
	private static final int VARSAK = 30342;
	private static final int SAMED = 30434;
	// Items
	private static final int ANATOMY_DIAGRAM = 1349;
	private static final int ZOMBIE_HEAD = 1350;
	private static final int ZOMBIE_HEART = 1351;
	private static final int ZOMBIE_LIVER = 1352;
	private static final int SKULL = 1353;
	private static final int RIB_BONE = 1354;
	private static final int SPINE = 1355;
	private static final int ARM_BONE = 1356;
	private static final int THIGH_BONE = 1357;
	private static final int COMPLETE_SKELETON = 1358;
	// Mobs
	private static final int TRACKER_SKELETON = 20035;
	private static final int TRACKER_SKELETON_LEADER = 20042;
	private static final int SKELETON_SCOUT = 20045;
	private static final int SKELETON_BOWMAN = 20051;
	private static final int SHIELD_SKELETON = 20514;
	private static final int SKELETON_INFANTRYMAN = 20515;
	private static final int RUIN_ZOMBIE = 20026;
	private static final int RUIN_ZOMBIE_LEADER = 20029;
	private static final int ZOMBIE_SOLDIER = 20457;
	private static final int ZOMBIE_WARRIOR = 20458;
	
	public Q00325_GrimCollector()
	{
		super(325);
		registerQuestItems(ZOMBIE_HEAD, ZOMBIE_HEART, ZOMBIE_LIVER, SKULL, RIB_BONE, SPINE, ARM_BONE, THIGH_BONE, COMPLETE_SKELETON, ANATOMY_DIAGRAM);
		addStartNpc(CURTIS);
		addTalkId(CURTIS, VARSAK, SAMED);
		addKillId(TRACKER_SKELETON, TRACKER_SKELETON_LEADER, SKELETON_SCOUT, SKELETON_BOWMAN, SHIELD_SKELETON, SKELETON_INFANTRYMAN, RUIN_ZOMBIE, RUIN_ZOMBIE_LEADER, ZOMBIE_SOLDIER, ZOMBIE_WARRIOR);
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
			case "30336-03.htm":
			{
				st.startQuest();
				break;
			}
			case "30434-03.htm":
			{
				playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
				giveItems(player, ANATOMY_DIAGRAM, 1);
				break;
			}
			case "30434-06.htm":
			{
				takeItems(player, ANATOMY_DIAGRAM, -1);
				payback(player);
				st.exitQuest(true, true);
				break;
			}
			case "30434-07.htm":
			{
				payback(player);
				break;
			}
			case "30434-09.htm":
			{
				final int skeletons = getQuestItemsCount(player, COMPLETE_SKELETON);
				if (skeletons > 0)
				{
					playSound(player, QuestSound.ITEMSOUND_QUEST_MIDDLE);
					takeItems(player, COMPLETE_SKELETON, -1);
					giveAdena(player, 543 + (341 * skeletons), true);
				}
				break;
			}
			case "30342-03.htm":
			{
				if (!hasQuestItems(player, SPINE, ARM_BONE, SKULL, RIB_BONE, THIGH_BONE))
				{
					htmltext = "30342-02.htm";
				}
				else
				{
					takeItems(player, SPINE, 1);
					takeItems(player, SKULL, 1);
					takeItems(player, ARM_BONE, 1);
					takeItems(player, RIB_BONE, 1);
					takeItems(player, THIGH_BONE, 1);
					
					if (getRandom(10) < 9)
					{
						giveItems(player, COMPLETE_SKELETON, 1);
					}
					else
					{
						htmltext = "30342-04.htm";
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
				htmltext = (player.getLevel() < 15) ? "30336-01.htm" : "30336-02.htm";
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case CURTIS:
					{
						htmltext = (!hasQuestItems(player, ANATOMY_DIAGRAM)) ? "30336-04.htm" : "30336-05.htm";
						break;
					}
					case SAMED:
					{
						if (!hasQuestItems(player, ANATOMY_DIAGRAM))
						{
							htmltext = "30434-01.htm";
						}
						else
						{
							if (getNumberOfPieces(player) == 0)
							{
								htmltext = "30434-04.htm";
							}
							else
							{
								htmltext = !hasQuestItems(player, COMPLETE_SKELETON) ? "30434-05.htm" : "30434-08.htm";
							}
						}
						break;
					}
					case VARSAK:
					{
						htmltext = "30342-01.htm";
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
		
		if (hasQuestItems(player, ANATOMY_DIAGRAM))
		{			
			switch (npc.getId())
			{
				case TRACKER_SKELETON:
				case TRACKER_SKELETON_LEADER:
				case SKELETON_SCOUT:
				case SKELETON_BOWMAN:
				case SHIELD_SKELETON:
				case SKELETON_INFANTRYMAN:
					final int skeletonDrop = getRandom(5);
					if (skeletonDrop == 0)
					{
						giveItemRandomly(player, npc, SKULL, 1, 0, 1, true);
					}
					else if (skeletonDrop == 1)
					{
						giveItemRandomly(player, npc, RIB_BONE, 1, 0, 1, true);
					}
					else if (skeletonDrop == 2)
					{
						giveItemRandomly(player, npc, ARM_BONE, 1, 0, 1, true);
					}
					else if (skeletonDrop == 3)
					{
						giveItemRandomly(player, npc, SPINE, 1, 0, 1, true);
					}
					else
					{
						giveItemRandomly(player, npc, THIGH_BONE, 1, 0, 1, true);
					}
					break;
				case RUIN_ZOMBIE:
				case RUIN_ZOMBIE_LEADER:
				case ZOMBIE_SOLDIER:
				case ZOMBIE_WARRIOR:
					final int zombieDrop = getRandom(3);
					if (zombieDrop == 0)
					{
						giveItemRandomly(player, npc, ZOMBIE_HEAD, 1, 0, 1, true);
					}
					else if (zombieDrop == 1)
					{
						giveItemRandomly(player, npc, ZOMBIE_HEART, 1, 0, 1, true);
					}
					else
					{
						giveItemRandomly(player, npc, ZOMBIE_LIVER, 1, 0, 1, true);
					}
					break;
			}
		}
		
		return null;
	}	
			
	private static int getNumberOfPieces(Player player)
	{
		return getQuestItemsCount(player, ZOMBIE_HEAD) + getQuestItemsCount(player, SPINE) + getQuestItemsCount(player, ARM_BONE) + getQuestItemsCount(player, ZOMBIE_HEART) + getQuestItemsCount(player, ZOMBIE_LIVER) + getQuestItemsCount(player, SKULL) + getQuestItemsCount(player, RIB_BONE) + getQuestItemsCount(player, THIGH_BONE) + getQuestItemsCount(player, COMPLETE_SKELETON);
	}
	
	private void payback(Player player)
	{
		final int count = getNumberOfPieces(player);
		if (count > 0)
		{
			int reward = (30 * getQuestItemsCount(player, ZOMBIE_HEAD)) + (20 * getQuestItemsCount(player, ZOMBIE_HEART)) + (20 * getQuestItemsCount(player, ZOMBIE_LIVER)) + (100 * getQuestItemsCount(player, SKULL)) + (40 * getQuestItemsCount(player, RIB_BONE)) + (14 * getQuestItemsCount(player, SPINE)) + (14 * getQuestItemsCount(player, ARM_BONE)) + (14 * getQuestItemsCount(player, THIGH_BONE)) + (341 * getQuestItemsCount(player, COMPLETE_SKELETON));
			if (count > 10)
			{
				reward += 1629;
			}
			
			if (hasQuestItems(player, COMPLETE_SKELETON))
			{
				reward += 543;
			}
			
			takeItems(player, ZOMBIE_HEAD, -1);
			takeItems(player, ZOMBIE_HEART, -1);
			takeItems(player, ZOMBIE_LIVER, -1);
			takeItems(player, SKULL, -1);
			takeItems(player, RIB_BONE, -1);
			takeItems(player, SPINE, -1);
			takeItems(player, ARM_BONE, -1);
			takeItems(player, THIGH_BONE, -1);
			takeItems(player, COMPLETE_SKELETON, -1);
			
			giveAdena(player, reward, true);
		}
	}
}
