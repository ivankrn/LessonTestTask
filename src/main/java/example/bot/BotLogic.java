package example.bot;

import java.util.List;
import java.util.Optional;

import static example.bot.Constants.*;

/**
 * Верхнеуровневая логика бота
 */
public class BotLogic {
    private final Bot bot;

    public BotLogic(Bot bot) {
        this.bot = bot;
    }

    private final List<Question> questions = List.of(
            new Question("Вычислите степень: 10^2", "100"),
            new Question("Сколько будет 2 + 2 * 2", "6"));

    /**
     * Функция для обработки сообщений пользователя
     *
     * @param user    - текущий пользователь
     * @param command - сообщение пользователя
     */
    public final void processCommand(User user, String command) {
        switch (command) {
            case COMMAND_START:
                bot.sendMessage(user.getChatId(), "Привет!");
                user.setState(State.INIT);
                break;
            case COMMAND_HELP:
                bot.sendMessage(user.getChatId(), HELP_INFO);
                break;
            case COMMAND_TEST:
                user.setState(State.TEST);
                bot.sendMessage(user.getChatId(),
                        questions.get(user.getCurrentQuestionIndex()).getText());
                break;
            case COMMAND_STOP:
                processStop(user);
                break;
            case COMMAND_REPEAT:
                user.getCurrentWrongAnswerQuestion().ifPresentOrElse(question -> {
                            user.setState(State.REPEAT);
                            bot.sendMessage(user.getChatId(),
                                    question.getText());
                        },
                        () -> bot.sendMessage(user.getChatId(), "Нет вопросов для повторения"));
                break;
            case COMMAND_NOTIFY:
                user.setState(State.SET_NOTIFY_TEXT);
                bot.sendMessage(user.getChatId(), "Введите текст напоминания");
                break;
            default:
                processNonCommand(user, command);
                break;
        }
    }

    private void processNonCommand(User user, String command) {
        switch (user.getState()) {
            case TEST -> checkTestAnswer(user, command);
            case REPEAT -> checkRepeatTestAnswer(user, command);
            case SET_NOTIFY_TEXT -> {
                user.createNotification(command);
                bot.sendMessage(user.getChatId(), "Через сколько секунд напомнить?");
                user.setState(State.SET_NOTIFY_DELAY);
            }
            case SET_NOTIFY_DELAY -> {
                Notification notification = user.getNotification().orElseThrow();
                try {
                    long delay = Long.parseLong(command);
                    notification.schedule(delay, bot);
                    bot.sendMessage(user.getChatId(), "Напоминание установлено");
                    user.setState(State.INIT);
                } catch (NumberFormatException e) {
                    bot.sendMessage(user.getChatId(), "Пожалуйста, введите целое число");
                }
            }
            default -> bot.sendMessage(user.getChatId(),
                    "Такой команды пока не существует, или Вы допустили ошибку в написании. " +
                    "Воспользуйтесь командой /help, чтобы прочитать инструкцию.");
        }
    }

    private void processStop(User user) {
        if (user.getState() != State.TEST
            && user.getState() != State.REPEAT) {
            bot.sendMessage(user.getChatId(),
                    "Вы не начинали тестирование. " +
                    "Воспользуйтесь командой /help, чтобы прочитать инструкцию.");
        } else {
            bot.sendMessage(user.getChatId(), "Тест завершен");
            user.setState(State.INIT);
        }
    }

    /**
     * Проверка ответов пользователя на тест
     *
     * @param user       пользователь
     * @param userAnswer ответ от пользователя
     */
    private void checkTestAnswer(User user, String userAnswer) {
        Question question = questions.get(user.getCurrentQuestionIndex());
        int nextQuestionIndex = user.nextQuestionIndex();
        Question nextQuestion = nextQuestionIndex < questions.size()
                ? questions.get(nextQuestionIndex)
                : null;
        checkAnswer(user, userAnswer, question, nextQuestion);
    }

    private void checkRepeatTestAnswer(User user, String userAnswer) {
        Question question = user.getCurrentWrongAnswerQuestion().orElseThrow();
        Optional<Question> nextWrongAnswerQuestion = user.nextWrongAnswerQuestion();
        checkAnswer(user, userAnswer, question, nextWrongAnswerQuestion.orElse(null));
    }

    private void checkAnswer(User user, String userAnswer, Question question, Question nextQuestion) {
        if (userAnswer.equalsIgnoreCase(question.getAnswer())) {
            bot.sendMessage(user.getChatId(), "Правильный ответ!");
            user.removeWrongAnswerQuestion(question);
        } else {
            bot.sendMessage(user.getChatId(),
                    "Вы ошиблись, верный ответ: " + question.getAnswer());
            user.addWrongAnswerQuestion(question);
        }
        if (nextQuestion == null) {
            processStop(user);
            return;
        }
        bot.sendMessage(user.getChatId(), nextQuestion.getText());
    }
}
