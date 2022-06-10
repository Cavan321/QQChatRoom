package chatroom;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

/**
 * @author Cavan
 * @date 2022-06-02
 * @qq 2069543852
 */
/*
 *如果说我们当前创建的类就是窗体，没有别的功能，继承JFrame类
 * */
public class LoginPage extends JFrame {

    //声明组件
    private JTextField useraccount = new JTextField();//声明用户名的文本框
    private JLabel useraccounttext = new JLabel();//声明用户标签
    private JLabel userpwdtext = new JLabel();//声明密码标签
    private JPasswordField userpwd = new JPasswordField();//声明密码的文本框

    //声明按钮
    private JButton loginbutton = new JButton("登录");//声明登录按钮
    private JButton exit = new JButton("关闭");//声明关闭按钮

    //分割线
    private JSeparator line = new JSeparator();

    //声明图片组件
    private ImageIcon imageIcon;

    //用来设置背景图片
    private JLabel label;

    private BufferedReader in = null;
    private PrintStream out = null;

    public LoginPage() {
        setTitle("登录页面");//设置窗体的标题
        //获取容器，并设置布局
        getContentPane().setLayout(null);

        this.setLocation(450, 230);//设置窗体的位置

        //实例化背景图片的载体
        label = new JLabel();
        //找到图片的路径(字符串):动态获取图片的路径
        String picFile = System.getProperty("user.dir") + File.separator + "picture" + File.separator + "7.jpg";
        //实例化图片对象，把图片设置进去
        imageIcon = new ImageIcon(new ImageIcon(picFile).getImage().getScaledInstance(333, 166, Image.SCALE_DEFAULT));
        //设置JLabel的内容对其方式:居中对齐
        label.setHorizontalAlignment(SwingConstants.CENTER);
        //把图片对象设置给JLabel
        label.setIcon(imageIcon);
        //设置图片的位置和大小，使用坐标和宽高
        label.setBounds(20, 0, 283, 88);
        getContentPane().add(label);

        //准备文本标签
        useraccounttext.setText("账号：");
        //设置文本标签“账号：”两字的位置和大小
        useraccounttext.setBounds(60, 100, 61, 32);
        //把文本标签添加到容器上
        getContentPane().add(useraccounttext);
        //准备文本框
        useraccount.setBounds(105, 107, 131, 22);
        //将文本框添加到容器上
        getContentPane().add(useraccount);

        //准备密码标签
        userpwdtext.setText("密码：");
        //设置文本标签“密码：”两字的位置和大小
        userpwdtext.setBounds(60, 138, 61, 32);
        //把我文本标签添加到容器上
        getContentPane().add(userpwdtext);
        //准备密码文本框
        userpwd.setBounds(105, 145, 131, 22);
        //将密码文本框添加到容器上
        getContentPane().add(userpwd);

        //登录按钮
        loginbutton.setText("登录");
        loginbutton.setBounds(105, 175, 60, 22);
        getContentPane().add(loginbutton);
        //关闭按钮
        exit.setText("关闭");
        exit.setBounds(175, 175, 60, 22);
        getContentPane().add(exit);
        //给关闭按钮添加事件监听器
        exit.addActionListener(new LoginListener());

        //给登录按钮添加事件监听器
        loginbutton.addActionListener(new LoginListener());


        //水平线
        line.setBackground(Color.LIGHT_GRAY);
        line.setBounds(20, 94, 282, 11);
        getContentPane().add(line);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置默认的关闭行为
        this.setSize(337, 241);//设置窗体的大小
        this.setVisible(true);//设置窗体可视化

    }

    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //拿到文本框中输入的账号
            String useraccountValue = useraccount.getText();
            //拿到文本框中输入的密码
            String userpwdValue = userpwd.getText();
            if (e.getSource() == loginbutton) {
                if (useraccountValue.equals("")) {
                    //使用一个弹窗告诉提示：用户名不能能为空
                    JOptionPane.showMessageDialog(null, "必须输入账号", "警告", JOptionPane.ERROR_MESSAGE);
                    //终止方法的执行
                    return;
                }
                if (userpwdValue.equals("")) {
                    JOptionPane.showMessageDialog(null, "必须输入密码", "警告", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Socket socket;
                try {
                    socket = new Socket("127.0.0.1", 2020);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintStream(socket.getOutputStream());
                    //按照约定，登录请求，账号密码拼接之前加一个0，账号和密码中间级一个分号(;)
                    out.println("0" + useraccountValue + ";" + userpwdValue);

                    new Thread(new LoginHelper(socket)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "服务器还没有打开", "提示", JOptionPane.INFORMATION_MESSAGE);

                }
            }
            if (e.getSource() == exit) {
                System.exit(-1);
            }
        }
    }

    //需要处理服务器返回的对于账号密码的验证结果的展示
    public class LoginHelper implements Runnable {
        private Socket socket;

        public LoginHelper(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("开始接收服务器返回的数据...");
            try {
                String login = in.readLine();
                System.out.println("接收到的服务器返回的数据为：" + login);
                while (login != null) {
                    //更具#拆分字符串
                    String[] temp = login.split("#");
                    if (login.equals("wait")){
                        TiDialog dialog = new TiDialog(LoginPage.this,"服务器忙",true);
                        dialog.setLocation(450,230);
                        dialog.setVisible(true);
                    }else {
                        if (temp[0].equals("true")) {
                            //账号和密码正确
                            //如果登陆成功，登录窗口关闭，聊天窗口开启
                            dispose();
                            ChatClient cc = new ChatClient(socket, temp[1]);
                            //打开聊天窗口
                            cc.lanchFrame();
                            //打开聊天窗口之后获取服务器的连接
                            cc.doConnect(socket);
                            //停止当前线程
                            Thread.currentThread().stop();
                        } else {
                            //账号或密码错误
                            JOptionPane.showMessageDialog(null, "账号或密码错误", "警告", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    login = in.readLine();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class TiDialog extends JDialog implements ActionListener {

        public TiDialog(JFrame parent, String title, boolean modal) {
            super(parent,title,modal);
            add(new JLabel("目前聊天人数太多请稍后..."),BorderLayout.NORTH);
            JButton b = new JButton("OK");
            add(b,BorderLayout.SOUTH);
            pack();
            b.addActionListener(this);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);

        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }

}
