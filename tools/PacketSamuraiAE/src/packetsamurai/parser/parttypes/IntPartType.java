package packetsamurai.parser.parttypes;


import packetsamurai.parser.PartType;
import packetsamurai.parser.datatree.DataTreeNodeContainer;
import packetsamurai.parser.datatree.IntValuePart;
import packetsamurai.parser.datatree.ValuePart;
import packetsamurai.parser.formattree.Part;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class IntPartType extends PartType
{
    public enum intType
    {
        c,
        h,
        d
    }
    
    intType _type;

    public IntPartType(String name, intType type)
    {
        super(name);
        _type = type;
    }

    @Override
    public ValuePart getValuePart(DataTreeNodeContainer parent, Part part)
    {
        return new IntValuePart(parent, part, _type);
    }

    @Override
    public boolean isBlockType()
    {
        return false;
    }

    @Override
    public boolean isReadableType()
    {
        return true;
    }

    @Override
    public int getTypeByteNumber()
    {
        switch(_type)
        {
            case c:
                return 1;
            case h:
                return 2;
            case d:
                return 4;
        }
        return 0; //useless
    }
    
}