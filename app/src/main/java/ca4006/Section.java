package ca4006;

public class Section {
    public String name;
    public int stock;
    public int maxStock;

    public Section(String name, int stock, int maxStock) {
        this.name = name;
        this.stock = stock;
        this.maxStock = maxStock;
    }

    public synchronized void takeFromSection() throws InterruptedException {
        while (this.stock <= 0) {
            wait();
        }
        this.stock -= 1;
        Thread.sleep(Main.TICK_TIME);
    }

    public synchronized boolean addToSection(String assistantName) {
        System.out.println(Utils.PURPLE + "__________DEBUG: " + Main.getCurrentTickTime() + assistantName + " is adding to section: " + this.name + "; before restock: " + this.stock);
        if (this.stock >= this.maxStock) {
            return false;
        }
        this.stock += 1;
        notify();
        return true;
    }
}
