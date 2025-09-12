package kh.mort.canvas;

public class ImageObject {
    public Box box;
    public int clazz;
    public ImageObject(Box b, int clazz) {
        this.box = b;
        this.clazz = clazz;
    }

    public ImageObject copy() {
        return new ImageObject(new Box(box.x1, box.y1, box.x2, box.y2), clazz);
    }
}
