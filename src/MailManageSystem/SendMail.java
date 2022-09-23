package MailManageSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SendMail extends javax.swing.JFrame {
    public SendMail() {initComponents();
        jTextFieldFrom.setText(Home_BeeHiveMailSystem.currentsender);
        jTextFieldTo.setText(Home_BeeHiveMailSystem.currentreceiver);
    }
    private String from;
    private String to;//收件人邮箱
    public static String user = Home_BeeHiveMailSystem.account;
    public static String password = Home_BeeHiveMailSystem.password;
    public static String smtpserver = Home_BeeHiveMailSystem.smtpServer;
    private String subject;
    private String text;
    private static String picturePATH;
    private static String pictureCode;
    private static String AttachFilePath;
    private static boolean IFpureText = true;
    private static boolean IFAddPicture = false;
    private static boolean IFAttachFiles = false;
    
    
    private void sendMails() {
        if (user == null || password == null || smtpserver == null) 
        {
            JOptionPane.showMessageDialog(this, "Please set your SMTP Server first,then sent your mail！");
            new Thread(new Runnable() {public void run() {new SetSMTPServer().setVisible(true);}}).start();
        } 
        else 
        {
            jButtonSendMails.setEnabled(false);
            from = jTextFieldFrom.getText();
            to = jTextFieldTo.getText();
            subject = jTextFieldSubject.getText();
            text = jTextAreaData.getText();
            try {
                Session session = createSession();
                MimeMessage message = creatMessage(session);
                Transport transport = session.getTransport("smtp");
                transport.connect(smtpserver, user, password);
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                if (transport.isConnected()) { transport.close(); }
                int returnValue = JOptionPane.showConfirmDialog(this, "Mail has been sent successfully,do you want to save it to 'Sent' tables?", "Please take notice that...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (returnValue == JOptionPane.YES_OPTION) {addSendTable();}              
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Fail in send!");
            } 
            finally {
                jButtonSendMails.setEnabled(true);
            }
        }
    }

    private Session createSession() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        return session;
    }

    private MimeMessage creatMessage(Session session) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        if (IFpureText && !IFAddPicture && !IFAttachFiles) {
            message.setText(text);
        } 
        else if (!IFAttachFiles && IFAddPicture && !IFpureText) {
            MimeMultipart allMultipart = new MimeMultipart("related");
            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            MimeBodyPart pictureBodypart = new MimeBodyPart();
            htmlBodyPart.setContent(text, "text/html;charset=gb2312");
            FileDataSource fds = new FileDataSource(picturePATH);
            pictureBodypart.setDataHandler(new DataHandler(fds));
            pictureBodypart.setContentID(pictureCode);
            allMultipart.addBodyPart(htmlBodyPart);
            allMultipart.addBodyPart(pictureBodypart);
            message.setContent(allMultipart);
        } 
        else if (IFAttachFiles && !IFAddPicture && IFpureText) {
            if (text.equals("")) {
                MimeBodyPart attachPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(AttachFilePath);
                attachPart.setDataHandler(new DataHandler(fds));
                attachPart.setFileName(fds.getName());
                MimeMultipart allMultipart = new MimeMultipart();
                allMultipart.addBodyPart(attachPart);
                message.setContent(allMultipart);
            } 
            else {
                MimeBodyPart plainText = new MimeBodyPart();
                plainText.setText(text);
                MimeBodyPart attachPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(AttachFilePath);
                attachPart.setDataHandler(new DataHandler(fds));
                attachPart.setFileName(fds.getName());
                MimeMultipart allMultipart = new MimeMultipart();
                allMultipart.addBodyPart(plainText);
                allMultipart.addBodyPart(attachPart);
                message.setContent(allMultipart);
            }
        } 
        else {            
            MimeBodyPart contentPart = createContent(text, picturePATH);      
            MimeBodyPart attachPart = creatAttachment(AttachFilePath);
            MimeMultipart allMultipart = new MimeMultipart("mixed");
            allMultipart.addBodyPart(contentPart);
            allMultipart.addBodyPart(attachPart);
            message.setContent(allMultipart);
        }
        message.saveChanges();
        File f = new File("");
        String currentPath = f.getAbsolutePath();
        if (!new File("sendedMails").exists()) {newFolder("sendedMails");}
        String fileName = currentPath + File.separator + "sendedMails"+ File.separator + from + "_" + to + "_" + subject + ".eml";
        message.writeTo(new FileOutputStream(fileName));
        int returnValue = JOptionPane.showConfirmDialog(this, "Your mail has been saved to location！\nDo you want to open the eml item?", "Confirm to open the eml item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.YES_OPTION) {
            try { Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + fileName); }
            catch (Exception e) {}  
        }
        return message;
    }

    private MimeBodyPart createContent(String text, String AttachFileName) throws Exception {//创建正文
        MimeBodyPart contentPart = new MimeBodyPart();
        MimeMultipart contentMultipart = new MimeMultipart("related");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(text, "text/html;charset=gb2312");
        contentMultipart.addBodyPart(htmlBodyPart);

        MimeBodyPart pictureBodypart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(AttachFileName);
        pictureBodypart.setDataHandler(new DataHandler(fds));
        pictureBodypart.setContentID(pictureCode);
        contentMultipart.addBodyPart(pictureBodypart);

        contentPart.setContent(contentMultipart);
        return contentPart;
    }

    private MimeBodyPart creatAttachment(String AttachFileName) throws Exception {//创建附件

        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(AttachFileName);
        attachPart.setDataHandler(new DataHandler(fds));
        attachPart.setFileName(fds.getName());
        return attachPart;
    }

    private void addSendTable() 
    {
        Date date = new Date();
        String BeeDate = ShowDate.showTime(date);
        DefaultTableModel myModel2 = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableSendedMails.getModel());
        Vector newRow = new Vector();
        newRow.add(from);//给Deleted Items table中添加
        newRow.add(to);
        newRow.add(subject);
        newRow.add(BeeDate);
        myModel2.getDataVector().add(newRow);
        myModel2.fireTableStructureChanged();
    }

    private void addPictures(){
        JFileChooser JFC = new JFileChooser();
        JFC.setCurrentDirectory(new File("."));
        JFC.setDialogTitle("Attach picture");
        if (JFC.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {  return;  }
        File attachFile = JFC.getSelectedFile();
        picturePATH = attachFile.getAbsolutePath();
        pictureCode = picturePATH.substring(picturePATH.lastIndexOf("\\") + 1);
        this.jTextAreaData.append("<ing src=\"cid:" + pictureCode + "\">");
        IFAddPicture = true;
        IFpureText = false;
    }
    
    private void addAttachments(){
        JFileChooser JFC = new JFileChooser();
        JFC.setCurrentDirectory(new File("."));
        JFC.setDialogTitle("Attach files");
        if (JFC.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) { return; }
        File attachFile = JFC.getSelectedFile();
        AttachFilePath = attachFile.getAbsolutePath();
        this.jLabelAttachment.setText("You attach a file：" + AttachFilePath);
        IFAttachFiles = true;
    }
    
     public void newFolder(String folderPath) {
        try {
            File myFilePath = new File(folderPath);
            if (!myFilePath.exists()) {  myFilePath.mkdir();  }
        } 
        catch (Exception e) {
            System.out.println("error in creation of contents");
            e.printStackTrace();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaData = new javax.swing.JTextArea();
        jButtonSendMails = new javax.swing.JButton();
        jButtonQunFa = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonAddAttachments = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButtonAddPictures = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonSetSMTPServer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel4 = new javax.swing.JPanel();
        jLabelAttachment = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelTo = new javax.swing.JLabel();
        jLabelSubject = new javax.swing.JLabel();
        jTextFieldTo = new javax.swing.JTextField();
        jTextFieldSubject = new javax.swing.JTextField();
        jLabelFrom = new javax.swing.JLabel();
        jTextFieldFrom = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemSendMails = new javax.swing.JMenuItem();
        jMenuItemQunFa = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemAddAttachments = new javax.swing.JMenuItem();
        jMenuItemAddPictures = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemSetSMTPServer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Send Mail");
        setBackground(new java.awt.Color(236, 244, 247));

        jPanel1.setBackground(new java.awt.Color(236, 244, 247));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel2.setBackground(new java.awt.Color(236, 244, 247));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Text", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DialogInput", 0, 11))); // NOI18N
        jPanel2.setToolTipText("Text");
        jPanel2.setPreferredSize(new java.awt.Dimension(138, 230));

        jTextAreaData.setColumns(20);
        jTextAreaData.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jTextAreaData.setRows(5);
        jScrollPane1.setViewportView(jTextAreaData);

        jButtonSendMails.setBackground(new java.awt.Color(232, 218, 209));
        jButtonSendMails.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        jButtonSendMails.setForeground(new java.awt.Color(158, 164, 154));
        jButtonSendMails.setText("Send");
        jButtonSendMails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonSendMails.setFocusable(false);
        jButtonSendMails.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSendMails.setPreferredSize(new java.awt.Dimension(57, 40));
        jButtonSendMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendMailsActionPerformed(evt);
            }
        });

        jButtonQunFa.setBackground(new java.awt.Color(248, 235, 216));
        jButtonQunFa.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonQunFa.setForeground(new java.awt.Color(158, 164, 154));
        jButtonQunFa.setText("Group Send");
        jButtonQunFa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonQunFa.setFocusable(false);
        jButtonQunFa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonQunFa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonQunFa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQunFaActionPerformed(evt);
            }
        });

        jButtonExit.setBackground(new java.awt.Color(248, 235, 216));
        jButtonExit.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonExit.setForeground(new java.awt.Color(158, 164, 154));
        jButtonExit.setText("Exit");
        jButtonExit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonExit.setFocusable(false);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButtonSendMails, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonQunFa, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonSendMails, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonQunFa, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.setBackground(new java.awt.Color(236, 244, 247));
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(480, 50));
        jToolBar1.add(jSeparator1);

        jButtonAddAttachments.setBackground(new java.awt.Color(232, 218, 209));
        jButtonAddAttachments.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        jButtonAddAttachments.setForeground(new java.awt.Color(158, 164, 154));
        jButtonAddAttachments.setText("Attach file");
        jButtonAddAttachments.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddAttachments.setFocusable(false);
        jButtonAddAttachments.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddAttachments.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddAttachments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttachmentsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddAttachments);
        jToolBar1.add(jSeparator5);
        jToolBar1.add(jSeparator7);

        jButtonAddPictures.setBackground(new java.awt.Color(232, 218, 209));
        jButtonAddPictures.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        jButtonAddPictures.setForeground(new java.awt.Color(158, 164, 154));
        jButtonAddPictures.setText("Attach picture");
        jButtonAddPictures.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddPictures.setFocusable(false);
        jButtonAddPictures.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddPictures.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddPictures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPicturesActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddPictures);
        jToolBar1.add(jSeparator2);

        jButtonSetSMTPServer.setBackground(new java.awt.Color(232, 218, 209));
        jButtonSetSMTPServer.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        jButtonSetSMTPServer.setForeground(new java.awt.Color(158, 164, 154));
        jButtonSetSMTPServer.setText("SMTP Server Settings");
        jButtonSetSMTPServer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSetSMTPServer.setFocusable(false);
        jButtonSetSMTPServer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSetSMTPServer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSetSMTPServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetSMTPServerActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonSetSMTPServer);
        jToolBar1.add(jSeparator3);

        jPanel4.setBackground(new java.awt.Color(236, 244, 247));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attach files' details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DialogInput", 0, 11))); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(481, 50));

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
                .addComponent(jLabelAttachment)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.getAccessibleContext().setAccessibleName("Attach files details");

        jPanel3.setBackground(new java.awt.Color(193, 207, 208));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Header", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Palatino Linotype", 0, 11))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Palatino Linotype", 0, 11)); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(497, 100));

        jLabelTo.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        jLabelTo.setText("To:");

        jLabelSubject.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        jLabelSubject.setText("Subject:");

        jTextFieldTo.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N

        jTextFieldSubject.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N

        jLabelFrom.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        jLabelFrom.setText("From:");

        jTextFieldFrom.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelFrom)
                    .addComponent(jLabelTo)
                    .addComponent(jLabelSubject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSubject)
                    .addComponent(jTextFieldTo)
                    .addComponent(jTextFieldFrom))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFrom)
                    .addComponent(jTextFieldFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTo)
                    .addComponent(jTextFieldTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSubject)
                    .addComponent(jTextFieldSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(158, 164, 154));

        jMenuFile.setText("File");

        jMenuItemSendMails.setText("Send");
        jMenuItemSendMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSendMailsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSendMails);

        jMenuItemQunFa.setText("Group Send");
        jMenuItemQunFa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemQunFaActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemQunFa);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenu1.setText("Attach");

        jMenuItemAddAttachments.setText("Attach file");
        jMenuItemAddAttachments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddAttachmentsActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAddAttachments);

        jMenuItemAddPictures.setText("Attach picture");
        jMenuItemAddPictures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddPicturesActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAddPictures);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("SMTP Server setting");

        jMenuItemSetSMTPServer.setText("SMTP Server");
        jMenuItemSetSMTPServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetSMTPServerActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSetSMTPServer);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
         this.dispose();
}//GEN-LAST:event_jButtonExitActionPerformed

    private void jButtonAddPicturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPicturesActionPerformed
        addPictures();
    }//GEN-LAST:event_jButtonAddPicturesActionPerformed

    private void jButtonAddAttachmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttachmentsActionPerformed
        addAttachments();
    }//GEN-LAST:event_jButtonAddAttachmentsActionPerformed

    private void jButtonSendMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendMailsActionPerformed
        new Thread(new Runnable() {public void run() { sendMails();  } }).start();        
    }//GEN-LAST:event_jButtonSendMailsActionPerformed

    private void jButtonQunFaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQunFaActionPerformed
        JOptionPane.showMessageDialog(this, "Use ';' between email address,example:one@163.com;two@qq.com;three@gmail.com");
    }//GEN-LAST:event_jButtonQunFaActionPerformed

    private void jButtonSetSMTPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetSMTPServerActionPerformed
        new Thread(new Runnable() {public void run() { new SetSMTPServer().setVisible(true); } }).start();
}//GEN-LAST:event_jButtonSetSMTPServerActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemSendMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSendMailsActionPerformed
        new Thread(new Runnable() { public void run() {sendMails(); } }).start();  
    }//GEN-LAST:event_jMenuItemSendMailsActionPerformed

    private void jMenuItemQunFaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemQunFaActionPerformed
        JOptionPane.showMessageDialog(this, "Use ';' between email address,example:one@163.com;two@qq.com;three@gmail.com");
    }//GEN-LAST:event_jMenuItemQunFaActionPerformed

    private void jMenuItemAddAttachmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddAttachmentsActionPerformed
       addAttachments();
    }//GEN-LAST:event_jMenuItemAddAttachmentsActionPerformed

    private void jMenuItemAddPicturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddPicturesActionPerformed
        addPictures();
    }//GEN-LAST:event_jMenuItemAddPicturesActionPerformed

    private void jMenuItemSetSMTPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetSMTPServerActionPerformed
        new Thread(new Runnable() { public void run() {new SetSMTPServer().setVisible(true);}}).start();
    }//GEN-LAST:event_jMenuItemSetSMTPServerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddAttachments;
    private javax.swing.JButton jButtonAddPictures;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonQunFa;
    private javax.swing.JButton jButtonSendMails;
    private javax.swing.JButton jButtonSetSMTPServer;
    private javax.swing.JLabel jLabelAttachment;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelSubject;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemAddAttachments;
    private javax.swing.JMenuItem jMenuItemAddPictures;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemQunFa;
    private javax.swing.JMenuItem jMenuItemSendMails;
    private javax.swing.JMenuItem jMenuItemSetSMTPServer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JTextArea jTextAreaData;
    private javax.swing.JTextField jTextFieldFrom;
    private javax.swing.JTextField jTextFieldSubject;
    public static javax.swing.JTextField jTextFieldTo;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
