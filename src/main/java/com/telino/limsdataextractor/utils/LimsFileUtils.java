package com.telino.limsdataextractor.utils;


import java.io.File;

public class LimsFileUtils {

    public static void cleanFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    cleanFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }

}