package architect.engine.mast.parser;

/**
 */
public class DataCharBuffer {

    public char[] data   = null;
    public int    length = 0;

    public DataCharBuffer() {
    }

    public DataCharBuffer(char[] data) {
        this.data = data;
        length = data.length;
    }

    public DataCharBuffer(int capacity) {
        this.data = new char[capacity];
        length = data.length;
    }
}
