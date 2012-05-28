/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Node;

/**
 *
 * @author slaweet
 */
public class XQueryEvaluatorTest {
    
    private File testFile;
    
    public XQueryEvaluatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        testFile = new File("test.xml");
        PrintWriter pw;
        try {
            testFile.createNewFile();
            pw = new PrintWriter(testFile);
            pw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            +"<books><shop/><shop/></books>\n");
            pw.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XQueryEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XQueryEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        testFile.delete();
    }
    
    /**
     * Test of eval method, of class XQueryEvaluator.
     */
    @Test
    public void testEval() {
        System.out.println("eval");
        String xQuery = "<books>{let $doc := doc('test.xml')"
                +"for $shop in $doc//shop \n"
                +"return 5}</books>";
        String invalidXQuery = "<books>{let $doc := doc('test.xml')"
                +"for $shop in $doc//shop \n"
                +"return $shop $shop}</books>";
        String filename = "test.xml";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            +"<books>5 5</books>";
        String result;
        
        Evaluator evaluator = new XQueryEvaluator();
        try {
            result = evaluator.eval(xQuery, filename);
            System.out.println(result);
            System.out.println(expected);
            assertEquals(result, expected);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
         
        try {
            result = evaluator.eval(invalidXQuery, filename);
            fail("invalid XQuery doesn't cause SyntaxErorException");
        } catch (SyntaxErorException ex) {
            //OK
        } catch (Exception ex) {
            fail("invalid XQuery cause exception:" + ex.getMessage());
        }
    }

}