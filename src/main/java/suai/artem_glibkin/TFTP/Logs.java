package suai.artem_glibkin.TFTP;

import suai.artem_glibkin.TFTP.AllLogs;

import javax.swing.*;
import java.util.Date;

public class Logs implements AllLogs {
    JTextArea logarea;
    public Logs(JTextArea logarea)
    {
        this.logarea=logarea;
    }
    public void writelogs(String a)
    {
        Date date=new Date();
        logarea.append(String.valueOf(date.getHours())+":"+String.valueOf(date.getMinutes())+":"+String.valueOf(date.getSeconds())+" "+a+"\n");
    }
}
