package project_2;


/** Timer class for roughly calculating running time of programs
 *  @author rbk
 *  Usage:  Timer timer = new Timer();
 *          timer.start();
 *          timer.end();
 *          System.out.println(timer);  // output statistics
 */
public class Timer {

    long startTime, endTime, elapsedTime, memAvailable, memUsed;
    boolean ready;

    /**
     * Initializes the timer and sets the start time.
     */
    public Timer() {
        startTime = System.currentTimeMillis();
        ready = false;
    }

    /**
     * Starts or restarts the timer by setting the current time as the start time.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        ready = false;
    }

    /**
     * Stops the timer, calculates elapsed time and memory usage.
     *
     * @return the current Timer instance after stopping.
     */
    public Timer end() {
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        memAvailable = Runtime.getRuntime().totalMemory();
        memUsed = memAvailable - Runtime.getRuntime().freeMemory();
        ready = true;
        return this;
    }

    /**
     * Returns the elapsed time in milliseconds.
     *
     * @return the time duration between start and end in milliseconds.
     */
    public long duration() {
        if (!ready) {
            end();
        }
        return elapsedTime;
    }

    /**
     * Returns the amount of memory used in bytes.
     *
     * @return the memory used during the program's execution in bytes.
     */
    public long memory() {
        if (!ready) {
            end();
        }
        return memUsed;
    }

    /**
     * Returns a string representation of the timer, showing elapsed time and memory usage.
     *
     * @return a formatted string with the elapsed time in milliseconds and memory usage in MB.
     */
    @Override
    public String toString() {
        if (!ready) {
            end();
        }
        return (
            "Time: " +
            elapsedTime +
            " msec.\n" +
            "Memory: " +
            (memUsed / 1048576) +
            " MB / " +
            (memAvailable / 1048576) +
            " MB."
        );
    }
}
