package ca4006;

public class Customer implements Runnable {
    public void purchaseItem(String sectionToPurchase) throws InterruptedException {
        Thread.sleep(Main.TICK_TIME);
        System.out.println("Customer purchasing item from " + sectionToPurchase);
        Section section = Main.sectionMap.get(sectionToPurchase);
        section.takeFromSection();
    }

    @Override
    public void run() {
        while (true) {
            if (Main.rand.nextInt(10) == 0) {
                String section = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
                if (Main.sectionMap.get(section).stock > 0) {
                    try {
                        System.out.println("Customer walking to " + section);
                        purchaseItem(section);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
