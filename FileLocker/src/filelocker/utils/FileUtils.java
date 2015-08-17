/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filelocker.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 *
 * @author i3-chathu-L
 * This class handles all the functions related to file access
 */
public class FileUtils {
    
    
    
    public File getSafeFile(String path){
        
        File file = new File(path);
        
        if(file.exists()){
            file.setReadOnly();
        }
        return file;
    }
    /**
     * Returns list of files and folders in given path
     */
    public File[] getFilesAndFolders(String path){
     
        return getFilesAndFolders(new File(path));
    }

    public File[] getFilesAndFolders(File file) {        
        return file.listFiles();
    }
}
