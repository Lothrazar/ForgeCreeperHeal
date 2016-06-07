package fr.eyzox.forgecreeperheal.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ProfilerCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "creeperhealprofiler";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/"+getCommandName() + "[enable|disable] <dimension|all>";
	}
//
//	@Override
//	public boolean canCommandSenderUseCommand(ICommandSender sender) {
//		return ForgeCreeperHealCommands.isOp(sender);
//	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args) {
		if(args.length > 0 && args[0] != null) {
			
			WorldHealer w = null;
			int startIndex = 1;
			boolean switchProfiler = false, force = false, all = false;
			
			//Gets switchProfiler
			if(args[0].equalsIgnoreCase("enable")) {switchProfiler = true;}
			else if(args[0].equalsIgnoreCase("disable")){ switchProfiler = false;}
			else{
				CommonProxy.addChatMessage(sender, this.getCommandUsage(sender));
			}
			//Gets all
			for(int i = startIndex; i<args.length ; i++) {
				if("all".equalsIgnoreCase(args[i])){
					all = true;
					break;
				}
			}
			
			//Gets WorldHealer
			if(!all) {
				if(args.length > startIndex) {
					int dimensionID;
					try {
						dimensionID = Integer.parseInt(args[startIndex]);
					}catch(NumberFormatException e) {
						CommonProxy.addChatMessage(sender, "Dimension must be an integer");
						return;
					}
					startIndex++;
					w = ForgeCreeperHeal.getWorldHealer((WorldServer) DimensionManager.getWorld(dimensionID));
				}else {
					w = ForgeCreeperHeal.getWorldHealer((WorldServer) sender.getEntityWorld());
				}
				
				if(w == null) {
					CommonProxy.addChatMessage(sender, "You must specified a valid dimensionID or use \"all\"");
					return;
				}
			}
			
			//Gets force
			if(!switchProfiler) {
				for(int i = startIndex; i<args.length ; i++) {
					if("force".equalsIgnoreCase(args[i])) {
						force = true;
						break;
					}
				}
			}
			


			//Process enable
			if(switchProfiler) {
				if(all) {
					for(WorldServer world : DimensionManager.getWorlds()) {
						WorldHealer wh = ForgeCreeperHeal.getWorldHealer(world);
						if(wh != null) wh.enableProfiler(sender);
					}
				}else {
					w.enableProfiler(sender);
				}
			//Process disable
			}else {
				if(all) {
					for(WorldServer world : DimensionManager.getWorlds()) {
						WorldHealer wh = ForgeCreeperHeal.getWorldHealer(world);
						if(wh != null) {
							if(force) wh.disableProfiler();
							else wh.disableProfiler(sender);
						}
					}
				}else {
					if(force) w.disableProfiler();
					else w.disableProfiler(sender);
				}
			}
			
		}

	}

}