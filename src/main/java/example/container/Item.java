package example.container;

/**
 * Элемент контейнера
 */
public class Item {
    private final long id;

    Item(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
