package de.fabiexe.sjql.util;

/**
 * A {@link Runnable} that can throw a checked exception.
 *
 * @param <T> the type of the checked exception
 */
@FunctionalInterface
public interface ThrowingRunnable<T extends Throwable> {
    /**
     * Runs this operation.
     *
     * @throws T if an error occurs
     */
    void run() throws T;
}
