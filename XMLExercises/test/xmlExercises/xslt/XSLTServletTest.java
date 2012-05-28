/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises.xslt;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.junit.Assert.*;
import xmlExercises.Constants;
import xmlExercises.Utils;

/**
 *
 * @author jobas
 */
public class XSLTServletTest {

    public XSLTServletTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processRequest method, of class XSLTServlet.
     */
    @Test
    public void testProcessRequest() throws Exception {
//        System.out.println("processRequest");
//        HttpServletRequest request = null;
//        HttpServletResponse response = null;
//        XSLTServlet instance = new XSLTServlet();
//        instance.processRequest(request, response);
        // TODO review the generated test code and remove the default call to fail.

        List<Assignment> assignments = XSLTUtils.scanDirectoryStructure(Utils.getPathTo("xslt"));
        String homeFolder = System.getProperty("user.home");

        for (Assignment assignment : assignments) {
            System.out.println("name: " + assignment.getName());
            String myTestXslt = XSLTUtils.getMyTestXslt(homeFolder + File.separator + Constants.ASSIGNMENTS_FOLDER_NAME + File.separator + "xslt" + File.separator + assignment.getName());
            assertNotNull(myTestXslt);

            assertTrue(XSLTUtils.evaluate(myTestXslt, assignment.getName()).isIsCorrect());

            try {
                XSLTUtils.evaluate("test", assignment.getName()).isIsCorrect();
                fail("An exception should occur");
            } catch (Exception e) {
            }
        }
    }
}
