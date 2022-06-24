package Player.Tool;

import Utilities.RenderSys;

import java.awt.*;

public class Tool {
    public ToolType Type;
    public int Level;
    public Image Image;
    public Tool(ToolType type, int lvl, String[] parts){
        RenderSys sys = new RenderSys();
        this.Type = type;
        this.Level = lvl;
        this.Image = sys.StackImages("Images/Tools/", parts);
    }
}