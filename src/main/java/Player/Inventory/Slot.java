package Player.Inventory;

public class Slot {
    public int Amount;
    public Item SlotItem;

    public Slot(Item item) {
        this.Amount = 1;
        this.SlotItem = item;
    }
}