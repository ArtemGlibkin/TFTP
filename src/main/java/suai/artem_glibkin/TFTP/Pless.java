package suai.artem_glibkin.TFTP;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by dns on 17.12.2017.
 */
public class Pless
{
    int[] key;
    int[] rslos;
    byte[] c;
    public Pless(int[] key) throws IOException {
        this.key=key;
        rslos=new int[9];
        c=new byte[16];
        c[0]=1;
        c[3]=1;
        c[4]=1;
        c[5]=1;
        c[8]=1;
        c[10]=1;
        c[12]=1;
        c[15]=1;
    }
    public void sdvigrslos()
    {
        for(int i=1;i<9;i++) {
            byte newbit=0;
            for(int j=0;j<16;j++)
                newbit= (byte) (newbit ^ (byte) (((rslos[i]>>>j)&1)*c[15-j]));
            rslos[i] = (rslos[i]<<1);
            rslos[i]=rslos[i]|newbit;
        }
    }
    public int generator() {
        //System.out.println(Integer.toBinaryString((int)key));
        int[] ckey=key;
        for(int i=1;i<=8;i++) {
            rslos[i] = (int) ckey[i-1];

           // System.out.println(rslos[i]+"\n");
        }
        byte y =0;
        byte ypr=0;
        int z=0;
        for(int i=0;i<4;i++)
        {
            byte zpr=0;
            ypr=0;
            for(int k=1;k<=4;k++)
            {
                sdvigrslos();
                y= (byte) (rslos[2*k-1]^(ypr*(1^rslos[2*k-1]^rslos[2*k])));
                ypr= y;
                //System.out.println("y "+y);
                //System.out.println(y&1);
                zpr= (byte) (zpr|(((y&1)<<(k-1))));
                //System.out.println("zpr promrj"+zpr);
            }
            //System.out.println("zpr"+zpr);
            z=z+(zpr<<(4*i));
        }
        System.out.println(z);
        frequencytest(z);
        autokorrelationtest(z);
        return z;
    }
    public void encode(String input,String output) throws IOException {
        int z=generator();
        FileInputStream a =new FileInputStream(new File(input));
        FileOutputStream out= new FileOutputStream(new File(output));
        int size=0;
        if((size=a.available())%4!=0)
        {
            while(size%4!=0)
                    size++;
        }
        else size=a.available();
        byte[] buffer = new byte[size];
        a.read(buffer, 0, a.available());
        //System.out.println("Содержимое файла:");
        for(int i=0; i<size;i+=4){
            int message = ((buffer[i] & 0xFF) << 24) + ((buffer[i+1] & 0xFF) << 16) + ((buffer[i+2] & 0xFF) << 8) + (buffer[i+3] & 0xFF);
            message=message^z;
            byte[] bytes = ByteBuffer.allocate(4).putInt(message).array();
            out.write(bytes);
        }
        System.out.println(output);
    }
    public void frequencytest(int z)
    {
        int ch=0;
        for(int i=0;i<32;i++)
        {
            int buf=z>>>i;
            int b= (byte) (buf&1);
            if(b==1)ch++;
            else ch--;
        }
        System.out.println("Частотный тест - "+abs(ch)/sqrt(32));
    }
    public void autokorrelationtest(int z)
    {
        int rez=0;
        Random r = new Random();
       int t= (r.nextInt())%32;
        for(int i=0;i<32;i++)
        {
            int a=(z>>>i)&0x1;
            int b=(z>>>((i+t)%32))&0x1;
            rez=rez+a^b;
        }
        System.out.println("Автокорреляционный тест - "+rez);
    }
    /*
    public void serialtest(int z) {
        int ch = 0;
        for (int i = 0; i < 32; i++) {
            int buf = z >>> i;
            int b = (byte) (buf & 1);
            if (b == 1) ch++;
            else ch--;
        }
        System.out.println("Частотный тест - " + abs(ch) / sqrt(32));
    }
    */
}
