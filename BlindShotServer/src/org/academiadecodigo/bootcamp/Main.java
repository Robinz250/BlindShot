import java.io.IOException;

/**
 * Created by ruimorais on 08/07/17.
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
