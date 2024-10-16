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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

public class SetupGauge extends ServerPacket
{
	public static final int BLUE = 0;
	public static final int RED = 1;
	public static final int CYAN = 2;
	
	@SuppressWarnings("unused")
	private final int _objectId; // Not used on Epilogue.
	private final int _dat1;
	private final int _time;
	private final int _time2;
	
	public SetupGauge(int charObjId, int dat1, int time)
	{
		_objectId = charObjId;
		_dat1 = dat1; // color 0-blue 1-red 2-cyan 3-green
		_time = time;
		_time2 = time;
	}
	
	public SetupGauge(int charObjId, int color, int currentTime, int maxTime)
	{
		_objectId = charObjId;
		_dat1 = color; // color 0-blue 1-red 2-cyan 3-green
		_time = currentTime;
		_time2 = maxTime;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.SETUP_GAUGE.writeId(this, buffer);
		buffer.writeInt(_dat1);
		buffer.writeInt(_time);
		buffer.writeInt(_time2);
	}
}
