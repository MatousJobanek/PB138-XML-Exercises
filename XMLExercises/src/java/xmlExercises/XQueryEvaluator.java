package xmlExercises;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author slaweet
 */
    
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import net.sf.saxon.Configuration;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

public class XQueryEvaluator implements Evaluator {

    public String eval(String expresion, String fileName) throws SyntaxErorException {

        String dataDir = "xquery/";
        expresion = expresion.replaceAll("test.xml", dataDir + fileName);
        
        Configuration config = new Configuration();

        StaticQueryContext staticContext = config.newStaticQueryContext();
        
        DynamicQueryContext dynamicContext = new DynamicQueryContext(config);

        Properties props = new Properties();
        props.setProperty(OutputKeys.METHOD, "xml");
        props.setProperty(OutputKeys.INDENT, "yes");
        
                
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        try {
            XQueryExpression exp = staticContext.compileQuery(expresion);
            exp.run(dynamicContext, result, props);
        } catch (XPathException xpe) {
            throw new SyntaxErorException(xpe.getMessage(), xpe.getCause());
        }
        
        return sw.toString();
    }


    @Override
    public boolean compare(String result1, String result2) {
        return result1.equals(result2);
    }
}
