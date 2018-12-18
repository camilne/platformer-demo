package camilne.engine.graphics;

public class Vertex {

    public static final int elementBytes = 4;

    public static final int positionElementCount = 3;
    public static final int textureElementCount = 2;

    public static final int positionByteCount = positionElementCount * elementBytes;
    public static final int textureByteCount = textureElementCount * elementBytes;

    public static final int positionByteOffset = 0;
    public static final int textureByteOffset = positionByteOffset + positionByteCount;

    public static final int elementCount = positionElementCount + textureElementCount;
    public static final int stride = positionByteCount + textureByteCount;

    private float[] xyz;
    private float[] uv;

    public Vertex() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Vertex(float x, float y, float z, float u, float v) {
        setXY(x, y, z);
        setUV(u, v);
    }

    public float[] getElements() {
        float[] res = new float[elementCount];
        int i = 0;

        res[i++] = xyz[0];
        res[i++] = xyz[1];
        res[i++] = xyz[2];
        res[i++] = uv[0];
        res[i++] = uv[1];

        return res;
    }

    public void setXY(float x, float y, float z) {
        this.xyz = new float[]{x, y, z};
    }

    public void setUV(float u, float v) {
        this.uv = new float[]{u, v};
    }

}
