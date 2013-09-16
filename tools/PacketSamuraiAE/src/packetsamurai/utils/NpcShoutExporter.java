package packetsamurai.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import packetsamurai.PacketSamurai;
import packetsamurai.parser.datatree.ValuePart;
import packetsamurai.session.DataPacket;


/**
 * @author ATracer - Kamui - Ginho1
 */
public class NpcShoutExporter
{
	private List<DataPacket> packets;
	private String sessionName;
	private SortedMap<String, String> npcIdMap = new TreeMap<String, String>();
	private SortedMap<String, String> npcShoutIdMap = new TreeMap<String, String>();

	public NpcShoutExporter(List<DataPacket> packets, String sessionName)
	{
		super();
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npc_shouts_" + sessionName + ".sql";

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					String objId = "";
					String npcId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npcId = valuePart.readValue();
						}
						else if("objId".equals(partName))
						{
							objId = valuePart.readValue();
						}
					}
					if(!"0".equals(objId))
					{
						npcIdMap.put(objId, npcId);
					}
				}
			}

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_SYSTEM_MESSAGE".equals(name))
				{
					String npcObjId = "";
					String msgId = "";

					List<ValuePart> valuePartList = packet.getValuePartList();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcObjId".equals(partName))
						{
							npcObjId = valuePart.readValue();
						}
						else if("msgId".equals(partName))
						{
							msgId = valuePart.readValue();
						}
					}
					if(!"0".equals(msgId))
					{
						String npcId = "";

						for(Entry<String, String> entry : npcIdMap.entrySet())
						{
							if(entry.getKey().equals(npcObjId))
								npcId = entry.getValue();
						}

						StringBuilder sb = new StringBuilder();

						if(!npcId.equals(""))
						{
						    npcShoutIdMap.put(npcId, msgId);

						    sb.append("INSERT INTO `npc_shouts`(`npc_id`, `message_id`, `_interval`) VALUES (");
						    sb.append(npcId);
						    sb.append(", ");
						    sb.append(msgId);
						    sb.append(", 0);\n");
						}
						out.write(sb.toString());
					}
				}
			}
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("NPC Shouts Have been Written Successful");
	}
}