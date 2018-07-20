package com.araceinspace.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class MyPacker {
    public static void main (String[] args) throws Exception {
        //Point This to the Assets folder
        String assetsFolder = "/home/slack/workspace/ARaceInSpace_assets/";
        //String input
       // String dir = "/mnt/A66656D06656A0BB/GameDesign/Sprite Work/Hero/Hero15pctResize/";
        String inputDir = assetsFolder + "MyPacker/input";
        String outputDir = assetsFolder + "MyPacker/output";
        System.out.println("cwd: " + System.getProperty("user.dir"));
        String packFileName = "HeroAnimations";
        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}