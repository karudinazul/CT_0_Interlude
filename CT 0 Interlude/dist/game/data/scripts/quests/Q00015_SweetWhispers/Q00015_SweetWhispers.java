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
package quests.Q00015_SweetWhispers;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00015_SweetWhispers extends Quest
{
	// NPCs
	private static final int VLADIMIR = 31302;
	private static final int HIERARCH = 31517;
	private static final int MYSTERIOUS_NECRO = 31518;
	
	public Q00015_SweetWhispers()
	{
		super(15);
		addStartNpc(VLADIMIR);
		addTalkId(VLADIMIR, HIERARCH, MYSTERIOUS_NECRO);
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
			case "31302-01.htm":
			{
				st.startQuest();
				break;
			}
			case "31518-01.htm":
			{
				st.setCond(2, true);
				break;
			}
			case "31517-01.htm":
			{
				addExpAndSp(player, 60217, 0);
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
				htmltext = (player.getLevel() < 60) ? "31302-00a.htm" : "31302-00.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case VLADIMIR:
					{
						htmltext = "31302-01a.htm";
						break;
					}
					case MYSTERIOUS_NECRO:
					{
						if (cond == 1)
						{
							htmltext = "31518-00.htm";
						}
						else if (cond == 2)
						{
							htmltext = "31518-01a.htm";
						}
						break;
					}
					case HIERARCH:
					{
						if (cond == 2)
						{
							htmltext = "31517-00.htm";
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