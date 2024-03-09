package ca4006;

public class Section {
    public String name;
    public int stock;

    public Section(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public synchronized void takeFromSection() {
        if (this.stock <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.stock -= 1;
    }

    public synchronized void addToSection() {
        this.stock += 1;
        notify();
    }
}
