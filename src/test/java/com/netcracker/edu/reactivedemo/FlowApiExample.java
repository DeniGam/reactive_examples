package com.netcracker.edu.reactivedemo;

import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FlowApiExample extends Common {


    @Test
    public void simpleSubscribe() throws InterruptedException {
        System.out.println("Start simpleSubscribe");
        SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new ConsumerSubscriber<>(this::print));
        data.forEach(publisher::submit);
        System.out.println("End simpleSubscribe");

        Thread.sleep(1000);
    }

    @Test
    public void transform() throws InterruptedException {
        System.out.println("Start transform");
        SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
        FunctionMapper<Object, String> mapper = new FunctionMapper<>(this::toTypedString);
        publisher.subscribe(mapper);
        mapper.subscribe(new ConsumerSubscriber<>(this::print));
        data.forEach(publisher::submit);
        System.out.println("End transform");
        Thread.sleep(1000);
    }


    @Test
    public void filter() throws InterruptedException {

        System.out.println("Start filter");

        SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
        FunctionMapper<Object, String> mapper = new FunctionMapper<>(this::toTypedString);
        Filter<Object> filter = new Filter<>(this::filterStrings);

        publisher.subscribe(filter);
        filter.subscribe(mapper);
        mapper.subscribe(new ConsumerSubscriber<>(this::print));


        data.forEach(publisher::submit);
        System.out.println("End filter");
        Thread.sleep(1000);
    }


    private static final class ConsumerSubscriber<T> implements Flow.Subscriber<T> {
        private final Consumer<T> consumer;

        private Flow.Subscription subscription;

        private ConsumerSubscriber(Consumer<T> consumer) {
            this.consumer = consumer;
        }


        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T item) {
            consumer.accept(item);
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("Done");
        }
    }

    private static class FunctionMapper<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {
        private final Function<T, R> function;

        private Flow.Subscription subscription;

        private FunctionMapper(Function<T, R> function) {
            super();
            this.function = function;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T item) {
            submit((R) function.apply(item));
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            close();
        }
    }

    private static class Filter<T> extends SubmissionPublisher<T> implements Flow.Processor<T, T> {

        private final Predicate<T> predicate;
        private Flow.Subscription subscription;

        private Filter(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T item) {
            if (predicate.test(item)) {
                submit(item);
                subscription.request(Long.MAX_VALUE);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            close();
        }
    }
}
