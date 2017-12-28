package suai.artem_glibkin.TFTPServer;

import suai.artem_glibkin.TFTP.Acknowledgment;
import suai.artem_glibkin.TFTP.Recieve;
import suai.artem_glibkin.TFTP.SendFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class ServerTFTP {
    private DatagramSocket a;
    private static final byte ReadRequest = 1;
    private static final byte WriteRequest = 2;
    private static final byte Data = 3;
    private static final byte Acknowledgment = 4;
    private static final byte Error = 5;
    public ServerLogs logs;
    public ServerTFTP(int port) throws IOException {
        a = new DatagramSocket(port);
        logs=new ServerLogs();
        Start();
    }

    public static void main(String[] args) throws IOException {
        ServerTFTP a = new ServerTFTP(75);
    }

    public void Start() throws IOException {
        while (true) {
            byte[] bufferByteArray = new byte[516];
            DatagramPacket packet = new DatagramPacket(bufferByteArray, bufferByteArray.length);
            a.receive(packet);
            switch (bufferByteArray[1]) {
                case 1: {
                    //byte[]buff = new byte[40];
                    String buff= new String();
                    int i=2;
                    while(bufferByteArray[i]!=0)
                    {
                        buff+=(char)bufferByteArray[i];
                        i++;
                    }
                    new SendFile(a,a.getInetAddress(),logs).Send(packet.getAddress(), packet.getPort(),buff);
                    break;
                }
                case 2: {
                    byte[] ackn = {0, 0};
                    new Acknowledgment(a,ackn,packet.getAddress(),packet.getPort());
                    ByteArrayOutputStream byteOutOS = new Recieve(a,a.getInetAddress(),logs).receiveFile();
                    String s = new String(bufferByteArray, "ASCII");
                    String name = s.substring(2);
                    int index = name.indexOf(0);
                    name = name.substring(0, index);
                    System.out.println(name);
                    OutputStream outputStream = new FileOutputStream(name);
                    outputStream.write(byteOutOS.toByteArray());
                }
                break;
            }
        }
    }
}
