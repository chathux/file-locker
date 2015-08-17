/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filelocker.ui;

import filelocker.crypto.EncryptedFileTunnel;
import filelocker.crypto.FileCipher;
import filelocker.ui.layouts.WrapLayout;
import filelocker.utils.FileUtils;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author i3-chathu-L
 */
public class FileLockerMainWindow extends javax.swing.JFrame implements FileOperationListener{

    /**
     * Creates new form FileExplorer
     */
    
    private FileUtils fileUtils;
    private ArrayList<GUIFileItem> fileExplorerCurrentFiles;
    private File fileExplorerCurrentDirectory;
    
    public FileLockerMainWindow() {
        initComponents();
        fileUtils = new FileUtils();
        fileExplorerCurrentFiles = new ArrayList<>();
        
               
        //update fileexplorer for the current directory
        fileExplorerCurrentDirectory = fileUtils.getSafeFile(System.getProperty("user.dir"));
        File[] files = fileUtils.getFilesAndFolders(fileExplorerCurrentDirectory);
        updateFileExplorerView(files);
        
       
       
    }

    
    private void updateFileExplorerView(File[] files){
        
        pnlFileExplorer.removeAll();
        fileExplorerCurrentFiles.clear();
        
        WrapLayout wrapLayout = new WrapLayout();
        wrapLayout.setAlignment(FlowLayout.LEFT);
        pnlFileExplorer.setLayout(wrapLayout);
        
        //pnlFileExplorer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        for(File file : files){ 
            GUIFileItem guiFItem = new GUIFileItem(file, this);
            pnlFileExplorer.add(guiFItem);
            fileExplorerCurrentFiles.add(guiFItem);
        }
        pnlFileExplorer.revalidate();
        pnlFileExplorer.updateUI();
        
        
        txtFileExplorerAddress.setText(fileExplorerCurrentDirectory.getAbsolutePath());
    }



    @Override
    public void fileOpenOperationRequested(GUIFileItem file) {
        fileExplorerCurrentDirectory = file.getFile();
        File[] files = fileUtils.getFilesAndFolders(fileExplorerCurrentDirectory);
        updateFileExplorerView(files);        
    }

    @Override
    public void clearFileSelectionOperationRequested() {
        for(GUIFileItem currentFile : fileExplorerCurrentFiles){
            
            currentFile.deselectFile();
            
        }
    }
    
    @Override
    public void selectedFilesEncryptOperationRequested() {
        
        final ArrayList<File> encFiles = new ArrayList<>();
        for(GUIFileItem guiFile : fileExplorerCurrentFiles){
            if(guiFile.isFileSelected()){
                encFiles.add(guiFile.getFile());            
            }
        }
        if(encFiles.size() < 1){
            System.out.println("No Files were selected");            
            return;
        }
        
       
        final String password = PasswordInputDialog.showPasswordDialog(this);
        
        if(password == null ){
            return;
        }
        
        

        final FileOperationProgressDialog operationDialog = new FileOperationProgressDialog(this, true);        
        
        new Thread(){
            public void run(){
                
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                       operationDialog.setVisible(true);
                    }
                });
                
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("Password" + password);                
                FileCipher cipher = FileCipher.getEncryptionCipher(password);
                cipher.initFileCiper();
                for(File encFile : encFiles){                        
                    File encOutFile = new File(encFile.getAbsolutePath() + ".enc");   ;

                    System.out.println(encOutFile.getAbsolutePath());
                    
                    try {
                        EncryptedFileTunnel  fTunnel = new EncryptedFileTunnel(encFile, encOutFile, cipher);
                        fTunnel.doConvertion();
                    } catch (Exception ex) {
                        Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                while(!operationDialog.isVisible()){
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                operationDialog.setVisible(false);
                
            }
        }.start();

       
        
        
    }

    @Override
    public void selectedFilesDecryptOperationRequested() {
     final ArrayList<File> decFiles = new ArrayList<>();
        for(GUIFileItem guiFile : fileExplorerCurrentFiles){
            if(guiFile.isFileSelected()){
                decFiles.add(guiFile.getFile());         
            }
        }
        if(decFiles.size() < 1){
            System.out.println("No Files were selected");
            return;
        }
        
       
        final String password = PasswordInputDialog.showPasswordDialog(this);
        
        if(password == null ){
            return;
        }
        
        final FileOperationProgressDialog operationDialog = new FileOperationProgressDialog(this, true);     
        new Thread(){
            public void run(){
                
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                       operationDialog.setVisible(true);
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println("Password" + password);
                FileCipher cipher = FileCipher.getDecryptionCipher(password);
                cipher.initFileCiper();
                
                for(File decFile : decFiles){
                        
                    File decOutFile = new File(decFile.getAbsolutePath().replaceFirst(".enc$",""));
                    System.out.println(decOutFile.getAbsolutePath());                                       

                    try {
                        EncryptedFileTunnel  fTunnel = new EncryptedFileTunnel(decFile, decOutFile, cipher);
                        fTunnel.doConvertion();
                    } catch (Exception ex) {
                        Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                while(!operationDialog.isVisible()){
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FileLockerMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                operationDialog.setVisible(false);
                
            }
        }.start();

        
       
       
           
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jkhj = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlFileExplorer = new javax.swing.JPanel();
        txtFileExplorerAddress = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jkhj.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jkhj.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout pnlFileExplorerLayout = new javax.swing.GroupLayout(pnlFileExplorer);
        pnlFileExplorer.setLayout(pnlFileExplorerLayout);
        pnlFileExplorerLayout.setHorizontalGroup(
            pnlFileExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlFileExplorerLayout.setVerticalGroup(
            pnlFileExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlFileExplorer);

        jkhj.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        txtFileExplorerAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFileExplorerAddressKeyPressed(evt);
            }
        });

        jLabel1.setText("Address");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jkhj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(txtFileExplorerAddress)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileExplorerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jkhj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("File Explorer", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 706, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Locked Files", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("asa");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFileExplorerAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFileExplorerAddressKeyPressed
 
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            fileExplorerCurrentDirectory = fileUtils.getSafeFile(txtFileExplorerAddress.getText().trim());
            File[] fileList = fileUtils.getFilesAndFolders(fileExplorerCurrentDirectory);
            if(fileList != null){
                updateFileExplorerView(fileList);
            }
                
        }
            
        
    }//GEN-LAST:event_txtFileExplorerAddressKeyPressed

    /**
     * @param args the command line arguments
     */
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jkhj;
    private javax.swing.JPanel pnlFileExplorer;
    private javax.swing.JTextField txtFileExplorerAddress;
    // End of variables declaration//GEN-END:variables






}
