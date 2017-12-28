package suai.artem_glibkin.TFTP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class SendFile {
    private static final byte Data = 3;
    DatagramSocket a;
    InetAddress iadress;
    AllLogs log;
    public SendFile(DatagramSocket a,InetAddress iadress,AllLogs log) throws IOException {
        this.iadress=iadress;
        this.a=a;
        this.log=log;
    }
    public int Send(InetAddress addr, int port, String name) throws IOException {
        try {
            System.out.println(name);
            FileInputStream file = new FileInputStream(name);
            short packetnumber = 1;
            byte[] buffer = new byte[file.available()];
            file.read(buffer, 0, file.available());
            byte[] packetbuffer = new byte[516];
            int j = 0;
            DatagramPacket packet = null;
            packetbuffer[j] = 0;
            j++;
            packetbuffer[j] = Data;
            j++;
            packetbuffer[j] = 0;
            j++;
            packetbuffer[j] = (byte) packetnumber;
            j++;
            System.out.println(buffer.length);
            for (int i = 0; i < buffer.length; i++) {
                packetbuffer[j] = buffer[i];
                j++;
                if ((j == 516) || (i == buffer.length - 1)) {
                    log.writelogs(" Пакет - " + packetnumber + " передан");
                    packet = new DatagramPacket(packetbuffer, j, addr, port);
                    a.send(packet);
                    byte[] bufferByteArray = new byte[516];
                    DatagramPacket packet2 = new DatagramPacket(bufferByteArray, bufferByteArray.length, iadress, a.getLocalPort());
                    a.receive(packet2);
                    if(bufferByteArray[1]!=4){
                           new Errors(log,a,iadress,port).ErrorHandler(bufferByteArray);
                            return -1;
                }
                    //System.out.println((int) ((packetbuffer[2] << 8) | (packetbuffer[3])));
                    packetbuffer = new byte[j];
                    packetnumber++;
                    j = 0;
                    packetbuffer[j] = 0;
                    j++;
                    packetbuffer[j] = Data;
                    j++;
                    packetbuffer[j] = (byte) (packetnumber >>> 8);
                    j++;
                    packetbuffer[j] = (byte) (packetnumber);
                    j++;
                }
            }
        }
        catch (SocketTimeoutException e) {
            log.writelogs("Ошибка: Время ожидания превышено.Хост недоступен");
            return -1;
        }
        catch(FileNotFoundException e)
        {
            log.writelogs("Ошибка: Файл не найден");
            new Errors(log,a,addr,port).SendError((byte)1);
            return -1;
        }
        return 1;
    }
}
