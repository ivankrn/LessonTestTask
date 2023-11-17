package example.container;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    private Container container;

    @Before
    public void setUp() {
        container = new Container();
    }

    /**
     * При добавлении вещи она должна добавиться в контейнер
     */
    @Test
    public void testAdd() {
        assertEquals(0, container.size());
        Item item = new Item(1L);
        container.add(item);
        assertEquals(1, container.size());
        assertEquals(item, container.get(0));
    }

    /**
     * Если контейнер не пуст, то получение элемента должно проходить успешно
     */
    @Test
    public void testGet() {
        Item item = new Item(1L);
        container.add(item);
        assertEquals(item, container.get(0));
    }

    /**
     * Тестирует наличие элемента в контейнере
     */
    @Test
    public void testContains() {
        Item item = new Item(1L);
        assertFalse(container.contains(item));
        container.add(item);
        assertTrue(container.contains(item));
    }

    /**
     * Тестирует получение размера контейнера
     */
    @Test
    public void testSize() {
        Item item = new Item(1L);
        assertEquals(0, container.size());
        container.add(item);
        assertEquals(1, container.size());
    }

    /**
     * Тестирует удаление из контейнера
     */
    @Test
    public void testRemove() {
        Item item = new Item(1L);
        container.add(item);
        container.remove(item);
        assertEquals(0, container.size());
        assertFalse(container.contains(item));
    }
}