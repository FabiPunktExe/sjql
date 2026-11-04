package de.fabiexe.sjql.util;

/**
 * A {@link java.util.function.Supplier} that can throw a checked exception.
 *
 * @param <T> the type of the supplied value
 * @param <U> the type of the checked exception
 */
@FunctionalInterface
public interface ThrowingSupplier<T, U extends Throwable> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws U if an error occurs
     */
    T get() throws U;
}
