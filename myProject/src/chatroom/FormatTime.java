package chatroom;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Cavan
 * @date 2022-06-10
 * @qq 2069543852
 */
public class FormatTime {
    public static String dataToString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

}
