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

import static org.junit.Assert.*;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.aionemu.gameserver.dataholders.WalkerData;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
import com.aionemu.gameserver.model.templates.zone.Point2D;
import com.aionemu.gameserver.spawnengine.WalkerGroup;
import com.aionemu.gameserver.spawnengine.WalkerGroupShift;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author Rolandas
 */
public class WalkGroupTest {

	static WalkerData walkerData;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File xml = new File("./data/static_data/npc_walker/npc_walker.xml");
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			schema = sf.newSchema(new File("./data/static_data/npc_walker/npc_walker.xsd"));
			JAXBContext jc = JAXBContext.newInstance(WalkerData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			walkerData = (WalkerData) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}
	}

	@Test
	public final void testNpcLinePositioning() {
		for (WalkerTemplate template : walkerData.getTemplates()) {
			if (template.getPool() < 2)
				continue;
			float distance = (1 - template.getPool()) / 2f * WalkerGroupShift.DISTANCE;
			Point2D orig = new Point2D(template.getRouteStep(1).getX(), template.getRouteStep(1).getY());
			Point2D dest = new Point2D(template.getRouteStep(2).getX(), template.getRouteStep(2).getY());

			for (int i = 0; i < template.getPool(); i++, distance += WalkerGroupShift.DISTANCE) {
				Point2D loc = WalkerGroup.getLinePoint(orig, dest, new WalkerGroupShift(distance, 0));
				float realDistance = (float) MathUtil.getDistance(orig, loc);
				if (Math.round(realDistance) != Math.abs(distance)) {
					fail("Distance is not right");
				}
				double b = MathUtil.getDistance(orig, dest);
				double c = MathUtil.getDistance(loc, dest);
				double angle = Math.acos((distance * distance + b * b - c * c) / 2 / realDistance / b) * 57.2957795;
				if (Math.abs(angle - 90) > 0.0305f) {
					fail("Angle is not right");
				}
			}

			if (template.getPool() == 3) {
				int[] rows = new int[] { 2, 1 };
				Point2D[] points = new Point2D[3];
				
				float rowDistances[] = new float[rows.length - 1];
				float coronalDist = 0;
				for (int i = 0; i < rows.length - 1; i++) {
					if (rows[i] % 2 != rows[i + 1] % 2)
						rowDistances[i] = 0.86602540378443864676372317075294f * WalkerGroupShift.DISTANCE;
					else
						rowDistances[i] = WalkerGroupShift.DISTANCE;
					coronalDist -= rowDistances[i];
				}

				orig = new Point2D(template.getRouteStep(1).getX(), template.getRouteStep(1).getY());
				dest = new Point2D(template.getRouteStep(2).getX(), template.getRouteStep(2).getY());
				
				int processed = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = WalkerGroup.getLinePoint(orig, dest, shift);
						points[processed] = loc;
						System.out.println(loc.getX() + "," + loc.getY());
						processed++;
					}
					if (i < rows.length - 1)
						coronalDist += rowDistances[i];
				}
				
				float c = (float) MathUtil.getDistance(points[0], points[1]);
				if (Math.round(c) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 3 members is not right");
				}
				float b = (float) MathUtil.getDistance(points[0], points[2]);
				if (Math.round(b) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 3 members is not right");
				}
				float a = (float) MathUtil.getDistance(points[1], points[2]);
				if (Math.round(a) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 3 members is not right");
				}
				double angle = Math.acos((a * a + b * b - c * c) / 2 / a / b) * 57.2957795;
				if (Math.abs(angle - 60) > 0.0305f) {
					fail("Angle is not right");
				}
			}
			else if (template.getPool() == 4) {
				int[] rows = new int[] { 2, 2 };
				Point2D[] points = new Point2D[4];
				float rowDistances[] = new float[rows.length - 1];
				float coronalDist = -1 * WalkerGroupShift.DISTANCE;
				orig = new Point2D(template.getRouteStep(1).getX(), template.getRouteStep(1).getY());
				dest = new Point2D(template.getRouteStep(2).getX(), template.getRouteStep(2).getY());
				
				int processed = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = WalkerGroup.getLinePoint(orig, dest, shift);
						points[processed] = loc;
						System.out.println(loc.getX() + "," + loc.getY());
						processed++;
					}
					if (i < rows.length - 1)
						coronalDist += WalkerGroupShift.DISTANCE;
				}
				
				float dist = (float) MathUtil.getDistance(points[0], points[1]);
				if (Math.round(dist) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				dist = (float) MathUtil.getDistance(points[0], points[2]);
				if (Math.round(dist) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				dist = (float) MathUtil.getDistance(points[1], points[3]);
				if (Math.round(dist) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				dist = (float) MathUtil.getDistance(points[2], points[3]);
				if (Math.round(dist) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				
				rows = new int[] { 3, 1 };
				coronalDist = 0;
				for (int i = 0; i < rows.length - 1; i++) {
					if (rows[i] % 2 != rows[i + 1] % 2)
						rowDistances[i] = 0.86602540378443864676372317075294f * WalkerGroupShift.DISTANCE;
					else
						rowDistances[i] = WalkerGroupShift.DISTANCE;
					coronalDist -= rowDistances[i];
				}
				
				processed = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = WalkerGroup.getLinePoint(orig, dest, shift);
						points[processed] = loc;
						System.out.println(loc.getX() + "," + loc.getY());
						processed++;
					}
					if (i < rows.length - 1)
						coronalDist += rowDistances[i];
				}
				
				float dist01 = (float) MathUtil.getDistance(points[0], points[1]);
				if (Math.round(dist01) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				float dist12 = (float) MathUtil.getDistance(points[1], points[2]);
				if (Math.round(dist12) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				float dist13 = (float) MathUtil.getDistance(points[1], points[3]);
				if (Math.round(dist13) != WalkerGroupShift.DISTANCE) {
					fail("Distance for 4 members is not right");
				}
				float dist03 = (float) MathUtil.getDistance(points[0], points[3]);
				float dist23 = (float) MathUtil.getDistance(points[2], points[3]);
				
				double angle = Math.acos((dist01 * dist01 + dist13 * dist13 - dist03 * dist03) / 2 / dist01 / dist13) * 57.2957795;
				if (Math.abs(angle - 90) > 0.0305f) {
					fail("Angle is not right for 3+1 group");
				}
				angle = Math.acos((dist12 * dist12 + dist13 * dist13 - dist23 * dist23) / 2 / dist12 / dist13) * 57.2957795;
				if (Math.abs(angle - 90) > 0.0305f) {
					fail("Angle is not right for 3+1 group");
				}
			}
			else if (template.getPool() == 5) {
				int[] rows = new int[] { 2, 2, 1 };
				Point2D[] points = new Point2D[5];
				
				float rowDistances[] = new float[rows.length - 1];
				float coronalDist = 0;
				for (int i = 0; i < rows.length - 1; i++) {
					if (rows[i] % 2 != rows[i + 1] % 2)
						rowDistances[i] = 0.86602540378443864676372317075294f * WalkerGroupShift.DISTANCE;
					else
						rowDistances[i] = WalkerGroupShift.DISTANCE;
					coronalDist -= rowDistances[i];
				}
				
				int processed = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = WalkerGroup.getLinePoint(orig, dest, shift);
						points[processed] = loc;
						System.out.println(loc.getX() + "," + loc.getY());
						processed++;
					}
					if (i < rows.length - 1)
						coronalDist += rowDistances[i];
				}
			}
			else if (template.getPool() == 6) {
				int[] rows = new int[] { 3, 3 };
				Point2D[] points = new Point2D[6];
				
				float rowDistances[] = new float[rows.length - 1];
				float coronalDist = 0;
				for (int i = 0; i < rows.length - 1; i++) {
					if (rows[i] % 2 != rows[i + 1] % 2)
						rowDistances[i] = 0.86602540378443864676372317075294f * WalkerGroupShift.DISTANCE;
					else
						rowDistances[i] = WalkerGroupShift.DISTANCE;
					coronalDist -= rowDistances[i];
				}
				
				int processed = 0;
				for (int i = 0; i < rows.length; i++) {
					float sagittalDist = (1 - rows[i]) / 2f * WalkerGroupShift.DISTANCE;
					for (int j = 0; j < rows[i]; j++, sagittalDist += WalkerGroupShift.DISTANCE) {
						WalkerGroupShift shift = new WalkerGroupShift(sagittalDist, coronalDist);
						Point2D loc = WalkerGroup.getLinePoint(orig, dest, shift);
						points[processed] = loc;
						System.out.println(loc.getX() + "," + loc.getY());
						processed++;
					}
					if (i < rows.length - 1)
						coronalDist += rowDistances[i];
				}
			}
		}
	}

}
