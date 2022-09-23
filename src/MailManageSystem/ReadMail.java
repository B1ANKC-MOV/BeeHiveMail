package MailManageSystem;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;

import java.awt.Cursor;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter; 
import java.io.InputStream; 
import java.io.InputStreamReader; 

public class ReadMail extends javax.swing.JFrame 
{
    public ReadMail() {
        initComponents();
        displayMail();
    }
    private static int AttachNum  = 0;
    private static String[] AttachFileName ;
    private static InputStream[] in ;
    private static String savePath ;
    
    private void displayMail() 
    {
        jLabelReceiver.setText("To：" + Home_BeeHiveMailSystem.Toaddress);
        jLabelFrom.setText("From：" + Home_BeeHiveMailSystem.Fromaddress);
        jLabelSize.setText("File Size：" + Integer.toString(Home_BeeHiveMailSystem.size) + " B");
        jLabelSendedDate.setText("Date：" + Home_BeeHiveMailSystem.BeeDate);
        jLabelSubject.setText("Subject：" + Home_BeeHiveMailSystem.subject);
        try 
        {
            int num = Home_BeeHiveMailSystem.nRN;
            Message message = Home_BeeHiveMailSystem.folder.getMessages()[num];
            Object o = message.getContent();
            if (o instanceof String) 
            {
                jEditorPaneData.setContentType(message.getContentType());
                jEditorPaneData.setText(o.toString());
            } 
            else if (o instanceof Multipart) 
            {
                jLabelAttachment.setText("Notice：this mail may have attach files！");
                Multipart mp = (Multipart) o;
                AttachFileName = new String[mp.getCount()];
                in = new InputStream[mp.getCount()];
                for (int i = 0,  j = mp.getCount(); i < j; i++) 
                {
                    Part part = mp.getBodyPart(i);
                    String d = part.getDisposition();//判断是否含有附件的方法
                    if ((d != null) && (d.equals(Part.ATTACHMENT)||d.equals(Part.INLINE))) 
                    {
                        AttachFileName[AttachNum] = part.getFileName();
                        in[AttachNum] = part.getInputStream();
                        AttachNum++;
                        jButtonDownload.setEnabled(true);
                        jMenuItemDownload.setEnabled(true);
                    }
                }
            }
        } 
        catch (Exception e) {}
    }
    
    private void display (URL pageUrl ) { 
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            jEditorPaneData.setPage(pageUrl);                       
        } 
        catch (Exception e) {}
        finally{
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void saveAttachment(int num, String[] AttachFileName, InputStream[] inputstream)
    {
        JFileChooser foder = new JFileChooser();
        foder.setCurrentDirectory(new File("."));
        foder.setDialogType(JFileChooser.DIRECTORIES_ONLY + JFileChooser.SAVE_DIALOG); 
        foder.setDialogTitle("Please choose the path you want to save the attach files");
        if (foder.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {return ;}         
        String path = foder.getCurrentDirectory().getAbsolutePath();       
        jButtonDownload.setEnabled(false);      
        try
        {
            for (int j = 0; j < num; j++)
            {
                File file = new File(path + File.separator + AttachFileName[j]);
                FileWriter out = new FileWriter(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputstream[j]));
                BufferedWriter bw = new BufferedWriter(out);
                String line;
                while ((line = br.readLine()) != null) { bw.write(line);} 
                bw.flush();bw.close();out.close(); 
            } 
        } 
        catch (Exception e) {} 
        finally {jButtonDownload.setEnabled(true);} 
    }

    private void getEML() {
        JFileChooser foder = new JFileChooser();
        foder.setCurrentDirectory(new File("."));  
        foder.setDialogType(JFileChooser.DIRECTORIES_ONLY + JFileChooser.SAVE_DIALOG); 
        foder.setDialogTitle("Please choose the path you want to save the attach files"); 
        if (foder.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {  return; } 
        jButtonEml.setEnabled(false);
        String path = foder.getCurrentDirectory().getAbsolutePath(); 
        String fileName = foder.getName(foder.getSelectedFile()); 
        savePath = path + File.separator + fileName + ".eml"; 
        FileOutputStream out = null; 
        try  
        {
            Message[] message = Home_BeeHiveMailSystem.folder.getMessages();
            out = new FileOutputStream(savePath); 
            message[Home_BeeHiveMailSystem.nRN].writeTo(out); 
        }  
        catch (Exception e) {} 
        finally 
        {
            try 
            {
                if (out != null) { out.close();} 
            } 
            catch (Exception e) {}
            
            jButtonEml.setEnabled(true);
            jButtonOpeneml.setEnabled(true); 
            jMenuItemOpeneml.setEnabled(true); 
        } 
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelReceiver = new javax.swing.JLabel();
        jLabelFrom = new javax.swing.JLabel();
        jLabelSendedDate = new javax.swing.JLabel();
        jLabelSize = new javax.swing.JLabel();
        jLabelSubject = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneData = new javax.swing.JEditorPane();
        jButtonReply = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabelAttachment = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonEml = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonOpeneml = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButtonDownload = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemReply = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jMenuItememl = new javax.swing.JMenuItem();
        jMenuItemOpeneml = new javax.swing.JMenuItem();
        jMenuItemDownload = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Read Mail");

        jPanel1.setBackground(new java.awt.Color(236, 244, 247));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel2.setBackground(new java.awt.Color(193, 207, 208));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Header", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Palatino Linotype", 0, 11))); // NOI18N

