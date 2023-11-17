package example.bot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Уведомление
 */
public class Notification {
    private static final int MILLISECONDS_IN_SECOND = 1000;
    private final String text;
    private final Long chatId;

    public Notification(String text, Long chatId) {
        this.text = text;
        this.chatId = chatId;
    }

    /**
     * Запланировать отправку уведомления
     *
     * @param delay через сколько секунд отправить
     */
    public void schedule(final long delay, final Bot bot) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                bot.sendMessage(chatId, "Сработало напоминание: '%s'".formatted(text));
            }
        }, delay * MILLISECONDS_IN_SECOND);

    }
}
