package Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualTimer implements ActionListener {
    public float TimeSet;
    public float Remaining;
    public Image image;
    public boolean isPaused;
    public boolean isFinished;
    final float Division;
    Timer timer;
    RenderSys sys = new RenderSys();

    public VisualTimer(float Number){
        this.TimeSet = Number;
        this.Remaining = 0;
        this.Division = Number / 6;
        this.isPaused = false;
        this.isFinished = true;
        timer = new Timer(60, this);
        timer.start();
    }

    public void Draw(Graphics g, int X, int Y, int Scale){
        sys.DrawImage(g, X, Y, this.image, Scale);
    }

    public void Reset(){
        this.Remaining = this.TimeSet;
        this.isFinished = false;
    }

    public void Stop(){
        this.Remaining = 0;
        this.isFinished = true;
    }

    public void actionPerformed(ActionEvent e) {
        if (!isPaused && !isFinished) {
            if (this.Remaining > 0) {
                this.Remaining -= 0.01f;
                if (this.Remaining > this.Division * 6) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer6.png"});
                } else if (this.Remaining > this.Division * 5) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer5.png"});
                } else if (this.Remaining > this.Division * 4) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer4.png"});;
                } else if (this.Remaining > this.Division * 3) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer3.png"});
                } else if (this.Remaining > this.Division * 2) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer2.png"});
                } else if (this.Remaining > this.Division) {
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer1.png"});
                } else if (this.Remaining < this.Division){
                    this.image = sys.StackImages("Images/Timer/", new String[]{"TimerBase.png", "Timer0.png"});
                }
            }
            else {
                this.Remaining = 0f;
                this.isFinished = true;
            }
        }
    }
}