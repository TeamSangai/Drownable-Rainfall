package geoves.drownrain.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class RainedPlayerMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean TurtleInRain(boolean original) {
        // Copied from Entity.isBeingRainedOn
        LivingEntity self = ((LivingEntity) (Object) this);
        BlockPos blockPos = self.getBlockPos();
        World world = self.getWorld();
        boolean isBeingRainedOn = world.hasRain(blockPos)
                || world.hasRain(BlockPos.ofFloored(blockPos.getX(), self.getBoundingBox().maxY, blockPos.getZ()));

        return original || isBeingRainedOn;
    }
    @ModifyExpressionValue(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean MineInRain(boolean original) {
        // Copied from Entity.isBeingRainedOn
        LivingEntity self = ((LivingEntity) (Object) this);
        BlockPos blockPos = self.getBlockPos();
        World world = self.getWorld();
        boolean isBeingRainedOn = world.hasRain(blockPos)
                || world.hasRain(BlockPos.ofFloored(blockPos.getX(), self.getBoundingBox().maxY, blockPos.getZ()));

        return original || isBeingRainedOn;
    }
}
