package example.bot;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class BotLogicTest {

    private BotLogic botLogic;
    private User user;
    private FakeBot bot;

    @Before
    public void setUp() {
        user = new User(1L);
        bot = new FakeBot();
        botLogic = new BotLogic(bot);
    }

    /**
     * Проверяет команду /test при правильных ответах пользователя на вопросы
     */
    @Test
    public void givenUserAnswersCorrect_whenTestCommand() {
        botLogic.processCommand(user, "/test");
        assertEquals(State.TEST, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(0));

        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", bot.getMessages().get(1));
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(2));

        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", bot.getMessages().get(3));
        assertTrue(user.getWrongAnswerQuestions().isEmpty());
        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(4));
    }

    /**
     * Проверяет команду /test при неправильных ответах пользователя на вопросы
     */
    @Test
    public void givenUserAnswersWrong_whenTestCommand() {
        botLogic.processCommand(user, "/test");
        assertEquals(State.TEST, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(0));

        botLogic.processCommand(user, "10");
        assertEquals("Вы ошиблись, верный ответ: 100", bot.getMessages().get(1));
        assertEquals(1, user.getWrongAnswerQuestions().size());
        assertEquals("Вычислите степень: 10^2", user.getWrongAnswerQuestions().get(0).getText());
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(2));

        botLogic.processCommand(user, "10");
        assertEquals("Вы ошиблись, верный ответ: 6", bot.getMessages().get(3));
        assertEquals(2, user.getWrongAnswerQuestions().size());
        assertEquals("Сколько будет 2 + 2 * 2", user.getWrongAnswerQuestions().get(1).getText());
        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(4));
    }

    /**
     * Проверяет команду /notify и отправление напоминания вовремя
     */
    @Test
    public void whenNotifyCommand_thenSendAtTime() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        assertEquals("Введите текст напоминания", bot.getMessages().get(0));

        botLogic.processCommand(user, "Текст");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Через сколько секунд напомнить?", bot.getMessages().get(1));

        botLogic.processCommand(user, "abc");
        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Пожалуйста, введите целое число", bot.getMessages().get(2));

        botLogic.processCommand(user, "1");
        assertEquals(State.INIT, user.getState());
        assertEquals("Напоминание установлено", bot.getMessages().get(3));

        assertEquals(4, bot.getMessages().size());

        Thread.sleep(1500L);

        assertEquals("Сработало напоминание: 'Текст'", bot.getMessages().get(4));
    }

    /**
     * Если у пользователя нет неверно отвеченных вопросов, то при отправке пользователем команды /repeat бот должен
     * прислать сообщение о том, что нет вопросов для повторения
     */
    @Test
    public void givenUserWithoutWrongAnswers_whenRepeat_thenSendNoQuestionsToRepeat() {
        botLogic.processCommand(user, "/repeat");

        assertEquals("Нет вопросов для повторения", bot.getMessages().get(0));
    }

    /**
     * Если у пользователя есть неправильно отвеченные вопросы, то при отправке пользователем команды /repeat бот
     * должен начать повторение
     */
    @Test
    public void givenUserWithWrongAnswers_whenRepeat_thenBeginRepeatSession() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");
        assertEquals(State.REPEAT, user.getState());
        assertEquals("Вычислите степень: 10^2", bot.getMessages().get(5));

        botLogic.processCommand(user, "100");
        assertEquals("Правильный ответ!", bot.getMessages().get(6));
        assertEquals(1, user.getWrongAnswerQuestions().size());
        assertEquals("Сколько будет 2 + 2 * 2", bot.getMessages().get(7));

        botLogic.processCommand(user, "6");
        assertEquals("Правильный ответ!", bot.getMessages().get(8));
        assertTrue(user.getWrongAnswerQuestions().isEmpty());
        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", bot.getMessages().get(9));
    }

}