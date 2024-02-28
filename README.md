请注意，本项目IDE建议为NetBeans 12.0，IDEA可以运行但需要自己进行配置，NetBeans是可以直接识别打开并运行的，jar包不能直接使用的话请用NetBeans运行项目后导出自己的jar包，同样可以实现文档里面的效果。
# BeeHiveMail
网络编程课设|邮件客户端系统开发-蜂巢邮件客户端
# 项目背景
邮件客户端编译语言现在可谓是百家争鸣，精彩纷呈，有经典的C++，也有近期才大力推出的Kotlin。  
但JAVA语言仍旧是当前市场上的主流语言，它发展较为成熟且配置完全，编译环境友好，非常符合本次课设的要求。  
故而本次课设我选择JAVA进行邮件客户端的实现。   
# 环境配置
## 实验环境
语言：JAVA  
IDE：IDEA 21.3/NetBean12.0  
操作系统：Windows10  
JDK版本:JDK 7.0  
运行必需：JDK环境  
## mail.jar包
本项目使用JAVA语言开发，而对于邮件，Sun公司有专门提供处理的API——JavaMail API。   
Sun提供的架构可以较为便捷地进行一些通用的邮件操作，并且不拘于IDE和平台、协议，较为模块化，不易出错。  
# 系统介绍
## 基本要求
难度系数(★★★☆)     邮件客户端系统，要求如下：  
a)       能够同时接收与发送邮件。  
b)      支持多用户的配置  
c)      界面友好，有收件箱、发件箱、已发邮件等 
## 功能汇总 
本邮件客户端总共包含的功能有：  
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

# 需求分析
在此次网络编程——邮件客户端系统课程设计中，主要需要完成的功能是：  
实现一个界面友好的邮件客户端系统，系统需包含收件箱、发件箱、已发邮件箱等；  
同时，能够接收、发送邮件，且支持各类多种用户的登录配置。  
  
因此，必须要利用基于SMTP的简单邮件传输协议和基于POP的接收下载邮件协议。
故而在本课设中，需设计以下基础模块：  
1.Home模块：客户端主页面，由用户自行选择要对邮箱的操作(利用java GUI设计)  
2.Login模块：对登录邮箱客户端的用户进行身份认证  
3.New Mail模块：基于SMTP协议下实现邮件发送功能，可实现一对多的邮件发送  
4.Read Mail模块：基于POP协议下实现邮件接收功能，即能读取邮件信息（附：基于POP协议下实现邮件下载功能）  
5.SMTP模块：连接与用户登录的邮箱的SMTP服务器   

# 设计思路
要开发邮件客户端程序，SMTP协议和POP协议是必不可少的。所以，首先我了解了SMTP和POP3的基本运行原理及方式：  
## ①SMTP协议  
SMTP协议在发送邮件之前，需要利用TCP协议在发送端与接收端之间建立一条连接。  
然后，发送端通过SMTP命令将邮件内容发送到接收端，接收端将接收到的命令和内容处理后，将应答返回给发送端。  
就这样，完成邮件发送的过程。  
## ②POP3协议  
POP3协议要求先登录邮箱，再查看邮件。  
登录需要用户名和密码。  
当登录成功后，就可以查看邮箱状态、查看邮件和管理邮件了。  
##    
因此，我们必须设置SMTP Server Setting模块，模块中需要用户输入SMTP服务器的地址、用户名、密码；  
设置Login模块，模块中需要用户输入用户名、密码、设置服务器。  
这样，不仅可以实现邮件客户端的基础功能(收发邮件)，还能使邮件端支持多用户的配置，  
登录的用户想要修改配置，只需要在SMTP Server Setting和Login模块中更改自己的服务器、用户名、密码即可。  
##  
实现了收发邮件和支持多用户配置的设计之后，我们还需要思考交互性优良的界面(至少包含收件箱、发件箱、已发邮件)，  
所以我采用JAVA GUI设计，可设计菜单栏，用户可以自行选择邮件客户端的功能进行体验。  
##  
除了课程设计的基本要求之外，考虑到这样的邮件客户端太单薄和简略了，我又思考了几个邮件客户端应该包含的功能，编译以添加。  
1.群发邮件/Group Send(各收件地址用’,’逗号隔开)  
2.发送附件/Attach File  
3.下载附件/Download Attach File  
4.回复邮件/Reply  
5.删除邮件/Delete Mail  
6.保存邮件/Save as eml  
7.注销登录/Log Off  
8.退出邮件客户端/Exit  

# 界面UI
## Home界面
Home界面需要进行对邮件的各种处理操作，因此界面较为复杂，Home界面的最终设计如图所示。
![image](https://user-images.githubusercontent.com/66285048/191895399-851192cc-a583-4b96-878c-a5eb950fa76c.png)  
##  Login界面
我们需要提供一个登录界面，让用户输入账号、密码，配置邮箱服务器。Login界面的最终设计如图所示。  
![image](https://user-images.githubusercontent.com/66285048/191895576-7f1d46a1-5483-4ecf-949f-e01e7b8ee228.png)  
## SMTP Server Setting界面
SMTP Server Setting界面主要用来配置用户发送邮件时所需的服务器设置。SMTP Server Setting界面的最终设计如图所示。  
![image](https://user-images.githubusercontent.com/66285048/191895595-4c581243-47ae-4e91-b882-a5fb3ae4cf1c.png)  
## New Mail界面  
New Mail界面需要提供一些文本输入框和一个文本输入区来让用户输入From(收件人)、To(发件人)、Subject(邮件主题)、Text(邮件内容)，  
Attach File(附件)通过提供一个JList来实现。New Mail界面的最终设计如图所示。  
![image](https://user-images.githubusercontent.com/66285048/191895633-df976738-a64b-4aeb-87e9-8b907b3f3949.png)  
## Read Mail界面
Read Mail界面的大致设计和New Mail类似，此处就不再赘述了。Read Mail界面的最终设计如图所示。  
![image](https://user-images.githubusercontent.com/66285048/191895660-ecf5f671-c88e-404f-a3d3-2427c249cb1e.png)  






