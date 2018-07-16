package com.araceinspace.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class MyPacker {
    public static void main (String[] args) throws Exception {
        String dir = "/mnt/A66656D06656A0BB/GameDesign/Sprite Work/Hero/Hero15pctResize/";
        String inputDir = dir+"Input/";
        String outputDir = dir+"Output/";
        String packFileName = "HeroAnimations";
        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}