        jLabelReceiver.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jLabelReceiver.setText("To:");

        jLabelFrom.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jLabelFrom.setText("From:");

        jLabelSendedDate.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jLabelSendedDate.setText("Date:");

        jLabelSize.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jLabelSize.setText("File Size:");

        jLabelSubject.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jLabelSubject.setText("Subject:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelReceiver)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(531, 531, 531))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSendedDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(519, 519, 519))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelSubject)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(519, 519, 519))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelReceiver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSendedDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSubject))
        );

        jPanel3.setBackground(new java.awt.Color(236, 244, 247));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Text", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DialogInput", 0, 11))); // NOI18N

        jEditorPaneData.setContentType("text/html"); // NOI18N
        jEditorPaneData.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jEditorPaneData.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jEditorPaneDataHyperlinkUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(jEditorPaneData);

        jButtonReply.setBackground(new java.awt.Color(232, 218, 209));
        jButtonReply.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        jButtonReply.setForeground(new java.awt.Color(158, 164, 154));
        jButtonReply.setText("Reply");
        jButtonReply.setFocusable(false);
        jButtonReply.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonReply.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplyActionPerformed(evt);
            }
        });

        jButtonDelete.setBackground(new java.awt.Color(232, 218, 209));
        jButtonDelete.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonDelete.setForeground(new java.awt.Color(158, 164, 154));
        jButtonDelete.setText("Delete");
        jButtonDelete.setFocusable(false);
        jButtonDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonExit.setBackground(new java.awt.Color(232, 218, 209));
        jButtonExit.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonExit.setForeground(new java.awt.Color(158, 164, 154));
        jButtonExit.setText("Exit");
        jButtonExit.setFocusable(false);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButtonReply)
                .addGap(18, 18, 18)
                .addComponent(jButtonDelete)
                .addGap(18, 18, 18)
                .addComponent(jButtonExit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReply)
                    .addComponent(jButtonDelete)
                    .addComponent(jButtonExit)))
        );

        jPanel4.setBackground(new java.awt.Color(236, 244, 247));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attach files' details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DialogInput", 0, 11))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabelAttachment)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jLabelAttachment))
        );

        jToolBar1.setBackground(new java.awt.Color(236, 244, 247));
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);
        jToolBar1.add(jSeparator5);
        jToolBar1.add(jSeparator1);

        jButtonEml.setBackground(new java.awt.Color(232, 218, 209));
        jButtonEml.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonEml.setForeground(new java.awt.Color(158, 164, 154));
        jButtonEml.setText("Save as EML");
        jButtonEml.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonEml.setFocusable(false);
        jButtonEml.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEml.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEmlActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonEml);
        jToolBar1.add(jSeparator2);

        jButtonOpeneml.setBackground(new java.awt.Color(232, 218, 209));
        jButtonOpeneml.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonOpeneml.setForeground(new java.awt.Color(158, 164, 154));
        jButtonOpeneml.setText("Open EML");
        jButtonOpeneml.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonOpeneml.setEnabled(false);
        jButtonOpeneml.setFocusable(false);
        jButtonOpeneml.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpeneml.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpeneml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenemlActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonOpeneml);
        jToolBar1.add(jSeparator4);

        jButtonDownload.setBackground(new java.awt.Color(232, 218, 209));
        jButtonDownload.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonDownload.setForeground(new java.awt.Color(158, 164, 154));
        jButtonDownload.setText("Download attach files");
        jButtonDownload.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonDownload.setEnabled(false);
        jButtonDownload.setFocusable(false);
        jButtonDownload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDownload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDownload);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(33, 33, 33)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.getAccessibleContext().setAccessibleName("Attach files details");

        jMenuBar1.setBackground(new java.awt.Color(158, 164, 154));

        jMenuFile.setText("File");
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuItemExit.setText("Exit");
        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuItemReply.setText("Reply");
        jMenuItemReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReplyActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemReply);

        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemDelete);

        jMenuItememl.setText("Save as EML");
        jMenuItememl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItememlActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItememl);

        jMenuItemOpeneml.setText("Open EML ");
        jMenuItemOpeneml.setEnabled(false);
        jMenuItemOpeneml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenemlActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemOpeneml);

        jMenuItemDownload.setText("Download attach files");
        jMenuItemDownload.setEnabled(false);
        jMenuItemDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDownloadActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemDownload);

        jMenuBar1.add(jMenuEdit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        this.dispose();
}//GEN-LAST:event_jButtonExitActionPerformed

    private void jButtonReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplyActionPerformed
        Home_BeeHiveMailSystem.currentsender = Home_BeeHiveMailSystem.Toaddress;
        Home_BeeHiveMailSystem.currentreceiver = Home_BeeHiveMailSystem.Fromaddress; 
        new Thread(new Runnable() {public void run() { new SendMail().setVisible(true); }}).start();        
    }//GEN-LAST:event_jButtonReplyActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        int returnValue = JOptionPane.showConfirmDialog(this, "Do you want to delete this mail?", "Confirm deletion of mail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.YES_OPTION) { 
            new Home_BeeHiveMailSystem().DeleteMailOne(Home_BeeHiveMailSystem.nRN);           
        }          
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonEmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmlActionPerformed
        getEML(); 
}//GEN-LAST:event_jButtonEmlActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed
        this.dispose(); 
    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReplyActionPerformed
        Home_BeeHiveMailSystem.currentsender = Home_BeeHiveMailSystem.Toaddress;
        Home_BeeHiveMailSystem.currentreceiver = Home_BeeHiveMailSystem.Fromaddress; 
        new Thread(new Runnable() {public void run() { new SendMail().setVisible(true);}}).start();        
    }//GEN-LAST:event_jMenuItemReplyActionPerformed

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        int returnValue = JOptionPane.showConfirmDialog(this, "Do you want to delete this mail?", "Confirm deletion of mail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.YES_OPTION) { 
            new Home_BeeHiveMailSystem().DeleteMailOne(Home_BeeHiveMailSystem.nRN);
        }   
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItememlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItememlActionPerformed
        getEML();
    }//GEN-LAST:event_jMenuItememlActionPerformed

    private void jButtonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadActionPerformed
        saveAttachment(AttachNum, AttachFileName, in);
    }//GEN-LAST:event_jButtonDownloadActionPerformed

    private void jMenuItemDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDownloadActionPerformed
        saveAttachment(AttachNum, AttachFileName, in);
    }//GEN-LAST:event_jMenuItemDownloadActionPerformed

    private void jButtonOpenemlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenemlActionPerformed
        try 
        {
            Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + savePath);  
        } 
        catch (Exception e) {}    
}//GEN-LAST:event_jButtonOpenemlActionPerformed

    private void jMenuItemOpenemlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenemlActionPerformed
        try 
        {
            Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + savePath);  
        } 
        catch (Exception e) {}    
    }//GEN-LAST:event_jMenuItemOpenemlActionPerformed

    private void jEditorPaneDataHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jEditorPaneDataHyperlinkUpdate
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {display(evt.getURL());}         
    }//GEN-LAST:event_jEditorPaneDataHyperlinkUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDownload;
    private javax.swing.JButton jButtonEml;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonOpeneml;
    private javax.swing.JButton jButtonReply;
    private javax.swing.JEditorPane jEditorPaneData;
    private javax.swing.JLabel jLabelAttachment;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelReceiver;
    private javax.swing.JLabel jLabelSendedDate;
    private javax.swing.JLabel jLabelSize;
    private javax.swing.JLabel jLabelSubject;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemDownload;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemOpeneml;
    private javax.swing.JMenuItem jMenuItemReply;
    private javax.swing.JMenuItem jMenuItememl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
