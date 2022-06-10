package chatroom;


import java.io.*;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cavan
 * @date 2022-06-06
 * @qq 2069543852
 */
public class ChatServer {

    //用来存储所有已连接的客户信息，这些客户端都是独立的线程，之间互不影响
    private List<ClientConnection> connectionsList = new ArrayList<>();

    //当前连接数
    private int connectionCount;
    //最大连接数
    private int maxConnectionCount = 2;

    public void startServer() {
        ServerSocket serverSocket = null;
        Socket socket;

        try {
            serverSocket = new ServerSocket(2020);

        } catch (IOException e) {
            e.printStackTrace();
            //一旦出现异常，强行终止虚拟机
            System.exit(-1);
        }
        System.out.println("启动服务器，端口号：" + 2020);
        while (true) {
            try {
                //等待客户端的连接
                socket = serverSocket.accept();
                //处理客户端的请求
                handleClientConnection(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleClientConnection(Socket socket) {

        //声明一个输出流
        PrintStream out = null;
        try {
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //判断的过程,同步锁
        synchronized (this) {
            while (connectionCount == maxConnectionCount) {
                try {
                    //向客户端回复字符串
                    out.println("wait");
                    out.flush();
                    //让客户进入等待。
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        ClientConnection con = new ClientConnection(socket);
        //每次创建一个新的客户线程，就把他添加到保存的所有课护短的集合里
        //最终我们需要把客户端发送来的消息返回给所有的客户端，才能实现所有热都可以看见。
        connectionsList.add(con);
        //让在线人数自增
        connectionCount++;
        Thread t = new Thread(con);
        t.start();//启动线程


    }

    //连接关闭的方法
    public synchronized void connectionClosed(ClientConnection connection){
        //把当前连接从集合中删除
        connectionsList.remove(connection);
        //当前在线人数递减
        connectionCount--;
        //唤醒所有正在等待的线程
        notifyAll();
    }

    class ClientConnection implements Runnable {

        private Socket socket;
        private PrintStream out = null;
        private BufferedReader in = null;

        public ClientConnection(Socket socket) {
            this.socket = socket;
        }

        //我们这个线程负责做什么事，就把这些写在run()方法里。
        @Override
        public void run() {
            //接收客户端发来的登录请求，接收用户名与密码
            OutputStream socketOutput = null;
            InputStream socketInput = null;
            String useraccount = null;
            String userpwd = null;
            try {
                //输出流，向客户端回复，账号密码是否正确
                socketOutput = socket.getOutputStream();
                out = new PrintStream(socketOutput);
                //输入流，用来读取客户端发送的数据
                socketInput = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(socketInput));
                String input = in.readLine();
                System.out.println("服务器接收到的数据" + input);
                while (true) {
                    //约定如果以0开头那么就是登录，如果以1开头那么就是聊天
                    if (input != null && input.startsWith("0")) {
                        //在服务器端接收到的input是一个字符串
                        //字符串怎么才能编程用户名和密码
                        //约定客户端发送数据时，用户和密码中间用分号（；）隔开
                        String[] temp = input.split(";");
                        useraccount = temp[0].substring(1);
                        userpwd = temp[1];
                        //判断用户名和密码是否正确
                        UserDao userDao = new UserDao();
                        boolean flag = userDao.login(useraccount, userpwd);
                        System.out.println("flag=" + flag);
                        out.println(new Boolean(flag) + "#" + userDao.getUser().getNickname());
                        out.flush();
                    }
                    if (input != null && input.startsWith("1")) {
                        //
//                        sendMessageToClient(input);
                        sendToAllClients(input);
                    }
                    input = in.readLine();
                }
            } catch (IOException e) {
                //关闭连接，释放资源
                ChatServer.this.connectionClosed(this);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            }

        }

        //把服务器收到的数据处理之后返回给客户端
        public void sendMessageToClient(String message) {
            //向客户端返回去掉开头的1之后的数据。
            out.println(message.substring(1));
            out.flush();

        }
    }

    public void sendToAllClients(String message) {
        //遍历存储客户端的集合，分别发送给每个客户端
        for (ClientConnection con : connectionsList) {
            con.sendMessageToClient(message);
        }

    }


    public static void main(String[] args) {
        new ChatServer().startServer();
    }
}
