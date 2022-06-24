package World;

import Storage.BiomeList;
import Storage.ObjectList;
import Utilities.RenderSys;

import java.awt.*;
import java.util.Random;

public class Chunk {
    Random Random = new Random();
    public BiomeList Biome;
    public Dimension Position;
    public Tile[][] Tiles;
    public Chunk(Dimension GridSize, int X, int Y, BiomeList biome){
        this.Position = new Dimension(X, Y);
        this.Biome = biome;
        this.Tiles = new Tile[GridSize.width][GridSize.height];
        for (int i = 0; i < this.Tiles.length; i++) {
            for (int j = 0; j < this.Tiles[i].length; j++) {
                this.Tiles[i][j] = new Tile(new Dimension(i,j), null);
            }
        }
    }

    public Tile GetTile(int X, int Y){
        return this.Tiles[X][Y];
    }

    public void SetTile(int X, int Y, ObjectList Obj){
        GetTile(X, Y).Object = Obj;
    }

    public ObjectList RandomObjectFromBiome(){
        return ObjectList.valueOf(this.Biome.biome.Objects[Random.nextInt(this.Biome.biome.Objects.length)].name());
    }

    public void Generate(){
        for (Tile[] object : this.Tiles) {
            for (Tile tile : object) {
                tile.Object = null;
            }
        }
        for (int i = 0; i < this.Biome.biome.MaxObjectRolls; i++) {
            int i1 = 2;
            i1 += Random.nextInt(3);
            for (int j = 0; j < i1; j++) {
                SetTile(Random.nextInt(Tiles.length), Random.nextInt(Tiles[0].length), RandomObjectFromBiome());
            }
        }
    }

    public void Draw(Graphics g, Dimension GridSize, int GridTileSize){
        RenderSys sys = new RenderSys();
        for (int i = 0; i < GridSize.width; i++) {
            for (int j = 0; j < GridSize.height; j++) {
                sys.DrawImage(g,i*GridTileSize,j*GridTileSize, Biome.biome.Background, GridTileSize);
                if (this.GetTile(i,j).Object != null) {
                    sys.DrawImage(g, i * GridTileSize, j * GridTileSize, this.GetTile(i, j).Object.object.Sprite, GridTileSize);
                }
            }
        }
    }

    public static class Tile{
        public Dimension Position;
        public ObjectList Object;
        public Tile(Dimension Position, ObjectList Object){
            this.Position = Position;
            this.Object = Object;
        }

        public String ToString(){
            return "(" + this.Position.width + "," + this.Position.height + "); " + this.Object;
        }
    }
}