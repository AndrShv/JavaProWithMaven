import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyServer {
    public static void main(String[] args) {
        int port = 8081;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено і слухає порт " + port);

            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Клієнт підключився");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); out.println("hello");
                String clientGreeting = in.readLine();
                System.out.println("Клієнт: " + clientGreeting);

                if (clientGreeting.matches(".*[ёЁъЪыЫэЭ].*")) {
                    out.println("що таке паляниця?");
                    String answer = in.readLine();
                    System.out.println("Відповідь клієнта: " + answer);

                    if (answer.equalsIgnoreCase("український хліб")) {
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        out.println("Поточна дата та час: " + currentDateTime.format(formatter));
                        out.println("до побачення");
                    } else {
                        out.println("Неправильна відповідь. Відключення.");
                    }
                } else {
                    out.println("Привітання без російських букв прийнято.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

