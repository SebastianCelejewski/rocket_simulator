package pl.sebcel.rc.gui.physics;

public interface PhysicalObject {

    public double getX();

    public double getY();
    
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime);
    
    public boolean isJustCreated();

}
