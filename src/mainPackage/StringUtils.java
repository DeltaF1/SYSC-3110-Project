package mainPackage;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;


public class StringUtils
{
	/**
	 * repeats a string a certain number of times
	 * @param str String to repeat
	 * @param times times to repeat the string
	 * @return
	 */
	public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
	
	/**
	 * converts and xml Document object to a String
	 * @param xml the xml Document to convert
	 * @return an XML formatted String
	 */
	public static String XMLToString(Document xml) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.transform(new DOMSource(xml), new StreamResult(sw));
		    return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
	    	return null;
	    }
	}
}
