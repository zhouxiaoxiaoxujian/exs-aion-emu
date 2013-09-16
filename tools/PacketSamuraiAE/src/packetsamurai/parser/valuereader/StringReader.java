/**
 * 
 */
package packetsamurai.parser.valuereader;

import javax.swing.JComponent;
import javax.swing.JLabel;

import packetsamurai.parser.datatree.ValuePart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * @author Ulysses R. Ribeiro
 *
 */
public class StringReader implements Reader
{

    public <T extends Enum<T>> T getEnum(ValuePart part)
    {
        return null;
    }

    public boolean loadReaderFromXML(Node n)
    {
        return true;
    }

    public String read(ValuePart part)
    {
    	byte[] bytes = part.getBytes();
    	StringBuilder sb = new StringBuilder();
    	char c = '\0';
    	
    	for (int i = 0; (bytes[i] != 0 || bytes[i+1] != 0) && i < bytes.length; i += 2) {
    		c = (char)(((bytes[i+1] & 0xff) << 8) + (bytes[i] & 0xff));
    		sb.append(c);
    	}
    	
        return sb.toString();
    }

    public JComponent readToComponent(ValuePart part)
    {
        return new JLabel(this.read(part));
    }

    public boolean saveReaderToXML(Element element, Document doc)
    {
        return true;
    }

    public boolean supportsEnum()
    {
        return false;
    }

}
