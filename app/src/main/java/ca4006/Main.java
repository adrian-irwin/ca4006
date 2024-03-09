/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca4006;

import java.util.List;
import java.util.Random;


public class Main {
    public static int TICK_TIME = 100;
    public static Random rand = new Random(42);
    public static int numberOfStoreAssistants = 3;

    public static List<String> sections = List.of("electronics", "clothing", "furniture", "toys", "sporting goods", "books");

    public static void main(String[] args) {
        System.out.println("TICK_TIME: " + TICK_TIME);

        DeliveryBox deliveryBox = new DeliveryBox();
        System.out.println(deliveryBox);

        while (true) {
            try {
                Thread.sleep(TICK_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int randomInt = rand.nextInt(100);

            if (randomInt == 0) {
                deliveryBox.newDelivery();
                System.out.println(deliveryBox);
            }
        }
    }

}
