/*
 * This file is part of aion-gates <aion-gates.fr>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.world;

public enum WorldMapType {
	// Asmodea
	PANDAEMONIUM(120010000),
	MARCHUTAN(120020000),
	ISHALGEN(220010000),
	MORHEIM(220020000),
	ALTGARD(220030000),
	BELUSLAN(220040000),
	BRUSTHONIN(220050000),

	// Elysia
	SANCTUM(110010000),
	KAISINEL(110020000),
	POETA(210010000),
	ELTNEN(210020000),
	VERTERON(210030000),
	HEIRON(210040000),
	THEOBOMOS(210060000),

	// Balaurea
	INGGISON(210050000),
	GELKMAROS(220070000),
	SILENTERA_CANYON(600010000),

	// Prison
	DE_PRISON(510010000), // For ELYOS
	DF_PRISON(520010000), // For ASMODIANS

	RESHANTA(400010000),

	// Instances
	NO_ZONE_NAME(300010000),
	ID_TEST_DUNGEON(300020000),
	NOCHSANA_TRAINING_CAMP(300030000),
	DARK_POETA(300040000),
	ASTERIA_CHAMBER(300050000),
	SULFUR_TREE_NEST(300060000),
	CHAMBER_OF_ROAH(300070000),
	LEFT_WING_CHAMBER(300080000),
	RIGHT_WING_CHAMBER(300090000),
	STEEL_RAKE(300100000),
	DREDGION(300110000),
	KYSIS_CHAMBER(300120000),
	MIREN_CHAMBER(300130000),
	KROTAN_CHAMBER(300140000),
	UDAS_TEMPLE(300150000),
	UDAS_TEMPLE_LOWER(300160000),
	BESHMUNDIR_TEMPLE(300170000),
	TALOCS_HOLLOW(300190000),
	HARAMEL(300200000),
	DREDGION_OF_CHANTRA(300210000),
	CORE(300220000),
	CROMEDE(300230000),
	KARAMATIS(310010000),
	KARAMATIS_B(310020000),
	AERDINA(310030000),
	GERANAIA(310040000),
	AETHEROGENETICS_LAB(310050000),
	FRAGMENT_OF_DARKNESS(310060000),
	IDLF1B_STIGMA(310070000),
	SANCTUM_UNDERGROUND_ARENA(310080000),
	TRINIEL_UNDERGROUND_ARENA(320090000),
	INDRATU_FORTRESS(310090000),
	AZOTURAN_FORTRESS(310100000),
	THEOBOMOS_LAB(310110000),
	IDAB_PRO_L3(310120000),
	ATAXIAR(320010000),
	ATAXIAR_B(320020000),
	BREGIRUN(320030000),
	NIDALBER(320040000),
	ARKANIS_TEMPLE(320050000),
	SPACE_OF_OBLIVION(320060000),
	SPACE_OF_DESTINY(320070000),
	DRAUPNIR_CAVE(320080000),
	FIRE_TEMPLE(320100000),
	ALQUIMIA_RESEARCH_CENTER(320110000),
	SHADOW_COURT_DUNGEON(320120000),
	ADMA_STRONGHOLD(320130000),
	IDAB_PRO_D3(320140000),

	// Maps 2.5
	KAISINEL_ACADEMY(110070000),
	MARCHUTAN_PRIORY(120080000),
	ESOTERRACE(300250000),
	EMPYREAN_CRUCIBLE(300300000),

	// Map 2.6
	CRUCIBLE_CHALLENGE(300320000),
	
	// Maps 2.7
	ARENA_OF_CHAOS(300350000),
	ARENA_OF_DISCIPLINE(300360000),
	CHAOS_TRAINING_GROUNDS(300420000),
	DISCIPLINE_TRAINING_GROUNDS(300430000),
	PADMARASHKA_CAVE(320150000),
	
	// Test Map
	TEST_BASIC(900020000),
	TEST_SERVER(900030000),
	TEST_GIANTMONSTER(900100000),
	HOUSING_BARRACK(900110000),
	Region_housing(900130000),
	Advanced_Personal_Housing(900140000),
	
	// Maps 3.0
	
	// Instances
	RAKSANG(300310000),
	RENTUS_BASE(300280000),
	ATURAN_SKY_FORTRESS(300240000),
	ELEMENTIS_FOREST(300260000),
	ARGENT_MANOR(300270000),
	MUADA_TRENCHER(300380000),
	STEEL_RAKE_CABIN(300460000),
	TERATH_DREDGION(300440000),
	
	// Map 3.5
	ARENA_OF_HARMONY(300450000),
	SATRA_TREASURE_HOARD(300470000),
	IDTiamat_Solo(300490000),
	IDTiamat_Israphel(300500000), //Israphel's Space
	TIAMAT_STRONGHOLD(300510000),
	DRAGON_LORDS_REFUGE(300520000), //Dragon Lord's Refuge instance
	IDArena_Glory(300550000),
	IDDF2Flying_Event01(300560000), //Sky Temple of Arkanis
	IDArena_Team01_T(300570000),
	UNSTABLE_SPLINTER(300600000), //Unstable Abyssal Splinter
	HEXWAY(300700000),
	
	// Instances 4.3 NA
	IDLDF5Re_02(300530000),
	IDLDF5b_TD(300540000),
	IDLDF5Re_03(300580000),
	IDLDF5_Under_01(300590000),
	IDRuneweapon(300800000),
    IDRuneweapon_Q(300900000),
    IDLDF5RE_solo_Q(301000000),
    IDShulack_Rose_Solo_01(301010000),
    IDShulack_Rose_Solo_02(301020000),
    IDShulack_Rose_01(301030000),
    IDShulack_Rose_02(301040000),
    IDShulack_Rose_03(301050000),
    IDLDF5_Under_Rune(301110000),
    IDKamar(301120000),
    IDVritra_Base(301130000),
    IDLDF5_Under_02(301140000),
    IDAsteria_IU_Party(301160000),
    IDLDF5RE_02_L(301170000),
    IDLDF5RE_03_L(301180000),
    IDLDF5RE_Solo_L(301190000),
    IDAsteria_IU_World(301200000),
    
	// Maps 4.3 NA
    NORHTERN_KATALAM(600050000),
    SOUTHERN_KATALAM(600060000),
    UNDERGROUND_KATALAM(600070000),
	IDIU(600080000),
	
	
	// Housing
	ORIEL(700010000),
	PERNON(710010000),
	
	// Maps
	SARPAN(600020000),
	SARPAN_SKY(300410000),
	TIAMARANTA(600030000),
	TIAMARANTA_EYE(300400000),
	TIAMARANTA_EYE_2(600040000),
	
	// Others
	PROTECTOR_REALM(300330000),
	ISRAPHEL_TRACT(300390000),
	
	HOUSING_LC_LEGION(700020000, true),
	HOUSING_DC_LEGION(710020000, true),
	HOUSING_IDLF_PERSONAL(720010000, true),
	HOUSING_IDDF_PERSONAL(730010000, true);
	
	private final int worldId;
	private final boolean isPersonal;

	WorldMapType(int worldId) {
		this(worldId, false);
	}
	
	WorldMapType(int worldId, boolean personal) {
		this.worldId = worldId;
		this.isPersonal = personal;
	}

	public int getId() {
		return worldId;
	}
	
	public boolean isPersonal() {
		return isPersonal;
	}

	/**
	 * @param id
	 *          of world
	 * @return WorldMapType
	 */
	public static WorldMapType getWorld(int id) {
		for (WorldMapType type : WorldMapType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return null;
	}
}