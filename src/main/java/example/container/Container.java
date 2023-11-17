package example.container;

import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер
 */
public class Container {
    private final List<Item> items = new ArrayList<>();

    public boolean add(Item item) {
        return items.add(item);
    }

    public boolean remove(Item item) {
        return items.remove(item);
    }

    public Item get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public boolean contains(Item item) {
        return items.contains(item);
    }
}
