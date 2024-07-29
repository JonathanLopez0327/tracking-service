package service.budget.tracking.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimpleTest {

    @DisplayName("Test")
    @Test
    void test() {
        System.out.println("Test");
        Assertions.assertTrue(true);
    }
}
