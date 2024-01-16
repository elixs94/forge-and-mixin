package com.example.examplemod;


import net.minecraft.block.Block;

public class BlockData {

    private int x;
    private int y;
    private int z;

    private String name;
    private int intValue;


    public BlockData(int x, int y, int z, String name, int intValue) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.name = name;
        this.intValue = intValue;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }



    public String getName() {
        return name;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setZ(int z) {
        this.z = z;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }


}