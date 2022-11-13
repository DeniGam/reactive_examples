package com.nc.reactive.flowapi;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

public class RxJavaExample extends Common {

    private Observable<Object> observable;

    @Before
    public void init() {
        observable = Observable.from(data);
    }

    @Test
    public void simpleSubscribe() {
        System.out.println("Start simpleSubscribe");
        observable
                .subscribe(this::print);
        System.out.println("End simpleSubscribe");

    }

    @Test
    public void transform() {
        System.out.println("Start transform");

        observable
                .map(this::toTypedString)
                .subscribe(this::print);

        System.out.println("End transform");

    }

    @Test
    public void filter() {
        System.out.println("Start filter");
        observable
                .filter(this::filterStrings)
                .map(this::toTypedString)
                .subscribe(this::print);
        System.out.println("End filter");
    }


    @Test
    public void async() throws InterruptedException {
        System.out.println("Start filter");
        observable
                .filter(this::filterStrings)
                .flatMap(o -> Observable.just(o)
                        .subscribeOn(Schedulers.computation())
                        .map(this::slowToTypedString))
                .subscribe(this::print);
        System.out.println("End filter");
        Thread.sleep(3000);
    }
}
