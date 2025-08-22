package geoves.drownrain.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BigDripleafBlock;


import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Tilt;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BigDripleafBlock.class)
public class DripLeafMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Final
    @Shadow private static final Object2IntMap NEXT_TILT_DELAYS = (Object2IntMap) Util.make(new Object2IntArrayMap(), (delays) -> {
        delays.defaultReturnValue(-1);
        delays.put(Tilt.UNSTABLE, 10);
        delays.put(Tilt.PARTIAL, 10);
        delays.put(Tilt.FULL, 100);
    });;
    @Shadow @Final private static EnumProperty<Tilt> TILT;

    @Shadow private void changeTilt(BlockState state, World world, BlockPos pos, Tilt tilt, @Nullable SoundEvent sound) {
    }

    @Shadow private static void playTiltSound(World world, BlockPos pos, @Nullable SoundEvent sound) {
    }

    @Shadow private static void changeTilt(BlockState state, World world, BlockPos pos, Tilt tilt) {
    }

    public void unstableFromRain(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            Tilt Tilt = state.get(TILT);
            if (Tilt == net.minecraft.block.enums.Tilt.NONE){
                changeTilt(state, world, pos, net.minecraft.block.enums.Tilt.UNSTABLE);
            } else if (Tilt == net.minecraft.block.enums.Tilt.UNSTABLE) {
                changeTilt(state, world, pos, net.minecraft.block.enums.Tilt.PARTIAL);
            }else if (Tilt == net.minecraft.block.enums.Tilt.PARTIAL) {
                changeTilt(state, world, pos, net.minecraft.block.enums.Tilt.FULL);
            }
        }
    }
    @Inject(method = "scheduledTick", at = @At(value = "HEAD"))
    void raintick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!world.isReceivingRedstonePower(pos)) {
            Biome.Precipitation precipitation = Biome.Precipitation.RAIN;
            unstableFromRain(state, world, pos, precipitation);
        }
    }

}
