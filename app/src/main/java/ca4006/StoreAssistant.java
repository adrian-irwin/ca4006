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

    public void stockItem(int itemsToStock, String sectionToStock) throws InterruptedException {
        for (int i = 0; i < itemsToStock; i++) {
            Thread.sleep(Main.TICK_TIME);
            Main.sectionMap.get(sectionToStock).addToSection();
            itemsHeld.put(sectionToStock, itemsHeld.get(sectionToStock) - 1);
        }
    }

    public void walkToSection() throws InterruptedException {
        int sum = itemsHeld.values().stream().mapToInt(Integer::intValue).sum();
        long ticks_to_walk = 10 + sum;
        Thread.sleep(Main.TICK_TIME * ticks_to_walk);
    }

    @Override
    public void run() {
        // pick up items from delivery box, go to sections of items held, stock item, walk back to delivery box
        while (true) {
            for (int i = 0; i < 10; i++) {
                String randomSection = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
                itemsHeld.put(randomSection, itemsHeld.get(randomSection) + 1);
            }
        }
    }
}
