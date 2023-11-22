package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фэйковая реализация бота для тестов
 */
public class FakeBot implements Bot {

    /**
     * Сообщения, отправленные ботом
     */
    private final List<String> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    /**
     * Возвращает сообщения, отправленные ботом
     *
     * @return сообщения, отправленные ботом
     */
    public List<String> getMessages() {
        return messages;
    }

}
