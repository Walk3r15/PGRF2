package objectdata;

import linalg.Vectorizable;
import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;

public class Vertex implements Vectorizable<Vertex>, Transformable<Vertex> {

    private final Point3D position;
    private final Col color;

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    @Override
    public Vertex mul(double t) {
        return new Vertex(
                position.mul(t),
                color.mul(t)
        );
    }

    @Override
    public Vertex add(Vertex other) {
        return new Vertex(
                position.add(other.position),
                color.add(other.color)
        );
    }

    @Override
    public Vertex transformed(Mat4 transformation) { // TODO
        return this;
    }

    @Override
    public Vertex dehomog() { // TODO
        return this;
    }

    @Override
    public Vertex toViewport(int width, int height) { // TODO
        return this;
    }
}
