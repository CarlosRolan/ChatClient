package GUI.view.components;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class ImageView extends Canvas {

    @Override
    public void paint(Graphics g) {

        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = t.getImage("../../../res/img/download.png.png");
        g.drawImage(i, getHeight(), getWidth(), this);

    }

}