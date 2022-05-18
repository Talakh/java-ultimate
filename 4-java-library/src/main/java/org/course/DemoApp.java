package org.course;

import org.course.data.Accounts;
import org.course.model.Account;

import java.util.Comparator;
import java.util.Objects;
import java.util.Stack;

public class DemoApp {
    public static void main(String[] args) {
        var head = createLinkedList(4, 3, 9, 1);
        printReversedRecursively(head);
        printReversedUsingStack(head);


        Comparator<Account> accountComparator = new RandomFieldComparator<>(Account.class);
        System.out.println(accountComparator);
        Accounts.generateAccountList(10)
                .stream()
                .sorted(accountComparator)
                .forEach(System.out::println);
    }

    /**
     * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
     *
     * @param elements an array of elements that should be added to the list
     * @param <T>      elements type
     * @return head of the list
     */
    public static <T> Node<T> createLinkedList(T... elements) {
        Objects.requireNonNull(elements);
        Node<T> head = null;
        Node<T> curr;
        if (elements.length > 0) {
            head = new Node<>(elements[0]);
            curr = head;
            for (int i = 1; i < elements.length; i++) {
                curr.next = new Node<>(elements[i]);
                curr = curr.next;
            }
        }
        return head;
    }

    /**
     * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
     * just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedRecursively(Node<T> head) {
        Objects.requireNonNull(head);
        printRecursively(head.next);
        System.out.println(head.element);
    }

    private static <T> void printRecursively(Node<T> head) {
        if (head != null) {
            printRecursively(head.next);
            System.out.print(head.element + " -> ");
        }
    }

    /**
     * Prints a list in a reserved order using a {@link java.util.Stack} instance. Please note that it should not change
     * the list, just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedUsingStack(Node<T> head) {
        Objects.requireNonNull(head);
        var stack = new Stack<T>();
        var curr = head;

        while (curr != null) {
            stack.push(curr.element);
            curr = curr.next;
        }

        while (stack.size() > 1) {
            System.out.print(stack.pop() + " -> ");
        }
        System.out.println(stack.pop());

    }
}
