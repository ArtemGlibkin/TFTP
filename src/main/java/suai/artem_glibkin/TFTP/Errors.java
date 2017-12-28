package suai.artem_glibkin.TFTP;

import suai.artem_glibkin.TFTP.AllLogs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Errors {
    AllLogs log;
    DatagramSocket a;
    InetAddress addr;
    int pt;
    String[] ErrorsName = {
            "Ошибка не определена","Файл не найден","Некорректное имя файла\\Доступ запрещен","Невозможно выделить место на диске",
            "Некорректная TFTP операция","Неправильный transfer ID","Файл уже существует"};
    public Errors(AllLogs log, DatagramSocket a, InetAddress addr,int port)
    {
        this.a=a;
        this.addr=addr;
        this.pt=port;
        this.log=log;
    }
    public void ErrorHandler(byte[] error) throws IOException {
        if(error[1]==5) {
            log.writelogs("Ошибка: " + ErrorsName[error[3]]);
        }
        else
        {
            SendError((byte)4);
        }
    }
    public void LogError(String error)
    {
        log.writelogs("Ошибка: " + error);
    }
    public void SendError(byte error) throws IOException {
        byte[] buff = {0, 5, 0, error};
        DatagramPacket packet = new DatagramPacket(buff, buff.length, addr, pt);
        a.send(packet);
    }
}
