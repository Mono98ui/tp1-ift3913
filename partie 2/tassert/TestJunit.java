public class TestJunit {
}
import org.junit.Test;
        import static org.junit.Assert.*;

public class ExampleTest {

    @Test
    public void testAssertions() {
        // Sample values for testing
        int a = 5;
        int b = 7;
        String str1 = "hello";
        String str2 = "world";

        // assertEquals: Verifies if two values are equal
        assertEquals("Values are not equal", a, b);

        // assertTrue and assertFalse: Verifies a condition is true or false
        assertTrue("Condition should be true", a < b);
        assertFalse("Condition should be false", a > b);

        // assertNull and assertNotNull: Verifies if an object is null or not null
        Object obj = null;
        assertNull("Object should be null", obj);
        assertNotNull("Object should not be null", str1);

        // assertSame and assertNotSame: Verifies if two objects are the same or not the same
        assertSame("Objects should be the same", str1, str2);
        assertNotSame("Objects should not be the same", str1, new String(str1));

        // assertArrayEquals: Verifies if two arrays are equal
        int[] expectedArray = {1, 2, 3};
        int[] actualArray = {1, 2, 3};
        assertArrayEquals("Arrays are not equal", expectedArray, actualArray);

        // Intentional failure
        fail("This test intentionally fails");
    }
}

