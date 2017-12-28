package suai.artem_glibkin.TFTP;

import suai.artem_glibkin.TFTP.Acknowledgment;
import suai.artem_glibkin.TFTP.AllLogs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Recieve {
    private static final byte Error = 5;
    DatagramSocket a;
    AllLogs log;
    InetAddress iadress;
    public Recieve(DatagramSocket a,InetAddress iadress,AllLogs log)
    {
        this.a=a;
        this.log=log;
    }
    public ByteArrayOutputStream receiveFile() throws IOException {
        ByteArrayOutputStream byteOutOS = new ByteArrayOutputStream();
        DatagramPacket packet;
        int block = 1;
        try {
            do {
                byte[] bufferByteArray = new byte[516];
                packet = new DatagramPacket(bufferByteArray, bufferByteArray.length, iadress, a.getLocalPort());
                a.receive(packet);
                byte[] opCode = {bufferByteArray[0], bufferByteArray[1]};
                if (opCode[1] == Error) {
                    if (block == 1)
                        log.writelogs("Ошибка: Файл не найден");
                    else
                        log.writelogs("Ошибка приема пакета");
                    return null;
                }
                log.writelogs(" Пакет -: " + block+" принят");
                block++;
                //else System.out.println((int) bufferByteArray[3]);
                byte[] blockNumber = {bufferByteArray[2], bufferByteArray[3]};
                DataOutputStream dos = new DataOutputStream(byteOutOS);
                dos.write(packet.getData(), 4, packet.getLength() - 4);
                new Acknowledgment(a,blockNumber, packet.getAddress(), packet.getPort());
            } while (!isLastPacket(packet));
        }
        catch (SocketTimeoutException e)
        {
            log.writelogs("Ошибка: Время ожидания превышено.Хост недоступен");
            return null;
        }
        return byteOutOS;
    }
    private boolean isLastPacket(DatagramPacket datagramPacket) {
        if (datagramPacket.getLength() < 512)
            return true;
        else
            return false;
    }
}
