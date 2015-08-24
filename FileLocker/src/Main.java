
import filelocker.crypto.EncryptedFileTunnel;
import filelocker.crypto.FileCipher;
import filelocker.ui.FileLockerMainWindow;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author i3-chathu-L
 */
public class Main {
    
    static{
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String args[]) throws Exception{
        
        FileLockerMainWindow fe = new FileLockerMainWindow();
        fe.setVisible(true);
        
    
/*
        File fIn = new File("E:/ssllab.aes");
        File fOut = new File("E:/ssllab_2.jpg");
        FileCipher cipher = FileCipher.getDecryptionCipher("chathu");
        cipher.initFileCiper();
        
        EncryptedFileTunnel  fTunnel = new EncryptedFileTunnel(fIn, fOut, cipher);
       

        System.out.println(fTunnel.doConvertion());
        */

    }

}
