package burp;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.math.BigInteger;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.net.URL;
import java.net.URLConnection;
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
        stdout.println("@Name:Un1kFiles");
        stdout.println("@Author:depy@happysec.cn");
        stdout.println("@Version:3.1.6");
        stdout.println("@Github:https://github.com/h4ckdepy/Un1kFiles");
        stdout.println("@Introduce:A plug-in for quickly pasting malicious code.");
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        //检测网络是否畅通 防止因为无网络环境导致右键菜单卡死的问题
        String host = "https://www.xxe.pub";


        int status = 0;

        try {
            status = this.testWsdlConnection(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(status == 200){
            this.stdout.println("[+] API connect success.");
            //获取更新
            this.checkupdate();
            //注册工厂
            callbacks.registerContextMenuFactory(this);
        }else{
            this.stdout.println("[-] API connect error.The plug-in was temporarily closed.");
        }
    }

    //测试接口是否可以访问
    public  int testWsdlConnection(String address) throws Exception {
        int status = 404;
        try {
            URL urlObj = new URL(address);
            HttpURLConnection oc = (HttpURLConnection) urlObj.openConnection();
            oc.setUseCaches(false);
            oc.setReadTimeout(1000);
            status = oc.getResponseCode();// 请求状态
            if (200 == status) {
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return status;

    }

    //菜单创建
    @Override
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {

        List<JMenuItem> listMenuItems = new ArrayList<JMenuItem>();
        JMenu jMenu = new JMenu("Un1kFiles"); //菜单主目录
        //检测api连通性

        int teststatus = 0;

        try {
            teststatus = this.testWsdlConnection("https://www.xxe.pub");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(teststatus == 200){
            this.checkupdate(); //检查更新
            //循环添加子父菜单
            String fullFilePath = System.getProperty("java.io.tmpdir")+"updatecheckcatas"; //从缓存文件中获取菜单字符串;
            String txt = null;
            try {
                txt = this.readFileByPath(fullFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String temp[] = txt.split(";"); //切割字符串 拿到菜单

            for(String type : temp)

            {

                JMenu menuItem = new JMenu(type); //创建子父菜单
                String pathtype = System.getProperty("java.io.tmpdir")+this.StringToMd5(type); //获取菜单保存的文件地址
                String s2 = null;

                try {
                    s2 = this.readFileByPath(pathtype);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        }else{
            this.stdout.println("网络异常。");
            JMenuItem errornet = new JMenuItem("NetworkError");
            listMenuItems.add(errornet);
            return listMenuItems;
        }

    }

    //检查更新
    private void checkupdate(){

        String update_md5 = this.sendGet("https://www.xxe.pub/api/un1kfiles/detail", ""); //接口获取最新版md5
        String path = System.getProperty("java.io.tmpdir")+"updatecheck"; //临时文件写入地址
        File file=new File(path); //实例化更新校验文件

        if(!file.exists() || !this.md5(file).equals(update_md5)) //文件不存在或更新md5与本地md5不同 代表需要更新
        {
            try {
                String content = this.sendGet("https://www.xxe.pub/api/un1kfiles", ""); //获取最新文件列表
                FileWriter writer;
                writer = new FileWriter(path);
                writer.write(content);
                writer.flush();
                writer.close();
                this.stdout.println("[+] Remote update request found, trying to update.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = this.sendGet("https://www.xxe.pub/api/catas", ""); //接口获取菜单数据
            String pathcatas = System.getProperty("java.io.tmpdir")+"updatecheckcatas"; //临时文件写入地址

            try {
                FileWriter writer;
                writer = new FileWriter(pathcatas);
                writer.write(s);
                writer.flush();
                writer.close();
                this.stdout.println("[+] Trying to update menus.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] temp = s.split(";");

            for(String type : temp){
                String pathtype = System.getProperty("java.io.tmpdir")+this.StringToMd5(type); //临时文件写入地址
                String typefiles = this.sendGet("https://www.xxe.pub/api/un1kfiles", "cataname="+type); //接口获取php分类下的文件数据
                try {
                    FileWriter writer;
                    writer = new FileWriter(pathtype);
                    writer.write(typefiles);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.stdout.println("[!]"+this.StringToMd5(type)+" has written.");
            }
        }else{
            this.stdout.println("[-] Verification passed, no update available.");
        }
    }

    //根据菜单获取代码
    private void copyMessages(String messages) {

        this.stdout.println("尝试获取"+messages+"相应代码!");
        String s = this.sendGet("https://www.xxe.pub/api/un1kfiles", "filename="+messages); //接口获取菜单数据
        s = base64(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s.toString()), this); //操作剪切板
    }

    //字符串转md5
    public  String StringToMd5(String psw) {
        {
            try {
                MessageDigest md5 = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md5.update(psw.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    //Base64 解码
    private  String base64(String str) {

        byte[] decoded = Base64.getDecoder().decode(str);
        String decodeStr = new String(decoded);
        return decodeStr;
    }

    //发送get请求
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

    //获取文件md5
    public  String md5(File file) {
        MessageDigest digest = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];

        try {
            if (!file.isFile()) {
                return "";
            }

            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            while (true) {
                int len;
                if ((len = fis.read(buffer, 0, 1024)) == -1) {
                    fis.close();
                    break;
                }

                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger var5 = new BigInteger(1, digest.digest());
        return String.format("%1$032x", new Object[]{var5});
    }

    //获取指定路径文件字符串
    public  String readFileByPath(String fullFilePath) throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            File file = new File(fullFilePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
