package Storage;

import Player.Tool.Tool;
import Player.Tool.ToolType;

public enum ToolList {
    WOODED_PICKAXE (ToolType.PICKAXE, 1, new String[]{"ToolHandle.png", "ToolPickaxeHead.png"}),
    WOODED_AXE (ToolType.AXE, 1, new String[]{"ToolHandle.png", "ToolAxeHead.png"}),
    WOODED_SHOVEL (ToolType.SHOVEL, 1, new String[]{"ToolHandle.png", "ToolShovelHead.png"});

    public final Tool tool;
    ToolList(ToolType type, int Level, String[] parts){
        this.tool = new Tool(type, Level, parts);
    }
}