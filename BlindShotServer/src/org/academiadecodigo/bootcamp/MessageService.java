import java.io.*;
import java.util.Map;

/**
 * Created by ruimorais on 08/07/17.
 */
public class MessageService implements Runnable {

    private Client client;

    public MessageService(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String name = receiveMessage();
            client.setName(name);
            sendMessage(Integer.toString(client.getId()));
            receiveMessage();
            sendMessage("Waiting for other players to connect...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    private void sendMessage(String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getClientSocket().getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }
}
