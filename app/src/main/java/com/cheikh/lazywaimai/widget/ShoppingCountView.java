package com.cheikh.lazywaimai.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;

import com.cheikh.lazywaimai.R;

/**
 * 购物车的加减号控件
 */
public class ShoppingCountView extends View {

    private final static int DEFAULT_DURATION = 300;
    private final static int DEFAULT_TEXT_SIZE_SP = 14;
    private final static int DEFAULT_SPACE_DP = 5;
    private final static int DEFAULT_TEXT_COLOR = Color.parseColor("#424242");

    private final static int STATE_NONE_MINUS = 0;
    private final static int STATE_PREP_DRAW_MINUS = 1;
    private final static int STATE_STRETCH_MINUS = 2;
    private final static int STATE_HAVE_MINUS = 3;
    private final static int STATE_PREP_HIDE_MINUS = 4;

    private Paint mCountPaint;
    private Paint mAddBgPaint;
    private Paint mAddTextPaint;
    private Paint mMinusBgPaint;
    private Paint mMinusTextPaint;

    private int mState = STATE_NONE_MINUS;

    private int mTextColor;
    private int mTextSize;
    private int mAddBtnBgColor;
    private int mAddBtnTextColor;
    private int mMinusBtnBgColor;
    private int mMinusBtnTextColor;
    private int mSpace;
    private int mDuration;

    private int mWidth;
    private int mHeight;
    private int mTextPosition;
    private int mMinusBtnPosition;
    private int mAngle;
    private int mAlpha;

    private boolean mIsExpand;
    private int mShoppingCount;
    private View mAnimTargetView;
    private ShoppingClickListener mShoppingClickListener;

    public ShoppingCountView(Context context) {
        this(context, null, 0);
    }

