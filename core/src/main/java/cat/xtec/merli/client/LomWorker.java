package cat.xtec.merli.client;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import cat.xtec.merli.domain.UID;


/**
 * Service for the processing of LOM resources on a background thread.
 *
 * This class creates a background thread that waits for LOM clients
 * to be queued and then processes each resource object returned by the
 * clients one after the other.
 *
 * It is important to note that this worker may get stuck indefinitely
 * if a client or a consumer performs a non-interrumpible operation;
 * preventing any new tasks from being processed. For that reason,
 * implementers must ensure that all the operations that are waiting for
 * some resource are interrumpible and to close any open sockets when
 * the returned future tasks are done.
 */
public final class LomWorker {

    /** Unique thread counter */
    private static int tid = 0;

    /** Queue of client tasks to process */
    private final BlockingQueue<FutureTask> queue;

    /** Main background thread of the worker */
    private final WorkerThread worker;

    /** Current task beeing procesed */
    private FutureTask runningTask;

    /** Whether the worker was shutted down */
    private volatile boolean terminated = false;


    /**
     * Intantiates a new worker.
     */
    public LomWorker() {
        queue = new LinkedBlockingQueue<>();
        worker = new WorkerThread();
        worker.start();
    }


    /**
     * Queues a new resource consumer task on a client.
     *
     * @param client            LOM client instance
     * @param consumer          Resource consumer
     *
     * @throws NullPointerException     If client is {@code null}
     * @throws IllegalStateException    If the worker was terminated
     */
    public synchronized Future<?> queue(
        final LomClient client, final LomConsumer consumer) {

        if (terminated == true) {
            throw new IllegalStateException(
                "Crawler was terminated");
        }

        WorkerTask task = new WorkerTask(client, consumer);
        FutureTask<?> future = new FutureTask<>(task, null);

        queue.add(future);

        return future;
    }


    /**
     * Gracefully terminates this worker.
     *
     * When a worker is terminated the background thread is destroyed
     * and any running or queued task canceled. After the worker is
     * terminated any attemps to enqueue new tasks will result on a
     * {@code IllegalStateException}.
     */
    public synchronized void shutdown() {
        terminated = true;
        worker.interrupt();

        if (runningTask instanceof FutureTask) {
            runningTask.cancel(true);
        }

        for (FutureTask task : queue) {
            task.cancel(true);
            queue.remove(task);
        }
    }


    /**
     * Background consumer thread. Listens for new task to be queued
     * and then processed them in order.
     */
    private class WorkerThread extends Thread {

        /**
         * Creates a new worker thread.
         */
        public WorkerThread() {
            super("LomWorker-" + (++tid));
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (!terminated) {
                try {
                    runningTask = queue.take();
                    runningTask.run();
                } catch (InterruptedException e) {
                    if (terminated == false) {
                        shutdown();
                    }
                } catch (Exception e) {
                    runningTask.cancel(false);
                }
            }
        }
    }


    /**
     * Represents a consuming task. When executed a list of resources
     * are obtained from the client and consumed in the order they are
     * returned by the client by executing the provided consumer.
     */
    private class WorkerTask implements Runnable {

        /** Client instance to process */
        private final LomClient client;

        /** Consumer of the resources */
        private final LomConsumer consumer;


        /**
         * Creates a new consumer task for a given client.
         *
         * @param client            LOM client to consume
         * @param consumer          Operation to perform on each resource
         */
        public WorkerTask(LomClient client, LomConsumer consumer) {
            if (client instanceof LomClient == false) {
                throw new NullPointerException(
                    "A client instance must be provided");
            }

            if (consumer instanceof LomConsumer == false) {
                throw new NullPointerException(
                    "A consumer instance must be provided");
            }

            this.client = client;
            this.consumer = consumer;
        }


        /**
         * {@inheritDoc}
         */
        public void run() {
            Iterator<UID> iterator = client.identifiers();

            while (!terminated && iterator.hasNext()) {
                UID id = iterator.next();
                consumer.accept(id);
            }
        }
    }

}
