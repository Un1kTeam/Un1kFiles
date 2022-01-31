# Un1kFiles

如果您觉得本插件对您有所帮助,欢迎star,万分感谢!

作者: [@depy](https://github.com/h4ckdepy)

博客: [blog](https://blog.happysec.cn)

## Notice

接口网站在多点ping时长为50ms左右 可以安心使用默认插件 

![image](https://user-images.githubusercontent.com/42985524/151750549-48729081-2b67-42b8-909b-bbf09eca88f9.png)

如果对于加载第三方网站数据十分介意 请自行重新编译接口

## Update log

2022.01.28 - 接入断网检测,防止因为无网使用插件导致菜单卡死,影响使用

![image](https://user-images.githubusercontent.com/42985524/151748047-231a2501-5447-409a-a9d0-e94f7b31c54c.png)

2022.01.28 - 接入guoke提供本地缓存思路,减少远端接口加载次数

![image](https://user-images.githubusercontent.com/42985524/151571727-e00b33d3-adb8-405b-9d67-32676f5cbdd0.png)

## Introduce

**Un1kFiles**是基于 `BurpSuite` 插件 `JavaAPI` 开发的`快速获取常用恶意文件代码`、`常用漏洞测试payload`、`常用配置文件代码`的辅助型插件。

该插件通过获取远程api数据,获取代码列表和代码分类列表,用户通过选择相应的代码分类,再选择需要用到的代码,从而简单迅速得将常用代码拷贝至剪切板中,从而避免由于忘记文件、代码位置导致的无法找到需要使用的代码的情况以及可能因更换环境无法从本机获得代码从而需要四处检索的过程。

这将极大提高我们测试人员在日常渗透测试过程中效率。

## Installation

1.下载插件jar包文件,或者自行编译源代码。

2.插件装载: `Extender - Extensions - Add - Select File - Next`

## Advantages

1. 获取迅速,代码集成
2. 配套web后台管理,方便更新管理
3. 插件端即时更新,无需重新编译、下载jar包

## Details

动图: https://www.xxe.pub/1.gif

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

**以上操作均为实时操作右键点击后拷贝,达到api更新即可插件实时更新使用的效果**

![image](https://user-images.githubusercontent.com/42985524/151457566-ce6533c0-7d38-4766-8b45-3cf2e6e22ea1.png)

## Others

1.本插件仅用于安全测试研究学习 使用时请遵守《中华人民共和国网络安全法》

2.web管理端代码涉及本人不开源业务代码故不公开代码,请自行根据数据获取形式自行编写api,重新编译jar包

3.如果有经常需要使用到的代码以及好用的文件,请在issue分享,采集到一定规模将以sql文件形式共享大家

4.如有bug,请在issue中提出,如果你直接能解决bug,请教我一下,谢谢

## Intranet version

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
        copyMessages("menuItem_php_behinder"); //向私有方法获取php有关的木马代码 后期根据子菜单拓展 拿到冰蝎、哥斯拉的木马区分
        }
});

if(messages.equals("menuItem_php_behinder")){
    StringBuilder py = new StringBuilder("木马代码");
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(py.toString()), this); //操作剪切板
    this.stdout.println("复制**成功!密码pass,请直接粘贴!");
}
```

可以封装成一个方法批量加入,建议只编译木马类有关代码用于文件上传测试。

或者以配置文件形式外部载入插件，具体代码请自行研究实现。
