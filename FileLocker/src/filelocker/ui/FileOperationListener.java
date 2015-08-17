/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filelocker.ui;

import java.io.File;

/**
 *
 * @author i3-chathu-L
 */
public interface FileOperationListener {
    
    public void fileOpenOperationRequested(GUIFileItem file);
    public void clearFileSelectionOperationRequested();

    public void selectedFilesEncryptOperationRequested();        
    public void selectedFilesDecryptOperationRequested();    
}
