package example.note;

/**
 * Логика по работе с заметками<br>
 * <b>На самом деле класс ничего не делает, только создаёт видимость деятельности</b>
 */
public class NoteLogic {
    /**
     * Обработать сообщение
     */
    public String handleMessage(String message) {
        if (message.startsWith("/add")) {
            return "Note added!";
        } else if (message.startsWith("/notes")) {
            return "Your notes:";
        } else if (message.startsWith("/edit")) {
            return "Note edited!";
        } else if (message.startsWith("/del")) {
            return "Note deleted!";
        }
        return "Unknown command";
    }
}
