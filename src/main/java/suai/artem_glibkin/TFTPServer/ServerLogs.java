package suai.artem_glibkin.TFTPServer;

import suai.artem_glibkin.TFTP.AllLogs;

import java.util.Date;

public class ServerLogs implements AllLogs {
    public void writelogs(String a)
    {
        Date date=new Date();
        System.out.println(String.valueOf(date.getHours())+":"+String.valueOf(date.getMinutes())+":"+String.valueOf(date.getSeconds())+" "+a+"\n");
    }
}
