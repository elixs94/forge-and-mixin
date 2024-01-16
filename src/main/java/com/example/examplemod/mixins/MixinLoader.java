package com.example.examplemod.mixins;

import com.example.examplemod.BlockData;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.ArrayList;
import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {
    public MixinLoader() {
        System.out.println("[ExampleMod] Injecting with IFMLLoadingPlugin.");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.examplemod.json"); //TODO: Add your own mixin config file here
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    public static ArrayList<BlockData> blockDataArrayList = new ArrayList<BlockData>();

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}