package ca4006;

public class Customer implements Runnable {
    public String name;
    public String sectionToPurchase;

    public Customer(String name, String sectionToPurchase) {
        this.name = name;
        this.sectionToPurchase = sectionToPurchase;
    }

    public void purchaseItem(String sectionToPurchase) throws InterruptedException {
        Section section = Main.sectionMap.get(sectionToPurchase);
        section.takeFromSection();
        System.out.println(Main.getCurrentTickTime() + name + " purchasing item from " + sectionToPurchase);
    }

    @Override
    public void run() {
        while (true) {
            if (Main.sectionMap.get(this.sectionToPurchase).stock > 0) {
                try {
                    purchaseItem(this.sectionToPurchase);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
