package suai.artem_glibkin.TFTPClientPro;

import suai.artem_glibkin.TFTP.*;

import java.io.*;
import java.net.*;

import javax.swing.*;

public class ClientTFTP {
    private static final byte ReadRequest = 1;
    private static final byte WriteRequest = 2;
    private static final byte Data = 3;
    private static final byte Acknowledgment = 4;
    public Logs log;
    public Errors error;
    DatagramSocket a;
    static InetAddress iadress;
    int port = 69;
    public ClientTFTP(String ip,int port, JTextArea area) throws IOException {
        iadress = InetAddress.getByName(ip);
        a = new DatagramSocket();
        a.setSoTimeout(3000);
        this.log=new Logs(area);
        this.port=port;
        error= new Errors(log,a,iadress,port);
    }

    public void ReadFile(String name) throws IOException {
        log.writelogs("Запрос на чтение "+" файла - "+name+" к "+iadress.toString().substring(1)+":"+port);
        new Request(a,iadress,port).CreateRequest(ReadRequest, "octet", name);
        Recieve rec=new Recieve(a,iadress,log);
        ByteArrayOutputStream byteOutOS = rec.receiveFile();
        if(byteOutOS!=null) {
            OutputStream outputStream = new FileOutputStream(name);
            outputStream.write(byteOutOS.toByteArray());
            if(name.lastIndexOf("pless")!=-1) new Deshifr(name);
            log.writelogs("Файл "+name+" успешно принят");
        }
    }

    public void WriteFile(String name) throws IOException {
        int index = name.lastIndexOf("\\");
        String name2 = name.substring(index + 1);
        log.writelogs("Запрос на запись "+" файла - "+name2+" к "+iadress.toString().substring(1)+":"+port);
        new Request(a,iadress,port).CreateRequest(WriteRequest, "octet", name);
        byte[] buffer = new byte[516];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, iadress, a.getLocalPort());
        a.receive(packet);
            if(buffer[1]!=4) {
               error.ErrorHandler(buffer);
                return;
            }
        int rez = new SendFile(a,iadress,log).Send(packet.getAddress(), packet.getPort(), name);
       if(rez==1) log.writelogs("Файл "+name2+" успешно передан");
    }
}