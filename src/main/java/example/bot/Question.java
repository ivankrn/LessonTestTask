package example.bot;

import java.util.Objects;

/**
 * Вопрос
 */
public class Question {
    private final String text;

    private final String answer;

    public Question(String text, String answer) {
        this.text = Objects.requireNonNull(text);
        this.answer = Objects.requireNonNull(answer);
    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return text.equals(question.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
