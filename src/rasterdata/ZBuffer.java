package rasterdata;

import transforms.Col;

public class ZBuffer {

    private final Raster<Col> colorRaster;
    private final Raster<Double> depthRaster;

    public ZBuffer(Raster<Col> colorRaster) {
        this.colorRaster = colorRaster;
        depthRaster = new DepthRaster(colorRaster.getWidth(), colorRaster.getHeight());
    }

    public void setPixel(int x, int y, double z, Col pixel) {
        // TODO
    }

    public void clear() {
        // TODO
    }
}
