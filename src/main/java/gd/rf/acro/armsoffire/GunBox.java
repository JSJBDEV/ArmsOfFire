package gd.rf.acro.armsoffire;


import net.minecraft.util.Identifier;

public class GunBox {
    private Identifier ammo;
    private int cooldown;
    private int bulletsPerShot;
    private float bulletAngle;
    private float damage;
    private boolean multishot;
    private float rangeMod;
    private int scopeAmount;
    private float explosionSize;
    private int reloadTime;
    public GunBox(Identifier ammoUsed, float doDamage)
    {
        this.cooldown=20;
        this.ammo=ammoUsed;
        this.bulletsPerShot=1;
        this.bulletAngle=0;
        this.damage=doDamage;
        this.rangeMod=3f;
        this.reloadTime=40;
    }
    public GunBox setCooldown(int cd)
    {
        this.cooldown=cd;
        return this;
    }
    public GunBox setMultipleBullets(int bullets, float angle)
    {
        this.bulletsPerShot=bullets;
        this.bulletAngle=angle;
        this.multishot=true;
        return this;
    }

    public GunBox setExplosionSize(float explosionSize) {
        this.explosionSize = explosionSize;
        return this;
    }

    public GunBox setScopeAmount(int scopeAmount) {
        this.scopeAmount = scopeAmount;
        return this;
    }

    public GunBox setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
        return this;
    }

    public GunBox setRangeMod(float rangeMod) {
        this.rangeMod = rangeMod;
        return this;
    }

    public Identifier getAmmo() {
        return ammo;
    }

    public float getBulletAngle() {
        return bulletAngle;
    }

    public int getBulletsPerShot() {
        return bulletsPerShot;
    }

    public int getCooldown() {
        return cooldown;
    }

    public float getRangeMod() {
        return rangeMod;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isMultishot() {
        return multishot;
    }

    public int getScopeAmount() {
        return scopeAmount;
    }

    public float getExplosionSize() {
        return explosionSize;
    }

    public int getReloadTime() {
        return reloadTime;
    }
}
