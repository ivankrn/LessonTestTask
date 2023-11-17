package example.bot;

import java.util.Scanner;

/**
 * Консольный бот
 */
public class ConsoleBot implements Bot {

    private final BotLogic botLogic = new BotLogic(this);

    private final User consoleUser = new User(0L);

    @Override
    public void sendMessage(Long chatId, String message) {
        System.out.println(message);
    }

    /**
     * Обработать полученное сообщение от пользователя
     */
    private void onMessageReceived(String message) {
        botLogic.processCommand(consoleUser, message);
    }

    /**
     * Запустить бота
     */
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.useDelimiter("\n");
            while (true) {
                String message = scanner.next();
                onMessageReceived(message);
            }
        }
    }

    public static void main(String[] args) {
        new ConsoleBot().run();
    }
}
