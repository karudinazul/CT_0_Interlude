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
package handlers.admincommandhandlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.l2jmobius.gameserver.handler.IAdminCommandHandler;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.util.BuilderUtil;
import org.l2jmobius.gameserver.util.GMAudit;

public class AdminInstanceZone implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_instancezone",
		"admin_instancezone_clear"
	};
	
	@Override
	public boolean useAdminCommand(String commandValue, Player activeChar)
	{
		final String target = (activeChar.getTarget() != null) ? activeChar.getTarget().getName() : "no-target";
		String command = commandValue;
		GMAudit.auditGMAction(activeChar.getName(), command, target, "");
		if (command.startsWith("admin_instancezone_clear"))
		{
			try
			{
				final StringTokenizer st = new StringTokenizer(command, " ");
				st.nextToken();
				final Player player = World.getInstance().getPlayer(st.nextToken());
				final int instanceId = Integer.parseInt(st.nextToken());
				final String name = InstanceManager.getInstance().getInstanceIdName(instanceId);
				InstanceManager.getInstance().deleteInstanceTime(player.getObjectId(), instanceId);
				BuilderUtil.sendSysMessage(activeChar, "Instance zone " + name + " cleared for player " + player.getName());
				player.sendMessage("Admin cleared instance zone " + name + " for you");
				return true;
			}
			catch (Exception e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Failed clearing instance time: " + e.getMessage());
				BuilderUtil.sendSysMessage(activeChar, "Usage: //instancezone_clear <playername> [instanceId]");
				return false;
			}
		}
		else if (command.startsWith("admin_instancezone"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			command = st.nextToken();
			if (st.hasMoreTokens())
			{
				Player player = null;
				final String playername = st.nextToken();
				
				try
				{
					player = World.getInstance().getPlayer(playername);
				}
				catch (Exception e)
				{
					// Handled bellow.
				}
				
				if (player != null)
				{
					display(player, activeChar);
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "The player " + playername + " is not online");
					BuilderUtil.sendSysMessage(activeChar, "Usage: //instancezone [playername]");
					return false;
				}
			}
			else if (activeChar.getTarget() != null)
			{
				if (activeChar.getTarget().isPlayer())
				{
					display((Player) activeChar.getTarget(), activeChar);
				}
			}
			else
			{
				display(activeChar, activeChar);
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void display(Player player, Player activeChar)
	{
		final Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player.getObjectId());
		final StringBuilder html = new StringBuilder(500 + (instanceTimes.size() * 200));
		html.append("<html><center><table width=260><tr><td width=40><button value=\"Main\" action=\"bypass admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui94\"></td><td width=180><center>Character Instances</center></td><td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui94\"></td></tr></table><br><font color=\"LEVEL\">Instances for " + player.getName() + "</font><center><br><table><tr><td width=150>Name</td><td width=50>Time</td><td width=70>Action</td></tr>");
		
		// for (int id : instanceTimes.keySet())
		for (Entry<Integer, Long> entry : instanceTimes.entrySet())
		{
			int hours = 0;
			int minutes = 0;
			final long remainingTime = (entry.getValue() - System.currentTimeMillis()) / 1000;
			if (remainingTime > 0)
			{
				hours = (int) (remainingTime / 3600);
				minutes = (int) ((remainingTime % 3600) / 60);
			}
			
			html.append("<tr><td>" + InstanceManager.getInstance().getInstanceIdName(entry.getKey()) + "</td><td>" + hours + ":" + minutes + "</td><td><button value=\"Clear\" action=\"bypass -h admin_instancezone_clear " + player.getName() + " " + entry.getKey() + "\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui94\"></td></tr>");
		}
		
		html.append("</table></html>");
		
		final NpcHtmlMessage ms = new NpcHtmlMessage();
		ms.setHtml(html.toString());
		
		activeChar.sendPacket(ms);
	}
}