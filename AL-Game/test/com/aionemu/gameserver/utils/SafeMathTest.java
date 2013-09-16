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
package com.aionemu.gameserver.utils;

import org.junit.Test;



/**
 * @author MrPoke
 *
 */
public class SafeMathTest {
	@Test
	public void intMultiTest(){
		try {
			SafeMath.multSafe(1000000, 10000000);
		}
		catch (OverfowException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void longMultiTest(){
		try {
			SafeMath.multSafe(100000000000L, 1000000000000L);
		}
		catch (OverfowException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void intAddTest(){
		try {
			SafeMath.addSafe(Integer.MAX_VALUE-10, 11);
		}
		catch (OverfowException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void longAddTest(){
		try {
			SafeMath.addSafe(Long.MAX_VALUE-10, 11);
		}
		catch (OverfowException e) {
			System.out.println(e.getMessage());
		}
	}
}
