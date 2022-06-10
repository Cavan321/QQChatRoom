package chatroom;

/**
 * @author Cavan
 * @date 2022-06-03
 * @qq 2069543852
 */
/*
* 用户模型类
* */
public class User {

    private String useraccount;//用户名
    private String userpwd;//密码
    private String nickname;//昵称

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User(){

    }
    public User(String useraccount, String userpwd, String nickname) {
        this.useraccount = useraccount;
        this.userpwd = userpwd;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "useraccount='" + useraccount + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
