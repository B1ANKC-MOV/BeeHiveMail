package MailManageSystem;

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import java.awt.Cursor;
import java.util.Properties;

import java.util.Vector;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Home_BeeHiveMailSystem extends javax.swing.JFrame {
    public Home_BeeHiveMailSystem() {initComponents();
        BeeInitJTree();//调用系统创建树函数
        BeeInitJTable();//调用系统创建表函数
    }
    
    public static String account;
    public static String password;
    public static String EmailAndress;
    private static String pop3Server;
    public static String smtpServer;
    public static String Toaddress;
    public static String Fromaddress;
    public static String subject;
    public static String BeeDate;
    public static int size = 0;
    
    public static String currentsender = null;
    public static String currentreceiver = null;
    public static Store store = null;
    public static Folder folder = null;
    
    private boolean flag = false;
    private boolean IFMailDeleted=false;
    private static boolean IFInboxSelected = false;
    private static boolean IFSentSelected = false;
    private static boolean IFDeletedItemsSelected = false;
    
    public static int nRN = -1;//选中Inbox
    private static int nRN2 = -1;//选中Sent
    private static int nRN3 = -1;//选中Deleted items
    
    private void BeeInitJTree() {TreeDealAction dealAction = new TreeDealAction();
        jTreeFiles.addTreeSelectionListener(dealAction);
    }
    private void BeeInitJTable(){
        TableDealAction da = new TableDealAction();
        jTableReceivedMails.getSelectionModel().addListSelectionListener(da);
        TableDealAction2 da2 = new TableDealAction2();
        jTableSendedMails.getSelectionModel().addListSelectionListener(da2);
        TableDealAction3 da3 = new TableDealAction3();
        jTableDeletedMails.getSelectionModel().addListSelectionListener(da3);
    }

    private DefaultTreeModel myTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("My Files");
        
        DefaultMutableTreeNode subRoot1 = new DefaultMutableTreeNode("Inbox");
              root.add(subRoot1);
        DefaultMutableTreeNode subRoot2 = new DefaultMutableTreeNode("Sent");
              root.add(subRoot2);
        DefaultMutableTreeNode subRoot3 = new DefaultMutableTreeNode("Deleted items");
              root.add(subRoot3);
        DefaultTreeModel myDefaultTreeModel = new DefaultTreeModel(root);
        
        return myDefaultTreeModel;
    }

    public void DeleteMailOne(int nRNer) {//删除Inbox里的邮件
            int num = nRNer;
        if (num >= 0) {
            try {
                Message[] message = folder.getMessages();
                message[num].setFlag(Flags.Flag.DELETED, true);
                DefaultTableModel BeeModelOne = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableReceivedMails.getModel());
                BeeModelOne.removeRow(num);//Inbox中删除该行
                DefaultTableModel BeeModelTwo = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableDeletedMails.getModel());//Delelted Item Table中添加被删除邮件信息
                Vector newRow = new Vector();                
                newRow.add(subject);//Deleted Items Table中添加主题 
                newRow.add(BeeDate);// 发送时间
                newRow.add(size);// 邮件大小
                newRow.add(Toaddress);//收件人
                newRow.add(Fromaddress);//发件人
                
                BeeModelTwo.getDataVector().add(newRow);
                BeeModelTwo.fireTableStructureChanged();
                
                IFMailDeleted=true;
                }
            catch (Exception e){}
        } 
        else {
            JOptionPane.showMessageDialog(this, "Please choose the mail you want to delete！");
        }
    }

    private void DeleteMailTwo(int nRNer) {//删除Sent里的邮件
            int num = nRNer;
        if (num >= 0) {
            DefaultTableModel BeeModelOne = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableSendedMails.getModel());//Inbox中删除该行
            BeeModelOne.removeRow(num);//Delelted Item table中添加被删除邮件信息
        } 
        else {
            JOptionPane.showMessageDialog(this, "Please choose the mail you want to delete！");
        }
    }

    private void DeleteMailThree(int nRNer) {//删除Deleted Item里的邮件
            int num = nRNer;
        if (num >= 0) {
            DefaultTableModel BeeModelOne = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableDeletedMails.getModel());//Inbox中删除该行
            BeeModelOne.removeRow(num);//Delelted Item table中添加被删除邮件信息
        } 
        else {
            JOptionPane.showMessageDialog(this, "Please choose the mail you want to delete！！");
        }
    }

    private void login() 
    {//用户登录函数        
        if (account == null || password == null || EmailAndress == null) {
            JOptionPane.showMessageDialog(this, "Please enter your account and password，then click login button！");
            new Thread(new Runnable() { public void run() { new Login().setVisible(true); }}).start();
        } 
        else 
        {
            jButtonLogin.setEnabled(false);
            if (EmailAndress.equals("Please choose your email address")) {
                JOptionPane.showMessageDialog(this, "Notice:Please choose your login email！");
                jButtonLogin.setEnabled(true);
                return;
            } 
            else if (EmailAndress.equals("@163.com")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.163.com";
                pop3Server = "pop3.163.com";
                flag = true;
            } 
            else if (EmailAndress.equals("@qq.com")) {      
                currentsender = account + EmailAndress;
                smtpServer = "smtp.qq.com";
                pop3Server = "pop.qq.com";
                flag = true;
            } 
            else if (EmailAndress.equals("@gmail.com")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.gmail.com";
                pop3Server = "pop.gmail.com";
                flag = true;
            } 
            else if (EmailAndress.equals("@tom.com")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.tom.com";
                pop3Server = "pop.tom.com";
                flag = true;
            } 
            else if (EmailAndress.equals("@sohu.com")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.sohu.com";
                pop3Server = "pop3.sohu.com";
                flag = true;
            } 
            else if (EmailAndress.equals("@sina.com")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.sina.com";
                pop3Server = "pop3.sina.com";
            } 
            else if (EmailAndress.equals("@cquc.edu.cn")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.cquc.edu.cn";
                pop3Server = "pop3.cquc.edu.cn";
                flag = true;
            } 
            else if (EmailAndress.equals("@yahoo.com.cn")) {
                currentsender = account + EmailAndress;
                smtpServer = "smtp.mail.yahoo.com.cn";
                pop3Server = "pop.mail.yahoo.com.cn";
                flag = true;
            } 
            else {
                flag = false;
                JOptionPane.showMessageDialog(this, "Sorry，the Email's SMTP Server don't support SMTP/POP3,login failed！");
            }
            
            if (flag) 
            {
                try 
                {
                    Properties prop = new Properties();
                    prop.setProperty("mail.pop3.host", pop3Server);
                    prop.setProperty("mail.store.protocol", "pop3");
                   
                    Session mailSession = Session.getDefaultInstance(prop, null);
                    mailSession.setDebug(false);
                    
                    store = mailSession.getStore("pop3");
                    store.connect(pop3Server, account, password);
                    if (store.isConnected()) 
                    {
                        folder = store.getFolder("inbox");
                        folder.open(Folder.READ_WRITE);
                        Message[] message = folder.getMessages();
                        int num = message.length;
                        System.out.print("****************"+num);
                        for (int i = 0; i < num; i++) {
                            while(folder.getMessages()[i].getRecipients(Message.RecipientType.TO).length==0){i=i+1;}
                            String subject1 = message[i].getSubject();
                            String from1 = message[i].getFrom()[0].toString();
                            String from = from1;
                            if (from.endsWith(">")) { from = from1.substring(from1.indexOf("<") + 1, from1.lastIndexOf(">")); }
                            String to1 = folder.getMessages()[i].getRecipients(Message.RecipientType.TO)[0].toString();
                            String to = to1;
                            if (to.endsWith(">")) { to = to1.substring(to1.indexOf("<") + 1, to.lastIndexOf(">")); }
                            String BeeDate1 = DateFormat.getInstance().format(message[i].getSentDate());
                            int size1 = message[i].getSize();
                            DefaultTableModel BeeModel = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableReceivedMails.getModel());
                            Vector newRow = new Vector();
                            newRow.add(from);//给Inbox table添加 发件人
                            newRow.add(to);//收件人
                            newRow.add(subject1);//主题
                            newRow.add(BeeDate1);//发送时间
                            newRow.add(size1);//邮件大小
                             
                            BeeModel.getDataVector().add(newRow);
                            BeeModel.fireTableStructureChanged();
                         }
                        jMenuItemLogOff.setEnabled(true);
                        jMenuItemDeleteMail.setEnabled(true);
                        jMenuItemReadMail.setEnabled(true);
                        jButtonReadMail.setEnabled(true);
                        jButtonDelete.setEnabled(true);
                        jButtonLogOff.setEnabled(true);
                    }
                } 
                catch (AuthenticationFailedException e) {
                    JOptionPane.showMessageDialog(this, "Connection failed,please enter correct account and password！");
                    jButtonLogin.setEnabled(true);
                }
                catch (IllegalStateException e) {
                    JOptionPane.showMessageDialog(this, "Sorry，it's connected！");
                    jButtonLogin.setEnabled(true);
                }                          
                catch (MessagingException e) {
                    JOptionPane.showMessageDialog(this, "Unknown reasons cause login failure,please check whether the account has opened SMTP/POP3 service or not ！");//由于其他未知原因不能登录,请确认该账号是否开启SMTP/POP3服务
                    jButtonLogin.setEnabled(true);
                }
                catch (Exception e) {
                    System.out.print(e);
                    JOptionPane.showMessageDialog(this, "Error,please check whether the account has opened SMTP/POP3 service or not！");
                    jButtonLogin.setEnabled(true);
                }
            }
        }
    }

    private void logOff() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        account = null;//ALLFree,全置空
        password = null;
        EmailAndress = null;
        currentsender = null;
        currentreceiver = null;
        Toaddress = null;
        Fromaddress = null;
        subject = null;
        BeeDate = null;
        try 
        {
            if(IFMailDeleted)
            {
                int returnValue = JOptionPane.showConfirmDialog(this, "Do you want to delete all “deleted” mails completely? ","Please take notice that...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (returnValue == JOptionPane.YES_OPTION) 
                {
                    folder.close(true);
                    store.close();
                } 
                else if (returnValue == JOptionPane.NO_OPTION)
                {
                    folder.close(false);
                    store.close();
                }
            } 
            else 
            {
                folder.close(false);
                store.close();
            }
            
            DefaultTableModel BeeModelOne = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableReceivedMails.getModel());
            int num1 = jTableReceivedMails.getRowCount();
            for (int i = num1 - 1; i >= 0; i--) { BeeModelOne.removeRow(i); }
            DefaultTableModel BeeModelTwo = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableSendedMails.getModel());
            int num2 = jTableSendedMails.getRowCount();
            for (int j = num2 - 1; j >= 0; j--) { BeeModelTwo.removeRow(j); }
            DefaultTableModel BeeModelThree = ((DefaultTableModel) Home_BeeHiveMailSystem.jTableDeletedMails.getModel());
            int num3 = jTableDeletedMails.getRowCount();
            for (int k = num3 - 1; k >= 0; k--) { BeeModelThree.removeRow(k);}
        } 
        catch (Exception e) {} 
        finally 
        {
            jEditorPaneContent.setText("");
            nRN = -1;
            jButtonLogin.setEnabled(true);
            jButtonReadMail.setEnabled(false);
            jButtonDelete.setEnabled(false);
            jButtonLogOff.setEnabled(false);
            jMenuItemLogOff.setEnabled(false);
            jMenuItemDeleteMail.setEnabled(false);
            jMenuItemReadMail.setEnabled(false);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButtonLogin = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButtonNewMail = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonReadMail = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonDelete = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabelSelct = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeFiles = new javax.swing.JTree();
        jSplitPane1 = new javax.swing.JSplitPane();
        jLayeredPane = new javax.swing.JLayeredPane();
        jScrollPaneReceivedMails = new javax.swing.JScrollPane();
        jTableReceivedMails = new javax.swing.JTable();
        jScrollPaneSendedMails = new javax.swing.JScrollPane();
        jTableSendedMails = new javax.swing.JTable();
        jScrollPaneDeletedMails = new javax.swing.JScrollPane();
        jTableDeletedMails = new javax.swing.JTable();
        jButtonExit = new javax.swing.JButton();
        jButtonHelp = new javax.swing.JButton();
        jButtonLogOff = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPaneContent = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemDengLu = new javax.swing.JMenuItem();
        jMenuItemLogOff = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemDeleteMail = new javax.swing.JMenuItem();
        jMenuItemNewMail = new javax.swing.JMenuItem();
        jMenuItemReadMail = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemSetdengLu = new javax.swing.JMenuItem();
        jMenuItemSetSMTP = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemIntroduction = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Beehive Email");
        setBackground(new java.awt.Color(158, 164, 154));

        jToolBar1.setBackground(new java.awt.Color(193, 207, 208));
        jToolBar1.setForeground(new java.awt.Color(204, 255, 255));
        jToolBar1.setRollover(true);
        jToolBar1.setToolTipText("");
        jToolBar1.setBorderPainted(false);

        jButtonLogin.setBackground(new java.awt.Color(232, 218, 209));
        jButtonLogin.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonLogin.setForeground(new java.awt.Color(158, 164, 154));
        jButtonLogin.setText("Mail Login");
        jButtonLogin.setFocusable(false);
        jButtonLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLogin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonLogin);
        jToolBar1.add(jSeparator6);

        jButtonNewMail.setBackground(new java.awt.Color(232, 218, 209));
        jButtonNewMail.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonNewMail.setForeground(new java.awt.Color(158, 164, 154));
        jButtonNewMail.setText("New Mail");
        jButtonNewMail.setFocusable(false);
        jButtonNewMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNewMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewMailActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonNewMail);
        jToolBar1.add(jSeparator2);

        jButtonReadMail.setBackground(new java.awt.Color(232, 218, 209));
        jButtonReadMail.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonReadMail.setForeground(new java.awt.Color(158, 164, 154));
        jButtonReadMail.setText("Read Mail");
        jButtonReadMail.setEnabled(false);
        jButtonReadMail.setFocusable(false);
        jButtonReadMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonReadMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonReadMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadMailActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonReadMail);
        jToolBar1.add(jSeparator1);

        jButtonDelete.setBackground(new java.awt.Color(232, 218, 209));
        jButtonDelete.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonDelete.setForeground(new java.awt.Color(158, 164, 154));
        jButtonDelete.setText("Delete Mail");
        jButtonDelete.setEnabled(false);
        jButtonDelete.setFocusable(false);
        jButtonDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDelete);
        jToolBar1.add(jSeparator5);
        jToolBar1.add(jSeparator7);
        jToolBar1.add(jSeparator4);
        jToolBar1.add(jSeparator3);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        jLabel1.setText("Welcome to the Mail System ^-^ ");
        jLabel1.setAlignmentY(0.3F);
        jToolBar1.add(jLabel1);

        jToolBar3.setBackground(new java.awt.Color(193, 203, 215));
        jToolBar3.setForeground(new java.awt.Color(193, 203, 215));
        jToolBar3.setRollover(true);
        jToolBar3.setToolTipText("");
        jToolBar3.setAlignmentY(0.5F);
        jToolBar3.setBorderPainted(false);
        jToolBar3.setFont(new java.awt.Font("Palatino Linotype", 0, 11)); // NOI18N
        jToolBar3.setOpaque(false);
        jToolBar3.add(jLabelSelct);

        jToolBar1.add(jToolBar3);

        jSplitPane2.setDividerLocation(120);
        jSplitPane2.setDividerSize(3);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(100, 300));

        jTreeFiles.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTreeFiles.setFont(new java.awt.Font("Palatino Linotype", 0, 11)); // NOI18N
        jTreeFiles.setModel(myTreeModel());
        jTreeFiles.setAutoscrolls(true);
        jTreeFiles.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jScrollPane1.setViewportView(jTreeFiles);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setAutoscrolls(true);

        jLayeredPane.setBackground(new java.awt.Color(236, 244, 247));

        jTableReceivedMails.setBackground(new java.awt.Color(236, 244, 247));
        jTableReceivedMails.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        jTableReceivedMails.setForeground(new java.awt.Color(158, 164, 154));
        jTableReceivedMails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From", "To", "Subject", "Date", "File Size(B)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableReceivedMails.setGridColor(new java.awt.Color(236, 244, 247));
        jTableReceivedMails.setSelectionForeground(new java.awt.Color(236, 244, 247));
        jTableReceivedMails.setShowGrid(false);
        jScrollPaneReceivedMails.setViewportView(jTableReceivedMails);

        jLayeredPane.add(jScrollPaneReceivedMails);
        jScrollPaneReceivedMails.setBounds(0, 0, 600, 400);

        jTableSendedMails.setFont(new java.awt.Font("Palatino Linotype", 0, 11)); // NOI18N
        jTableSendedMails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From", "To", "Subject", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneSendedMails.setViewportView(jTableSendedMails);

        jLayeredPane.add(jScrollPaneSendedMails);
        jScrollPaneSendedMails.setBounds(0, 0, 600, 400);

        jTableDeletedMails.setAutoCreateRowSorter(true);
        jTableDeletedMails.setFont(new java.awt.Font("Palatino Linotype", 0, 11)); // NOI18N
        jTableDeletedMails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From", "To", "Subject", "Date", "File Size(B)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneDeletedMails.setViewportView(jTableDeletedMails);

        jLayeredPane.add(jScrollPaneDeletedMails);
        jScrollPaneDeletedMails.setBounds(0, 0, 600, 400);

        jButtonExit.setBackground(new java.awt.Color(248, 235, 216));
        jButtonExit.setFont(new java.awt.Font("Palatino Linotype", 0, 24)); // NOI18N
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
        jLayeredPane.add(jButtonExit);
        jButtonExit.setBounds(600, 200, 130, 50);

        jButtonHelp.setBackground(new java.awt.Color(248, 235, 216));
        jButtonHelp.setFont(new java.awt.Font("Palatino Linotype", 0, 24)); // NOI18N
        jButtonHelp.setForeground(new java.awt.Color(162, 153, 136));
        jButtonHelp.setText("Help");
        jButtonHelp.setFocusable(false);
        jButtonHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHelpActionPerformed(evt);
            }
        });
        jLayeredPane.add(jButtonHelp);
        jButtonHelp.setBounds(600, 250, 130, 50);

        jButtonLogOff.setBackground(new java.awt.Color(248, 235, 216));
        jButtonLogOff.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jButtonLogOff.setForeground(new java.awt.Color(158, 164, 154));
        jButtonLogOff.setText("Log Off");
        jButtonLogOff.setToolTipText("");
        jButtonLogOff.setEnabled(false);
        jButtonLogOff.setFocusable(false);
        jButtonLogOff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLogOff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogOffActionPerformed(evt);
            }
        });
        jLayeredPane.add(jButtonLogOff);
        jButtonLogOff.setBounds(600, 150, 130, 50);

        jSplitPane1.setLeftComponent(jLayeredPane);

        jEditorPaneContent.setBackground(new java.awt.Color(236, 244, 247));
        jScrollPane2.setViewportView(jEditorPaneContent);

        jSplitPane1.setRightComponent(jScrollPane2);

        jSplitPane2.setRightComponent(jSplitPane1);

        jMenuBar1.setBackground(new java.awt.Color(158, 164, 154));
        jMenuBar1.setForeground(new java.awt.Color(162, 153, 136));

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("File");
        jMenu1.setToolTipText("");
        jMenu1.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N

        jMenuItemDengLu.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemDengLu.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemDengLu.setText("Login");
        jMenuItemDengLu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDengLuActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemDengLu);

        jMenuItemLogOff.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemLogOff.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemLogOff.setText("Log Off");
        jMenuItemLogOff.setEnabled(false);
        jMenuItemLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogOffActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemLogOff);

        jMenuItemExit.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemExit.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExit);

        jMenuBar1.add(jMenu1);

        jMenu3.setBackground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("Tools");
        jMenu3.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N

        jMenuItemDeleteMail.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemDeleteMail.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemDeleteMail.setText("Delete Mail");
        jMenuItemDeleteMail.setEnabled(false);
        jMenuItemDeleteMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteMailActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemDeleteMail);

        jMenuItemNewMail.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemNewMail.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemNewMail.setText("New Mail");
        jMenuItemNewMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewMailActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemNewMail);

        jMenuItemReadMail.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemReadMail.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemReadMail.setText("Read Mail");
        jMenuItemReadMail.setEnabled(false);
        jMenuItemReadMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReadMailActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemReadMail);

        jMenuBar1.add(jMenu3);

        jMenu2.setBackground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Mail Setting");
        jMenu2.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N

        jMenuItemSetdengLu.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemSetdengLu.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemSetdengLu.setText("Login Setting");
        jMenuItemSetdengLu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetdengLuActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSetdengLu);

        jMenuItemSetSMTP.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemSetSMTP.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemSetSMTP.setText("SMTP Server Setting");
        jMenuItemSetSMTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetSMTPActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSetSMTP);

        jMenuBar1.add(jMenu2);

        jMenu4.setBackground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Help");
        jMenu4.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N

        jMenuItemIntroduction.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItemIntroduction.setFont(new java.awt.Font("Palatino Linotype", 0, 12)); // NOI18N
        jMenuItemIntroduction.setText("ReadMe");
        jMenuItemIntroduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIntroductionActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemIntroduction);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
}//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        System.exit(0);
}//GEN-LAST:event_jButtonExitActionPerformed

    private void jButtonNewMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewMailActionPerformed
        new Thread(new Runnable() { public void run() { new SendMail().setVisible(true);}}).start();
    }//GEN-LAST:event_jButtonNewMailActionPerformed

    private void jButtonReadMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadMailActionPerformed
        if (nRN >= 0) {
            new Thread(new Runnable() {public void run() {new ReadMail().setVisible(true);}}).start();
        } 
        else {
            JOptionPane.showMessageDialog(this, "Please choose the mail you want to read!");
        }        
    }//GEN-LAST:event_jButtonReadMailActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        int returnValue = JOptionPane.showConfirmDialog(this, "Do you want to delete this mail?", "Confirm deletion of mail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.YES_OPTION)
        {
            if (IFInboxSelected && !IFSentSelected && !IFDeletedItemsSelected) {
                DeleteMailOne(nRN);
            } 
            else if (!IFInboxSelected && IFSentSelected && !IFDeletedItemsSelected) {
                DeleteMailTwo(nRN2);
            } 
            else if (!IFInboxSelected && !IFSentSelected && IFDeletedItemsSelected) {
                DeleteMailThree(nRN3);
            }
        }      
}//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        new Thread(new Runnable() { public void run() { login();} }).start();
}//GEN-LAST:event_jButtonLoginActionPerformed

    private void jButtonLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOffActionPerformed
        logOff();
}//GEN-LAST:event_jButtonLogOffActionPerformed

    private void jMenuItemDengLuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDengLuActionPerformed
        new Thread(new Runnable() {public void run() { login();}}).start();
}//GEN-LAST:event_jMenuItemDengLuActionPerformed

    private void jMenuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogOffActionPerformed
        logOff();
}//GEN-LAST:event_jMenuItemLogOffActionPerformed

    private void jMenuItemSetdengLuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetdengLuActionPerformed
        new Thread(new Runnable() {public void run() {new Login().setVisible(true);}}).start();
}//GEN-LAST:event_jMenuItemSetdengLuActionPerformed

    private void jMenuItemSetSMTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetSMTPActionPerformed
        new Thread(new Runnable() {public void run() { new SetSMTPServer().setVisible(true);}}).start();
}//GEN-LAST:event_jMenuItemSetSMTPActionPerformed

    private void jMenuItemDeleteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteMailActionPerformed
        int returnValue = JOptionPane.showConfirmDialog(this, "Do you want to delete this mail?", "Confirm deletion of mail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (returnValue == JOptionPane.YES_OPTION) 
        {
            if (IFInboxSelected && !IFSentSelected && !IFDeletedItemsSelected) 
            {
                DeleteMailOne(nRN);
            } 
            else if (!IFInboxSelected && IFSentSelected && !IFDeletedItemsSelected) 
            {
                DeleteMailTwo(nRN2);
            } 
            else if (!IFInboxSelected && !IFSentSelected && IFDeletedItemsSelected) 
            {
                DeleteMailThree(nRN3);
            }
        }      
}//GEN-LAST:event_jMenuItemDeleteMailActionPerformed

    private void jMenuItemNewMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewMailActionPerformed
        new Thread(new Runnable() {public void run() {new SendMail().setVisible(true);}}).start();
    }//GEN-LAST:event_jMenuItemNewMailActionPerformed

    private void jMenuItemReadMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReadMailActionPerformed
        if (nRN >= 0) 
        {
            new Thread(new Runnable() {public void run() { new ReadMail().setVisible(true);}}).start();
        } 
        else 
        {
            JOptionPane.showMessageDialog(this, "Please choose the mail you want to read！");//"请选择所要读取的邮件！"
        }        
    }//GEN-LAST:event_jMenuItemReadMailActionPerformed

    private void jMenuItemIntroductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIntroductionActionPerformed
        try {
            Runtime.getRuntime().exec("notepad Readme.txt");
        } 
        catch (Exception e) {}
    }//GEN-LAST:event_jMenuItemIntroductionActionPerformed

    private void jButtonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHelpActionPerformed
        try {
            Runtime.getRuntime().exec("notepad Readme.txt");
        } 
        catch (Exception e) {}
}//GEN-LAST:event_jButtonHelpActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {public void run() {new Home_BeeHiveMailSystem().setVisible(true); }});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonHelp;
    private javax.swing.JButton jButtonLogOff;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonNewMail;
    private javax.swing.JButton jButtonReadMail;
    private javax.swing.JEditorPane jEditorPaneContent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelSelct;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemDeleteMail;
    private javax.swing.JMenuItem jMenuItemDengLu;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemIntroduction;
    private javax.swing.JMenuItem jMenuItemLogOff;
    private javax.swing.JMenuItem jMenuItemNewMail;
    private javax.swing.JMenuItem jMenuItemReadMail;
    private javax.swing.JMenuItem jMenuItemSetSMTP;
    private javax.swing.JMenuItem jMenuItemSetdengLu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneDeletedMails;
    private javax.swing.JScrollPane jScrollPaneReceivedMails;
    private javax.swing.JScrollPane jScrollPaneSendedMails;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    public static javax.swing.JTable jTableDeletedMails;
    public static javax.swing.JTable jTableReceivedMails;
    public static javax.swing.JTable jTableSendedMails;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTree jTreeFiles;
    // End of variables declaration//GEN-END:variables
    class TreeDealAction implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent event1) {
            TreePath tp = event1.getNewLeadSelectionPath();
            if (tp != null) 
            {
                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
                String select = tempNode.toString();
                jLabelSelct.setText("You select " + select + "♥");
                if (select.equals("Sent")) 
                {
                    jLayeredPane.moveToFront(jScrollPaneSendedMails);
                } 
                else if (select.equals("Deleted items")) 
                {
                    jLayeredPane.moveToFront(jScrollPaneDeletedMails);
                } 
                else 
                {
                    jLayeredPane.moveToFront(jScrollPaneReceivedMails);
                }
            }
        }
    }

    class TableDealAction implements ListSelectionListener 
    {
        int t = 0;
        public void valueChanged(ListSelectionEvent e) 
        {
            t++;
            if (t == 2) { 
                createMessage();
                t = 0;
            }
        }
        public void createMessage() 
        {
            IFInboxSelected = true;
            IFSentSelected = false;
            IFDeletedItemsSelected = false;
            nRN = jTableReceivedMails.getSelectedRow();
            jLabelSelct.setText("NO." + (nRN + 1) + " mail has been selected ♥");
            try 
            {
                Date senddate = folder.getMessages()[nRN].getSentDate();
                BeeDate = DateFormat.getInstance().format(senddate);
                subject = folder.getMessages()[nRN].getSubject();
                size = folder.getMessages()[nRN].getSize();
                String from1 = folder.getMessages()[nRN].getFrom()[0].toString();
                Fromaddress = from1;
                if (Fromaddress.endsWith(">")) {Fromaddress = from1.substring(from1.indexOf("<") + 1, from1.lastIndexOf(">"));}
                String to = folder.getMessages()[nRN].getRecipients(Message.RecipientType.TO)[0].toString();
                Toaddress = to;
                if (Toaddress.endsWith(">")) {Toaddress = to.substring(to.indexOf("<") + 1, to.lastIndexOf(">")); }
                jEditorPaneContent.setText(
                        "To：" + Toaddress + 
                        "\nFrom：" + Fromaddress + 
                        "\nSubject：" + subject + 
                        "\nDate：" + BeeDate + 
                        "\nFile Size：" + Integer.toString(size) + "B" +
                        "\nFile Type：" + folder.getMessages()[nRN].getContentType());
                new Thread(new Runnable() {  public void run() { new ReadMail().setVisible(true); } }).start();
            } 
            catch (Exception e) {}
        }
    }

    class TableDealAction2 implements ListSelectionListener 
    {
        public void valueChanged(ListSelectionEvent e) 
        {
            IFInboxSelected = false;
            IFSentSelected = true;
            IFDeletedItemsSelected = false;
            nRN2 = jTableSendedMails.getSelectedRow();
            jLabelSelct.setText("NO." + (nRN2 + 1) + "mail has been selected ♥");
        }
    }

    class TableDealAction3 implements ListSelectionListener 
    {
        public void valueChanged(ListSelectionEvent e) 
        {
            IFInboxSelected = false;
            IFSentSelected = false;
            IFDeletedItemsSelected = true;
            nRN3 = jTableDeletedMails.getSelectedRow();
            jLabelSelct.setText("NO." + (nRN3 + 1) + "mail has been selected ♥");
        }
    }
}
