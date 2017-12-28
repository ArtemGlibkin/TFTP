package suai.artem_glibkin.TFTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Request {
    DatagramSocket a;
    InetAddress iadress;
    int port;
    public Request(DatagramSocket a, InetAddress iadress,int port)
    {
        this.a=a;
        this.iadress=iadress;
        this.port=port;
    }
    public void CreateRequest(byte type, String mode, String path) throws IOException {
        int index = path.lastIndexOf("\\");
        String name = path.substring(index + 1);
        int bufflength = 2 + name.length() + 1 + mode.length() + 1;
        byte[] ba = new byte[bufflength];
        byte empty = 0;
        int ch = 0;
        ba[ch] = empty;
        ch++;
        ba[ch] = type;
        ch++;
        for (int i = 0; i < name.length(); i++) {
            ba[ch] = (byte) name.charAt(i);
            ch++;
        }
        ba[ch] = empty;
        ch++;
        for (int i = 0; i < mode.length(); i++) {
            ba[ch] = (byte) mode.charAt(i);
            ch++;
        }
        ba[ch] = empty;
        DatagramPacket packet = new DatagramPacket(ba, ba.length, iadress, port);
        a.send(packet);
    }
}
