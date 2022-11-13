package com.nc.reactive.flowapi;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FlowApiExample {

    @Test
    public void simpleSubscribe() throws InterruptedException {
        System.out.println("Start test");
        List<Object> elements = List.of(1, 2L, 0.1, "string", new Object(), 'c');
        try (SubmissionPublisher<Object> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(new ConsumerSubscriber<>(System.out::println));
            elements.forEach(publisher::submit);
            System.out.println("End test");
        }
        Thread.sleep(1000);
    }

    @Test
    public void transform() throws InterruptedException {
        System.out.println("Start test");
        List<Object> elements = List.of(1, 2L, 0.1, "string", new Object(), 'c');
        SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
        FunctionMapper<Object, String> mapper = new FunctionMapper<>(o -> o == null ? "null" : o.getClass().getSimpleName() + "\t:\t" + o);
        publisher.subscribe(mapper);
        mapper.subscribe(new ConsumerSubscriber<>(System.out::println));
        elements.forEach(publisher::submit);
        System.out.println("End test");
        Thread.sleep(1000);
    }


    @Test
    public void filter() throws InterruptedException {

        System.out.println("Start test");

        List<Object> elements = List.of(1, 2L, 0.1, "string", new Object(), 'c');
        SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
        FunctionMapper<Object, String> mapper = new FunctionMapper<>(o -> o == null ? "null" : o.getClass().getSimpleName() + "\t:\t" + o);
        Filter<Object> filter = new Filter<>(o -> !(o instanceof CharSequence));

        publisher.subscribe(filter);
        filter.subscribe(mapper);
        mapper.subscribe(new ConsumerSubscriber<>(System.out::println));


        elements.forEach(publisher::submit);
        System.out.println("End test");
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
