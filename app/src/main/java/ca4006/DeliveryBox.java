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
        for (int i = 0; i < 10; i++) {
            String randomSection = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
            deliveryBox.put(randomSection, deliveryBox.get(randomSection) + 1);
        }
        notify();
    }

    public synchronized void takeFromDeliveryBox(StoreAssistant storeAssistant) throws InterruptedException {
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + "Start of takeFromDeliveryBox: " + deliveryBox + " by " + storeAssistant.name + ", store assistant currently has: " + storeAssistant.itemsHeld + Utils.RESET);
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
            System.out.println(Main.getCurrentTickTime() + Utils.GREEN + storeAssistant.name + " is trying to take " + items + " items from " + section + " from delivery box" + Utils.RESET);
            if (items + itemsHeldByAssistant > 10) {
                break;
            }
            storeAssistant.itemsHeld.put(section, storeAssistant.itemsHeld.get(section) + items);
            deliveryBox.put(section, deliveryBox.get(section) - items);
        }
        System.out.println(Main.getCurrentTickTime() + Utils.GREEN + "End of takeFromDeliveryBox" + deliveryBox + " by " + storeAssistant.name + ", store assistant now has: " + storeAssistant.itemsHeld + Utils.RESET);
        notify();
    }

    @Override
    public String toString() {
        return "DeliveryBox" + deliveryBox;
    }
}
