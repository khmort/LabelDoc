import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;

public class Button extends JButton {

    public Button(String text, Icon icon, int maxWidth) {
        this(text, icon);
        this.setMaxWidth(maxWidth);
    }

    public Button(String text, Icon icon) {
        super(text, icon);
    }

    public Button(String text) {
        super(text);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension superDim = super.getPreferredSize();
        if (maxWidth == -1) {
            return superDim;
        }
        return new Dimension(
                    (int) Math.min(superDim.width, this.maxWidth),
                    (int) superDim.height);
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public void setMaxWidth(int width) {
        this.maxWidth = (int) Math.abs(width);
    }

    private int maxWidth = -1;
}
