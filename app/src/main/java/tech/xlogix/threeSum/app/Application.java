package tech.xlogix.threeSum.app;

import tech.xlogix.threeSum.model.WorkerThread;

public class Application extends android.app.Application {
    private final String TAG = Application.class.getSimpleName();


    private static Application mInstance;

    public static Application the() {
        return mInstance;
    }

    public Application() {
        mInstance = this;
    }


    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }
}