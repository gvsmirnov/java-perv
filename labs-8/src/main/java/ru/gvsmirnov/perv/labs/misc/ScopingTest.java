package ru.gvsmirnov.perv.labs.misc;

public class ScopingTest {

    public volatile int size = (int) (Runtime.getRuntime().maxMemory() * 0.6);
    public volatile int allocatedSize;

    public static void main(String[] args) throws Exception {

        ScopingTest test = new ScopingTest();

        for (int i = 0; i < 1000; i++) {
            test.allocateMemory(i);
        }
    }

    private void allocateMemory(int i) {
        try {
            {
                byte[] bytes = new byte[size];
                allocatedSize = bytes.length;
            }

            byte[] moreBytes = new byte[size];
            allocatedSize = moreBytes.length;

            System.out.println("I allocated memory successfully " + i);

        } catch (OutOfMemoryError e) {
            System.out.println("I failed to allocate memory " + i);
        }
    }

}
