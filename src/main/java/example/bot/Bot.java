package example.bot;

/**
 * Интерфейс Бота со списком его возможностей.
 */
public interface Bot {
    /**
     * Отправка сообщения пользователю
     *
     * @param chatId идентификатор чата
     * @param message текст сообщения
     */
    void sendMessage(Long chatId, String message);
}
