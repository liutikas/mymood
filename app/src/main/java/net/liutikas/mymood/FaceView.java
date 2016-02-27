package net.liutikas.mymood;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class FaceView extends View {
    private Paint mFacePaint;
    private Paint mOutlinePaint;
    private Paint mWhitePaint;
    private Paint mIrisPaint;
    public int mHappiness = 0;

    private int mFaceRadius;
    private RectF mMouthBounds;
    private int mMouthTop;
    private int mVerticalCenter;
    private int mHorizontalCenter;

    private RectF mLeftEyeBounds;
    private RectF mRightEyeBounds;

    private RectF mLeftIrisBounds;
    private RectF mRightIrisBounds;

    private RectF mLeftEyebrowBounds;
    private RectF mRightEyebrowBounds;

    public FaceView(Context context) {
        super(context);
        init();
    }

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public FaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setHappiness(int happiness) {
        mHappiness = happiness;
        updateMouthBounds();
        updateEyeBounds();
        mFacePaint.setARGB(255, 255, (100 - mHappiness) + 100, 20);
        invalidate();
    }

    private void init() {
        mFacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFacePaint.setStyle(Paint.Style.FILL);

        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setColor(Color.BLACK);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(5);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setColor(Color.WHITE);

        mIrisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIrisPaint.setColor(0xff663300);

        mMouthBounds = new RectF();
        mLeftEyeBounds = new RectF();
        mRightEyeBounds = new RectF();
        mLeftIrisBounds = new RectF();
        mRightIrisBounds = new RectF();
        mLeftEyebrowBounds = new RectF();
        mRightEyebrowBounds = new RectF();
        setHappiness(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mFaceRadius = Math.min(w, h) / 2 - 10;
        mHorizontalCenter = w / 2;
        mVerticalCenter = h / 2;
        updateMouthBounds();
        updateEyeBounds();
    }

    private void updateEyeBounds() {
        final int eyeRadius = mFaceRadius / 7;
        final int irisRadius = mFaceRadius / 14;
        final int eyebrowRadius = mFaceRadius / 4;
        final int eyeVerticalCenter = mVerticalCenter - mFaceRadius / 7;
        final float leftEyeHorizontalCenter = mHorizontalCenter - mFaceRadius / 3.5f;
        final float rightEyeHorizontalCenter = mHorizontalCenter + mFaceRadius / 3.5f;
        final float eyeHalfHeight = eyeRadius / (2f - 0.01f * mHappiness);

        mLeftEyeBounds.set(
                leftEyeHorizontalCenter - eyeRadius,
                eyeVerticalCenter - eyeHalfHeight,
                leftEyeHorizontalCenter + eyeRadius,
                eyeVerticalCenter + eyeHalfHeight);

        mRightEyeBounds.set(
                rightEyeHorizontalCenter - eyeRadius,
                eyeVerticalCenter - eyeHalfHeight,
                rightEyeHorizontalCenter + eyeRadius,
                eyeVerticalCenter + eyeHalfHeight);

        mLeftIrisBounds.set(
                leftEyeHorizontalCenter - irisRadius,
                eyeVerticalCenter - irisRadius,
                leftEyeHorizontalCenter + irisRadius,
                eyeVerticalCenter + irisRadius);

        mRightIrisBounds.set(
                rightEyeHorizontalCenter - irisRadius,
                eyeVerticalCenter - irisRadius,
                rightEyeHorizontalCenter + irisRadius,
                eyeVerticalCenter + irisRadius);

        mLeftEyebrowBounds.set(
                leftEyeHorizontalCenter - eyebrowRadius,
                eyeVerticalCenter - eyebrowRadius + (eyeRadius - eyeHalfHeight),
                leftEyeHorizontalCenter + eyebrowRadius,
                eyeVerticalCenter + eyebrowRadius - (eyeRadius - eyeHalfHeight));

        mRightEyebrowBounds.set(
                rightEyeHorizontalCenter - eyebrowRadius,
                eyeVerticalCenter - eyebrowRadius + (eyeRadius - eyeHalfHeight),
                rightEyeHorizontalCenter + eyebrowRadius,
                eyeVerticalCenter + eyebrowRadius - (eyeRadius - eyeHalfHeight));
    }

    private void updateMouthBounds() {
        int mouthHeight;
        int mouthOffset = 0;
        if (mHappiness < 50) {
            mouthHeight = Math.round((mFaceRadius / 3) * (1 - 0.02f * mHappiness));
            mouthOffset = mouthHeight;
        } else {
            mouthHeight = Math.round((mFaceRadius / 3 * 2) * (0.02f * mHappiness - 1));
        }
        mMouthBounds.set(
                mHorizontalCenter - mFaceRadius / 2.5f,
                mVerticalCenter + mFaceRadius / 3 - mouthOffset,
                mHorizontalCenter + mFaceRadius / 2.5f,
                mVerticalCenter + mFaceRadius / 3 + mouthHeight);

        mMouthTop = mVerticalCenter + mFaceRadius / 3 + (mouthHeight - mouthOffset) / 2;
    }

    protected void onDraw (Canvas canvas) {
        // Draw face
        canvas.drawCircle(mHorizontalCenter, mVerticalCenter, mFaceRadius, mFacePaint);
        canvas.drawCircle(mHorizontalCenter, mVerticalCenter, mFaceRadius, mOutlinePaint);

        // Draw eyes
        canvas.drawOval(mLeftEyeBounds, mWhitePaint);
        canvas.drawOval(mLeftIrisBounds, mIrisPaint);
        canvas.drawOval(mLeftEyeBounds, mOutlinePaint);
        canvas.drawOval(mRightEyeBounds, mWhitePaint);
        canvas.drawOval(mRightIrisBounds, mIrisPaint);
        canvas.drawOval(mRightEyeBounds, mOutlinePaint);

        // Draw eyebrows
        canvas.drawArc(mLeftEyebrowBounds, 220, 100, false, mOutlinePaint);
        canvas.drawArc(mRightEyebrowBounds, 220, 100, false, mOutlinePaint);

        // Draw mouth
        canvas.drawArc(mMouthBounds, mHappiness < 50 ? 0 : 180, 180, false, mWhitePaint);
        canvas.drawArc(mMouthBounds, mHappiness < 50 ? 0 : 180, 180, false, mOutlinePaint);
        // Draw the top of the mouth
        canvas.drawLine(mMouthBounds.left - 2.5f, mMouthTop, mMouthBounds.right + 2.5f, mMouthTop, mOutlinePaint);
    }
}
