package geoves.drownrain.mixin;



import net.minecraft.entity.Entity;


import org.spongepowered.asm.mixin.Mixin;




@Mixin(Entity.class)
public abstract class RainDrownerMixin {
    public boolean drownsInRain = false;
}
