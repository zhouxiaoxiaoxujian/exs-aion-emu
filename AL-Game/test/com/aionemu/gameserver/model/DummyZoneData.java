/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.aionemu.gameserver.dataholders.ZoneData;
import com.aionemu.gameserver.model.templates.zone.AreaType;
import com.aionemu.gameserver.model.templates.zone.Point2D;
import com.aionemu.gameserver.model.templates.zone.Points;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;

/**
 * @author Rolandas
 */
public class DummyZoneData extends ZoneData {

	public static final int DEFAULT_SIZE = 1000;

	public DummyZoneData() {
		super.zoneList = new ArrayList<ZoneTemplate>();
		zoneList.add(new DummyZoneTemplate());
		super.afterUnmarshal(null, null);
	}

	public static class DummyZoneTemplate extends ZoneTemplate {

		public DummyZoneTemplate() {
			super.areaType = AreaType.POLYGON;
			super.zoneType = ZoneClassName.SUB;
			super.mapid = DummyWorldMapData.DEFAULT_MAP;
			super.setXmlName("SANCTUM_GATE_110010000");
			super.points = new DummyPoints();
		}
	}

	public static class DummyPoints extends Points {

		public DummyPoints() {
			super.top = DEFAULT_SIZE;
			super.bottom = -DEFAULT_SIZE;
			super.point = Arrays.asList(new Point2D[] { new Point2D(0, 0), new Point2D(DEFAULT_SIZE, 0),
				new Point2D(DEFAULT_SIZE, DEFAULT_SIZE), new Point2D(0, DEFAULT_SIZE) });
		}
	}
}
