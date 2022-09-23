
package MailManageSystem;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowDate {
    public static String showTime(Date dd){  
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
        return sd.format(dd);
    }
}
