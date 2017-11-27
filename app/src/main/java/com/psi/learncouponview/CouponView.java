package com.psi.learncouponview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by dorado on 2017/11/27.
 */

public class CouponView extends FrameLayout {

  private Paint semiCirclePaint;
  private Paint dottedLinePaint;

  private int width;
  private int height;

  private float semiCircleRadius = 10;
  private float semiCircleGap = 10;
  private int semiCircleColor = getContext().getResources().getColor(R.color.colorWhite);
  private boolean drawSemiCircleLeft, drawSemiCircleRight, drawSemiCircleTop, drawSemiCircleBottom;
  private int semiCircleNumberInX, semiCircleNumberInY;
  private float remainderCircleXHalf, remainderCircleYHalf;

  private float dottedLineWidth = 20;
  private float dottedLineHeight = 2;
  private float dottedLineGap = 5;
  private float dottedLineMargin = 10;
  private int dottedLineColor = getContext().getResources().getColor(R.color.colorWhite);
  private boolean drawDottedLineLeft, drawDottedLineRight, drawDottedLineTop, drawDottedLineBottom;
  private int dottedLineNumberInX, dottedLineNumberInY;
  private float remainderLineXHalf, remainderLineYHalf;

  public CouponView(@NonNull Context context) {
    this(context, null);
  }

