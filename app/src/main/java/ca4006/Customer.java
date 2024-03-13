package ca4006;

public class Customer implements Runnable {
    public String name;

    public Customer(String name) {
        this.name = name;
    }

    public void purchaseItem(String sectionToPurchase) throws InterruptedException {
        System.out.println(Main.getCurrentTickTime() + name + " purchasing item from " + sectionToPurchase);
        Section section = Main.sectionMap.get(sectionToPurchase);
        section.takeFromSection();
    }

    @Override
    public void run() {
        String section = Main.sections.get(Main.rand.nextInt(Main.sections.size()));
        while (true) {
            if (Main.sectionMap.get(section).stock > 0) {
                try {
                    purchaseItem(section);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
