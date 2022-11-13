package com.nc.reactive.flowapi;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Scanner;

public class Example1 {

    public static void main(String[] args) {


        getKeyReader()
                .filter(val -> !val.startsWith("_"))
                .observeOn(Schedulers.immediate())
                .subscribe(val -> {
                    if (val.startsWith("wait")) {
                        Integer timeout = Integer.parseInt(val.split(":")[1]);
                        try {
                            Thread.sleep(timeout);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("received -> " + val);
                }, System.err::println);
    }

    private static Observable<String> getKeyReader() {
        Scanner keyboard = new Scanner(System.in);
        return Observable.unsafeCreate(subscriber -> {
            while (!subscriber.isUnsubscribed()) {
                String input = keyboard.nextLine();
                if (input.equals("exit")) {
                    subscriber.unsubscribe();
                } else {
                    subscriber.onNext(input);
                }
            }
        });
    }
}
