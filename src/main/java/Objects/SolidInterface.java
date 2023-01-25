package Objects;
import Math.*;

public interface SolidInterface {
    public double getAlbedo();
    public double getLambertian();
    public double getBlinn();
    public int getBlinnExp();
    public HitInfo calculateIntersection(Ray ray);
    public Vec3d getPosition();
    public Vec3d getColor();
    public Vec3d getTextureColor(Vec3d point);
    public double getReflectivity();
    public double getRoughness();
    public void makeTransparent(double idx);
    public boolean isTransparent();
    public double getRefractionIndex();
}
