package ar.edu.itba.ss.voyager;

public class Planet implements Cloneable{

    int id;
    double x;
    double y;
    double vx;
    double vy;
    double mass;
    double prevX;
    double prevY;
    double prevAx;
    double prevAy;
    double ax;
    double ay;
    double radius;
    int[] colour;

    public Planet(int id, double x, double y, double vx, double vy, double mass, double radius, int[] colour) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;
        this.colour = colour;
    }

    public Planet(int id, double x, double y, double vx, double vy){
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    Planet getClone() throws CloneNotSupportedException {
        return (Planet) super.clone();
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", vx=" + vx +
                ", vy=" + vy +
                '}';
    }
}
