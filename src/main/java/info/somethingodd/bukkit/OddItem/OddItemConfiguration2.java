/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, "either" version 3 of the License, "or"
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, "see" <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.bukkit.OddItem;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spout.api.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.logging.Logger;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemConfiguration2 {
    private File configurationFile;
    private Logger log;
    private String logPrefix;
    private YamlConfiguration defaultConfiguration;

    public OddItemConfiguration2(OddItemBase oddItemBase) {
        this.configurationFile = new File(oddItemBase.getDataFolder() + File.separator + "OddItem.yml");
        this.log = oddItemBase.log;
        this.logPrefix = oddItemBase.logPrefix;
        setDefaults();
    }

    public void configure() {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.setDefaults(defaultConfiguration);
        configuration.options().copyDefaults(true);
        try {
            configuration.load(configurationFile);
        } catch (Exception e) {
            log.warning(logPrefix + "Configuration not found, loading defaults.");
            try {
                configuration.save(configurationFile);
            } catch (IOException e2) {
                log.severe(logPrefix + "Failure saving configuration" + e2.getMessage());
                e2.printStackTrace();
            }
        }
        OddItem.itemMap = Collections.synchronizedMap(new HashMap<String, ItemStack>());
        OddItem.items = Collections.synchronizedMap(new HashMap<String, NavigableSet<String>>());
        OddItem.groups = Collections.synchronizedMap(new HashMap<String, OddItemGroup>());

    }

    private void setDefaults() {
        defaultConfiguration = new YamlConfiguration();
        defaultConfiguration.set("comparator", "r");

        ConfigurationSection groupsSection = defaultConfiguration.createSection("groups");
        ConfigurationSection data = groupsSection.createSection("oddtransport").createSection("data");
        ConfigurationSection oddgive = data.createSection("oddgive");
        oddgive.set("type", new String[]{"kit", "whitelist"});
        oddgive.set("priority", 1);
        data.set("items", new String[] {"lapisblock,2", "greendye", "reddye", "air"});

        ConfigurationSection itemsSection = defaultConfiguration.createSection("items");
        itemsSection.set("0",  new String[] {"air", "empty", "sky"});
        itemsSection.set("1",  new String[] {"rock", "smoothrock", "smoothstone", "stone"});
        itemsSection.set("2",  new String[] {"grass"});
        itemsSection.set("3",  new String[] {"dirt"});
        itemsSection.set("4",  new String[] {"cob", "cobble", "cobblestone"});
        itemsSection.set("5",  new String[] {"plank", "wood"});
        itemsSection.set("6;0",  new String[] {"sapling", "oaksapling"});
        itemsSection.set("6;1",  new String[] {"pinesapling"});
        itemsSection.set("6;2",  new String[] {"birchsapling"});
        itemsSection.set("7",  new String[] {"adminium", "bedrock"});
        itemsSection.set("8",  new String[] {"water"});
        itemsSection.set("9",  new String[] {"stillwater", "swater"});
        itemsSection.set("10",  new String[] {"lava"});
        itemsSection.set("11",  new String[] {"stilllava", "slava"});
        itemsSection.set("12",  new String[] {"sand"});
        itemsSection.set("13",  new String[] {"gravel"});
        itemsSection.set("14",  new String[] {"goldore", "gore"});
        itemsSection.set("15",  new String[] {"iore", "ironore"});
        itemsSection.set("16",  new String[] {"coalore", "core"});
        itemsSection.set("17;0",  new String[] {"log", "oak", "oaklog", "oakwood"});
        itemsSection.set("17;1",  new String[] {"pine", "pinelog", "pinewood"});
        itemsSection.set("17;2",  new String[] {"birch", "birchlog", "birchwood"});
        itemsSection.set("18;0",  new String[] {"leaf", "leaves"});
        itemsSection.set("18;1",  new String[] {"darkleaf", "darkleaves", "pineleaf", "pineleaves"});
        itemsSection.set("18;2",  new String[] {"birchleaf", "birchleaves", "whiteleaf", "whiteleaves"});
        itemsSection.set("19",  new String[] {"sponge"});
        itemsSection.set("20",  new String[] {"glass"});
        itemsSection.set("21",  new String[] {"lapislazuliore", "lapisore", "llore"});
        itemsSection.set("22",  new String[] {"lapisblock", "lapislazuliblock", "lblock", "llblock"});
        itemsSection.set("23",  new String[] {"dispenser"});
        itemsSection.set("24",  new String[] {"sandstone", "sstone"});
        itemsSection.set("25",  new String[] {"musicblock", "noteblock"});
        itemsSection.set("27",  new String[] {"boosterrail", "boosterrails", "brail", "btrack", "poweredrail", "poweredrails", "prail", "ptrack"});
        itemsSection.set("28",  new String[] {"detectorrail", "detectorrails", "drail", "dtrack"});
        itemsSection.set("29",  new String[] {"spiston", "stickypiston"});
        itemsSection.set("30",  new String[] {"web"});
        itemsSection.set("31",  new String[] {"tallgrass", "wildgrass"});
        itemsSection.set("32",  new String[] {"deadshrub", "desertgrass", "deserttallgrass", "deserttree"});
        itemsSection.set("33",  new String[] {"piston"});
        itemsSection.set("35;0",  new String[] {"cloth", "whitecloth", "whitewool", "wool"});
        itemsSection.set("35;1",  new String[] {"orangecloth", "orangewool"});
        itemsSection.set("35;2",  new String[] {"magentacloth", "magentawool"});
        itemsSection.set("35;3",  new String[] {"lightbluecloth", "lightbluewool"});
        itemsSection.set("35;4",  new String[] {"yellowcloth", "yellowwool"});
        itemsSection.set("35;5",  new String[] {"lightgreencloth", "lightgreenwool", "limecloth", "limewool"});
        itemsSection.set("35;6",  new String[] {"pinkcloth", "pinkwool"});
        itemsSection.set("35;7",  new String[] {"graycloth", "graywool"});
        itemsSection.set("35;8",  new String[] {"lightgraycloth", "lightgraywool", "silvercloth", "silverwool"});
        itemsSection.set("35;9",  new String[] {"cyancloth", "cyanwool"});
        itemsSection.set("35;10",  new String[] {"purplecloth", "purplewool"});
        itemsSection.set("35;11",  new String[] {"bluecloth", "bluewool"});
        itemsSection.set("35;12",  new String[] {"browncloth", "brownwool"});
        itemsSection.set("35;13",  new String[] {"darkgrencloth", "darkgreenwool", "greencloth", "greenwool"});
        itemsSection.set("35;14",  new String[] {"redcloth", "redwool"});
        itemsSection.set("35;15",  new String[] {"blackcloth", "blackwool"});
        itemsSection.set("37",  new String[] {"dandelion", "yellowflower"});
        itemsSection.set("38",  new String[] {"redflower", "rose"});
        itemsSection.set("39",  new String[] {"brownmushroom"});
        itemsSection.set("40",  new String[] {"redmushroom"});
        itemsSection.set("41",  new String[] {"gblock", "gold", "goldblock"});
        itemsSection.set("42",  new String[] {"iblock", "iron", "ironblock"});
        itemsSection.set("43;0",  new String[] {"doubleslab", "doublestep"});
        itemsSection.set("43;1",  new String[] {"dsandslab", "dsandstep", "dsslab", "dsstep"});
        itemsSection.set("43;2",  new String[] {"dwoodslab", "dwoodstep", "dwslab", "dwstep"});
        itemsSection.set("43;3",  new String[] {"dcobbleslab", "dcobblestep", "dcslab", "dcstep"});
        itemsSection.set("44;0",  new String[] {"slab", "step"});
        itemsSection.set("44;1",  new String[] {"sandslab", "sandstep", "sslab", "sstep"});
        itemsSection.set("44;2",  new String[] {"woodslab", "woodstep", "wslab", "wstep"});
        itemsSection.set("44;3",  new String[] {"cobbleslab", "cobblestep", "cslab", "cstep"});
        itemsSection.set("44;3",  new String[] {"brickslab", "brickstep", "bslab", "bstep"});
        itemsSection.set("44;3",  new String[] {"sbrickslab", "sbrickstep", "sbslab", "sbstep"});
        itemsSection.set("45",  new String[] {"brickblock", "brickwall"});
        itemsSection.set("46",  new String[] {"tnt"});
        itemsSection.set("47",  new String[] {"bookcase", "bookshelf"});
        itemsSection.set("48",  new String[] {"moss", "mossy", "mossycobble", "mossycobblestone"});
        itemsSection.set("49",  new String[] {"obsidian"});
        itemsSection.set("50",  new String[] {"torch"});
        itemsSection.set("51",  new String[] {"fire"});
        itemsSection.set("52",  new String[] {"mobspawner", "spawner"});
        itemsSection.set("53",  new String[] {"woodstair", "woodstairs", "woodenstair", "woodenstairs", "wstair", "wstairs"});
        itemsSection.set("54",  new String[] {"chest"});
        itemsSection.set("55",  new String[] {"redstoneblock"});
        itemsSection.set("56",  new String[] {"diamondore", "dore"});
        itemsSection.set("57",  new String[] {"dblock", "diamondblock"});
        itemsSection.set("58",  new String[] {"crafttable", "workbench", "worktable"});
        itemsSection.set("59",  new String[] {"cropblock", "wheatblock"});
        itemsSection.set("60",  new String[] {"farm", "farmground", "soil"});
        itemsSection.set("61",  new String[] {"furnace"});
        itemsSection.set("62",  new String[] {"litfurnace"});
        itemsSection.set("63",  new String[] {"signblock"});
        itemsSection.set("64",  new String[] {"fencegate", "wooddoorblock"});
        itemsSection.set("65",  new String[] {"ladder"});
        itemsSection.set("66",  new String[] {"rail", "rails", "track", "tracks"});
        itemsSection.set("67",  new String[] {"cobblestairs", "cobblestonestairs", "cobstairs", "cstair", "cstairs"});
        itemsSection.set("68",  new String[] {"signblocktop"});
        itemsSection.set("69",  new String[] {"lever"});
        itemsSection.set("70",  new String[] {"rockplate", "rockpressureplate", "stoneplate", "stonepressureplate"});
        itemsSection.set("71",  new String[] {"irondoorblock"});
        itemsSection.set("72",  new String[] {"woodenplate", "woodenpressureplate", "woodplate", "woodpressureplate"});
        itemsSection.set("73",  new String[] {"redstoneore", "rore"});
        itemsSection.set("74",  new String[] {"glowingredstoneore", "glowingrore", "redstoneorelit", "rorelit"});
        itemsSection.set("75",  new String[] {"redstonetorchoff", "rtorchoff"});
        itemsSection.set("76",  new String[] {"redstonetorch", "rtorch"});
        itemsSection.set("77",  new String[] {"button"});
        itemsSection.set("78",  new String[] {"snow"});
        itemsSection.set("79",  new String[] {"ice"});
        itemsSection.set("80",  new String[] {"snowblock"});
        itemsSection.set("81",  new String[] {"cactus", "cacti"});
        itemsSection.set("82",  new String[] {"clayblock"});
        itemsSection.set("83",  new String[] {"reedblock"});
        itemsSection.set("84",  new String[] {"jukebox"});
        itemsSection.set("85",  new String[] {"fence"});
        itemsSection.set("86",  new String[] {"pumpkin"});
        itemsSection.set("87",  new String[] {"bloodstone", "brimstone", "hellsand", "netherrack", "nethersand", "netherstone", "redsand"});
        itemsSection.set("88",  new String[] {"mud", "slowsand", "soulsand"});
        itemsSection.set("89",  new String[] {"glow", "glowblock", "glowstone", "lightblock", "lightstone"});
        itemsSection.set("90",  new String[] {"portal", "portalblock"});
        itemsSection.set("91",  new String[] {"jack", "jackolantern", "lantern", "pumpkinlantern", "pumpkintorch"});
        itemsSection.set("92",  new String[] {"cakeblock"});
        itemsSection.set("93",  new String[] {"diodeoff", "repeateroff"});
        itemsSection.set("94",  new String[] {"diodeon", "repeateron"});
        itemsSection.set("95",  new String[] {"aprilchest", "foolchest", "lockedchest", "mysterychest"});
        itemsSection.set("96",  new String[] {"hatch", "trapdoor"});
        itemsSection.set("97",  new String[] {"silverfish"});
        itemsSection.set("98",  new String[] {"dungeon", "sbrick", "stonebrick"});
        itemsSection.set("98;1",  new String[] {"mossydungeon", "mossysbrick", "mossystonebrick", "msbrick"});
        itemsSection.set("98;2",  new String[] {"crackeddungeon", "crackedsbrick", "crackedstonebrick", "csbrick"});
        itemsSection.set("99",  new String[] {"bshroomcap", "brownmushroomcap"});
        itemsSection.set("100",  new String[] {"redmushroomcap", "rshroomcap"});
        itemsSection.set("101",  new String[] {"ibars", "ironbars"});
        itemsSection.set("102",  new String[] {"glasspane", "pane", "window"});
        itemsSection.set("103",  new String[] {"melon"});
        itemsSection.set("104",  new String[] {"pstem", "pumpkinstem"});
        itemsSection.set("105",  new String[] {"melonstem", "mstem"});
        itemsSection.set("106",  new String[] {"vines"});
        itemsSection.set("107",  new String[] {"gate"});
        itemsSection.set("108",  new String[] {"brickstair", "brickstairs", "bstair", "bstairs"});
        itemsSection.set("109",  new String[] {"sbstair", "sbstairs", "stonebrickstair", "stonebstair", "stonebstairs", "stonebrickstairs"});
        itemsSection.set("110",  new String[] {"mycelium"});
        itemsSection.set("111",  new String[] {"lilypad"});
        itemsSection.set("112",  new String[] {"nbrick", "netherbrick"});
        itemsSection.set("113",  new String[] {"nbrickfence", "netherbrickfence", "netherfence", "nfence"});
        itemsSection.set("114",  new String[] {"nbrickstair", "nbrickstairs", "nbstair", "nbstairs", "netherbrickstair", "netherbrickstairs"});
        itemsSection.set("115",  new String[] {"netherwartblock"});
        itemsSection.set("117",  new String[] {"brewingstandblock"});
        itemsSection.set("256",  new String[] {"ironshovel", "ishovel"});
        itemsSection.set("257",  new String[] {"ipick", "ipickaxe", "ironpick", "ironpickaxe"});
        itemsSection.set("258",  new String[] {"iaxe", "ironaxe"});
        itemsSection.set("259",  new String[] {"fands", "firestarter", "flintandsteel", "lighter"});
        itemsSection.set("260",  new String[] {"apple"});
        itemsSection.set("261",  new String[] {"bow"});
        itemsSection.set("262",  new String[] {"arrow"});
        itemsSection.set("263;0",  new String[] {"coal"});
        itemsSection.set("263;1",  new String[] {"charcoal"});
        itemsSection.set("264",  new String[] {"diamond"});
        itemsSection.set("265",  new String[] {"iingot", "ironbar", "ironingot"});
        itemsSection.set("266",  new String[] {"gingot", "goldbar", "goldingot"});
        itemsSection.set("267",  new String[] {"ironsword", "isword"});
        itemsSection.set("268",  new String[] {"woodsword", "woodensword", "wsword"});
        itemsSection.set("269",  new String[] {"woodshovel", "wshovel"});
        itemsSection.set("270",  new String[] {"woodenpick", "woodenpickaxe", "woodpick", "woodpickaxe", "wpick", "wpickaxe"});
        itemsSection.set("271",  new String[] {"waxe", "woodaxe", "woodenaxe"});
        itemsSection.set("272",  new String[] {"ssword", "stonesword"});
        itemsSection.set("273",  new String[] {"sshovel", "stoneshovel"});
        itemsSection.set("274",  new String[] {"spick", "spickaxe", "stonepick", "stonepickaxe"});
        itemsSection.set("275",  new String[] {"saxe", "stoneaxe"});
        itemsSection.set("276",  new String[] {"diamondsword", "dsword"});
        itemsSection.set("277",  new String[] {"diamondshovel", "dshovel"});
        itemsSection.set("278",  new String[] {"diamondpick", "diamondpickaxe", "dpick", "dpickaxe"});
        itemsSection.set("279",  new String[] {"daxe", "diamondaxe"});
        itemsSection.set("280",  new String[] {"stick"});
        itemsSection.set("281",  new String[] {"bowl"});
        itemsSection.set("282",  new String[] {"mushroomsoup", "mushroomstew", "soup", "stew"});
        itemsSection.set("283",  new String[] {"goldsword", "gsword"});
        itemsSection.set("284",  new String[] {"goldshovel", "gshovel"});
        itemsSection.set("285",  new String[] {"goldpick", "goldpickaxe", "gpick", "gpickaxe"});
        itemsSection.set("286",  new String[] {"gaxe", "goldaxe"});
        itemsSection.set("287",  new String[] {"string"});
        itemsSection.set("288",  new String[] {"feather"});
        itemsSection.set("289",  new String[] {"gunpowder", "sulfur"});
        itemsSection.set("290",  new String[] {"whoe", "woodenhoe", "woodhoe"});
        itemsSection.set("291",  new String[] {"shoe", "stonehoe"});
        itemsSection.set("292",  new String[] {"ihoe", "ironhoe"});
        itemsSection.set("293",  new String[] {"dhoe", "diamondhoe"});
        itemsSection.set("294",  new String[] {"ghoe", "goldhoe"});
        itemsSection.set("295",  new String[] {"seed", "seeds"});
        itemsSection.set("296",  new String[] {"wheat"});
        itemsSection.set("297",  new String[] {"bread"});
        itemsSection.set("298",  new String[] {"leatherhelm", "leatherhelmet", "lhelm", "lhelmet"});
        itemsSection.set("299",  new String[] {"lchest", "leatherchest", "leathershirt", "lshirt"});
        itemsSection.set("300",  new String[] {"leatherleggings", "leatherpants", "lleggings", "lpants"});
        itemsSection.set("301",  new String[] {"lboots", "leatherboots", "leathershoes", "lshoes"});
        itemsSection.set("302",  new String[] {"chainhelm", "chainhelmet", "chelm", "chelmet"});
        itemsSection.set("303",  new String[] {"cchest", "chainchest", "chainshirt", "cshirt"});
        itemsSection.set("304",  new String[] {"chainpants", "cpants"});
        itemsSection.set("305",  new String[] {"cboots", "chainboots", "cshoes", "chainshoes"});
        itemsSection.set("306",  new String[] {"ihelm", "ihelmet", "ironhelm", "ironhelmet"});
        itemsSection.set("307",  new String[] {"ichest", "ironchest", "ironshirt", "ishirt"});
        itemsSection.set("308",  new String[] {"ileggings", "ipants", "ironleggings", "ironpants"});
        itemsSection.set("309",  new String[] {"iboots", "ironboots", "ironshoes", "ishoes"});
        itemsSection.set("310",  new String[] {"dhelm", "dhelment", "diamondhelm", "diamondhelmet"});
        itemsSection.set("311",  new String[] {"dchest", "diamondchest", "diamondshirt", "dshirt"});
        itemsSection.set("312",  new String[] {"diamondleggings", "diamondpants", "dleggings", "dpants"});
        itemsSection.set("313",  new String[] {"dboots", "diamondboots", "diamondshoes", "dshoes"});
        itemsSection.set("314",  new String[] {"ghelm", "ghelmet", "goldhelm", "goldhelmet"});
        itemsSection.set("315",  new String[] {"gchest", "goldchest", "goldshirt", "gshirt"});
        itemsSection.set("316",  new String[] {"gleggings", "goldleggins", "goldpants", "gpants"});
        itemsSection.set("317",  new String[] {"gboots", "goldboots", "goldshoes", "gshoes"});
        itemsSection.set("318",  new String[] {"flint"});
        itemsSection.set("319",  new String[] {"pork", "porkchop"});
        itemsSection.set("320",  new String[] {"cookedpork", "cookedporkchop"});
        itemsSection.set("321",  new String[] {"painting"});
        itemsSection.set("322",  new String[] {"gapple", "goldapple", "goldenapple"});
        itemsSection.set("323",  new String[] {"sign"});
        itemsSection.set("324",  new String[] {"wdoor", "wooddoor", "woddendoor"});
        itemsSection.set("325",  new String[] {"bucket"});
        itemsSection.set("326",  new String[] {"waterbucker", "wbucket"});
        itemsSection.set("327",  new String[] {"lavabucket", "lbucket"});
        itemsSection.set("328",  new String[] {"cart", "minecart"});
        itemsSection.set("329",  new String[] {"saddle"});
        itemsSection.set("330",  new String[] {"idoor", "irondoor"});
        itemsSection.set("331",  new String[] {"rdust", "redstone", "redstonedust", "rsdust"});
        itemsSection.set("332",  new String[] {"snowball"});
        itemsSection.set("333",  new String[] {"boat"});
        itemsSection.set("334",  new String[] {"hide", "leather"});
        itemsSection.set("335",  new String[] {"mbucket", "milkbucket"});
        itemsSection.set("336",  new String[] {"brick"});
        itemsSection.set("337",  new String[] {"clay"});
        itemsSection.set("338",  new String[] {"bamboo", "reed", "sugarcane"});
        itemsSection.set("339",  new String[] {"paper"});
        itemsSection.set("340",  new String[] {"book"});
        itemsSection.set("341",  new String[] {"slimeorb"});
        itemsSection.set("342",  new String[] {"scart", "sminecart", "storagecart", "storageminecart"});
        itemsSection.set("343",  new String[] {"pcart", "pminecart", "poweredcart", "poweredminecart"});
        itemsSection.set("344",  new String[] {"egg"});
        itemsSection.set("345",  new String[] {"compass"});
        itemsSection.set("346",  new String[] {"fishingpole", "fishingrod", "pole", "rod"});
        itemsSection.set("347",  new String[] {"clock", "watch"});
        itemsSection.set("348",  new String[] {"gdust", "glowdust", "gsdust", "glowstonedust", "ldust", "lightstonedust", "lsdust", "lightdust"});
        itemsSection.set("349",  new String[] {"fish"});
        itemsSection.set("350",  new String[] {"cookedfish", "filet"});
        itemsSection.set("351;0",  new String[] {"black", "blackdye", "ink", "inksac", "inksack"});
        itemsSection.set("351;1",  new String[] {"red", "reddye", "rosered"});
        itemsSection.set("351;2",  new String[] {"cactusgreen", "green", "greendye"});
        itemsSection.set("351;3",  new String[] {"brown", "browndye", "cocoabeans"});
        itemsSection.set("351;4",  new String[] {"blue", "bluedye", "lapis", "lapislazuli"});
        itemsSection.set("351;5",  new String[] {"purple", "purpledye"});
        itemsSection.set("351;6",  new String[] {"cyan", "cyandye"});
        itemsSection.set("351;7",  new String[] {"lightgray", "lightgraydye", "lightgrey", "lightgreydye", "silver", "silverdye"});
        itemsSection.set("351;8",  new String[] {"gray", "graydye", "grey", "greydye"});
        itemsSection.set("351;9",  new String[] {"pink", "pinkdye"});
        itemsSection.set("351;10",  new String[] {"lime", "limedye"});
        itemsSection.set("351;11",  new String[] {"dandelionyellow", "yellow", "yellowdye"});
        itemsSection.set("351;12",  new String[] {"lightblue", "lightbluedye", "skyblue", "skybluedye"});
        itemsSection.set("351;13",  new String[] {"magenta", "magentadye"});
        itemsSection.set("351;14",  new String[] {"orange", "orangedye"});
        itemsSection.set("351;15",  new String[] {"bonemeal", "white", "whitedye"});
        itemsSection.set("352",  new String[] {"bone"});
        itemsSection.set("353",  new String[] {"sugar"});
        itemsSection.set("354",  new String[] {"cake"});
        itemsSection.set("355",  new String[] {"bed"});
        itemsSection.set("356",  new String[] {"diode", "repeater"});
        itemsSection.set("357",  new String[] {"cookie"});
        itemsSection.set("358",  new String[] {"map"});
        itemsSection.set("359",  new String[] {"scissors", "shears"});
        itemsSection.set("360",  new String[] {"melonslice", "mslice"});
        itemsSection.set("361",  new String[] {"pseed", "pseeds", "pumpkinseed", "pumpkinseeds"});
        itemsSection.set("362",  new String[] {"melonseed", "melonseeds", "mseed", "mseeds"});
        itemsSection.set("363",  new String[] {"beef", "rawbeef"});
        itemsSection.set("364",  new String[] {"cookedbeef", "steak"});
        itemsSection.set("365",  new String[] {"chicken", "rawchicken"});
        itemsSection.set("366",  new String[] {"cookedchicken"});
        itemsSection.set("367",  new String[] {"rottenflesh"});
        itemsSection.set("368",  new String[] {"enderpearl"});
        itemsSection.set("369",  new String[] {"blazerod"});
        itemsSection.set("370",  new String[] {"ghasttear"});
        itemsSection.set("371",  new String[] {"goldnugget"});
        itemsSection.set("372",  new String[] {"netherwart"});
        itemsSection.set("373",  new String[] {"mundanepotion", "potion"});
        itemsSection.set("374",  new String[] {"bottle", "glassbottle"});
        itemsSection.set("375",  new String[] {"spidereye"});
        itemsSection.set("376",  new String[] {"fermentedspidereye", "fspidereye"});
        itemsSection.set("377",  new String[] {"blazepowder"});
        itemsSection.set("387",  new String[] {"magmacream"});
        itemsSection.set("379",  new String[] {"brewingstand"});
        itemsSection.set("380",  new String[] {"cauldron"});
        itemsSection.set("381",  new String[] {"endereye", "eyeofender"});
        itemsSection.set("382",  new String[] {"glisteringmelon", "gmelon"});
        itemsSection.set("2256",  new String[] {"thirteen", "yellowdisc", "yellowrecord"});
        itemsSection.set("2257",  new String[] {"cat", "greendisc", "greenrecord"});
        itemsSection.set("2258",  new String[] {"blocks", "orangedisc", "orangerecord"});
        itemsSection.set("2259",  new String[] {"chirp", "reddisc", "redrecord"});
        itemsSection.set("2260",  new String[] {"far", "limedisc", "limerecord"});
        itemsSection.set("2261",  new String[] {"mall", "purpledisc", "purplerecord", "violetdisc", "violetrecord"});
        itemsSection.set("2262",  new String[] {"mellohi", "pinkdisc", "pinkrecord"});
        itemsSection.set("2263",  new String[] {"blackdisc", "blackrecord", "stal"});
        itemsSection.set("2264",  new String[] {"strad", "whitedisc", "whiterecord"});
        itemsSection.set("2265",  new String[] {"tealdisc", "tealrecord", "ward"});
        itemsSection.set("2266",  new String[] {"eleven", "worndisc", "wornrecord"});
    }
}
