package com.OOP.EventTicketingSystemBackend.CLI.repositories;

import com.OOP.EventTicketingSystemBackend.CLI.models.User;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LoginRepository {
    private static BlockingQueue<String> usernames = new LinkedBlockingQueue<String>();
    private static BlockingQueue<User> users = new LinkedBlockingQueue<User>();
    private static Iterator<User> listOfUsers;

    // Get a json reading method that reads user info from a file and creates users

    // For registering users
    public static void addUser(User user){
        if (usernames.contains(user.getUserName())){
            throw new RuntimeException("Username taken");
        } else {
            try {
                System.out.println("Before placing user in repo");
                users.put(user);
                usernames.put(user.getUserName());
                System.out.println("After placing user in repo");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // For login
    public static String findUserRole(User loginUser){
        if (users.contains(loginUser)){
            return loginUser.getRole();
        } else {
            return "0"; // Some sort of error code
        }
    }

    public static synchronized String findUser(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return user.getRole();
            }
        }
        return "";
    }

    public static BlockingQueue<User> getUsers() {
        return users;
    }

    public static boolean checkUsername(String username){
        return usernames.contains(username);
    }

    public static void seeUsers(){
        listOfUsers = users.iterator();
        while (listOfUsers.hasNext()){
            System.out.println(listOfUsers.next().toString());
        }


    }
}
