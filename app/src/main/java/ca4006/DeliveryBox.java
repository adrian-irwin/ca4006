package ca4006;

import java.util.HashMap;
import java.util.Map;

public class DeliveryBox {
    public static Map<String, Integer> deliveryBox = new HashMap<>();

    public DeliveryBox() {
        for (String section : Main.sections) {
            deliveryBox.put(section, 0);
        }
    }

    public int sumOfItemsInDeliveryBox() {
        int sum = 0;
        for (int value : deliveryBox.values()) {
            sum += value;
        }
        return sum;
    }

    public synchronized void newDelivery() {
        Map<String, Integer> itemsDelivered = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String randomSection = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
            deliveryBox.put(randomSection, deliveryBox.get(randomSection) + 1);
            itemsDelivered.put(randomSection, itemsDelivered.getOrDefault(randomSection, 0) + 1);
        }
        System.out.print(Main.getCurrentTickTime() + Utils.PURPLE + "New delivery of items: ");
        for (String section : itemsDelivered.keySet()) {
            System.out.print("'" + section + "'=" + itemsDelivered.get(section) + " ");
        }
        System.out.println(Utils.RESET);
        notify();
    }

    public synchronized Map<String, Integer> takeFromDeliveryBox(StoreAssistant storeAssistant) throws InterruptedException {
        Map<String, Integer> itemsTaken = new HashMap<>();
        while (sumOfItemsInDeliveryBox() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (String section : Main.sections) {
            int itemsHeldByAssistant = storeAssistant.sumOfItemsHeld();
            if (itemsHeldByAssistant >= 10) {
                break;
            }
            int items = (deliveryBox.get(section) >= 3) ? Math.min(3, 10 - itemsHeldByAssistant) : deliveryBox.get(section);
            if (items == 0) {
                continue;
            }
            if (items + itemsHeldByAssistant > 10) {
                break;
            }
            storeAssistant.itemsHeld.put(section, storeAssistant.itemsHeld.get(section) + items);
            deliveryBox.put(section, deliveryBox.get(section) - items);
            itemsTaken.put(section, items);
        }
        notify();
        return itemsTaken;
    }

    @Override
    public String toString() {
        return "DeliveryBox" + deliveryBox;
    }
}
