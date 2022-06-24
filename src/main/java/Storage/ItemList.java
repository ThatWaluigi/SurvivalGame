package Storage;

import Player.Inventory.Item;
import Utilities.RenderSys;

public enum ItemList {
    STONE ("Stone", "Rock.png", 300),
    WOOD ("Wood", "Tree.png", 300),
    FLINT ("Flint", "SharpRock.png", 120);

    public final Item item;
    ItemList(String ItemName, String Image, int Max) {
        RenderSys sys = new RenderSys();
        this.item = new Item(ItemName, sys.getImage("Images/Objects/" + Image), Max);
    }
}