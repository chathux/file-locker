/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filelocker.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author i3-chathu-L
 */
public class EncryptedFileTunnel{
    private File fileIn;
    private File fileOut;
    private FileOutputStream fOutputStream;
    private FileInputStream fInputStream;
        
    private FileCipher fileCipher;
    
    
    public EncryptedFileTunnel(File fileIn, File fileOut, FileCipher cipher) throws FileNotFoundException{
        this.fileIn = fileIn;
        this.fileCipher = cipher;
        
        this.fInputStream = new FileInputStream(fileIn);
        this.fOutputStream = new FileOutputStream(fileOut);
    }
   
    
    public long doConvertion() throws IOException, IllegalBlockSizeException, BadPaddingException{        
        long totalBytes = 0;
        
        byte[] buffer = new byte[1024];
        int readByteCount = 0;
        while((readByteCount = fInputStream.read(buffer)) != -1){
            
            byte[] convertedBytes = fileCipher.convertBytes(buffer, 0, readByteCount, fInputStream.available() == 0);
            if(convertedBytes != null){
                fOutputStream.write(convertedBytes);
            }
                        
            buffer = new byte[1024];
            
            totalBytes += readByteCount;
        }
        
        fInputStream.close();
        fOutputStream.flush();
        fOutputStream.close();
              
        return totalBytes;
    }
}
