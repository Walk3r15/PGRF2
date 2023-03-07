package objectops;

import linalg.Lerp;
import objectdata.Part;
import objectdata.Scene;
import objectdata.Solid;
import objectdata.Vertex;
import rasterdata.ZBuffer;
import rasterops.Liner;
import transforms.Mat4;

import java.util.List;

public class Renderer {

    private Liner liner;
    private ZBuffer zBuffer;
    private Lerp lerp = new Lerp();

    public void drawScene(Scene scene, Mat4 viewMat, Mat4 projectionMat) {
        final List<Solid> solids = scene.getSolids();
        final List<Mat4> modelMats = scene.getModelMats();
        final Mat4 viewProjection = viewMat.mul(projectionMat);
        for (int i = 0; i < solids.size(); i++) {
            final Mat4 transformation = modelMats.get(i).mul(viewProjection);
            drawSolid(solids.get(i), transformation);
        }
    }

    private void drawSolid(Solid solid, Mat4 transformation) {
        final List<Vertex> vertices = solid.getVertices().stream()
                .map(v -> v.transformed(transformation))
                .toList();
        final List<Integer> indices = solid.getIndices();
        for (Part part: solid.getParts()) {
            switch (part.getTopology()) {
                case LINE_LIST -> {
                    // for all lines
                    for (int i = part.getOffset(); i < part.getOffset() + part.getCount() * 2; i += 2) {
                        final Vertex v1 = vertices.get(indices.get(i));
                        final Vertex v2 = vertices.get(indices.get(i + 1));
                        if (!isOutOfViewSpace(List.of(v1, v2))) {
                            List<Vertex> clippedZ = clipZ(v1, v2);
                            liner.draw(
                                    clippedZ.get(0).dehomog().toViewport(zBuffer.getWidth(), zBuffer.getWidth()),
                                    clippedZ.get(1).dehomog().toViewport(zBuffer.getWidth(), zBuffer.getWidth())
                            );
                        }
                    }
                }
                case TRIANGLE_FAN -> {
                    final Vertex center = vertices.get(indices.get(part.getOffset()));
                    Vertex penultimate = vertices.get(indices.get(part.getOffset() + 1));
                    for (int i = part.getOffset() + 2; i < part.getOffset() + 2 + part.getCount(); i += 1) {
                        final Vertex last = vertices.get(indices.get(i));
                        // TODO
                        penultimate = last;
                    }
                }
                // TODO other topologies
            }
        }
    }

    private boolean isOutOfViewSpace(List<Vertex> vertices) { // TODO
        final boolean allTooLeft = vertices.stream().allMatch(v -> v.getPosition().getX() < -v.getPosition().getW());
        final boolean allTooRight = vertices.stream().allMatch(v -> v.getPosition().getX() > v.getPosition().getW());
        final boolean allTooUp = false;
        final boolean allTooDown = false;
        final boolean allTooClose = false;
        final boolean allTooFar = false;
        return false;
    }

    private List<Vertex> clipZ(Vertex v1, Vertex v2) {
        final Vertex min = (v1.getPosition().getZ() < v2.getPosition().getZ()) ? v1 : v2;
        final Vertex max = (min == v1) ? v2 : v1;
        if (min.getPosition().getZ() < 0) {
            final double t = -min.getPosition().getZ() / (max.getPosition().getZ() - min.getPosition().getZ());
            final Vertex P = lerp.compute(min, max, t);
            return List.of(P, max);
        }
        return List.of(v1, v2);
    }

    private List<Vertex> clipZ(Vertex v1, Vertex v2, Vertex v3) {
        // TODO
        return List.of(v1, v2, v3);
    }

}
