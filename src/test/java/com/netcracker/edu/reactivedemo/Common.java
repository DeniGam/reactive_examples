package com.netcracker.edu.reactivedemo;

import java.util.Collection;
import java.util.List;

public class Common {

    private static final long TIMEOUT = 1000L;

    protected final Collection<Object> data = List.of(1, 2L, 0.1, "string", new Object(), 'c');

    protected String toTypedString(Object o) {
        return "[" + Thread.currentThread().getName() + "] " + (o == null ? "null" : o.getClass().getSimpleName() + "\t:\t" + o);
    }

    protected String slowToTypedString(Object o) {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return toTypedString(o);
    }

    protected boolean filterStrings(Object o) {
        return !(o instanceof CharSequence);
    }

    protected boolean slowFilterString(Object o) {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return filterStrings(o);
    }

    protected void print(Object o) {
        System.out.println(o);
    }

    protected void slowPrint(Object o) {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        print(o);
    }

}
