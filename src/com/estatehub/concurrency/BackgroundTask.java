
package com.estatehub.concurrency;

public class BackgroundTask implements Runnable {

    private String taskName;

    public BackgroundTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(
            Thread.currentThread().getName()
            + " is executing: " + taskName
        );

        for (int i = 1; i <= 3; i++) {
            System.out.println(taskName + " - Step " + i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(taskName + " was interrupted.");
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println(taskName + " completed.");
    }
}
