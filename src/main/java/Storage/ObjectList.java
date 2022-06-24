package Storage;

import Player.Tool.ToolType;
import World.Objects;

import java.util.Random;

public enum ObjectList {
    ROCK("Rock.png", new ItemList[]{ItemList.STONE}, null, 0, 1, 1, 3),
    TREE("Tree.png", new ItemList[]{ItemList.WOOD}, ToolType.AXE, 1, 1, 2, 5);

    public final Objects object;
    public final int Rolls;
    public final int Min;
    public final int Max;
    ObjectList(String Filename, ItemList[] Drops, ToolType type, int Level, int Rolls, int min, int max){
        this.object = new Objects(Filename,Drops,type,Level);
        this.Rolls = Rolls;
        this.Max = max;
        this.Min = min;
    }
}