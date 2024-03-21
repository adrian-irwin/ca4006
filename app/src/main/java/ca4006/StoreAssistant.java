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
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + name + " began stocking " + itemsToStock + " items in section: " + sectionToStock);
        for (int i = 0; i < itemsToStock; i++) {
            if (!Main.sectionMap.get(sectionToStock).addToSection()) {
                System.out.println(Utils.PURPLE + "__________DEBUG: " + Utils.RED + name + " could not stock item in section: " + sectionToStock + Utils.RESET);
                stockTries += 1;
                break;
            }
            itemsHeld.put(sectionToStock, itemsHeld.get(sectionToStock) - 1);
        }
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + name + " finished stocking " + itemsToStock + " items in section: " + sectionToStock);
    }

    public void walkToSection(String section) throws InterruptedException {
        long ticks_to_walk = 10 + sumOfItemsHeld();
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + name + " is walking to " + section + "; time to walk: " + ticks_to_walk + " ticks" + Utils.RESET);
        Thread.sleep(Main.TICK_TIME * ticks_to_walk);
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + name + " has arrived at " + section + Utils.RESET);
    }

    @Override
    public void run() {
        int prevPickup = 0;
        int newPickup;
        // pick up items from delivery box, go to sections of items held, stock item, walk back to delivery box
        while (true) {
            try {
                if (sumOfItemsHeld() <= 0 || stockTries >= 3) {
                    if (Main.current_tick != 0) {
                        walkToSection("delivery box");
                    }
                    newPickup = Main.current_tick;
                    Map<String, Integer> itemsTaken = Main.deliveryBox.takeFromDeliveryBox(this);
                    int timeSinceLastPickup = newPickup - prevPickup;

                    StringBuilder itemsTakenString = new StringBuilder();
                    for (String section : itemsTaken.keySet()) {
                        itemsTakenString.append("'").append(section).append("'=");
                        itemsTakenString.append(itemsTaken.get(section)).append("; ");
                    }

                    System.out.println(Main.getCurrentTickTime() + Utils.GREEN + name + " picked up items from delivery box: " + itemsTakenString + "Time since last pickup: " + timeSinceLastPickup + Utils.RESET);

                    Main.addAssistantDeliveryPickup(timeSinceLastPickup);
                    prevPickup = newPickup;
                    stockTries = 0;
                }
                for (String section : Main.sections) {
                    if (itemsHeld.get(section) > 0) {
                        walkToSection(section);
                        stockItem(itemsHeld.get(section), section);
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
