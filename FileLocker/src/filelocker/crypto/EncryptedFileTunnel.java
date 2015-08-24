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
 * This class creates a tunnel between two files by encrypting
 * content of one file and writing to the other.
 *
 * @author i3-chathu-L
 */
public class EncryptedFileTunnel{
    private final File fileIn;
    private final File fileOut;
    private final FileOutputStream fOutputStream;
    private final FileInputStream fInputStream;
        
    private final FileCipher fileCipher;
    
    /**
     *
     * @param fileIn File to be encrypted
     * @param fileOut File to write the encrypted contents 
     * @param cipher {@link filelocker.crypto.FileCipher} object denoting the encrypt or decrypt operation 
     * @throws FileNotFoundException
     * 
     */
    public EncryptedFileTunnel(File fileIn, File fileOut, FileCipher cipher) throws FileNotFoundException{
        this.fileIn = fileIn;
        this.fileOut = fileOut;
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
