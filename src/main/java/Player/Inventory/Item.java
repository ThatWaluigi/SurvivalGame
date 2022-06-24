package Player.Inventory;

import java.awt.*;

public class Item {
    public int SlotCapacity;
    public String Name;
    public Image Icon;
    public Item(String Name, Image Icon, int MaxAmount){
        this.SlotCapacity = MaxAmount;
        this.Name = Name;
        this.Icon = Icon;
    }
}