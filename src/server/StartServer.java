package server;

public class StartServer {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new WorkThread()).start();
        }
        ChattingServer cs= new ChattingServer();
        cs.startServer();

    }
}