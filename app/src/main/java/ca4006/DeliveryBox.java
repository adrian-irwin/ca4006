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

    public void newDelivery() {
        for (int i = 0; i < 10; i++) {
            String randomSection = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
            deliveryBox.put(randomSection, deliveryBox.get(randomSection) + 1);
        }
    }

    @Override
    public String toString() {
        return "DeliveryBox" + deliveryBox;
    }
}