  public CouponView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CouponView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
    initPaint();
  }

  // @formatter:off
  private void initAttrs(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CouponView);
    semiCircleRadius = typedArray.getDimensionPixelOffset(R.styleable.CouponView_SemiCircleRadius, (int)semiCircleRadius);
    semiCircleGap = typedArray.getDimensionPixelOffset(R.styleable.CouponView_SemiCircleGap, (int)semiCircleGap);
    semiCircleColor = typedArray.getColor(R.styleable.CouponView_SemiCircleColor, semiCircleColor);
    drawSemiCircleLeft = typedArray.getBoolean(R.styleable.CouponView_drawSemiCircleLeft, false);
    drawSemiCircleRight = typedArray.getBoolean(R.styleable.CouponView_drawSemiCircleRight, false);
    drawSemiCircleTop = typedArray.getBoolean(R.styleable.CouponView_drawSemiCircleTop, false);
    drawSemiCircleBottom = typedArray.getBoolean(R.styleable.CouponView_drawSemiCircleBottom, false);

    dottedLineWidth = typedArray.getDimensionPixelOffset(R.styleable.CouponView_dottedLineWidth, (int)dottedLineWidth);
    dottedLineHeight = typedArray.getDimensionPixelOffset(R.styleable.CouponView_dottedLineHeight, (int)dottedLineHeight);
    dottedLineGap = typedArray.getDimensionPixelOffset(R.styleable.CouponView_dottedLineGap, (int)dottedLineGap);
    dottedLineMargin = typedArray.getDimensionPixelOffset(R.styleable.CouponView_dottedLineMargin,(int)dottedLineMargin);
    drawDottedLineLeft = typedArray.getBoolean(R.styleable.CouponView_drawDottedLineLeft, drawDottedLineLeft);
    drawDottedLineRight = typedArray.getBoolean(R.styleable.CouponView_drawDottedLineRight, drawDottedLineRight);
    drawDottedLineTop = typedArray.getBoolean(R.styleable.CouponView_drawDottedLineTop, drawDottedLineTop);
    drawDottedLineBottom = typedArray.getBoolean(R.styleable.CouponView_drawDottedLineBottom, drawDottedLineBottom);
    typedArray.recycle();
  }

  // @formatter:on
  private void initPaint() {
    semiCirclePaint = new Paint();
    semiCirclePaint.setAntiAlias(true);
    semiCirclePaint.setDither(true);
    semiCirclePaint.setStyle(Paint.Style.FILL);
    semiCirclePaint.setColor(semiCircleColor);

    dottedLinePaint = new Paint();
    dottedLinePaint.setAntiAlias(true);
    dottedLinePaint.setDither(true);
    dottedLinePaint.setStyle(Paint.Style.STROKE);
    dottedLinePaint.setStrokeCap(Paint.Cap.ROUND);
    dottedLinePaint.setStrokeWidth(dottedLineHeight);
    dottedLinePaint.setColor(dottedLineColor);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;
    height = h;
    initSemiCircleNumber();
  }

  // @formatter:off
  private void initSemiCircleNumber() {
    if (drawSemiCircleTop || drawSemiCircleBottom) {
      // 为了美观 其实和结束为止应该为gap 而不是圆 所以画的时候最前和最后要预留一段
      // 两边各预留的位置等于= gap + remainderXHalf
      remainderCircleXHalf = (width - semiCircleGap) % (semiCircleGap + 2 * semiCircleRadius) / 2;
      semiCircleNumberInX = (int) ((width - semiCircleGap) / (semiCircleGap + 2 * semiCircleRadius));
    }
    if (drawSemiCircleLeft || drawSemiCircleRight) {
      remainderCircleYHalf = (height - semiCircleGap) % (semiCircleGap + 2 * semiCircleRadius) / 2;
      semiCircleNumberInY = (int) ((height - semiCircleGap) / (semiCircleGap + 2 * semiCircleRadius));
    }

    if (drawDottedLineTop || drawDottedLineBottom) {
      remainderLineXHalf =  (width  + dottedLineGap - dottedLineMargin * 2) % (dottedLineGap + dottedLineWidth) / 2f;
      dottedLineNumberInX = (int) ((width  + dottedLineGap - dottedLineMargin * 2) / (dottedLineGap + dottedLineWidth));
    }

    if (drawDottedLineLeft || drawDottedLineRight) {
      remainderLineYHalf = (height  + dottedLineGap - dottedLineMargin * 2) % (dottedLineGap + dottedLineWidth) / 2;
      dottedLineNumberInY = (int) ((height   + dottedLineGap - dottedLineMargin * 2) / (dottedLineGap + dottedLineWidth));
    }
  }

  // @formatter:off
  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // 画半圆
    if (drawSemiCircleTop) {
      for (int i = 0; i < semiCircleNumberInX; i++) {
        float centerX = remainderCircleXHalf + semiCircleGap + semiCircleRadius + (semiCircleGap + semiCircleRadius * 2) * i;
        canvas.drawCircle(centerX, 0, semiCircleRadius, semiCirclePaint);
      }
    }

    if (drawSemiCircleBottom) {
      for (int i = 0; i < semiCircleNumberInX; i++) {
        float centerX = remainderCircleXHalf + semiCircleGap + semiCircleRadius + (semiCircleGap + semiCircleRadius * 2) * i;
        canvas.drawCircle(centerX, height, semiCircleRadius, semiCirclePaint);
      }
    }

    if (drawSemiCircleLeft) {
      for (int i = 0; i < semiCircleNumberInY; i++) {
        float centerY = remainderCircleYHalf + semiCircleGap + semiCircleRadius + (semiCircleGap + semiCircleRadius * 2) * i;
        canvas.drawCircle(0, centerY, semiCircleRadius, semiCirclePaint);
      }
    }

    if (drawSemiCircleTop) {
      for (int i = 0; i < semiCircleNumberInY; i++) {
        float centerY = remainderCircleYHalf + semiCircleGap + semiCircleRadius + (semiCircleGap + semiCircleRadius * 2) * i;
        canvas.drawCircle(width, centerY, semiCircleRadius, semiCirclePaint);
      }
    }
    // 画虚线
    if (drawDottedLineTop) {
      for (int i = 0; i < dottedLineNumberInX; i++) {
        float startX = dottedLineMargin + remainderLineXHalf + (dottedLineWidth + dottedLineGap) * i;
        float startY = dottedLineMargin;
        float stopY = dottedLineMargin;
        canvas.drawLine(startX, startY, startX + dottedLineWidth, stopY, dottedLinePaint);
      }
    }

    if (drawDottedLineBottom) {
      for (int i = 0; i < dottedLineNumberInX; i++) {
        float startX = dottedLineMargin + remainderLineXHalf + (dottedLineWidth + dottedLineGap) * i;
        float startY = height - dottedLineMargin;
        float stopY = height - dottedLineMargin;
        canvas.drawLine(startX, startY, startX + dottedLineWidth, stopY, dottedLinePaint);
      }
    }

    if (drawDottedLineLeft) {
      for (int i = 0; i < dottedLineNumberInY; i++) {
        float startX = dottedLineMargin;
        float stopX = dottedLineMargin;
        float startY = dottedLineMargin + remainderLineYHalf + (dottedLineWidth + dottedLineGap) * i;
        canvas.drawLine(startX, startY, stopX, startY + dottedLineWidth, dottedLinePaint);
      }
    }

    if (drawDottedLineRight) {
      for (int i = 0; i < dottedLineNumberInY; i++) {
        float startX = width - dottedLineMargin;
        float stopX = width - dottedLineMargin;
        float startY = dottedLineMargin + remainderLineYHalf + (dottedLineWidth + dottedLineGap) * i;
        canvas.drawLine(startX, startY, stopX, startY + dottedLineWidth, dottedLinePaint);
      }
    }
  }
}
