//package org.leviverkerk.todolist;
//
//import org.leviverkerk.todolist.model.User;
//import org.leviverkerk.todolist.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@SpringBootApplication
//public class Application implements CommandLineRunner {
//
//    @Autowired
//    private PasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//
//    @Override
//    public void run(String...args) throws Exception {
//        userRepository.save(new User("lmiv", bCryptPasswordEncoder.encode("test")));
//    }
//}