package org.course;


import org.course.data.Accounts;

import java.time.LocalDate;

public class DemoApp {
    public static void main(String[] args) {
        var accounts = Accounts.generateAccountList(10);
        var emailToBirthdayTable = new HashTable<String, LocalDate>(10);
        accounts.forEach(a -> emailToBirthdayTable.put(a.getEmail(), a.getBirthday()));
        emailToBirthdayTable.printTable();
    }
}
