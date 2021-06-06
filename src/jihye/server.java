package jihye;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    public static Socket socket = null;
    public static Socket socket2 = null;
    public static byte str;
    public static int in;

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("<<Server>>");

        while(true) {
            //Runnable 생성
            Runnable recieveRunnable = new RecieveRunnable3();
            Runnable giveRunnable = new GiveRunnable3();

            Thread[] myThread = new Thread[500];

            for(int i=0; i<1000; i+=2) {
                //Thread 생성
                myThread[i] = new Thread(recieveRunnable);
                myThread[i+1] =new Thread(giveRunnable);

                //Thread 실행
                if(myThread[i].getState() == Thread.State.NEW ) {
                    myThread[i].start();
                }
                try {myThread[i].join();} catch (InterruptedException e) {}

                if(myThread[i+1].getState() == Thread.State.NEW ) {
                    myThread[i+1].sleep(500);
                    myThread[i+1].start();
                }
                try {myThread[i+1].join();} catch (InterruptedException e) {}
            }
        }

    }
}

class RecieveRunnable3 implements Runnable {

    ServerSocket serverSocket1 = null;

    @Override
    public void run() {

        //System.out.println(androidserverarduino3.socket);

        if(server.socket==null) {
            try {
                serverSocket1 = new ServerSocket(12340);

            } catch (IOException e) {
                System.out.println("안드로이드포트를 열 수 없습니다.");
                System.exit(0);   //프로그램 종료
            }

        }

        while(true) {
            try {
                System.out.println();
                System.out.println(" - 안드로이드 클라이언트 접속 대기...");

                server.socket = serverSocket1.accept();
                System.out.println("Android Client 연결 수락");
                System.out.println("접속 client 주소:"+server.socket.getInetAddress()+":"+server.socket.getPort());

                DataInputStream dis = new DataInputStream(new BufferedInputStream(server.socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(server.socket.getOutputStream()));

                System.out.println(" - 받아오기 대기...");
                server.str = dis.readByte();
//            dos.writeByte(androidserverarduino2.in);
                System.out.println("client: "+server.str);
//            dos.flush();

                break;
            } catch(Exception e) {}

        }

    }
}
class GiveRunnable3 implements Runnable {

    ServerSocket serverSocket2 = null;

    @Override
    public void run() {

        if(server.socket2==null) {
            try {
                serverSocket2 = new ServerSocket(12365);

            } catch (IOException e) {
                System.out.println("아두이노포트를 열 수 없습니다.");
                System.exit(0);   //프로그램 종료
            }
        }

        while(true) {
            try {
                System.out.println();
                System.out.println(" - 아두이노 클라이언트 접속 대기...");

                server.socket2 = serverSocket2.accept();
                System.out.println("Arduino Client 연결 수락");
                System.out.println("접속 client 주소:"+server.socket2.getInetAddress()+":"+server.socket2.getPort());

                DataInputStream dis = new DataInputStream(new BufferedInputStream(server.socket2.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(server.socket2.getOutputStream()));


                System.out.println(" - 보내기 대기...");
                dos.writeByte(server.str);
                System.out.println("server: "+server.str);
//            if(+dis.read()>0) {
//               System.out.println("아두이노에서 보냈음 : " +dis.read()*13);
//               androidserverarduino2.in = dis.read()*13;
//            }
                dos.flush();

                server.str = 0;
                break;
            } catch(Exception e) {}

        }

    }
}