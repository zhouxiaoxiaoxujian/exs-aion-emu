package packetsamurai.session;

import java.nio.BufferUnderflowException;

import packetsamurai.PacketSamurai;
import packetsamurai.parser.DataStructure;
import packetsamurai.protocol.PacketId;
import packetsamurai.protocol.Protocol;
import packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import packetsamurai.protocol.protocoltree.PacketFormat;




/**
 * 
 * @author Gilles Duboscq
 *
 */
public class DataPacket extends DataStructure
{
    private packetDirection _direction;
    private Protocol _protocol;
    private long _timeStamp;
    private PacketFormat _packetFormat;
    private PacketId _packetID;
    private byte[] _IdData;
    private int _size;
    
    public DataPacket(byte[] data, packetDirection dir, long timeStamp, Protocol proto)
    {
        this(data,dir,timeStamp,proto,true);
    }
    
    public DataPacket(byte[] data, packetDirection direction, long timeStamp, Protocol protocol, boolean parse)
    {
        super(data,null);
        _direction = direction;
        _protocol = protocol;
        _timeStamp = timeStamp;
        _packetID = new PacketId();
        _packetFormat = _protocol.getFormat(this,_packetID);
        if (_packetFormat == null)
        {
            this.getByteBuffer().rewind();
            _packetID = null;
        }
        _size = data.length;

// getting 2 extra bytes fixes decoding but messes up header, _IdData causing packets not to be processed properly although data is decoded and good.
// need to trace how packet id are read and data is passed. Buffer positions need to be correct for everything to parse out correctly. +2 for sm_key, unknown for other packets
// +2 causes opcode to not show in viewpane, I beleive that is why the packetid/packet format is not parsed correctly. getting close ;)
        
// Update opcodes are now two bytes. Need to fix.
        
    	//short unknown = this.getByteBuffer().getShort();
    	//if (!(this.getName().equals("SM_KEY"))) { byte unknown2 = this.getByteBuffer().get(); } 
        _IdData = new byte[this.getByteBuffer().position()];
        System.arraycopy(data, 0, _IdData, 0, this.getByteBuffer().position());

        this.getByteBuffer().compact();
        this.getByteBuffer().flip();
        if (_packetFormat != null)
        {
            this.setFormat(_packetFormat.getDataFormat());
        }
        if (parse)
        {
            try
            {
                this.parse();
                if (this.getProtocol() != null && this.getProtocol().isStrictLength() && this.getUnparsedData().length > 0)
                {
                    _warning = "Incomplete Format";
                }
                else if (this.getFormat() == null)
                {
                    _warning = "Missing Format";
                }
            }
            catch (BufferUnderflowException e)
            {
                _error = "Insuficient data for the specified format";
                if(PacketSamurai.VERBOSITY.ordinal() >= PacketSamurai.VerboseLevel.VERBOSE.ordinal())
                {
                    if(this.getFormat() != null)
                        System.out.println(this.getFormat().toString());
                    dumpParts();
                }
                PacketSamurai.getUserInterface().log("ERROR: Parsing packet ("+this.getName()+"), insuficient data for the specified format. Please verify the format."); 
            }
        }
    }
    
    public Protocol getProtocol()
    {
        return _protocol;
    }
    
    public boolean fromServer()
    {
        return (_direction == packetDirection.serverPacket);
    }
    
    public long getTimeStamp()
    {
        return _timeStamp;
    }

    public packetDirection getDirection()
    {
        return _direction;
    }
    
    public PacketFormat getPacketFormat()
    {
        return _packetFormat;
    }
    
    public String getName()
    {
        if (this.getPacketFormat() == null)
        {
            return null;
        }
        return this.getPacketFormat().getName();
    }
    
    public PacketId getPacketId()
    {
        return _packetID;
    }
    
    public int getRawSize()
    {
        return _size;
    }
    
    public byte[] getIdData()
    {
        return _IdData;
    }
}