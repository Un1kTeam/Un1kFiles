package burp;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.*;
import java.io.PrintWriter;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;






public class BurpExtender implements IBurpExtender,IContextMenuFactory,ClipboardOwner{

    private final static String NAME = "Un1kFiles";
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {

        //设置拓展名
        callbacks.setExtensionName(NAME); //设置扩展名称为 “Un1kFiles”

        //设置信息输出
        PrintWriter stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("@Name:un1kFiles");
        stdout.println("@Author:depy@happysec.cn");
        stdout.println("@Version:2.1.4");
        stdout.println("@Github:https://github.com/h4ckdepy/Un1kFiles");
        stdout.println("@Introduce:A plug-in for quickly pasting malicious file code.");

        //实例引入输出ui属性
        this.stdout = new PrintWriter(callbacks.getStdout(), true);

        //注册工厂
        callbacks.registerContextMenuFactory(this);
    }

    @Override
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {

        List<JMenuItem> listMenuItems = new ArrayList<JMenuItem>();
        final IHttpRequestResponse[] messages = invocation.getSelectedMessages();
        if (messages == null || messages.length == 0) return null;

        //判断是否是Repeater模块


            JMenu jMenu = new JMenu("Un1kFiles"); //菜单主目录

            //循环添加子父菜单
            String s = this.sendGet("https://www.xxe.pub/api/catas", ""); //接口获取菜单数据
            String temp[] = s.split(";");

            for(String type : temp)
            {
                JMenu menuItem = new JMenu(type); //创建子父菜单php
                String s2 = this.sendGet("https://www.xxe.pub/api/un1kfiles", "cataname="+type); //接口获取php分类下的文件数据
                String temp2[] = s2.split(";"); //该分类下的文件列表
                //循环给子父菜单添加子菜单

                if(s2.equals(null)){

                }else{
                    for(String type2 : temp2) {
                        JMenuItem menuItem2 = new JMenuItem(type2); //冰蝎木马
                        menuItem2.addActionListener(new ActionListener() { //给菜单php添加监听事件
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                copyMessages(type2); //向私有方法获取php有关的木马代码 后期根据子菜单拓展 拿到冰蝎子、哥斯拉的木马区分
                            }
                        });
                        menuItem.add(menuItem2);
                    }
                }
                jMenu.add(menuItem); //添加到子父菜单
            }


            //父级菜单
            listMenuItems.add(jMenu);

        return listMenuItems;
    }





    private void copyMessages(String messages) {

        this.stdout.println("尝试获取"+messages+"相应代码!");
        String s = this.sendGet("https://www.xxe.pub/api/un1kfiles", "filename="+messages); //接口获取菜单数据
        s = base64(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s.toString()), this); //操作剪切板
    }

    private  String base64(String str) {

        //Base64 解码
        byte[] decoded = Base64.getDecoder().decode(str);
        String decodeStr = new String(decoded);
        return decodeStr;
    }


    private String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