    public ShoppingCountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShoppingCountView);

        mTextColor = typeArray.getColor(R.styleable.ShoppingCountView_scv_text_color, DEFAULT_TEXT_COLOR);
        mTextSize = (int) typeArray.getDimension(R.styleable.ShoppingCountView_scv_text_size, sp2px(DEFAULT_TEXT_SIZE_SP));

        mAddBtnBgColor = typeArray.getColor(R.styleable.ShoppingCountView_scv_add_btn_bg_color,
                ContextCompat.getColor(context, android.R.color.holo_blue_light));
        mAddBtnTextColor = typeArray.getColor(R.styleable.ShoppingCountView_scv_add_btn_text_color,
                ContextCompat.getColor(context, android.R.color.white));

        mMinusBtnBgColor = typeArray.getColor(R.styleable.ShoppingCountView_scv_minus_btn_bg_color,
                ContextCompat.getColor(context, android.R.color.white));
        mMinusBtnTextColor = typeArray.getColor(R.styleable.ShoppingCountView_scv_minus_btn_text_color,
                ContextCompat.getColor(context, android.R.color.holo_blue_light));

        mSpace = (int) typeArray.getDimension(R.styleable.ShoppingCountView_scv_space, dp2px(DEFAULT_SPACE_DP));
        mDuration = typeArray.getInt(R.styleable.ShoppingCountView_scv_duration, DEFAULT_DURATION);

        typeArray.recycle();

        initPaints();
    }

    /**
     * 初始化各种画笔
     */
    private void initPaints() {
        // 绘制数量的画笔
        mCountPaint = new Paint();
        mCountPaint.setColor(mTextColor);
        mCountPaint.setTextSize(mTextSize);
        mCountPaint.setStrokeWidth(mTextSize / 6);
        mCountPaint.setAntiAlias(true);

        // 绘制加号按钮背景的画笔
        mAddBgPaint = new Paint();
        mAddBgPaint.setColor(mAddBtnBgColor);
        mAddBgPaint.setStyle(Paint.Style.FILL);
        mAddBgPaint.setAntiAlias(true);

        // 绘制加号的画笔
        mAddTextPaint = new Paint();
        mAddTextPaint.setColor(mAddBtnTextColor);
        mAddTextPaint.setStyle(Paint.Style.STROKE);
        mAddTextPaint.setAntiAlias(true);
        mAddTextPaint.setStrokeWidth(mTextSize / 6);

        // 绘制减号按钮背景的画笔
        mMinusBgPaint = new Paint();
        mMinusBgPaint.setColor(mMinusBtnBgColor);
        mMinusBgPaint.setStyle(Paint.Style.FILL);
        mMinusBgPaint.setAntiAlias(true);

        // 绘制减号的画笔
        mMinusTextPaint = new Paint();
        mMinusTextPaint.setColor(mMinusBtnTextColor);
        mMinusTextPaint.setStyle(Paint.Style.STROKE);
        mMinusTextPaint.setAntiAlias(true);
        mMinusTextPaint.setStrokeWidth(mTextSize / 6);
    }

    /**
     * 触摸点是否在加号按钮内
     * @param event
     * @return
     */
    private boolean isClickAddBtn(MotionEvent event) {
        PointF pointF = new PointF(event.getX(), event.getY());
        PointF circle = new PointF(mWidth - mHeight / 2, mHeight / 2);
        return Math.pow((pointF.x - circle.x), 2) + Math.pow((pointF.y - circle.y), 2) <= Math.pow(mHeight / 2, 2);
    }

    /**
     * 触摸点是否在减号按钮内
     * @param event
     * @return
     */
    private boolean isClickMinusBtn(MotionEvent event) {
        PointF pointF = new PointF(event.getX(), event.getY());
        PointF circle = new PointF(mHeight / 2, mHeight / 2);
        return Math.pow((pointF.x - circle.x), 2) + Math.pow((pointF.y - circle.y), 2) <= Math.pow(mHeight / 2, 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState == STATE_HAVE_MINUS && isClickMinusBtn(event)) {
                    mShoppingCount--;
                    if (mShoppingClickListener != null) {
                        mShoppingClickListener.onMinusClick(mShoppingCount);
                    }
                    if (mShoppingCount == 0) {
                        mState = STATE_PREP_HIDE_MINUS;
                        mIsExpand = false;
                    }
                    requestLayout();
                }
                if (isClickAddBtn(event)) {
                    mShoppingCount++;
                    if (mShoppingClickListener != null) {
                        mShoppingClickListener.onAddClick(mShoppingCount);
                    }
                    if (mShoppingCount == 1) {
                        mState = STATE_PREP_DRAW_MINUS;
                        mIsExpand = true;
                    }
                    if (mAnimTargetView != null) {
                        startParabolaAnim(this, mAnimTargetView);
                    }
                    requestLayout();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeight = mTextSize / 2 * 3;
        mWidth = mHeight * 2 + mSpace * 2 + getTextWidth(String.valueOf(mShoppingCount), mCountPaint);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState == STATE_NONE_MINUS) {
            drawAddButton(canvas);
        } else if (mState == STATE_PREP_DRAW_MINUS) {
            mState = STATE_STRETCH_MINUS;
            drawAddButton(canvas);
            startStretchAnim();
        } else if (mState == STATE_STRETCH_MINUS) {
            mMinusBgPaint.setAlpha(mAlpha);
            mMinusTextPaint.setAlpha(mAlpha);
            mCountPaint.setAlpha(mAlpha);
            drawAddButton(canvas);
            drawMinusBtn(canvas);
            drawCountText(canvas);
        } else if (mState == STATE_HAVE_MINUS) {
            mMinusBgPaint.setAlpha(255);
            mMinusTextPaint.setAlpha(255);
            mCountPaint.setAlpha(255);
            drawAddButton(canvas);
            drawMinusBtn(canvas);
            drawCountText(canvas);
        } else if (mState == STATE_PREP_HIDE_MINUS) {
            mState = STATE_STRETCH_MINUS;
            drawAddButton(canvas);
            startStretchAnim();
        }
    }

    /**
     * 绘制加号按钮
     * @param canvas
     */
    private void drawAddButton(Canvas canvas) {
        // 绘制加号的背景圆形
        canvas.drawCircle(mWidth - mHeight / 2, mHeight / 2, mHeight / 2, mAddBgPaint);
        // 绘制竖线
        canvas.drawLine(mWidth - mHeight / 2, mHeight / 4, mWidth - mHeight / 2, mHeight / 4 * 3, mAddTextPaint);
        // 绘制横线
        canvas.drawLine(mWidth - mHeight / 2 - mHeight / 4, mHeight / 2, mWidth - mHeight / 4, mHeight / 2, mAddTextPaint);
    }

    /**
     * 绘制选购数量的文本
     * @param canvas
     */
    private void drawCountText(Canvas canvas) {
        if (mState == STATE_STRETCH_MINUS) {
            if (mAngle != 0) {
                canvas.rotate(mAngle, mTextPosition, mHeight / 2);
            }
            int x = (int) (mTextPosition - getTextWidth(String.valueOf(mShoppingCount), mCountPaint) / 2f);
            int y = (int) (mHeight / 2 + getTextHeight(String.valueOf(mShoppingCount), mCountPaint) / 2f);
            canvas.drawText(String.valueOf(mShoppingCount), x, y, mCountPaint);

            if (mAngle != 0) {
                canvas.rotate(-mAngle, x, y);
            }
        } else {
            int x = (int) (mWidth / 2 - getTextWidth(String.valueOf(mShoppingCount), mCountPaint) / 2f);
            int y = (int) (mHeight / 2 + getTextHeight(String.valueOf(mShoppingCount), mCountPaint) / 2f);
            canvas.drawText(String.valueOf(mShoppingCount), x, y, mCountPaint);
        }
    }

    /**
     * 绘制减号按钮
     * @param canvas
     */
    private void drawMinusBtn(Canvas canvas) {
        if (mState == STATE_STRETCH_MINUS) {
            if (mAngle != 0) {
                canvas.rotate(mAngle, mMinusBtnPosition, mHeight / 2);
            }
            canvas.drawCircle(mMinusBtnPosition, mHeight / 2, mHeight / 2, mMinusBgPaint);
            mMinusTextPaint.setStrokeWidth(mTextSize / 10);
            canvas.drawCircle(mMinusBtnPosition, mHeight / 2, mHeight / 2 - mHeight / 20, mMinusTextPaint);
            mMinusTextPaint.setStrokeWidth(mTextSize / 6);
            canvas.drawLine(mMinusBtnPosition - mHeight / 4, mHeight / 2, mMinusBtnPosition + mHeight / 4, mHeight / 2, mMinusTextPaint);

            if (mAngle != 0) {
                canvas.rotate(-mAngle, mMinusBtnPosition, mHeight / 2);
            }
        } else {
            canvas.drawCircle(mHeight / 2, mHeight / 2, mHeight / 2, mMinusBgPaint);
            mMinusTextPaint.setStrokeWidth(mTextSize / 10);
            canvas.drawCircle(mHeight / 2, mHeight / 2, mHeight / 2 - mHeight / 20, mMinusTextPaint);
            mMinusTextPaint.setStrokeWidth(mTextSize / 6);
            canvas.drawLine(mHeight / 4, mHeight / 2, mHeight / 4 * 3, mHeight / 2, mMinusTextPaint);
        }
    }

    /**
     * 开始伸缩动画
     */
    private void startStretchAnim() {
        Collection<Animator> animatorList = new ArrayList<>();

        // 旋转购物车数量和减号的动画
        ValueAnimator animatorTextRotate;
        if (mIsExpand) {
            animatorTextRotate = ValueAnimator.ofInt(0, 360);
        } else {
            animatorTextRotate = ValueAnimator.ofInt(360, 0);
        }
        animatorTextRotate.setDuration(mDuration);
        animatorTextRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAngle = (Integer) valueAnimator.getAnimatedValue();
                if (mIsExpand) {
                    if (mAngle == 360) {
                        mState = STATE_HAVE_MINUS;
                    }
                } else {
                    if (mAngle == 0) {
                        mState = STATE_NONE_MINUS;
                    }
                }
            }
        });
        animatorList.add(animatorTextRotate);

        // 渐变购物车数量和减号的动画
        ValueAnimator animatorAlpha;
        if (mIsExpand) {
            animatorAlpha = ValueAnimator.ofInt(0, 255);
        } else {
            animatorAlpha = ValueAnimator.ofInt(255, 0);
        }
        animatorAlpha.setDuration(mDuration);
        animatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mAlpha = (Integer) valueAnimator.getAnimatedValue();
            if (mIsExpand) {
                if (mAlpha == 255) {
                    mState = STATE_HAVE_MINUS;
                }
            } else {
                if (mAlpha == 0) {
                    mState = STATE_NONE_MINUS;
                }
            }
            }
        });
        animatorList.add(animatorAlpha);

        // 购物车数量移动的动画
        ValueAnimator animatorTextMove;
        if (mIsExpand) {
            animatorTextMove = ValueAnimator.ofInt(mWidth - mHeight / 2, mWidth / 2);
        } else {
            animatorTextMove = ValueAnimator.ofInt(mWidth / 2, mWidth - mHeight / 2);
        }
        animatorTextMove.setDuration(mDuration);
        animatorTextMove.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTextPosition = (Integer) valueAnimator.getAnimatedValue();
                if (mIsExpand) {
                    if (mTextPosition == mWidth / 2) {
                        mState = STATE_HAVE_MINUS;
                    }
                } else {
                    if (mTextPosition == mWidth - mHeight / 2) {
                        mState = STATE_NONE_MINUS;
                    }
                }
            }
        });
        animatorList.add(animatorTextMove);

        // 减号移动的动画
        ValueAnimator animatorBtnMove;
        if (mIsExpand) {
            animatorBtnMove = ValueAnimator.ofInt(mWidth - mHeight / 2, mHeight / 2);
        } else {
            animatorBtnMove = ValueAnimator.ofInt(mHeight / 2, mWidth - mHeight / 2);
        }
        animatorBtnMove.setDuration(mDuration);
        animatorBtnMove.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mMinusBtnPosition = (Integer) valueAnimator.getAnimatedValue();
                if (mIsExpand) {
                    if (mMinusBtnPosition == mHeight / 2) {
                        mState = STATE_HAVE_MINUS;
                    }
                } else {
                    if (mMinusBtnPosition == mWidth - mHeight / 2) {
                        mState = STATE_NONE_MINUS;
                    }
                }
                requestLayout();
            }
        });
        animatorList.add(animatorBtnMove);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(animatorList);
        animatorSet.start();
    }

    /**
     * 启动加入到购物车的动画
     */
    public void startParabolaAnim(View startView, View endView) {
        // 创建一个覆盖在界面上的动画图层并生成对话对象
        final ViewGroup animLayout = createAnimLayer();
        if (animLayout == null) {
            return;
        }
        final View animView = createAnimView(startView);
        animLayout.addView(animView);
        // 设置动画对象的初始位置
        int[] animStartLoc = getViewLocation(startView);
        setAnimStartLoc(animStartLoc, animView);
        // 获取动画对象的终点位置
        int[] animEndLoc = getViewLocation(endView);
        //  根据起始位置和终点位置创建动画的轨迹
        int[] offset = getAnimOffset(animStartLoc, animEndLoc);
        final Animation animation = buildAnimation(offset);
        // 设置动画的执行监听器
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 将动画图层移除
                final ViewGroup parent = (ViewGroup) animLayout.getParent();
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(animLayout);
                    }
                });
            }
        });
        animView.startAnimation(animation);
    }

    /**
     * 创建动画层
     *
     * @return
     */
    private ViewGroup createAnimLayer() {
        Context context = getContext();
        if (context instanceof Activity) {
            final ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            LinearLayout ll = new LinearLayout(getContext());
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            ll.setBackgroundResource(android.R.color.transparent);
            decorView.addView(ll);
            return ll;
        }
        return null;
    }

    /**
     * 创建动画对象
     *
     * @param v
     * @return
     */
    private View createAnimView(View v) {
        v.buildDrawingCache();
        Bitmap animBitmap = v.getDrawingCache();
        ImageView ivAnim = new ImageView(getContext());
        ivAnim.setImageBitmap(animBitmap);
        return ivAnim;
    }

    /**
     * 获得动画开始位置，即价格视图所在位置
     *
     * @param v
     * @return
     */
    private int[] getViewLocation(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    /**
     * 设置动画起始位置
     *
     * @param startLoc
     * @param animView
     */
    private void setAnimStartLoc(int[] startLoc, View animView) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = startLoc[0];
        lp.topMargin = startLoc[1];
        animView.setLayoutParams(lp);
    }

    /**
     * 获得动画x，y坐标的偏移量
     *
     * @param startLoc
     * @param endLoc
     * @return
     */
    private int[] getAnimOffset(int[] startLoc, int[] endLoc) {
        int[] offset = new int[2];
        offset[0] = endLoc[0] - startLoc[0];
        offset[1] = endLoc[1] - startLoc[1];
        return offset;
    }

    /**
     * 根据偏移量创建动画
     *
     * @param offset
     * @return
     */
    private Animation buildAnimation(int[] offset) {
        AnimationSet as = new AnimationSet(false);
        TranslateAnimation translateX = new TranslateAnimation(0, offset[0], 0, 0);
        TranslateAnimation translateY = new TranslateAnimation(0, 0, 0, offset[1]);
        translateY.setInterpolator(new AccelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        as.addAnimation(scaleAnimation);
        as.addAnimation(translateX);
        as.addAnimation(translateY);
        as.setDuration(500);
        return as;
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(float dpValue) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getContext().getResources().getDisplayMetrics());
        return (int) px;
    }

    private int getTextWidth(String str, Paint paint) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private int getTextHeight(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return (int) (rect.height() / 33f * 29);
    }



    /////////////////////////////////////////////////////
    ///            以下是暴露给外部的公共方法              ///
    /////////////////////////////////////////////////////

    /**
     * 设置购买数量
     * @param shoppingCount
     */
    public void setShoppingCount(int shoppingCount) {
        shoppingCount = shoppingCount > 0 ? shoppingCount : 0;
        mShoppingCount = shoppingCount;
        if (shoppingCount > 0) {
            mState = STATE_HAVE_MINUS;
        } else {
            mState = STATE_NONE_MINUS;
        }
        requestLayout();
    }

    /**
     * 设置加入到购物车动画的目标视图
     * @param animTargetView
     */
    public void setAnimTargetView(View animTargetView) {
        mAnimTargetView = animTargetView;
    }

    /**
     * 获取加入到购物车动画的目标视图
     * @param animTargetView
     */
    public View getAnimTargetView(View animTargetView) {
        return mAnimTargetView;
    }

    /**
     * 获取购物车数量
     * @return
     */
    public int getShoppingCount() {
        return mShoppingCount;
    }

    /**
     * 设置监听器
     * @param shoppingClickListener
     */
    public void setOnShoppingClickListener(ShoppingClickListener shoppingClickListener) {
        mShoppingClickListener = shoppingClickListener;
    }

    public interface ShoppingClickListener {
        void onAddClick(int num);

        void onMinusClick(int num);
    }
}
