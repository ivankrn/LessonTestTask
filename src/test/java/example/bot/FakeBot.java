package example.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Фэйковая реализация бота для тестов
 */
public class FakeBot implements Bot {

    /**
     * Сообщения, отправленные ботом
     */
    private final Map<Long, List<String>> messages = new HashMap<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        if (!messages.containsKey(chatId)) {
            messages.put(chatId, new ArrayList<>());
        }
        messages.get(chatId).add(message);
    }

    /**
     * Возвращает сообщения, отправленные в чат с указанным ID
     *
     * @param chatId ID чата
     * @return сообщения, отправленные в чат с указанным ID
     */
    public List<String> getMessagesByChatId(Long chatId) {
        return messages.get(chatId);
    }

}
