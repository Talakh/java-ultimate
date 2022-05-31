package org.course;

import org.course.data.Accounts;
import org.course.model.Account;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public class DemoApp {
    public static void main(String[] args) {
        var accounts = Accounts.generateAccountList(20);

        var accountWithMaxBalance = accounts.stream()
                .max(composeComparatorThenComparing(createComparatorComparing(Account::getSex), Account::getBalance))
                .orElseThrow();

        System.out.println(accountWithMaxBalance);
    }

    private static <T, R extends Comparable<? super R>> Comparator<T> createComparatorComparing(Function<? super T, ? extends R> extractionFunction) {
        Objects.requireNonNull(extractionFunction);
        return (o1, o2) -> extractionFunction.apply(o1).compareTo(extractionFunction.apply(o2));
    }

    private static <T, R extends Comparable<? super R>> Comparator<T> composeComparatorThenComparing(Comparator<? super T> comparator, Function<? super T, ? extends R> extractionFunction) {
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(extractionFunction);
        return (o1, o2) -> {
            int compareResult = comparator.compare(o1, o2);
            return compareResult == 0 ? createComparatorComparing(extractionFunction).compare(o1, o2) : compareResult;
        };
    }
}
