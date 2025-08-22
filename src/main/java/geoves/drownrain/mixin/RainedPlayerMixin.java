package geoves.drownrain.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class RainedPlayerMixin {


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
    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void rainTick(CallbackInfo ci){
        PlayerEntity player = ((PlayerEntity) (Object) this);
        BlockPos blockPos = player.getBlockPos();
        World world = player.getWorld();
        boolean playerIsBeingRainedOn = world.hasRain(blockPos)
                || world.hasRain(BlockPos.ofFloored(blockPos.getX(), player.getBoundingBox().maxY, blockPos.getZ()));
        if (playerIsBeingRainedOn) {
            float playerSpeed = player.isSprinting() ? 1.0F : 0.8F;
            float playerRainTravelEfficiency = (float) player.getAttributeValue(EntityAttributes.WATER_MOVEMENT_EFFICIENCY);
            if (!player.isOnGround()){
                playerRainTravelEfficiency *= 0.95F;
            }
            if (playerRainTravelEfficiency > 0.15){
                playerSpeed *= playerRainTravelEfficiency;
            } else {
                playerSpeed *= 0.15F;
            }
            Vec3d velocity1 = player.getVelocity();
            Vec3d velocity = velocity1.multiply(playerSpeed, 1.0F, playerSpeed);
            player.setVelocity(velocity);
            player.velocityDirty = true;
        }
    }
}
