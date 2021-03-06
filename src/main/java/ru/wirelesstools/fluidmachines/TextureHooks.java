package ru.wirelesstools.fluidmachines;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

@SideOnly(Side.CLIENT)
public class TextureHooks {

	public static class Icons {
		public static IIcon xpJuiceStill;
		public static IIcon xpJuiceFlowing;
	}

	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			Icons.xpJuiceStill = event.map.registerIcon(Reference.PathTex + "xpliquid_still");
			Icons.xpJuiceFlowing = event.map.registerIcon(Reference.PathTex + "xpjuice_flowing");
			MainWI.FluidXP.xpJuice.setIcons(Icons.xpJuiceStill, Icons.xpJuiceFlowing);

		}
	}

}
