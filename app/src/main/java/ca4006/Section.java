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

    public synchronized int takeFromSection(Integer startTick) throws InterruptedException {
        while (this.stock <= 0) {
            wait();
        }
        this.stock -= 1;
        int waitTime = Main.current_tick - startTick;
        Main.addCustomerWaitTime(waitTime);
        Thread.sleep(Main.TICK_TIME);
        return waitTime;
    }

    public synchronized boolean addToSection() throws InterruptedException {
        if (this.stock >= this.maxStock) {
            return false;
        }
        this.stock += 1;
        Thread.sleep(Main.TICK_TIME);
        notify();
        return true;
    }
}
