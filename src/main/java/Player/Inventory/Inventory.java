package Player.Inventory;

import Utilities.RenderSys;

import java.awt.*;
import java.util.ArrayList;

public class Inventory {
    int Slots;
    public ArrayList<Slot> List = new ArrayList<>();
    public Inventory(int Slots){
        this.Slots = Slots;
    }

    public Slot GetSlotByItem(Item Item){
        for(Slot slot : List){
            if (slot.SlotItem == Item){
                return slot;
            }
        }
        return null;
    }

    public void AddItem(Item item, int Amount){
        Slot slot = GetSlotByItem(item);
        if (slot == null){
            if (List.size() < Slots) {
                Slot NewSlot = new Slot(item);
                NewSlot.Amount = Amount;
                List.add(NewSlot);
            }
        }
        else{
            if(slot.Amount + Amount < slot.SlotItem.SlotCapacity){
                slot.Amount += Amount;
            }else{
                slot.Amount = slot.SlotItem.SlotCapacity;
            }
        }
    }

    public void Draw(Graphics g, int invX, int invY){
        RenderSys sys = new RenderSys();
        g.setColor(new Color(100, 100, 100, 180));
        g.fillRect(invX - 5, invY - 5, 250, 120);
        for(int i = 0 ; i<List.size(); i++) {
            Slot slot = List.get(i);
            g.drawString(slot.SlotItem.Name + ": " + slot.Amount + " / " + slot.SlotItem.SlotCapacity, invX + 25, invY + 20 + 12 * i);
            sys.DrawImage(g, invX + 10, invY + 7 + 12 * i, slot.SlotItem.Icon, 15);
        }
    }
}