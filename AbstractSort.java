
public abstract class AbstractSort {
    private long count;
    private long startTime;
    private long time;

    public abstract void sort(int[] list);

    public void startSort() {
        count = 0;
        startTime = System.nanoTime();
    }

    public void endSort() {
        time = System.nanoTime() - startTime;
    }

    public void incrementCount() {
        count++;
    }

    public long getCount() {
        return count;
    }

    public long getTime() {
        return time;
    }
}
