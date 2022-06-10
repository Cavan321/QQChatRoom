# QQChatRoom
简易版QQ聊天室

聊天窗口的搭建。在进行聊天窗口的搭建过程中，先要去声明我们所需要的组件并通过构造器对其进行实例化。当我们想要使用Java创建窗体的话，需要时使用JFrame来进行窗体的创建。因为窗体不能进行布局，所以我们需要一个容器进行布局，容器是依赖于窗体的。在此项目中，我通过边界布局，对相应组件进行布局，以及相应的一些事件监听等。

登陆页面的搭建 。在对登录页面进行搭建的时候，我们可以先进性一些思考，对登录页面所需要的组件，以及后期如何使用登录页面进行一些思考。这样能避免我们在后期使用登录页面的时候少走一些弯路，节约一些JVM的资源。当我们打开聊天程序的时候，首先进入的便是登陆页面，只有登陆成功，才能回跳到“聊天窗口”，所以我们在聊天窗口通过方法对窗口组件进行实例化，便于满足登录时的调用。在登录页面，我们可以使本类继承JFrame类，通过直接父类或者间接父类的中的方法来对登录页面进行大致框图的布局。当我们点击“登录、关闭”按钮时，会执行相应的操作，因此需要我们对其添加事件监听。
用户模型类、信息匹配类以及相应属性，对应的getXxx()和setXxx()方法，构造器，重写toString()方法，IO流等操作完成对用户输入的账号密码与文件中的账号密码进行校验操作。

客户端和服务端的搭建。我认为这一步也是此次项目逻辑相对复杂最难理解的一步。通过Socket创建客户端，传入IP和端口号,以保证跟构建好的服务端建立连接，通过IO流实现客户端与服务端的信息交互。对于当我们输入账号密码进行登录时，点击登陆后，我们输入的账号密码就会发送到服务端，在服务端中进行校验。当我们构建服务端时，需要用套Socket套接字，通过accept()方法监听向这个socket的连接并接收连接，它将会阻塞直到连接被建立好。通过Socet创建客户端，传入IP和端口，以确定能够和服务端建立连接。在客户端，使用IO流进从键盘读入数据，以实现跟客户端进行信息交互。

聊天功能的搭建和限流操作。主要操作是给相应按钮添加事件监听。通过IO流完成读取服务器发回来的信息和服务器发送的信息；把服务器收到的信息进行处理后返回给客户端，以及完善一些聊天信息发送时不能为空的操作。对于限流，我们可以通过wait()\notify()\notifyall()方法、同步锁等来完成相应的需求。


![1](https://user-images.githubusercontent.com/105766840/173053084-51cb7444-3491-4a42-82e4-c8c8b11eae70.png)

![2](https://user-images.githubusercontent.com/105766840/173053111-fb8a8ba2-4e65-41cb-b15f-d2b9670dd4fd.png)
