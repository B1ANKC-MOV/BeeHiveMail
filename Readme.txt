(＾▽＾)嗨！亲爱的用户！欢迎使用蜂巢邮件客户端！

一、使用说明O(∩_∩)O：
①在你的邮箱支持SMTP/POP3服务的前提下，你可以登录自己的邮箱进行邮件管理(＾▽＾)
1.前置操作
	使用此邮件客户端，需要先开放你想要登录的邮箱的POP3/SMTP服务
（此处拿163.com邮箱进行示范）
	首先，点击邮箱设置，找到POP3/SMTP，将POP3/SMTP服务开启
	然后，拿到邮箱给的密钥，这才是你要填的password
	最后，记下邮箱的SMTP服务器地址，163邮箱是smtp.163.com
2.用户登录设置
	点开BeeHiveMail.jar打开蜂巢邮件客户端(需有JDK配置环境)
	显示BeeHive客户端的Home界面
	点击Mail Setting->Login Setting设置用户登录配置
	输入账户名和密码（注意，此处的密码不是真正的密码，是你之前拿到的密钥）
	点击Mail Setting->SMTP Server Setting设置服务器配置
3.登录邮箱
	点击OK后返回Home界面，点击Mail Login按钮，进行登录
	这样就登录成功啦！(。＾▽＾)ノ

②本邮件客户端包含的功能有(。＾▽＾)ノ：
	1.登录邮箱/Mail Login
	2.新建邮件/New Mail
	3.群发邮件/Group Send(各收件地址用逗号","隔开)
	4.阅读邮件/Read Mail
	5.回复邮件/Reply
	6.删除邮件/Delete Mail
	7.保存邮件/Save as eml
	8.打开邮件/Open eml
	9.发送附件/Attach File
	10.下载附件/Download Attach File
	11.注销登录/Log Off
	12.退出邮件客户端/Exit

二、运行技巧
①(。＾▽＾)ノ本客户端的运行首先需要JDK环境，请保证自己的运行环境配置好了JDK~

②蜂巢客户端可以单独提取出jar包进行运行，但是要至少将dist文件夹也贴出来（。＾▽＾）~否则点击BeeHiveMail.jar包是无响应的

③教你一招ヾ(≧▽≦*)o
根据域名查询SMTP服务器：
    在DOS命令行中输入：nslookup
    接着输入：set type=mx
    再输入：@后面的主机名（即邮箱域名），即可返回SMTP服务器的主机名
    还可以输入：set type=a
    再输入：返回的SMTP服务器的主机名，便可以得到它的IP地址



