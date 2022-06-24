package World;

import Storage.ObjectList;
import Utilities.RenderSys;

import java.awt.*;

public class Biome {
    public Image Background;
    public ObjectList[] Objects;
    public int MaxObjectRolls;

    public Biome(String bg, ObjectList[] objs, int Rolls){
        RenderSys sys = new RenderSys();
        this.MaxObjectRolls = Rolls;
        this.Background = sys.getImage("Images/Backgrounds/" + bg);
        this.Objects = objs;
    }
}