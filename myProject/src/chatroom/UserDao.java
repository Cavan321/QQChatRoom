package chatroom;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Cavan
 * @date 2022-06-03
 * @qq 2069543852
 */
public class UserDao {

    //用来存放读入的用户信息。
    private static ArrayList<User> userlist = new ArrayList<>();

    private User user = new User();

    public static ArrayList<User> getUserlist() {
        return userlist;
    }

    public static void setUserlist(ArrayList<User> userlist) {
        UserDao.userlist = userlist;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //静态代码块
    static {
        String usersFile = System.getProperty("user.dir") + File.separator + "files" + File.separator + "userstable.txt";
        BufferedReader reader = null;
        try {
            //声明一个流
            reader = new BufferedReader(new FileReader(usersFile));
            String line = reader.readLine();//读入一行数据
            while (line != null){
                String[] split = line.split("#");//将读到的数据按“#”拆分
                userlist.add(new User(split[0],split[1],split[2]));
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean login(String useraccount,String userpwd){
        for (User user : userlist){
            if (user.getUseraccount().equals(useraccount) && user.getUserpwd().equals(userpwd)){
                this.user = user;
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        UserDao dao = new UserDao();
        System.out.println(dao.login("2012100319", "123456"));
    }
}
