package Player;

import Player.Inventory.Inventory;
import Player.Inventory.Item;
import Player.Tool.Tool;
import Storage.ObjectList;
import Storage.ToolList;
import Utilities.RenderSys;
import Utilities.VisualTimer;
import World.Chunk;
import World.Objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Player implements ActionListener {
    public final float MoveCooldown = 0.11f;
    public final VisualTimer Visual;
    public Dimension MaxGridBound;
    public Chunk CurrentChunk;
    public Chunk PreviousChunk;
    public Position Position;
    public Image PlayerImage;
    public Inventory PlayerInv;
    public float CurrentCooldown;
    public Tool HeldTool;
    Timer timer;
    Random random = new Random();
    RenderSys sys = new RenderSys();
    public Directions Facing;
    Directions PreviousFacing;
    public Player(String Path, int InvSlots, Dimension Bounds){
        this.MaxGridBound = Bounds;
        this.Visual = new VisualTimer(this.MoveCooldown);
        this.PlayerInv = new Inventory(InvSlots);
        this.PlayerImage = sys.getImage(Path);
        this.Position = new Position(this, 0, 0);
        this.Facing = Directions.EAST;
        this.PreviousFacing = null;
        this.HeldTool = ToolList.WOODED_AXE.tool;
        timer = new Timer(60, this);
        timer.start();
    }

    public void Mine(int X, int Y){
        if(this.CurrentChunk.Tiles[X][Y].Object != null){
            ObjectList obj = this.CurrentChunk.Tiles[X][Y].Object;
            if(this.HeldTool != null){
                if(this.HeldTool.Level >= obj.object.NeededToolLevel){
                    for (int i = 0; i < obj.Rolls; i++) {
                        int amount = obj.Min + random.nextInt(obj.Max - obj.Min);
                        this.PlayerInv.AddItem(obj.object.ReturnRandomDrop().item, amount);
                        this.CurrentChunk.Tiles[X][Y].Object = null;
                    }
                }
            }
        }
    }

    public void SetDirection(Directions direction){
        this.PreviousFacing = this.Facing;
        this.Facing = direction;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.CurrentCooldown > 0){
            this.CurrentCooldown -= 0.01f;
            if(this.CurrentCooldown < 0){
                this.CurrentCooldown = 0;
            }
        }
    }

    public void ChangeDirection(String image){
        this.PlayerImage = sys.getImage("Images/" + image);
    }

    public void Draw(Graphics g, int GridTileSize){
        int X = this.Position.X * GridTileSize;
        int Y = this.Position.Y * GridTileSize;
        sys.DrawCenteredImage(g, X - 22, Y - 20, this.PlayerImage, 60);
        if (!this.Visual.isFinished) {
            this.Visual.Draw(g, X + 27, Y + 27, 24);
        }
    }

    public void StopVisual(){
        this.Visual.Stop();
        this.CurrentCooldown = 0;
    }

    public static class Position{
        Player player;
        public int X;
        public int Y;
        public int PrevX;
        public int PrevY;
        public Position(Player player, int X, int Y){
            this.player = player;
            this.X = X;
            this.Y = Y;
        }

        public void GotoPreviousPosition(){
            this.X = this.PrevX;
            this.Y = this.PrevY;
            this.player.Facing = this.player.PreviousFacing;
        }

        public void Move(int X, int Y){
            if (this.player.CurrentCooldown == 0){
                this.player.Visual.Reset();
                this.PrevX = this.player.Position.X;
                this.PrevY = this.player.Position.Y;
                this.X += X;
                this.Y += Y;
                this.player.CurrentCooldown = this.player.MoveCooldown;
                switch (X){
                    case -1 -> this.player.SetDirection(Directions.WEST);
                    case 1 -> this.player.SetDirection(Directions.EAST);
                    case 0 ->{
                       switch (Y){
                           case -1 -> this.player.SetDirection(Directions.NORTH);
                           case 1 -> this.player.SetDirection(Directions.SOUTH);
                       }
                    }
                }
                switch (this.player.Facing){
                    case EAST -> this.player.ChangeDirection("PlayerE.png");
                    case WEST -> this.player.ChangeDirection("PlayerW.png");
                    case NORTH -> this.player.ChangeDirection("PlayerN.png");
                    case SOUTH -> this.player.ChangeDirection("PlayerS.png");
                }
            }
        }
    }
}