package zhu.minhui.com.shudu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by minhui.zhu
 */

public class GraphTextHelper {

    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    private TextMetrics textMetrics = new TextMetrics();

    public void drawText(Canvas canvas, Paint paint, String text, float x, float y, float padding, TextAxisType type) {
        TextMetrics metrics = getTextMetrics(paint, text, x, y, padding, type);
        canvas.drawText(text, metrics.x, metrics.y, paint);
    }

    public void drawTextArea(Canvas canvas, Paint paint, String text, TextMetrics metrics, int lineColor, int background, int textColor) {

        paint.setAntiAlias(true);
        paint.setColor(background);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(metrics.textRectF, paint);

        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(metrics.textRectF, paint);

        paint.setColor(textColor);
        canvas.drawText(text, metrics.x, metrics.y, paint);
    }

    public TextMetrics getSuitableTextMetrics(Paint paint, String text, float x, float y, float padding, TextAxisType type, RectF container) {
        TextMetrics metrics = getTextMetrics(paint, text, x, y, padding, type);
        return getSuitableTextMetrics(metrics, container);
    }

    public TextMetrics getSuitableTextMetrics(TextMetrics sourceMetrics, RectF container) {
        RectF source = sourceMetrics.textRectF;
        if (!container.contains(source)) {
            float offsetX = 0;
            float offsetY = 0;

            if (source.width() < container.width()) {
                if (source.left < container.left) {
                    offsetX = container.left - source.left;
                }

                if (source.right > container.right) {
                    offsetX = container.right - source.right;
                }
            }

            if (source.height() < container.height()) {
                if (source.top < container.top) {
                    offsetY = container.top - source.top;
                }

                if (source.bottom > container.bottom) {
                    offsetY = container.bottom - source.bottom;
                }
            }

            sourceMetrics.offset(offsetX, offsetY);
        }
        return sourceMetrics;
    }

    public TextMetrics getTextMetrics(Paint paint, String text, float x, float y, float padding, TextAxisType type) {
        TextMetrics metrics = getCenterTextRect(paint, text, x, y);
        float offsetX = metrics.textWidth / 2 + padding;
        float offsetY = metrics.textHeight / 2 + padding;
        switch (type) {
            case LEFT_CENTER:
                offsetY = 0;
                break;
            case LEFT_BOTTOM:
                offsetY = -offsetY;
                break;
            case RIGHT_TOP:
                offsetX = -offsetX;
                break;
            case RIGHT_CENTER:
                offsetX = -offsetX;
                offsetY = 0;
                break;
            case RIGHT_BOTTOM:
                offsetX = -offsetX;
                offsetY = -offsetY;
                break;
            case CENTER_TOP:
                offsetX = 0;
                break;
            case CENTER_BOTTOM:
                offsetX = 0;
                offsetY = -offsetY;
                break;
            case CENTER:
                offsetX = 0;
                offsetY = 0;
                break;
            default:
                break;
        }
        return metrics.offset(offsetX, offsetY);

    }

    private TextMetrics getCenterTextRect(Paint paint, String text, float x, float y) {

        paint.setTextAlign(Paint.Align.CENTER);

        float textWidth = paint.measureText(text);
        float textHeight = paint.getTextSize();

        textMetrics.textHeight = textHeight;
        textMetrics.textWidth = textWidth;

        paint.getFontMetrics(fontMetrics);
        float deltaY = -(fontMetrics.ascent + fontMetrics.descent) / 2;

        textMetrics.x = x;
        textMetrics.y = y + deltaY;

        textMetrics.textRectF.set(x - textWidth / 2, y - textHeight / 2, x + textWidth / 2, y + textHeight / 2);

        return textMetrics;
    }

}
