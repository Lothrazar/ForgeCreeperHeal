package fr.eyzox.forgecreeperheal.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgecreeperheal.network.ProfilerInfoMessage;

@SideOnly(Side.CLIENT)
public class ProfilerRenderEventHandler {

	private Map<String, ProfilerInfoMessage> map = new HashMap<String, ProfilerInfoMessage>();

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
		if(!Minecraft.getMinecraft().isGamePaused()) {
			Iterator<Entry<String, ProfilerInfoMessage>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, ProfilerInfoMessage> item = it.next();
				
				ProfilerInfoMessage msg = item.getValue();
				event.left.add(getDrawString(msg));
				msg.setDisplayTicks(msg.getDisplayTicks()+1);
				
				if(msg.getDisplayTicks() > 40) {	
					it.remove();
				}
			}
		}
	}

	public void onMessage(ProfilerInfoMessage msg) {
		map.put(getKey(msg.getWorldName(),  msg.getDimensionID()), msg);
	}

	private String getDrawString(ProfilerInfoMessage msg) {
		return String.format("[%s#%d] Ticks : %.4f ms | Memory Usage : %d blocks", msg.getWorldName(),  msg.getDimensionID(), msg.getTicks(), msg.getBlocksUsed());
	}

	private String getKey(String worldName, int dimensionID) {
		return worldName+'#'+dimensionID;
	}


}
