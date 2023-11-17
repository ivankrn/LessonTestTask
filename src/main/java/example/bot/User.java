package example.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Пользователь
 */
public class User {

    /**
     * id чата пользователя
     */
    private final Long chatId;

    /**
     * Состояние пользователя
     */
    private State state = State.INIT;

    private Notification notification = null;

    private int currentQuestionIndex = 0;

    private final List<Question> wrongAnswerQuestions = new ArrayList<>();

    private int currentWrongAnswerQuestionIndex = 0;

    /**
     * Конструктор - создание нового пользователя по id чата
     *
     * @param chatId - id чата нового пользователя
     */
    public User(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    /**
     * Функция получения значения поля {@link User#state}
     *
     * @return возвращает значение состояния
     */
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = Objects.requireNonNull(state);
        switch (state) {
            case INIT, TEST, REPEAT -> {
                notification = null;
                currentQuestionIndex = 0;
                currentWrongAnswerQuestionIndex = 0;
            }
        }
    }

    public Optional<Notification> getNotification() {
        return Optional.ofNullable(notification);
    }

    /**
     * Создать напоминание
     *
     * @param text текст напоминания
     */
    public Notification createNotification(String text) {
        this.notification = new Notification(text, chatId);
        return notification;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int nextQuestionIndex() {
        return ++currentQuestionIndex;
    }

    public List<Question> getWrongAnswerQuestions() {
        return wrongAnswerQuestions;
    }

    public void addWrongAnswerQuestion(Question question) {
        if (wrongAnswerQuestions.contains(question)) {
            return;
        }
        wrongAnswerQuestions.add(question);
    }

    public void removeWrongAnswerQuestion(Question question) {
        if (wrongAnswerQuestions.remove(question)) {
            currentWrongAnswerQuestionIndex--;
        }
    }

    public Optional<Question> nextWrongAnswerQuestion() {
        currentWrongAnswerQuestionIndex++;
        return getCurrentWrongAnswerQuestion();
    }

    public Optional<Question> getCurrentWrongAnswerQuestion() {
        if (currentWrongAnswerQuestionIndex >= wrongAnswerQuestions.size()) {
            return Optional.empty();
        }
        return Optional.of(wrongAnswerQuestions.get(currentWrongAnswerQuestionIndex));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chatId.equals(user.chatId);
    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}