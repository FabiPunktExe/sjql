package de.fabiexe.sjql.util;

@FunctionalInterface
public interface ThrowingSupplier<T, U extends Throwable> {
    T get() throws U;
}
