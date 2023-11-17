package example.bot;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class BotLogicTest {

    private BotLogic botLogic;
    private User user;
    private final FakeBot bot = new FakeBot();

    @Before
    public void setUp() {
        user = new User(1L);
        botLogic = new BotLogic(bot);
    }

    /**
     * Если пользователь присылает команду /test, то его состояние должно стать {@link State#TEST State.TEST} и бот
     * должен отправить первый вопрос
     */
    @Test
    public void whenTestCommand_thenSetUserStateToTestAndSendFirstQuestion() {
        botLogic.processCommand(user, "/test");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.TEST, user.getState());
        assertEquals("Вычислите степень: 10^2", chatMessages.get(0));
    }

    /**
     * Если во время теста пользователь отвечает на последний вопрос, то состояние пользователя должно стать
     * {@link State#INIT State.INIT} и бот должен отправить сообщение о том, что тест завершен
     */
    @Test
    public void givenUserInTestState_whenUserAnswersLastQuestion_thenSetUserStateToInitAndSendStopMessage() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");

        botLogic.processCommand(user, "6");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", chatMessages.get(4));
    }

    /**
     * Если во время теста пользователь отвечает на непоследний вопрос, то бот должен отправить сообщение со следующим
     * вопросом
     */
    @Test
    public void givenUserInTestState_whenUserAnswers_thenSendNextQuestion() {
        botLogic.processCommand(user, "/test");

        botLogic.processCommand(user, "100");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals("Сколько будет 2 + 2 * 2", chatMessages.get(2));
    }

    /**
     * Если во время теста пользователь правильно отвечает на вопрос, то бот должен отправить сообщение о том, что
     * ответ правильный
     */
    @Test
    public void givenUserInTestState_whenUserAnswersCorrect_thenSendApprove() {
        botLogic.processCommand(user, "/test");

        botLogic.processCommand(user, "100");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals("Правильный ответ!", chatMessages.get(1));
    }

    /**
     * Если во время теста пользователь неправильно отвечает на вопрос, то бот должен отправить сообщение о том, что
     * ответ неправильный и указать верный ответ
     */
    @Test
    public void givenUserInTestState_whenUserAnswersWrong_thenSendWrongAnswer() {
        botLogic.processCommand(user, "/test");

        botLogic.processCommand(user, "10");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals("Вы ошиблись, верный ответ: 100", chatMessages.get(1));
    }

    /**
     * Если во время теста пользователь отвечает правильно, то список неправильных ответов на вопросы пользователя
     * не должен измениться
     */
    @Test
    public void givenUserInTestState_whenUserAnswersCorrect_thenDoNotAddWrongAnswer() {
        botLogic.processCommand(user, "/test");

        botLogic.processCommand(user, "100");

        assertTrue(user.getWrongAnswerQuestions().isEmpty());
    }

    /**
     * Если во время теста пользователь отвечает неправильно, то в список неправильных ответов на вопросы пользователя
     * должен добавиться неправильно отвеченный вопрос
     */
    @Test
    public void givenUserInTestState_whenUserAnswersWrong_thenAddWrongAnswer() {
        botLogic.processCommand(user, "/test");

        botLogic.processCommand(user, "10");

        assertEquals("Вычислите степень: 10^2", user.getWrongAnswerQuestions().get(0).getText());
    }

    /**
     * Если пользователь присылает команду /notify, тогда его состояние должно стать {@link State#SET_NOTIFY_TEXT
     * STATE.SET_NOTIFY_TEXT} и бот должен отправить сообщение с просьбой ввести текст напоминания
     */
    @Test
    public void whenNotifyCommand_thenSetUserStateToNotifyTextAndAskForText() {
        botLogic.processCommand(user, "/notify");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        assertEquals("Введите текст напоминания", chatMessages.get(0));
    }

    /**
     * Если во время ввода текста напоминания пользователь присылает текст напоминания, то состояние пользователя
     * должно стать {@link State#SET_NOTIFY_DELAY STATE.SET_NOTIFY_DELAY} и бот должен отправить сообщение с просьбой
     * ввести количество секунд напоминания
     */
    @Test
    public void givenUserStateIsSetNotifyText_whenUserSendsText_thenSetUserStateToNotifyDelayAndAskForDelay() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Через сколько секунд напомнить?", chatMessages.get(1));
    }

    /**
     * Если во время ввода текста напоминания пользователь присылает текст напоминания, то у пользователя должно
     * создаться напоминание
     */
    @Test
    public void givenUserStateIsSetNotifyText_whenUserSendsText_thenCreateNotification() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");

        assertTrue(user.getNotification().isPresent());
    }

    /**
     * Если во время ввода задержки напоминания пользователь присылает корректное число, то состояние пользователя
     * должно стать {@link State#INIT STATE.INIT} и бот должен отправить сообщение с подтверждением создания
     * напоминания
     */
    @Test
    public void givenUserStateIsSetNotifyDelay_whenUserSendsCorrectNumber_thenSetUserStateToInitAndSendApprove() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");

        botLogic.processCommand(user, "2");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.INIT, user.getState());
        assertEquals("Напоминание установлено", chatMessages.get(2));
    }

    /**
     * Если во время ввода задержки напоминания пользователь присылает не число, то состояние пользователя
     * должно остаться прежним и бот должен отправить сообщение с просьбой повторно ввести целое число
     */
    @Test
    public void givenUserStateIsSetNotifyDelay_whenUserSendsNotANumber_thenDoNotUpdateStateAndAskDelayAgain() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");

        botLogic.processCommand(user, "abc");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        assertEquals("Пожалуйста, введите целое число", chatMessages.get(2));
    }

    /**
     * Если у пользователя установлено напоминание, то бот не должен отправлять его преждевременно
     */
    @Test
    public void givenUserCreatedNotification_whenItsTooEarly_thenDoNotSendANotification() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");

        botLogic.processCommand(user, "1");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertTrue(chatMessages.stream().noneMatch("Текст"::equals));
    }

    /**
     * Если у пользователя установлено напоминание и при этом настало время, то бот должен прислать напоминание
     *
     * @throws InterruptedException
     */
    @Test
    public void givenUserCreatedNotification_whenTimeArrives_thenSendANotification() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Текст");

        botLogic.processCommand(user, "1");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        Thread.sleep(1500);
        assertEquals("Сработало напоминание: 'Текст'", chatMessages.get(3));
    }

    /**
     * Если у пользователя нет неверно отвеченных вопросов, то при отправке пользователем команды /repeat бот должен
     * прислать сообщение о том, что нет вопросов для повторения
     */
    @Test
    public void givenUserWithoutWrongAnswers_whenRepeat_thenSendNoQuestionsToRepeat() {
        botLogic.processCommand(user, "/repeat");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals("Нет вопросов для повторения", chatMessages.get(0));
    }

    /**
     * Если у пользователя есть неправильно отвеченные вопросы, то при отправке пользователем команды /repeat состояние
     * пользователя должно стать {@link State#REPEAT State.REPEAT} и бот должен прислать сообщение с первым вопросом
     */
    @Test
    public void givenUserWithWrongAnswers_whenRepeat_thenSetUserStateToRepeatAndSendFirstQuestion() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "6");

        botLogic.processCommand(user, "/repeat");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.REPEAT, user.getState());
        assertEquals("Вычислите степень: 10^2", chatMessages.get(5));
    }

    /**
     * Если пользователь отвечает на последний вопрос повторения, то состояние пользователя должно стать
     * {@link State#INIT State.INIT} и бот должен прислать сообщение о завершении теста
     */
    @Test
    public void givenUserWithWrongAnswers_whenRepeatLastQuestion_thenSetUserStateToInitAndSendStopMessage() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "6");

        botLogic.processCommand(user, "/repeat");
        botLogic.processCommand(user, "100");
        List<String> chatMessages = bot.getMessagesByChatId(user.getChatId());

        assertEquals(State.INIT, user.getState());
        assertEquals("Тест завершен", chatMessages.get(7));
    }
}