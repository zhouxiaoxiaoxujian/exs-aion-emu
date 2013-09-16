package packetsamurai.parser.valuereader;

import javax.swing.JComponent;

import packetsamurai.parser.datatree.ValuePart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * 
 * @author Gilles Duboscq
 *
 */
public interface Reader
{
    public boolean loadReaderFromXML(Node n);
    
    public boolean saveReaderToXML(Element element, Document doc);
    
    public JComponent readToComponent(ValuePart part);
    
    public String read(ValuePart part);
    
    public boolean supportsEnum();
    
    public <T extends Enum<T>> T getEnum(ValuePart part);
}