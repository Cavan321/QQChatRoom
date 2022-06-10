package chatroom;

import com.sun.nio.sctp.SendFailedNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Date;

/**
 * @author Cavan
 * @date 2022-06-01
 * @qq 2069543852
 */
public class ChatClient {

    //JTextArea 文本区
    private JTextArea output;
    //JTextField 文本框
    private JTextField input;

    private JButton sendButton;//发送按钮
    private JButton quitButton;//关闭按钮

    private String nickname;

    //用来读取服务器回来的信息
    private BufferedReader in = null;
    //用来向服务器发送信息
    private PrintStream out = null;

    //已经在登录的时候就连接上服务器的那个socket对象
    private Socket socket;

    //构造器
    public ChatClient(Socket socket, String nickname) {
        output = new JTextArea(10, 50);//聊天区
        output.setEditable(false);
        input = new JTextField(50);//信息输入区
        sendButton = new JButton("发送");//发送按钮
        quitButton = new JButton("关闭");//关闭按钮

        //把已经连接号服务器的那个socket对象存起来
        this.socket = socket;
        this.nickname = nickname;
    }

    //加载窗体
    public void lanchFrame() {
        //窗体
        JFrame frame = new JFrame("欢迎：" + nickname);

        //窗体没有布局功能，在窗体上放一个容器container，容器具有布局功能
        //容器依赖于窗体
        Container container = frame.getContentPane();
        //为容器设置布局：边界布局
        container.setLayout(new BorderLayout());
        //将output,input,sentButton,quitButton放入相应布局上
        //new JScrollPane(output):生成一个带有滚动条的文本区
        container.add(new JScrollPane(output), BorderLayout.NORTH);//将output放入 north
        container.add(input, BorderLayout.CENTER);//将input放入 center

        //两个按钮怎么放？
        //把这两个按钮当作一个整体
        //创建一个面板
        JPanel panel = new JPanel();
        //把按钮钉在面板上
        panel.add(sendButton);
        panel.add(quitButton);
        container.add(panel, BorderLayout.SOUTH);

        //对窗体的默认关闭行为进行设置
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //对关闭按钮进行处理
        //事件处理，通过事件监听器
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //强行退出
                System.exit(-1);
            }
        });

        //对窗体的设置
        frame.setSize(510, 270);//设置窗体的尺寸，宽和高
        frame.setLocation(450, 230);
        //设置窗体显示
        frame.setVisible(true);

        //给发送按钮添加事件
        sendButton.addActionListener(new SendHandler());
        //当我在输入区输入内容后，点击回车，同样可以发送信息
        input.addActionListener(new SendHandler());

    }

    private void sendMessageToServer() {
        //获取文本框中输入的内容
        String text = input.getText();
        text = "1" + nickname + "：" + text;
        System.out.println(text);
        if (text.trim().split("：").length == 1) {
            JOptionPane.showMessageDialog(null, "聊天信息不能为空", "警告", JOptionPane.ERROR_MESSAGE);
            return;

        }
        //发送消息的格式：1黄洪泰：text
        out.println(text + "  " + "（" + FormatTime.dataToString() + "）");
        //发送完消息后，把文本框清空。
        input.setText("");

    }

    public void doConnect(Socket socket) {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            new Thread(new MessageReader()).start();


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("无法连接服务器...");
        }

    }

    private class MessageReader implements Runnable {

        //定义一个标记，保持监听任务。
        private boolean keepListening = true;

        @Override
        public void run() {
            while (keepListening == true) {
                try {
                    String nextLine = in.readLine();
                    //服务器返回的消息要在聊天区进行展示
                    output.append(nextLine + "\n");

                } catch (IOException e) {
                    e.printStackTrace();
                    keepListening = false;
                }
            }

        }
    }

    private class SendHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //把信息发送给服务器
            sendMessageToServer();
        }
    }


}
