import Player.Inventory.Inventory;
import Player.Player;
import Player.Tool.Tool;
import Storage.BiomeList;
import Storage.ObjectList;
import Storage.ToolList;
import Utilities.Encryption.Encryption;
import Utilities.RenderSys;
import World.Chunk;
import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    public final String SALT = "Fitness-Gram_Pacer_Test";
    public final Dimension SCREEN_SIZE = new Dimension(700, 700);
    public final int GRID_TILE_SIZE = 50;
    public final Dimension GRID_SIZE = new Dimension(SCREEN_SIZE.width/GRID_TILE_SIZE, SCREEN_SIZE.height/GRID_TILE_SIZE);
    public ArrayList<Chunk> Map;
    public boolean isRunning = false;
    public boolean isSaving = false;
    public Player Player = new Player("Images/PlayerE.png", 20, GRID_SIZE);
    public Random Random = new Random();
    public RenderSys Render = new RenderSys();
    Timer timer;
    public final Font SaveFont = new Font("ComicSansMS", Font.BOLD, 10);
    GamePanel(){
        setPreferredSize(SCREEN_SIZE);
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new GamePanel.GameKeyListener());
    }

    public void LoadGame() throws FileNotFoundException {
        Key key = Encryption.ReturnKey(SALT);
        JsonObject SaveData = JsonParser.parseReader(new FileReader("Save.json")).getAsJsonObject();
        JsonObject MapData = SaveData.get("Map").getAsJsonObject();
        JsonArray ChunkData = MapData.get("Chunks").getAsJsonArray();

        JsonObject PlayerData = SaveData.get("Player").getAsJsonObject();
        Player.Position.X = Integer.parseInt(Encryption.Decrypt(PlayerData.get("Position").getAsString(), key).split(",")[0]);
        Player.Position.Y = Integer.parseInt(Encryption.Decrypt(PlayerData.get("Position").getAsString(), key).split(",")[1]);
        int[] CurrChunk = new int[2];
        CurrChunk[0] = Integer.parseInt(Encryption.Decrypt(PlayerData.get("CurrentChunk").getAsString(), key).split(",")[0]);
        CurrChunk[1] = Integer.parseInt(Encryption.Decrypt(PlayerData.get("CurrentChunk").getAsString(), key).split(",")[1]);

        for (JsonElement chunk : ChunkData) {
            int X = Integer.parseInt(Encryption.Decrypt(chunk.getAsJsonObject().get("Position").getAsString(), key).split(",")[0]);
            int Y = Integer.parseInt(Encryption.Decrypt(chunk.getAsJsonObject().get("Position").getAsString(), key).split(",")[1]);
            BiomeList biome = BiomeList.valueOf(Encryption.Decrypt(chunk.getAsJsonObject().get("Biome").getAsString(), key));
            Chunk newChunk = new Chunk(GRID_SIZE, X, Y, biome);
            for (JsonElement lists : chunk.getAsJsonObject().get("Tiles").getAsJsonArray()){
                for (String tiles : lists.getAsString().split(", ")){
                    tiles = tiles.replace("[", "");
                    tiles = tiles.replace("]", "");
                    tiles = Encryption.Decrypt(tiles, key);
                    String[] newtile = tiles.split("; ");
                    newtile[0] = newtile[0].replace("(", "");
                    newtile[0] = newtile[0].replace(")", "");
                    int[] tilepos = new int[2];
                    tilepos[0] = Integer.parseInt(newtile[0].split(",")[0]);
                    tilepos[1] = Integer.parseInt(newtile[0].split(",")[1]);
                    ObjectList object;
                    try{
                        object = ObjectList.valueOf(newtile[1]);
                    } catch (IllegalArgumentException e) {
                        object = null;
                    }
                    newChunk.SetTile(tilepos[0], tilepos[1], object);
                }
            }
            Map.add(newChunk);
            if (newChunk.Position.width == CurrChunk[0] && newChunk.Position.height == CurrChunk[1]){
                Player.CurrentChunk = newChunk;
            }
        }
        isRunning = true;
    }

    public void SaveGame(){
        Key key = Encryption.ReturnKey(SALT);
        JsonObject Save = new JsonObject();
        JsonObject MapSave = new JsonObject();
        JsonArray MapChunks = new JsonArray();
        for(Chunk chunk : Map){
            JsonObject ChunkSave = new JsonObject();
            JsonArray Tiles = new JsonArray();
            for(Chunk.Tile[] tilelist : chunk.Tiles){
                ArrayList<String> tilesave = new ArrayList<>();
                for(Chunk.Tile tile : tilelist){
                    if (tile.Object != null){
                        tilesave.add(Encryption.Encrypt(tile.ToString(), key));
                    }
                }
                if (tilesave.size() > 0){
                    Tiles.add(tilesave.toString());
                }
            }
            ChunkSave.add("Tiles", Tiles);
            ChunkSave.addProperty("Position", Encryption.Encrypt("" + chunk.Position.width + "," + chunk.Position.height, key));
            ChunkSave.addProperty("Biome", Encryption.Encrypt(chunk.Biome.toString(), key));
            MapChunks.add(ChunkSave);
        }
        MapSave.add("Chunks", MapChunks);
        JsonObject PlayerSave = new JsonObject();
        PlayerSave.addProperty("Position", Encryption.Encrypt("" + Player.Position.X + "," + Player.Position.Y, key));
        PlayerSave.addProperty("CurrentChunk", Encryption.Encrypt("" + Player.CurrentChunk.Position.width + "," + Player.CurrentChunk.Position.height, key));
        Save.add("Map", MapSave);
        Save.add("Player", PlayerSave);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("Save.json")){
            gson.toJson(Save, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isSaving = true;
    }

    public void StartGame(){
        Map = new ArrayList<>();
        if(new File("Save.json").exists()){
            try {
                LoadGame();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            GenerateChunk(0,0);
            Player.Position.X = Random.nextInt(GRID_SIZE.width);
            Player.Position.Y = Random.nextInt(GRID_SIZE.height);
            isRunning = true;
        }
        timer = new Timer(60, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public Chunk GetChunk(int X, int Y){
        for(Chunk chunk : Map){
            if (chunk.Position.width == X && chunk.Position.height == Y){
                return chunk;
            }
        }
        return null;
    }

    public void ChunkBorderCollisions(){
        if (Player.CurrentChunk.GetTile(Player.Position.X, Player.Position.Y).Object != null){
            Player.CurrentChunk = Player.PreviousChunk;
            Player.Position.GotoPreviousPosition();
            Player.StopVisual();
        }
    }

    public void Collisions(){
        if (Player.Position.X < 0){
            GenerateChunk(Player.CurrentChunk.Position.width -1, Player.CurrentChunk.Position.height);
            Player.Position.X = GRID_SIZE.width-1;
            ChunkBorderCollisions();
        }
        else if (Player.Position.Y < 0){
            GenerateChunk(Player.CurrentChunk.Position.width, Player.CurrentChunk.Position.height -1);
            Player.Position.Y = GRID_SIZE.height-1;
            ChunkBorderCollisions();
        }
        else if (Player.Position.Y > GRID_SIZE.height-1){
            GenerateChunk(Player.CurrentChunk.Position.width, Player.CurrentChunk.Position.height +1);
            Player.Position.Y = 0;
            ChunkBorderCollisions();
        }
        else if (Player.Position.X > GRID_SIZE.width-1){
            GenerateChunk(Player.CurrentChunk.Position.width +1, Player.CurrentChunk.Position.height);
            Player.Position.X = 0;
            ChunkBorderCollisions();
        }
        else if (Player.CurrentChunk.GetTile(Player.Position.X, Player.Position.Y).Object != null) {
            if (Player.HeldTool.Type == Player.CurrentChunk.GetTile(Player.Position.X, Player.Position.Y).Object.object.NeededTool){
                Player.Mine(Player.Position.X, Player.Position.Y);
            }else{
                Player.StopVisual();
                Player.Position.GotoPreviousPosition();
            }
        }
    }

    public void GenerateChunk(int X, int Y){
        if (GetChunk(X, Y) == null){
            Chunk chunk = new Chunk(GRID_SIZE, X, Y, BiomeList.RandomBiome());
            Player.PreviousChunk = Player.CurrentChunk;
            Player.CurrentChunk = chunk;
            chunk.Generate();
            Map.add(chunk);
        }
        else{
            Player.PreviousChunk = Player.CurrentChunk;
            Player.CurrentChunk = GetChunk(X, Y);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Draw(g);
    }

    public void Draw(Graphics g) {
        g.setColor(new Color(34, 255, 0));
        g.setFont(SaveFont);
        g.drawString("Loading. . .", SCREEN_SIZE.width/2, SCREEN_SIZE.height/2);
        if (isRunning) {
            g.setColor(null);
            g.setFont(null);
            Render.DrawGrid(g, GRID_SIZE, GRID_TILE_SIZE, SCREEN_SIZE, Color.GRAY);
            Player.CurrentChunk.Draw(g, GRID_SIZE, GRID_TILE_SIZE);
            Player.Draw(g, GRID_TILE_SIZE);
            if (Player.InvOpen){
                Player.PlayerInv.Draw(g, 50, getHeight() - 150);
                g.drawString(String.valueOf(Player.HeldTool.Type), 50, getHeight() - 160);
            }
            if (isSaving){
                g.setColor(new Color(34, 255, 0));
                g.setFont(SaveFont);
                g.drawString("Save Complete", 5, 10);
            }
        }
    }

    public class GameKeyListener extends KeyAdapter {
        public GameKeyListener(){}

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> {Player.Position.Move(0, -1); Collisions();}
                case KeyEvent.VK_A -> {Player.Position.Move(-1, 0); Collisions();}
                case KeyEvent.VK_S -> {Player.Position.Move(0, 1); Collisions();}
                case KeyEvent.VK_D -> {Player.Position.Move(1, 0); Collisions();}
                case KeyEvent.VK_P -> SaveGame();
                case KeyEvent.VK_T -> Player.CurrentChunk.GetTile(Player.Position.X, Player.Position.Y).Object = ObjectList.ROCK;
                case KeyEvent.VK_1 -> Player.HeldTool = ToolList.WOODED_AXE.tool;
                case KeyEvent.VK_2 -> Player.HeldTool = ToolList.WOODED_PICKAXE.tool;
                case KeyEvent.VK_3 -> Player.HeldTool = ToolList.WOODED_SHOVEL.tool;
                case KeyEvent.VK_E -> Player.InvOpen = !Player.InvOpen;
            }
        }
    }
}