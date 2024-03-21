# Design Documentation

## 1. List of working functionality

- Time in the thrift store is measured in ticks. Every 100 ticks (on average) a delivery is made of 10 items, with a
  random number of items for each of the categories.
- All tick timings for walking to sections, picking up items, stocking sections etc.
- Capacity is limited to 10 in each section.
- Multi-threaded Customers and assistants.
- 6 sections ("electronics", "clothing", "furniture", "toys", "sporting goods", "books")
- Real-time reporting
- Adjustable tick time
- Prioritising sections with lower stock
- Configurable parameters (tick time, number of assistants and maximum number of customers waiting at each section)
- Statistic gathering (Average customer wait time and average time between assistant delivery pickups)
- Partial stocking of sections

## 2. Division of work

We worked on this assignment together and made equal contributions in all regards. We worked together
using pair programming techniques and collaboration tools like the 'Code With Me' feature on IntelliJ. We also used a
GitHub repository to commit our changes periodically. This repo can be found at https://github.com/adrian-irwin/ca4006/

## 3. Running the code

Our code can be compiled with `javac *.java` and executed with `java main`.
To execute the code with custom parameters, provide the name of the parameter followed by the value you want to
configure it to. The Parameters that can be customised are as follows:

- `tick` is the length of a single tick in milliseconds (default: 100 ms).
- `assistants` is the number of assistants in the shop (default: 3).
- `customers` is the maximum number of customers waiting at each section (default: 3)
- `end` is the number of days the simulation will run for (default: -1 (infinite)).

## 4. The considered tasks/dependencies in your program

### Tasks

- StoreAssistant
    - Restocking sections
    - Picking up deliveries
    - Walking to sections
- Customer
    - Purchasing from sections

## 5. Patterns/strategies used to manage concurrency

We used a thread pool managed by ExecutorService for both the customer per section and all the assistants. This allowed
to us to manage the number
of running threads at a time and move the customers through the sections in a FIFO order.

Both the Customer and StoreAssistant classes extend the Runnable interface this allows us to define a run method for
each of the threads that are created. The ExecutorService runs this defined method on the start of each thread.

We also used implicit monitors to encapsulate operations that manipulate any shared data to prevent any synchronisation
issues.

## 6. Detailed description of how solution addresses issues like fairness, prevention of starvation etc.

### Race conditions

To prevent race conditions with the delivery box and the sections we implemented implicit monitors using
Java `synchronized` keyword in critical sections of code. For example, when updating the stock in a section.

```java
public synchronized boolean addToSection() throws InterruptedException {
    if (this.stock >= this.maxStock) {
        return false;
    }
    this.stock += 1;
    Thread.sleep(Main.TICK_TIME);
    notify();
    return true;
}
```

### Fairness

Using thread pools for customers and assistants allows us to manage the number of threads that are running at any given
time. Setting the max customers to 1 allows us to ensure that customers are served in the order they arrive at a
section, ExecutorService creates a FIFO queue of customers. Store assistants are all able to run at the same time and
eventually make progress independent of each other. We also had to consider that using the synchronized keyword did not
guarantee fairness.

### Starvation

Our system prevents starvation in the sections by using wait() and notify() to allow the assistants to restock a section
if it is empty.

```java
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
```

A customer could potentially starve other threads by holding onto the lock on a section if it is empty.
The ` wait()` prevents this by releasing the lock and letting the thread sleep until it is awakened by
the assistants ` notify()` in ` addToSection()`.