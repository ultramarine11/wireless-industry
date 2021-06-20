package ru.wirelesstools.entityarrow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import ru.wirelesstools.config.ConfigWI;

import java.util.List;

public class ArrowVampEUNew extends EntityArrow implements IProjectile {

    private int field_145791_d = -1;
    private int field_145792_e = -1;
    private int field_145789_f = -1;
    private Block field_145790_g;
    private int inData;
    private boolean inGround;
    /**
     * 1 if the player can pick up the arrow
     */
    public int canBePickedUp;
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake;
    /**
     * The owner of this arrow.
     */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength;

    private int shooterarmorcount;

    public ArrowVampEUNew(World p_i1753_1_) {
        super(p_i1753_1_);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public ArrowVampEUNew(World p_i1754_1_, double p_i1754_2_, double p_i1754_4_, double p_i1754_6_) {
        super(p_i1754_1_);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
        this.yOffset = 0.0F;
    }

    public ArrowVampEUNew(int armorcount, World world, EntityLivingBase entityliving, float p_i1756_3_) {
        this(world, entityliving, p_i1756_3_);
        this.shooterarmorcount = armorcount;
    }

    public ArrowVampEUNew(World p_i1756_1_, EntityLivingBase p_i1756_2_, float p_i1756_3_) {
        super(p_i1756_1_, p_i1756_2_, p_i1756_3_);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1756_2_;

        if (p_i1756_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 2;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(p_i1756_2_.posX, p_i1756_2_.posY + (double) p_i1756_2_.getEyeHeight(),
                p_i1756_2_.posZ, p_i1756_2_.rotationYaw, p_i1756_2_.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5F, 1.0F);
    }

    public ArrowVampEUNew(World p_i1755_1_, EntityLivingBase p_i1755_2_, EntityLivingBase p_i1755_3_, float p_i1755_4_,
                          float p_i1755_5_) {
        super(p_i1755_1_);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1755_2_;

        if (p_i1755_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.posY = p_i1755_2_.posY + (double) p_i1755_2_.getEyeHeight() - 0.10000000149011612D;
        double d0 = p_i1755_3_.posX - p_i1755_2_.posX;
        double d1 = p_i1755_3_.boundingBox.minY + (double) (p_i1755_3_.height / 3.0F) - this.posY;
        double d2 = p_i1755_3_.posZ - p_i1755_2_.posZ;
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D) {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(p_i1755_2_.posX + d4, this.posY, p_i1755_2_.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float) d3 * 0.2F;
            this.setThrowableHeading(d0, d1 + (double) f4, d2, p_i1755_4_, p_i1755_5_);
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
                    / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D
                    / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e,
                    this.field_145789_f);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.field_145791_d,
                    this.field_145792_e, this.field_145789_f);

            if (axisalignedbb != null
                    && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);

            if (block == this.field_145790_g && j == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
                    this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
                    this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
                        movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
                    this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f1, f1, f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null) {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer
                        && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f4;
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    f2 = MathHelper.sqrt_double(
                            this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int k = MathHelper.ceiling_double_int((double) f2 * this.damage);

                    if (this.getIsCritical()) {
                        k += this.rand.nextInt(k / 2 + 2);
                    }

                    DamageSource damagesource;

                    if (this.shootingEntity == null) {
                        damagesource = DamageSource.causeArrowDamage(this, this);
                    } else {
                        damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
                        movingobjectposition.entityHit.setFire(5);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) k)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                            if (!this.worldObj.isRemote) {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0) {
                                f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (f4 > 0.0F) {
                                    movingobjectposition.entityHit.addVelocity(
                                            this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
                                                    / (double) f4,
                                            0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D
                                                    / (double) f4);
                                }
                            }

                            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity,
                                        entitylivingbase);
                            }

                            if (this.shootingEntity instanceof EntityPlayer
                                    && movingobjectposition.entityHit instanceof EntityPlayer
                                    && movingobjectposition.entityHit != this.shootingEntity) {

                                EntityPlayer victim = (EntityPlayer) movingobjectposition.entityHit;
                                EntityPlayer shooter = (EntityPlayer) this.shootingEntity;
                                if (!this.worldObj.isRemote) {
                                    this.stealEUAndChargeShooter2(shooter, victim);
                                }

                                /*for (int ist = 0; ist < victim.inventory.armorInventory.length; ist++) {
                                    if (victim.inventory.armorInventory[ist] == null)
                                        continue;
                                    if (victim.inventory.armorInventory[ist].getItem() instanceof IElectricItem) {
                                        this.stealEUAndChargeShooter(victim.inventory.armorInventory[ist], shooter,
                                                ConfigWI.stolenEnergyEUFromArmor, ist);
                                        // System.out.println("Invoke from update my");
                                    }
                                }*/
                            }

                            if (this.shootingEntity instanceof EntityPlayerMP
                                    && movingobjectposition.entityHit instanceof EntityPlayer
                                    && movingobjectposition.entityHit != this.shootingEntity) {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler
                                        .sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        this.motionX *= -0.1D;
                        this.motionY *= -0.1D;
                        this.motionZ *= -0.1D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else {
                    this.field_145791_d = movingobjectposition.blockX;
                    this.field_145792_e = movingobjectposition.blockY;
                    this.field_145789_f = movingobjectposition.blockZ;
                    this.field_145790_g = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e,
                            this.field_145789_f);
                    this.inData = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e,
                            this.field_145789_f);
                    this.motionX = movingobjectposition.hitVec.xCoord - this.posX;
                    this.motionY = movingobjectposition.hitVec.yCoord - this.posY;
                    this.motionZ = movingobjectposition.hitVec.zCoord - this.posZ;
                    f2 = MathHelper.sqrt_double(
                            this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * 0.05D;
                    this.posY -= this.motionY / (double) f2 * 0.05D;
                    this.posZ -= this.motionZ / (double) f2 * 0.05D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (this.field_145790_g.getMaterial() != Material.air) {
                        this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d,
                                this.field_145792_e, this.field_145789_f, this);
                    }
                }
            }

            if (this.getIsCritical()) {
                for (i = 0; i < 4; ++i) {
                    this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double) i / 4.0D,
                            this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D,
                            -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            for (this.rotationPitch = (float) (Math.atan2(this.motionY, f2) * 180.0D
                    / Math.PI); this.rotationPitch
                         - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f3 = 0.99F;
            f1 = 0.05F;
            if (this.isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    f4 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f4,
                            this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4,
                            this.motionX, this.motionY, this.motionZ);
                }

                f3 = 0.8F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= f3;
            this.motionY *= f3;
            this.motionZ *= f3;
            this.motionY -= f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }

    public void setKnockbackStrength(int p_70240_1_) {
        this.knockbackStrength = p_70240_1_;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        this.motionX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.motionZ = p_70016_5_;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        p_70014_1_.setShort("xTile", (short) this.field_145791_d);
        p_70014_1_.setShort("yTile", (short) this.field_145792_e);
        p_70014_1_.setShort("zTile", (short) this.field_145789_f);
        p_70014_1_.setShort("life", (short) this.ticksInGround);
        p_70014_1_.setByte("inTile", (byte) Block.getIdFromBlock(this.field_145790_g));
        p_70014_1_.setByte("inData", (byte) this.inData);
        p_70014_1_.setByte("shake", (byte) this.arrowShake);
        p_70014_1_.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        p_70014_1_.setByte("pickup", (byte) this.canBePickedUp);
        p_70014_1_.setDouble("damage", this.damage);
        p_70014_1_.setInteger("armorcount", this.shooterarmorcount);
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        this.field_145791_d = p_70037_1_.getShort("xTile");
        this.field_145792_e = p_70037_1_.getShort("yTile");
        this.field_145789_f = p_70037_1_.getShort("zTile");
        this.ticksInGround = p_70037_1_.getShort("life");
        this.field_145790_g = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
        this.inData = p_70037_1_.getByte("inData") & 255;
        this.arrowShake = p_70037_1_.getByte("shake") & 255;
        this.inGround = p_70037_1_.getByte("inGround") == 1;

        if (p_70037_1_.hasKey("damage", 99)) {
            this.damage = p_70037_1_.getDouble("damage");
        }

        if (p_70037_1_.hasKey("pickup", 99)) {
            this.canBePickedUp = p_70037_1_.getByte("pickup");
        } else if (p_70037_1_.hasKey("player", 99)) {
            this.canBePickedUp = p_70037_1_.getBoolean("player") ? 1 : 0;
        }

        this.shooterarmorcount = p_70037_1_.getInteger("armorcount");
    }

    public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && p_70100_1_.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !p_70100_1_.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
                flag = false;
            }

            if (flag) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                p_70100_1_.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    public double getDamage() {
        return this.damage;
    }

    public boolean getIsCritical() {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 1) != 0;
    }

    public void setIsCritical(boolean p_70243_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70243_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public void setDamage(double p_70239_1_) {
        this.damage = p_70239_1_;
    }

    private void stealEUAndChargeShooter2(EntityPlayer shooter, EntityPlayer victim) {
        double totaldischargedamount = 0;
        int shooterarmorcount = 0;

        for (ItemStack victimarmorstack : victim.inventory.armorInventory) {
            if (victimarmorstack != null && victimarmorstack.getItem() instanceof IElectricItem) {
                totaldischargedamount += ElectricItem.manager.discharge(victimarmorstack,
                        ConfigWI.stolenEnergyEUFromArmor, Integer.MAX_VALUE, true, false, false);
            }
        }

        for (ItemStack shooterarmorstack : shooter.inventory.armorInventory) {
            if (shooterarmorstack != null && shooterarmorstack.getItem() instanceof IElectricItem) shooterarmorcount++;
        }

        if (shooterarmorcount > 0) {
            if (totaldischargedamount > 0) {
                double energyperarmor = totaldischargedamount / shooterarmorcount;
                for (ItemStack shooterarmor : shooter.inventory.armorInventory) {
                    if (shooterarmor != null && shooterarmor.getItem() instanceof IElectricItem) {
                        ElectricItem.manager.charge(shooterarmor,
                                energyperarmor, Integer.MAX_VALUE, true, false);
                    }
                }
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.BLUE
                                + StatCollector.translateToLocal("chat.message.stolen.from.enemy.total")
                                + ": " + String.valueOf((int) totaldischargedamount) + " EU"));
                victim.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.DARK_PURPLE
                                + StatCollector.translateToLocal("chat.message.somebody.has.stolen.energy")));
            } else {
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.YELLOW
                                + StatCollector.translateToLocal("chat.message.stolen.zero.amount.eu")));
            }
        } else {
            shooter.addChatMessage(new ChatComponentTranslation(
                    EnumChatFormatting.DARK_RED
                            + StatCollector.translateToLocal("chat.message.no.electric.armor.on.you")));
        }
    }

    @Deprecated
    private void stealEUAndChargeShooter(ItemStack armorStackToDischarge, EntityPlayer shooter, int stolenEUPerArmor,
                                         int armorslotvictim) {
        double amount = ElectricItem.manager.discharge(armorStackToDischarge, stolenEUPerArmor, Integer.MAX_VALUE, true,
                false, false);
        int shooterarmorcount = 0;
        for (int ist = 0; ist < shooter.inventory.armorInventory.length; ist++) {
            if (shooter.inventory.armorInventory[ist] == null)
                continue;
            if (shooter.inventory.armorInventory[ist].getItem() instanceof IElectricItem)
                shooterarmorcount++;
        }

        if (shooterarmorcount > 0) {
            for (ItemStack shooterarmorcurrent : shooter.inventory.armorInventory) {
                if (shooterarmorcurrent == null)
                    continue;
                if (shooterarmorcurrent.getItem() instanceof IElectricItem) {
                    ElectricItem.manager.charge(shooterarmorcurrent, amount / (double) shooterarmorcount,
                            Integer.MAX_VALUE, true, false);
                    // System.out.println("Invoke from method stealEUAndChargeShooter");
                }
            }
        }
        switch (armorslotvictim) {
            case 0:
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.AQUA + StatCollector.translateToLocal("chat.message.stolen.from.boots") + ": "
                                + String.valueOf((int) amount) + " EU"));
                break;
            case 1:
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.GREEN + StatCollector.translateToLocal("chat.message.stolen.from.pants") + ": "
                                + String.valueOf((int) amount) + " EU"));
                break;
            case 2:
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("chat.message.stolen.from.chestplate")
                                + ": " + String.valueOf((int) amount) + " EU"));
                break;
            case 3:
                shooter.addChatMessage(new ChatComponentTranslation(
                        EnumChatFormatting.YELLOW + StatCollector.translateToLocal("chat.message.stolen.from.helmet") + ": "
                                + String.valueOf((int) amount) + " EU"));
                break;
        }
    }

}