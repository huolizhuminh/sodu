package zhu.minhui.com.shudu;

import android.graphics.RectF;


public class TextMetrics {
    public float x;
    public float y;
    public float textWidth;
    public float textHeight;
    public RectF textRectF = new RectF();

    public TextMetrics offset(float dx, float dy) {
        x += dx;
        y += dy;
        textRectF.offset(dx, dy);
        return this;
    }
}
