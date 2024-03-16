package ca4006;

import java.util.HashMap;
import java.util.Map;

public class StoreAssistant implements Runnable {
    public Map<String, Integer> itemsHeld = new HashMap<>();
    public String name;
    public int stockTries;

    public StoreAssistant(String name) {
        this.name = name;
        for (String section : Main.sections) {
            itemsHeld.put(section, 0);
        }
        this.stockTries = 0;
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
//                System.out.println(Utils.PURPLE + "__________DEBUG: " + Utils.RED + name + " could not stock item in section: " + sectionToStock + Utils.RESET);
                stockTries += 1;
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
        int prevPickup = 0;
        int newPickup;
        // pick up items from delivery box, go to sections of items held, stock item, walk back to delivery box
        while (true) {
            try {
//                System.out.println(Utils.PURPLE + "__________DEBUG: " + Main.getCurrentTickTime() + Utils.CYAN + name + " has " + itemsHeld + Utils.RESET);
                if (sumOfItemsHeld() <= 0 || stockTries >= 3) {
                    newPickup = Main.current_tick;
                    Main.deliveryBox.takeFromDeliveryBox(this);
                    Main.assistantDeliveryPickups.add(newPickup - prevPickup);
                    prevPickup = newPickup;
                    walkToSection();
                    stockTries = 0;
                }
                for (String section : Main.sections) {
                    if (itemsHeld.get(section) > 0) {
                        stockItem(itemsHeld.get(section), section);
                        walkToSection();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
