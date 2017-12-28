package suai.artem_glibkin.TFTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Acknowledgment {
    DatagramSocket a;
    public Acknowledgment(DatagramSocket a,byte[] blocknumber, InetAddress addr, int pt) throws IOException {
        this.a=a;
        SendAcknowledgment(blocknumber,addr,pt);
    }
    public void SendAcknowledgment(byte[] blocknumber, InetAddress addr, int pt) throws IOException {
        byte[] buff = {0, 4, blocknumber[0], blocknumber[1]};
        DatagramPacket packet = new DatagramPacket(buff, buff.length, addr, pt);
        a.send(packet);
    }
}
