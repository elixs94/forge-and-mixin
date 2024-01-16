package com.example.examplemod.mixins;


import com.example.examplemod.BlockData;
import com.example.examplemod.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Mixin(NetHandlerPlayClient.class)

public abstract class PacketListener {


    @Shadow public abstract void addToSendQueue(Packet p_147297_1_);

    @Shadow public int currentServerMaxPlayers;
    private RenderManager renderManager;

    private Minecraft mc = Minecraft.getMinecraft();

    public float renderDistance = 10f;
    public PacketListener() {
        this.renderManager = this.mc.getRenderManager();

    }
    private static final Set<Block> movableBlocks = new HashSet<Block>();

    public String lightred = EnumChatFormatting.RED + "";
    public String lightgray = EnumChatFormatting.GRAY + "";
    private String bold = EnumChatFormatting.BOLD + "";
    private String yellow = EnumChatFormatting.YELLOW + "";
    private String green = EnumChatFormatting.GREEN + "";
    private String orange = EnumChatFormatting.GOLD + "";
    static {
        movableBlocks.add(Blocks.trapdoor);

        movableBlocks.add(Blocks.acacia_fence_gate);
        movableBlocks.add(Blocks.spruce_fence_gate);
        movableBlocks.add(Blocks.jungle_fence_gate);
        movableBlocks.add(Blocks.oak_fence_gate);
        movableBlocks.add(Blocks.dark_oak_fence_gate);
        movableBlocks.add(Blocks.birch_fence_gate);


        movableBlocks.add(Blocks.stone_button);
        movableBlocks.add(Blocks.wooden_button);
        movableBlocks.add(Blocks.lever);
        movableBlocks.add(Blocks.heavy_weighted_pressure_plate);
        movableBlocks.add(Blocks.light_weighted_pressure_plate);
        movableBlocks.add(Blocks.stone_pressure_plate);
        movableBlocks.add(Blocks.wooden_pressure_plate);


        movableBlocks.add(Blocks.acacia_door);
        movableBlocks.add(Blocks.spruce_door);
        movableBlocks.add(Blocks.jungle_door);
        movableBlocks.add(Blocks.oak_door);
        movableBlocks.add(Blocks.dark_oak_door);
        movableBlocks.add(Blocks.birch_door);
        movableBlocks.add(Blocks.iron_door);

        movableBlocks.add(Blocks.iron_trapdoor);
        movableBlocks.add(Blocks.piston);
        movableBlocks.add(Blocks.sticky_piston);
        movableBlocks.add(Blocks.chest);
        movableBlocks.add(Blocks.ender_chest);
        movableBlocks.add(Blocks.trapped_chest);
        movableBlocks.add(Blocks.bed);
        movableBlocks.add(Blocks.anvil);

        movableBlocks.add(Blocks.potatoes);
        movableBlocks.add(Blocks.carrots);
        movableBlocks.add(Blocks.wheat);

        movableBlocks.add(Blocks.piston_head);

        movableBlocks.add(Blocks.grass);
        movableBlocks.add(Blocks.water);
        movableBlocks.add(Blocks.flowing_water);
        movableBlocks.add(Blocks.lava);
        movableBlocks.add(Blocks.flowing_lava);
        movableBlocks.add(Blocks.dirt);
        movableBlocks.add(Blocks.cactus);


    }



    private Object myObject;

    private Object getMyObject() {
        return myObject;
    }




    @Inject(method = "handleBlockChange", at = @At("HEAD"), cancellable = true)
    private void onBlockChange(S23PacketBlockChange packetIn, CallbackInfo info) {

        info.cancel();


        EntityPlayer playerlocal = Minecraft.getMinecraft().thePlayer;
        World world = playerlocal.worldObj;
        if (packetIn.getBlockState().getBlock().equals(Blocks.air)) {
            playerlocal.addChatMessage(new ChatComponentText(yellow + "Block removed: " + packetIn.getBlockPosition().getX() + " " + packetIn.getBlockPosition().getY() + " " + packetIn.getBlockPosition().getZ()));


            for (EntityPlayer playerE : world.playerEntities) { //list

                if (new Vec3(playerE.posX, playerE.posY, playerE.posZ).distanceTo(new Vec3(packetIn.getBlockPosition().getX(), packetIn.getBlockPosition().getY(), packetIn.getBlockPosition().getZ())) < 5) {
                    playerlocal.addChatMessage(new ChatComponentText(orange + "player_in_near: " + playerE.getName()));
                    Vec3 playerPos = new Vec3(playerE.posX, playerE.posY + playerE.getEyeHeight(), playerE.posZ);
                    Vec3 playerLook = playerE.getLookVec();
                    boolean calculatedBoolean = calculateBlockHitDistance222(playerPos, playerLook, packetIn.getBlockPosition());

                    if (calculatedBoolean) {
                        playerlocal.addChatMessage(new ChatComponentText(green + "filtered_player: " + playerE.getName()));
                        MixinLoader.blockDataArrayList.add(new BlockData(packetIn.getBlockPosition().getX(), packetIn.getBlockPosition().getY()+1, packetIn.getBlockPosition().getZ(), playerE.getName(), 0));
                    }
                }


            }


        } else if (!movableBlocks.contains(packetIn.getBlockState().getBlock())) {

            playerlocal.addChatMessage(new ChatComponentText(yellow + "Block added: " + packetIn.getBlockPosition().getX() + " " + packetIn.getBlockPosition().getY() + " " + packetIn.getBlockPosition().getZ() + " Block: " + packetIn.getBlockState().getBlock().getRegistryName()));

            for (EntityPlayer playerE : world.playerEntities) { //list

                if (new Vec3(playerE.posX, playerE.posY, playerE.posZ).distanceTo(new Vec3(packetIn.getBlockPosition().getX(), packetIn.getBlockPosition().getY(), packetIn.getBlockPosition().getZ())) < 5) {
                    playerlocal.addChatMessage(new ChatComponentText(orange + "player_in_near: " + playerE.getName()));
                    Vec3 playerPos = new Vec3(playerE.posX, playerE.posY + playerE.getEyeHeight(), playerE.posZ);
                    Vec3 playerLook = playerE.getLookVec();
                    boolean calculatedBoolean = calculateBlockHitDistance222(playerPos, playerLook, packetIn.getBlockPosition());

                    if (calculatedBoolean) {
                        playerlocal.addChatMessage(new ChatComponentText(green + "filtered_player: " + playerE.getName()));
                        MixinLoader.blockDataArrayList.add(new BlockData(packetIn.getBlockPosition().getX(), packetIn.getBlockPosition().getY()+1, packetIn.getBlockPosition().getZ(), playerE.getName(), 1));

                    }
                }

            }
        }
    }


    private boolean calculateBlockHitDistance222(Vec3 start, Vec3 direction, BlockPos blockPos){
        Vec3 end = start.addVector(direction.xCoord * 5, direction.yCoord * 5, direction.zCoord * 5);

        IBlockState blockState = mc.theWorld.getBlockState(blockPos);
        Block block = blockState.getBlock();
        mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0, 0, 0);
        mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, blockPos.getX()+1, blockPos.getY()+1, blockPos.getZ()+1, 0, 0, 0); // Partikel erstellen (in diesem Fall rote Partikel)

        AxisAlignedBB entityBoundingBox = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX()+1, blockPos.getY()+1, blockPos.getZ()+1);
        MovingObjectPosition intercept = entityBoundingBox.calculateIntercept(start, end);

        if (intercept != null) {
            return true;
        } else {
            return false;
        }
    }



}
