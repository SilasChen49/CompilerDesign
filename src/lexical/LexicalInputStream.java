package lexical;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class LexicalInputStream extends PushbackInputStream {

    private int lineNumber;

    LexicalInputStream(InputStream in) {
        super(in, 8);
        lineNumber = 1;
    }

    @Override
    public int read() throws IOException {
        int value = super.read();

        if (value == '\n')
            lineNumber++;

        return value;
    }

    @Override
    public void unread(int b) throws IOException {
        if (b == '\n')
            lineNumber--;
        super.unread(b);
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
