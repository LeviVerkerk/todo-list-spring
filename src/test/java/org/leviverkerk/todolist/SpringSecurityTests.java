package org.leviverkerk.todolist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

public class SpringSecurityTests {

    @Test
    void hashTest()
    {
        final PasswordEncoder bCrypt = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        IntStream.range(0, 10)
                .mapToObj(i -> bCrypt.encode("test"))
                .peek(System.out::println)
                .forEach(s -> Assertions.assertTrue(bCrypt.matches("test", s)));
    }

}
