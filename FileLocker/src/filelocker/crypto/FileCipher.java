/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filelocker.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author i3-chathu-L
 */
public abstract class  FileCipher {
 //
    
    protected Cipher cipher;
    public FileCipher(){
        
    }
    
    public abstract void initFileCiper();

    public static FileCipher getEncryptionCipher(final String password){
        //return new FileCipher();
        return new FileCipher() {
            
            @Override
            public void initFileCiper() {
                try {                
                    this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    
                    SecretKey key = passwordToPBEKey(password);
                    IvParameterSpec initializationVector = new IvParameterSpec(key.getEncoded());   
                    this.cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(),"AES"), initializationVector);
                    
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPaddingException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidAlgorithmParameterException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeySpecException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        };
    }
    
    public static FileCipher getDecryptionCipher(final String password){
        //return new FileCipher();
        return new FileCipher() {

            @Override
            public void initFileCiper() {
                try {                
                    this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    
                    SecretKey key = passwordToPBEKey(password);
                    IvParameterSpec initializationVector = new IvParameterSpec(key.getEncoded());                       
                    this.cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getEncoded(),"AES"), initializationVector);
                    
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPaddingException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidAlgorithmParameterException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeySpecException ex) {
                    Logger.getLogger(FileCipher.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        };
    }
    
    public SecretKey passwordToPBEKey(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException{

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), "chathux".getBytes(), 2, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey skey = keyFactory.generateSecret(pbeKeySpec);

        return  skey;
    }
      
    public byte[] convertBytes(byte[] bytes, int start, int length, boolean lastBuffer) throws IllegalBlockSizeException, BadPaddingException{
        if(lastBuffer){
            return cipher.doFinal(bytes, start, length);
        }else{
            return cipher.update(bytes, start, length);
        }
    }

    
}
