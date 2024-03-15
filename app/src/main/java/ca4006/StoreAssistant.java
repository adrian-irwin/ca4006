package ca4006;

import java.util.HashMap;
import java.util.Map;

public class StoreAssistant implements Runnable {
    public Map<String, Integer> itemsHeld = new HashMap<>();
    public String name;


    public StoreAssistant(String name) {
        this.name = name;
        for (String section : Main.sections) {
            itemsHeld.put(section, 0);
        }
    }

    public int sumOfItemsHeld() {
        int sum = 0;
        for (int value : itemsHeld.values()) {
            sum += value;
        }
        return sum;
    }

    public void stockItem(int itemsToStock, String sectionToStock) throws InterruptedException {
        for (int i = 0; i < itemsToStock; i++) {
            Thread.sleep(Main.TICK_TIME);
            boolean stockedStatus = Main.sectionMap.get(sectionToStock).addToSection(name);
            if (!stockedStatus) {
                System.out.println(Main.PURPLE + "__________DEBUG: " + Main.RED + name + " could not stock item in section: " + sectionToStock + Main.RESET);
                break;
            }
            itemsHeld.put(sectionToStock, itemsHeld.get(sectionToStock) - 1);
        }
    }

    public void walkToSection() throws InterruptedException {
        int sum = sumOfItemsHeld();
        long ticks_to_walk = 10 + sum;
        Thread.sleep(Main.TICK_TIME * ticks_to_walk);
    }

    @Override
    public void run() {
        // pick up items from delivery box, go to sections of items held, stock item, walk back to delivery box
        while (true) {
            try {
                walkToSection();
                System.out.println(Main.PURPLE + "__________DEBUG: " + Main.CYAN + name + " has " + itemsHeld + Main.RESET);
                Main.deliveryBox.takeFromDeliveryBox(this);
                for (String section : Main.sections) {
                    if (itemsHeld.get(section) > 0) {
                        stockItem(itemsHeld.get(section), section);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
