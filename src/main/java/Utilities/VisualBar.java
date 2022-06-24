package Utilities;

import java.awt.*;

public class VisualBar {
    public int Max;
    public float Current;

    public Image image;
    float Division;

    public VisualBar(int Max){
        this.Max = Max;
        this.Current = this.Max;
        this.Division = Max / 12;
    }

}