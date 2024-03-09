package ca4006;

import java.util.HashMap;
import java.util.Map;

public class StoreAssistant implements Runnable {
    public static Map<String, Integer> itemsHeld = new HashMap<>();

    public StoreAssistant() {
        for (String section : Main.sections) {
            itemsHeld.put(section, 0);
        }
    }

    public void stockItem() throws InterruptedException {
        Thread.sleep(Main.TICK_TIME * 10L);
    }

    @Override
    public void run() {
    }
}
