package example.bot;

/**
 * Хранит текстовые константы.
 */
public class Constants {
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_TEST = "/test";
    public static final String COMMAND_STOP = "/stop";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_NOTIFY = "/notify";
    public static final String COMMAND_REPEAT = "/repeat";
    public static final String HELP_INFO = """
            Бот для обучения. Ты можешь проходить здесь тесты и проверять свой скилл.
            Просто используй:
            /start - для запуска бота
            /help - если тебе вдруг что-то стало непонятно
            /test - для запуска теста
            /stop - для завершения работы в режиме теста
            /notify - отправить уведомление с текстом <text> через <seconds> секунд
            /repeat - повторно задать вопросы, на которые был дан неправильный ответ (как только дан правильный ответ, вопрос удаляется списка на повторение)""";
}
