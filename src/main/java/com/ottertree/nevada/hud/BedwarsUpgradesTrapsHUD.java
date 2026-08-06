package com.ottertree.nevada.hud;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.TextHud;
import com.ottertree.nevada.util.BedwarsUtil;
import com.ottertree.nevada.util.MinecraftColor;
import com.ottertree.nevada.util.bedwars.BedwarsUpgradesTrapsManager;
import java.util.List;

public class BedwarsUpgradesTrapsHUD extends TextHud {

    @Switch(
        name = "Short Names",
        description = "Use short names (Sharp, Prot, FF, Haste, etc.)"
    )
    public boolean shortNames = false;

    @Switch(
        name = "Roman Numerals",
        description = "Use Roman numerals (I, II, III, IV) instead of numbers"
    )
    public boolean romanNumerals = true;

    @Dropdown(
        name = "Heading Color",
        description = "Color for section headings (Upgrades/Traps)",
        options = {
            "Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red",
            "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue",
            "Green", "Aqua", "Red", "Light Purple", "Yellow", "White",
        }
    )
    public int headingColorIndex = 5; // Dark Purple

    @Dropdown(
        name = "Text Color",
        description = "Color for upgrade and trap names",
        options = {
            "Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red",
            "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue",
            "Green", "Aqua", "Red", "Light Purple", "Yellow", "White",
        }
    )
    public int textColorIndex = 15; // White

    public BedwarsUpgradesTrapsHUD() {
        super(
            true,  // enabled by default
            5,     // x
            65,    // y
            1,     // normal size
            false, // no background
            false, // no rounded corners
            0,     // rounded corner radius
            0,     // x padding
            0,     // y padding
            new OneColor(0, 0, 0, 0), // background color
            false, // no border
            0,     // border size
            new OneColor(0, 0, 0, 0)  // border color
        );
        textType = 1;
    }

    @Override
    public boolean shouldShow() {
        return super.shouldShow() && BedwarsUtil.inBedwars();
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            lines.add("§d§lUpgrades:");
            lines.add("§fSharpened Swords §7II");
            lines.add("§fReinforced Armor §7III");
            lines.add("§fHeal Pool");
            lines.add("");
            lines.add("§d§lTraps:");
            lines.add("§fCounter-Offensive Trap");
            lines.add("§fBlindness Trap");
        } else {
            lines.clear();
            MinecraftColor headingColor = MinecraftColor.fromIndex(headingColorIndex);
            MinecraftColor textColor = MinecraftColor.fromIndex(textColorIndex);

            lines.addAll(
                BedwarsUpgradesTrapsManager.getInstance().getDisplayLinesWithFormatting(
                    shortNames,
                    romanNumerals,
                    headingColor.getRed(), headingColor.getGreen(), headingColor.getBlue(), 255,
                    textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 255
                )
            );
        }
    }
}
