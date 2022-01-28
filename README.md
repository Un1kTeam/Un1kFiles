# Un1kFiles - In order to quickly obtain malicious code

作者: [@depy](https://github.com/h4ckdepy)

博客: [blog](https://blog.happysec.cn)

## 远端接口地址

项目引用接口网站:https://www.xxe.pub

目前可正常使用 实际使用效果不错

![image](https://user-images.githubusercontent.com/42985524/151464700-5e2d371b-148a-4c20-88f0-64dac7fa09fc.png)


## 介绍

**Un1kFiles**是基于 `BurpSuite` 插件 `JavaAPI` 开发的恶意代码、payload快速远程获取的辅助型插件。

该插件通过远程api,获取文件分类表后生成菜单,进一步根据分类生成子菜单,用户通过选择相应的文件、测试payload,简单迅速得将代码拷贝至剪切板中,从而避免繁琐的找代码、找测试payload过程,提高渗透测试效率。


## 使用方法

1.下载插件jar包文件,或者自行编译源代码。

2.插件装载: `Extender - Extensions - Add - Select File - Next`

## 插件优点

1. 获取迅速
2. 配套web后台管理,方便更新管理
3. 插件端即时更新,无需重新编译、下载jar包


## 使用详情

1.后台管理分类、文件、payloads

![image](https://user-images.githubusercontent.com/42985524/151456799-48ebe874-bdf1-463f-abcd-877f03ed0b87.png)

![image](https://user-images.githubusercontent.com/42985524/151456946-1c760ee8-2692-4cd2-9eb9-bdad5de16390.png)

2.插件端动态更新

![image](https://user-images.githubusercontent.com/42985524/151457037-3bfbb09b-781e-4302-a352-f2d5dd06c46d.png)

![image](https://user-images.githubusercontent.com/42985524/151457155-5a9abd5b-a060-47f1-9495-686f2e2e87f2.png)

3.点击选择后 右键粘贴

![image](https://user-images.githubusercontent.com/42985524/151457221-b2cb67cd-0e5e-4e06-8cc8-8d73da5b443b.png)

选择其他木马文件测试:

![image](https://user-images.githubusercontent.com/42985524/151457441-266776f0-3a9d-45ed-a065-f4ffe0a94727.png)

![image](https://user-images.githubusercontent.com/42985524/151457469-6337a253-80d8-4b0f-8038-1d55e47f0fa9.png)



**注意:以上操作均为实时操作右键点击后拷贝,达到api更新即可插件实时更新使用的效果**

![image](https://user-images.githubusercontent.com/42985524/151457566-ce6533c0-7d38-4766-8b45-3cf2e6e22ea1.png)

## 其他

1.本插件仅用于安全测试研究学习 使用时请遵守《中华人民共和国网络安全法》

2.web管理端代码涉及本人不开源业务代码故不公开代码,请自行根据数据获取形式自行编写api,重新编译jar包

3.如果有经常需要使用到的代码以及好用的文件,请在issue分享,采集到一定规模将以sql文件形式共享大家

4.如有bug,请在issue中提出,如果你直接能解决bug,请教我一下,谢谢

## 本地内网版使用

找到对应代码循环添加子父菜单注释下 替换为

```
            JMenu menuItem_php = new JMenu("PHP"); //创建php父菜单
            JMenuItem menuItem_php_behinder = new JMenuItem("冰蝎"); //冰蝎木马
            menuItem_php.add(menuItem_php_behinder);//把冰蝎木马添加到子菜单
```

之后加入监听器

```
            menuItem_php_behinder.addActionListener(new ActionListener() { //给菜单php添加监听事件
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyMessages("menuItem_php_behinder"); //向私有方法获取php有关的木马代码 后期根据子菜单拓展 拿到冰蝎子、哥斯拉的木马区分
                }
            });
            
            if(messages.equals("menuItem_php_behinder")){
            StringBuilder py = new StringBuilder("木马代码");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(py.toString()), this); //操作剪切板
            this.stdout.println("复制冰蝎PHP木马成功!密码pass,请直接粘贴!");
        }
```

可以封装成一个方法批量加入,建议只编译木马类有关代码用于文件上传测试。
