package instructionselection;

import java.io.*;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VM2MTest {
    private static final InputStream DEFAULT_STDIN = System.in;
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void rollbackChangesToStdin() {
        try {
            outContent.reset();
            errContent.reset();
        } catch (Exception e) {

        }

        System.setIn(DEFAULT_STDIN);
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.out.println(errContent.toString());
    }

    @Test
    public void factorialTest() {
        try {
            File inputFile = new File("./tester/Phase4Tester/SelfTestCases/Factorial.vaporm");
            System.setIn(new FileInputStream(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }

        VM2M.instructionSelection();
        assertEquals("ALWAYS FAIL", outContent.toString());
    }

    @Test
    public void binaryTreeTest() {
        try {
            File inputFile = new File("./tester/Phase4Tester/SelfTestCases/BinaryTree.vaporm");
            System.setIn(new FileInputStream(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }

        VM2M.instructionSelection();
        assertEquals("ALWAYS FAIL", outContent.toString());
    }

    @Test
    public void binaryTreeOptTest() {
        try {
            File inputFile = new File("./tester/Phase4Tester/SelfTestCases/BinaryTree.opt.vaporm");
            System.setIn(new FileInputStream(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }

        VM2M.instructionSelection();
        assertEquals("ALWAYS FAIL", outContent.toString());
    }

    @Test
    public void bubbleSortTest() {
        try {
            File inputFile = new File("./tester/Phase4Tester/SelfTestCases/BubbleSort.vaporm");
            System.setIn(new FileInputStream(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }

        VM2M.instructionSelection();
        assertEquals("ALWAYS FAIL", outContent.toString());
    }

    @Test
    public void linearSearchTest() {
        try {
            File inputFile = new File("./tester/Phase4Tester/SelfTestCases/LinearSearch.vaporm");
            System.setIn(new FileInputStream(inputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }

        VM2M.instructionSelection();
        assertEquals("ALWAYS FAIL", outContent.toString());
    }

}
