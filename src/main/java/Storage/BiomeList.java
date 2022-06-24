package Storage;

import World.Biome;

import java.util.Arrays;
import java.util.Random;

public enum BiomeList {
    QUARRY("StoneGround.png", new ObjectList[]{ObjectList.ROCK}, 5),
    FOREST("GrassGround.png", new ObjectList[]{ObjectList.TREE}, 7),
    GRASSLAND("GrassGround.png", new ObjectList[]{ObjectList.TREE}, 2);

    public final Biome biome;
    BiomeList(String bg, ObjectList[] objs, int maxrolls){
        this.biome = new Biome(bg, objs, maxrolls);
    }

    static final int Size = Arrays.asList(values()).size();
    static final Random random = new Random();
    public static BiomeList RandomBiome(){
        return values()[random.nextInt(Size)];
    }
}