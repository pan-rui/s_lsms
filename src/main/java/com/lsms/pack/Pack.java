package com.lsms.pack;

import com.alibaba.fastjson.JSON;
import net.jimmc.jshortcut.JShellLink;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-07-04 10:27)
 * @version: \$Rev: 3996 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-08-08 09:14:56 +0800 (周二, 08 8月 2017) $
 */
public class Pack implements ActionListener {
    public static Pack pack = null;
    public static String setting;
    private JFrame f = null;
    private JPanel topPanel;
    private JLabel hostL;
    private JLabel serverL;
    private JTextField hostT;
    private JTextField serverT;
    private JPanel centerPanel;
    private JButton syncB;
    private JButton startB;
    private JLabel startT;
    private JButton stopB;
    private JLabel stopT;
    private JPanel bottomPanel;
    private JCheckBox autoBox;
    private JTextField descT;
    private static Toolkit kit = Toolkit.getDefaultToolkit();
    private Color background = new Color(212, 208, 199);
    private static String targetDir = "";
    private static String CHARSET = "UTF-8";
    private ScheduledExecutorService schedu = Executors.newScheduledThreadPool(2);

    //    private static String logFile = "C:\\Program Files\\lsms\\output.log";
    public Pack() {
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        UIManager.put("RootPane.setupButtonVisible", false);
//        UIManager.put("JFrame.activeTitleBackground", Color.red);
        try {
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        f = new JFrame("靠得筑两制服务管理器");
        f.setLayout(null);
        f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        try {
            f.setIconImage(ImageIO.read(getClass().getResource("/logo32.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        f.setBackground(new Color(212,208,199));
//        ((JPanel) f.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
//        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
//        JPanel backPanel = new JPanel(new BorderLayout(10,10));
//        backPanel.setBackground(new Color(212, 208, 199));
//        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        topPanel = new JPanel(null);
        topPanel.setBackground(background);
        hostL = new JLabel("服 务 器:");
        hostL.setBounds(534 - 515 + 8, 278 - 232 - 30, 91, 22);
        hostT = new JTextField("127.0.0.1");
        hostT.setBackground(new Color(0xFFFFFF));
        hostT.setBounds(615 - 515 + 8, 278 - 232 - 30, 154, 22);
        hostT.disable();
        serverL = new JLabel("服 务 名:");
        serverL.setBounds(534 - 515 + 8, 278 - 222, 91, 22);
        serverT = new JTextField("靠得筑两制管理客户端");
        serverT.setBackground(new Color(0xFFFFFF));
        serverT.setBounds(615 - 515 + 8, 278 - 222, 154, 22);
        serverT.disable();
        topPanel.add(hostL);
        topPanel.add(hostT);
        topPanel.add(serverL);
        topPanel.add(serverT);
        topPanel.setBounds(0, 0, 347, 100);

//        syncB = new JButton("立 即 同 步");
/*        syncB = new JButton(new ImageIcon(getClass().getResource("/tongbu.png"), "立即同步"));
        syncB.setBounds(635 - 515 + 8, 0, 152, 20);
        syncB.setActionCommand("sync");
//        syncB.addActionListener(this);
        syncB.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                syncB .setIcon(new ImageIcon(getClass().getResource("/tongbu_sel.png"), "立即同步"));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                syncB .setIcon(new ImageIcon(getClass().getResource("/tongbu.png"), "立即同步"));
                sync();
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });*/
        startB = new JButton(new ImageIcon(getClass().getResource("/start_icon_no_choose.png"), "启动"));
        startB.setBounds(615 - 515 + 8, 25, 25, 20);
        startB.setActionCommand("start");
        startB.addActionListener(this);
        startT = new JLabel("开始");
        startT.setBounds(615 - 515 + 8 + 35, 25, 40, 20);
        stopB = new JButton(new ImageIcon(getClass().getResource("/stop_icon.png"), "停止"));
        stopB.setBounds(615 - 515 + 8 + 75, 25, 25, 20);
        stopB.setActionCommand("stop");
        stopB.addActionListener(this);
        stopT = new JLabel("停止");
        stopT.setBounds(615 - 515 + 8 + 110, 25, 40, 20);
        centerPanel = new JPanel(null);
        centerPanel.setBackground(background);
//        centerPanel.add(syncB);
        centerPanel.add(startB);
        centerPanel.add(startT);
        centerPanel.add(stopT);
        centerPanel.add(stopB);
        centerPanel.setBounds(0, 75, 310, 55);

        bottomPanel = new JPanel(null);
        bottomPanel.setBackground(background);
        autoBox = new JCheckBox("当系统启动时自动启动服务", true);
        autoBox.setBounds(0, 0, 310, 20);
        autoBox.setActionCommand("auto");
        autoBox.addActionListener(this);
//        autoBox.setMargin(new Insets(0,0,0,0));
        autoBox.setBackground(background);
        descT = new JTextField("正在运行...");
        descT.disable();
        descT.setBounds(0, 25, 310, 25);
        bottomPanel.add(autoBox);
        bottomPanel.add(descT);
        bottomPanel.setBounds(0, 130, 310, 60);
        f.add(topPanel);
        f.add(centerPanel);
        f.add(bottomPanel);
        f.setBounds((int) (kit.getScreenSize().getWidth() - 310) / 2, (int) (kit.getScreenSize().getHeight() - 215) / 2, 310, 215);
        f.setVisible(true);
        f.setResizable(false);
//        f.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case "start":
                    if (startB.isSelected()) return;
//        r.exec("cmd /c start winword");
                    new Thread(() -> {
                        Runtime r = Runtime.getRuntime();
                        Process process = null;
                        String cmdStr = "@echo off\n" +
                                "%1 %2 \n" +
                                "ver|find \"5.\">nul&&goto :st \n" +
                                "mshta vbscript:createobject(\"shell.application\").shellexecute(\"%~s0\",\"goto :st\",\"\",\"runas\",1)(window.close)&goto :eof \n" +
                                ":st \n" +
                                "call \""+targetDir+"\\lsms.exe\"\n" +
                                ":eof\n" +
                                "exit";
                        String tPath = System.getenv("TEMP") + "\\start.bat";
                            ExecutorUtil.syncExecute(targetDir + "\\lsms.exe");
/*                        try {
                            FileUtils.write(new File(tPath),cmdStr,"UTF-8");
                            process = r.exec("cmd /c call \""+tPath+"\"");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
*//*                            String str = "";
                            while ((str = reader.readLine()) != null) {
                                System.out.println("接收到=======\t" + str);
                            }*//*
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }*/
                    }).start();
                    stopB.setIcon(new ImageIcon(getClass().getResource("/stop_icon.png"), "停止"));
                    startB.setIcon(new ImageIcon(getClass().getResource("/start_icon_no_choose.png"), "启动"));
                    break;
                case "stop":
                    if (stopB.isSelected()) return;
                    String tPath = System.getenv("TEMP") + "\\killLsms.bat";
                    FileUtils.write(new File(tPath), "@echo off\n" +
                            "%1 %2 \n" +
                            "ver|find \"5.\">nul&&goto :st \n" +
                            "mshta vbscript:createobject(\"shell.application\").shellexecute(\"%~s0\",\"goto :st\",\"\",\"runas\",1)(window.close)&goto :eof \n" +
                            ":st \n" +
                            "for /f \"tokens=2\" %%a in ('tasklist ^|find /i \"lsms.exe\"') do taskkill /pid %%a /f\n" +
                            ":eof\n" +
                            "exit", "UTF-8", false);
                    new Thread(()->{
                    Runtime r = Runtime.getRuntime();
                        try {
                            Process process1 = r.exec("cmd /c call \"" + tPath + "\"");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }).start();
                    stopB.setIcon(new ImageIcon(getClass().getResource("/stop_icon_no_choose.png"), "停止"));
                    startB.setIcon(new ImageIcon(getClass().getResource("/start_icon.png"), "启动"));
                    break;
                case "sync":
                    sync();
                    break;
                case "auto":
                    String userHome = System.getProperty("user.home");
                    String dir = "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
                    String linkName = "靠得筑服务.lnk";
//                    r.exec("cmd /c copy C:\\Program Files\\lsms\\靠得筑服务管理.exe "+userHome+dir+linkName);
                    if (autoBox.isSelected()) {
                        createShortCut(targetDir + "靠得筑服务管理.exe " + targetDir, userHome + dir + linkName);
                    } else {
                        new Thread(()->{
                        Runtime r = Runtime.getRuntime();
                            try {
                                r.exec("cmd /c del /F " + userHome + dir + linkName);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }).start();
                    }
                    break;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length < 1) return;
        for (String arg : args)
            targetDir += (" " + arg);
        targetDir = targetDir.substring(1);
/*        if(args.length>0){
            targetDir = args[0];
        }
        File logFi = new File(targetDir + "output.log");
        if (!logFi.exists()) {
            Runtime run = Runtime.getRuntime();
            try {
                File file2 = new File(targetDir + "install.bat");
                if (file2.exists()) {
                    Process process = run.exec(targetDir + "install.bat");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    FileUtils.write(logFi, sb.toString(), "UTF-8", true);
                    TimeUnit.MILLISECONDS.sleep(1000l);
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
        pack = new Pack();
//        pack.autoBox.setText("dsgdsr");
        pack.startB.doClick();
        pack.f.dispose();
        if (SystemTray.isSupported()) {   //判断系统是否托盘
            //创建弹出菜单
            PopupMenu menu = new PopupMenu();
            //创建一个托盘图标对象
            TrayIcon icon = new TrayIcon(kit.getImage(Pack.class.getResource("/logo.png")), "靠得筑客户端", menu);
            MenuItem openItem = new MenuItem("打开");
            openItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pack.f.setVisible(true);
                }
            });
            //添加一个用于退出的按钮
            MenuItem item = new MenuItem("退出");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            menu.add(openItem);
            menu.add(item);
//            menu.add(null);
            SystemTray tray = SystemTray.getSystemTray();
            try {
                tray.add(icon);
            } catch (AWTException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        final Runnable task = new Runnable() {
            private Runnable a = this;

            @Override
            public void run() {
                HttpPost post = new HttpPost("http://127.0.0.1/system/isAlive");
//                    String params = "{\"params\":{\"tenantId\":\"1\",\"password\":\"ZTEwYWRjMzk0OWJhNTlhYmJlNTZlMDU3ZjIwZjg4M2U=\",\"phone\":\"13800000000\"}}";
//                    post.setEntity(new StringEntity(params, CHARSET));
                try {
                    HttpClientBuilder builder = HttpClients.custom();
                    builder.setConnectionTimeToLive(5000L, TimeUnit.MILLISECONDS);
                    builder.setDefaultHeaders(Arrays.asList(new BasicHeader("Content-Type", "application/json; charset=utf-8"), new BasicHeader("Connection", "keep-alive"), new BasicHeader("application2", "1")));
                    HttpEntity entity = builder.build().execute(post).getEntity();
                    String resultStr = EntityUtils.toString(entity, CHARSET);
                    Map<String, Object> returnMap = new HashMap<>();
                    if (resultStr.startsWith("{")) {
                        returnMap = JSON.parseObject(resultStr, Map.class);
                    } else {
                        returnMap.put("code", 1);
                    }
                    if (Integer.parseInt(returnMap.get("code").toString()) != 0) {
                        pack.startB.doClick();
                    }
                    Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            pack.schedu.scheduleAtFixedRate(a, 60l, 180l, TimeUnit.SECONDS);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        pack.schedu.scheduleAtFixedRate(task, 60l, 180l, TimeUnit.SECONDS);

    }

    public static void createShortCut(String filePath, String shortcutPath) {
        JShellLink link = new JShellLink();
        shortcutPath.replaceAll("/", "\\");
        String folder = shortcutPath.substring(0, shortcutPath.lastIndexOf("\\"));
        String name = shortcutPath.substring(shortcutPath.lastIndexOf("\\") + 1, shortcutPath.length());
        link.setName(name);
        link.setFolder(folder);
        link.setPath(filePath);
        link.save();
    }

    public static void sync() {
//        HttpPost post = new HttpPost("http://116.7.226.222:100/labor/login");
        HttpPost post = new HttpPost("http://127.0.0.1/system/synchroAllData");
//        String params = "{\"params\":{\"tenantId\":\"1\",\"password\":\"ZTEwYWRjMzk0OWJhNTlhYmJlNTZlMDU3ZjIwZjg4M2U=\",\"phone\":\"13800000000\"}}";
//        post.setEntity(new StringEntity(params, CHARSET));
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setConnectionTimeToLive(5000L, TimeUnit.MILLISECONDS);
            builder.setDefaultHeaders(Arrays.asList(new BasicHeader("Content-Type", "application/json; charset=utf-8"), new BasicHeader("Connection", "keep-alive"), new BasicHeader("application2", "1")));
            HttpEntity entity = builder.build().execute(post).getEntity();
            String resultStr = EntityUtils.toString(entity, CHARSET);
            Map<String, Object> userInfo = JSON.parseObject(resultStr, Map.class);
            if ((int) userInfo.get("code") == 0) {
/*                HttpPost post2 = new HttpPost("http://116.7.226.222:100/labor/admin/laborAttendanceDetailRecord/addList");
                Map<String, Object> uData = (Map<String, Object>) userInfo.get("data");
                post.addHeader(new BasicHeader("application1", (String) uData.get("token")));
                post.setEntity(new StringEntity(JSON.toJSONStringWithDateFormat(new HashMap(), "yyyy-MM-dd HH:mm:ss"), CHARSET));
                HttpEntity entity2 = builder.build().execute(post2).getEntity();
                String resultStr2 = EntityUtils.toString(entity2, CHARSET);
                Map<String, Object> returnMap = null;
                if (resultStr2.startsWith("{")) {
                    returnMap = JSON.parseObject(resultStr, Map.class);
                    if((int)returnMap.get("code")==0)
                        JOptionPane.showInternalMessageDialog(null,"同步成功!");
                }else
                    JOptionPane.showInternalMessageDialog(null,"同步失败!");*/
                JOptionPane.showInternalMessageDialog(null, "同步成功!");

            } else {
                FileUtils.write(new File(targetDir + "error.log"), resultStr);
                JOptionPane.showInternalMessageDialog(null, "同步失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
