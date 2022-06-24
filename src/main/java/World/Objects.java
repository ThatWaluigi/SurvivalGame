package World;

import Player.Inventory.Item;
import Player.Tool.ToolType;
import Storage.ItemList;
import Utilities.RenderSys;

import java.awt.*;
import java.util.Random;

public class Objects {
    public Image Sprite;
    String ImageFile;
    public ItemList[] Drops;
    public ToolType NeededTool;
    public int NeededToolLevel;
    public Random random = new Random();

    public Objects(String Sprite, ItemList[] Drops, ToolType NeededTool, int NeededLevel) {
        RenderSys sys = new RenderSys();
        this.ImageFile = Sprite;
        this.Sprite = sys.getImage("Images/Objects/" + Sprite);
        this.Drops = Drops;
        this.NeededTool = NeededTool;
        this.NeededToolLevel = NeededLevel;
    }
    public ItemList ReturnRandomDrop(){
        return this.Drops[random.nextInt(this.Drops.length)];
    }
}