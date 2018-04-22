package zhu.minhui.com.shudu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

import zhu.minhui.com.shudu.engin.SoduNode;
import zhu.minhui.com.shudu.engin.SuDuOperation;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/4/11.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class PositionView extends View {
    private static final int BOLD_LINE_WITH_DP = 2;
    private static final float NO_BOLD_LINE_WITH_DP = 1.5f;
    private static final int SHUDU_LENGTH = 81;
    private static final int PADDING_DP = 5;
    private static final String TAG = "PositionView";
    private static final float AXIS_TEXT_SIZE = 20;
    private SoduNode[][] data;
    private int boldLineWith;
    private int noBoldLineWith;
    private int padding;
    private int width;
    private float nodeWidth;
    Paint paint = new TextPaint();
    private int boldColor;
    private int noBoldColor;
    protected final GraphTextHelper graphTextHelper = new GraphTextHelper();
    private int leftMargin;
    private int topMargin;
    private RectF drawRetF;
    private float textSize;
    private boolean isFocus = false;
    private int currentSelectX;
    private int currentSelectY;
    private int ligtbule;
    private int selectColor;
    Stack<SuDuOperation> stack = new Stack<SuDuOperation>();
    public static final int EDIT_MODE = 1;
    public static final int SOLVE_MODE = 0;
    int mode = SOLVE_MODE;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public PositionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        baseInit(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawInnerLine(canvas);
        drawOuterLine(canvas);
        drawData(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (data == null) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        currentSelectX = (int) ((x - drawRetF.left - padding) / (nodeWidth + noBoldLineWith));
        currentSelectY = (int) ((y - drawRetF.top - padding) / (nodeWidth + noBoldLineWith));
        if (currentSelectX > 8) {
            currentSelectX = 8;
        }
        if (currentSelectX < 0) {
            currentSelectX = 0;
        }
        if (currentSelectY > 8) {
            currentSelectY = 8;
        }
        if (currentSelectY < 0) {
            currentSelectY = 0;
        }
        Log.d(TAG, "x is " + currentSelectX + "  y is " + currentSelectY);
        isFocus = data[currentSelectY][currentSelectX].needTobeSolve||mode==EDIT_MODE;
        invalidate();

        return super.onTouchEvent(event);
    }

    public void reSetFocus() {
        isFocus = false;
    }

    private void drawBackground(Canvas canvas) {

        if (!isFocus) {
            return;
        }
        paint.reset();
        paint.setColor(selectColor);

        drawGroupBg(canvas);
        drawRowBg(canvas);
        drawListBg(canvas);
    }

    private void drawGroupBg(Canvas canvas) {
        int startx = (int) (drawRetF.left + padding + (currentSelectX / 3) * (boldLineWith + 2 * noBoldLineWith + 3 * nodeWidth) + 1.0f / 2 * boldLineWith);
        int starty = (int) (drawRetF.top + padding + (currentSelectY / 3) * (boldLineWith + 2 * noBoldLineWith + 3 * nodeWidth) + 1.0f / 2 * boldLineWith);
        int endx = (int) (drawRetF.left + padding + (currentSelectX / 3 + 1) * (boldLineWith + 2 * noBoldLineWith + 3 * nodeWidth) + 1.0f / 2 * boldLineWith);
        int endy = (int) (drawRetF.top + padding + (currentSelectY / 3 + 1) * (boldLineWith + 2 * noBoldLineWith + 3 * nodeWidth) + 1.0f / 2 * boldLineWith);
        canvas.drawRect(new RectF(startx, starty, endx, endy), paint);

    }

    private void drawListBg(Canvas canvas) {
        int startx = (int) (drawRetF.left + padding);
        int starty = (int) (drawRetF.top + padding + noBoldLineWith * currentSelectY + nodeWidth * currentSelectY);
        int endX = (int) (drawRetF.right - padding - boldLineWith);
        int endY = (int) (starty + nodeWidth + boldLineWith + noBoldLineWith);
        canvas.drawRect(new RectF(startx, starty, endX, endY), paint);

    }

    private void drawRowBg(Canvas canvas) {
        int startx = (int) (drawRetF.left + padding + noBoldLineWith * currentSelectX + nodeWidth * currentSelectX);
        int starty = (int) (drawRetF.top + padding);
        int endX = (int) (startx + nodeWidth + boldLineWith + noBoldLineWith);
        int endY = (int) (drawRetF.bottom - padding - boldLineWith);
        canvas.drawRect(new RectF(startx, starty, endX, endY), paint);
    }

    private void drawData(Canvas canvas) {
        if (data == null || data.length == 0) {
            return;
        }
        paint.reset();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        a:
        for (int i = 0; i < 9; i++) {
            b:
            for (int j = 0; j < 9; j++) {
                SoduNode soduNode = data[i][j];
                if (soduNode.value == 0) {
                    continue b;
                }
                if (soduNode.needTobeSolve) {
                    paint.setColor(Color.RED);
                } else {
                    paint.setColor(Color.BLUE);
                }
                int x = (int) ((j + 1.0f / 2) * nodeWidth + (j / 3 + 1) * boldLineWith
                        + (j / 3 * 2 + i % 3) * noBoldLineWith + padding + drawRetF.left);
                int y = (int) ((i + 1.0f / 2) * nodeWidth + (i / 3 + 1) * boldLineWith
                        + (i / 3 * 2 + j % 3) * noBoldLineWith + padding + drawRetF.top);
                graphTextHelper.drawText(canvas, paint, String.valueOf(soduNode.value), x, y, 4, TextAxisType.CENTER);
            }
        }
    }

    private void drawInnerLine(Canvas canvas) {
        paint.reset();
        paint.setColor(noBoldColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(noBoldLineWith);
        paint.setAntiAlias(true);

        int startX, startY, endX, endY;
        for (int i = 0; i < 6; i++) {
            startX = (int) (drawRetF.left + padding);
            startY = (int) ((i / 2 + 1 + 1.0 / 2) * boldLineWith + (((int) i / 2) * 3 + 1 + i % 2) * nodeWidth
                    + (((int) i / 2) * 2) * noBoldLineWith + padding + drawRetF.top);
            endY = startY;
            endX = (int) (drawRetF.right - boldLineWith - padding);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
        for (int i = 0; i < 6; i++) {
            startY = padding;
            startX = (int) ((i / 2 + 1 + 1.0 / 2) * boldLineWith + (((int) i / 2) * 3 + 1 + i % 2) * nodeWidth
                    + (((int) i / 2) * 2 + i % 2) * noBoldLineWith + padding + drawRetF.left);
            endX = startX;
            endY = (int) (drawRetF.bottom - padding - boldLineWith);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

    }

    private void drawOuterLine(Canvas canvas) {
        paint.reset();
        paint.setColor(boldColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(boldLineWith);
        paint.setAntiAlias(true);
        int startX, startY, endX, endY;
        //行
        for (int i = 0; i < 4; i++) {
            startX = (int) (padding + drawRetF.left);
            startY = (int) ((i + 1.0f / 2) * boldLineWith + 2 * i * noBoldLineWith + 3 * i * nodeWidth + padding + drawRetF.top);
            endX = (int) (drawRetF.right - padding - boldLineWith);
            endY = startY;
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
        //列
        for (int i = 0; i < 4; i++) {
            startY = (int) (drawRetF.top + padding);
            startX = (int) ((i + 1.0f / 2) * boldLineWith + 2 * i * noBoldLineWith + 3 * i * nodeWidth + padding + drawRetF.left);
            endY = (int) (drawRetF.bottom - padding - boldLineWith);
            endX = startX;
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    private void baseInit(AttributeSet attrs) {
        initColor();
    }

    private void initColor() {
        boldColor = getContext().getResources().getColor(R.color.boldColor);
        noBoldColor = getContext().getResources().getColor(R.color.noBoldColor);
        selectColor = getContext().getResources().getColor(R.color.select_back);
    }

    public SoduNode[][] getData() {
        return data;
    }

    public void setData(SoduNode[][] data) {
        if (data == null) {
            return;
        }
        this.data = data;
        isFocus = false;
        stack.empty();
        invalidate();
    }


    public void setData(int i) {
        setData(i, true);
    }

    public void setData(int value, int x, int y) {
        if (data == null) {
            return;
        }
        SoduNode soduNode = data[y][x];
        if (!soduNode.isSuitableValue(value)) {
            return;
        }
        int beforeValue = soduNode.value;
        SuDuOperation suDuOperation = new SuDuOperation(soduNode, beforeValue);
        stack.push(suDuOperation);
        soduNode.value = value;
        isFocus = false;
        invalidate();
    }

    public void setResultData(SoduNode[][] nodes) {
        if (data == null) {
            return;
        }
        data = nodes;
        stack.clear();
        isFocus = false;
        invalidate();
    }

    public void back() {
        SuDuOperation suDuOperation;
        try {
            suDuOperation = stack.pop();
        } catch (Exception e) {
            return;
        }

        if (suDuOperation == null) {
            return;
        }
        suDuOperation.reSet();
        isFocus = false;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawParameter(w, h);
    }

    private void initDrawParameter(int w, int h) {
        boldLineWith = GraphUtils.dip2px(getContext(), BOLD_LINE_WITH_DP);
        noBoldLineWith = GraphUtils.dip2px(getContext(), NO_BOLD_LINE_WITH_DP);
        padding = GraphUtils.dip2px(getContext(), PADDING_DP);
        textSize = GraphUtils.dip2px(getContext(), AXIS_TEXT_SIZE);
        if (w > h) {
            width = h;
            leftMargin = (w - h) / 2;
        } else {
            width = w;
            topMargin = (h - 2) / 2;
        }
        drawRetF = new RectF();
        drawRetF.left = leftMargin;
        drawRetF.top = topMargin;
        drawRetF.right = w - leftMargin;
        drawRetF.bottom = h - topMargin;
        nodeWidth = (width - 4 * boldLineWith - 2 * padding - 6 * noBoldLineWith) / 9;
        Log.d(TAG, "boldLineWith:" + boldLineWith + "noBoldLineWith"
                + noBoldLineWith + "padding" + padding + "width" + width + "nodeWidth" + nodeWidth);
    }

    public void refreshData() {

        isFocus = false;
        invalidate();
    }

    public void backup(SoduNode soduNode, int value) {
        stack.push(new SuDuOperation(soduNode, soduNode.value));
        soduNode.value = value;
    }

    public void setData(int value, boolean needSolve) {

        if (data == null || !isFocus) {
            return;
        }

        SoduNode soduNode = data[currentSelectY][currentSelectX];
        if (!soduNode.isSuitableValue(value) ) {
            return;
        }
        int beforeValue = soduNode.value;
        SuDuOperation suDuOperation = new SuDuOperation(soduNode, beforeValue, soduNode.needTobeSolve);
        stack.push(suDuOperation);
        soduNode.value = value;
        soduNode.needTobeSolve = needSolve;
        isFocus = false;
        invalidate();
    }
}
