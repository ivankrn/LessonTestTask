package example.bot;

/**
 * Состояние
 */
public enum State {
    INIT,
    TEST,
    REPEAT,
    /**
     * Настройка напоминания - текст напоминания
     */
    SET_NOTIFY_TEXT,
    /**
     * Настройка напоминания - через сколько секунд отправить напоминание
     */
    SET_NOTIFY_DELAY
}
