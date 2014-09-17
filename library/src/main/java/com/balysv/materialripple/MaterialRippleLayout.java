/*
 * Copyright (C) 2014 Balys Valentukevicius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.balysv.materialripple;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

import static android.view.GestureDetector.SimpleOnGestureListener;

public class MaterialRippleLayout extends FrameLayout {

    private static final int     DEFAULT_DURATION    = 500;
    private static final float   DEFAULT_DIAMETER_DP = 35;
    private static final float   DEFAULT_ALPHA       = 0.7f;
    private static final int     DEFAULT_COLOR       = Color.BLACK;
    private static final int     DEFAULT_BACKGROUND  = Color.WHITE;
    private static final boolean DEFAULT_HOVER       = true;

    private static final boolean DEFAULT_RIPPLE_OVERLAY = true;

    private static final long FADE_DURATION    = 200;
    private static final int  FADE_EXTRA_DELAY = 25;
    private static final long HOVER_DURATION   = 200;

    private final Paint paint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect  bounds = new Rect();

    private int      rippleColor;
    private boolean  rippleOverlay;
    private boolean  rippleHover;
    private int      rippleDiameter;
    private int      rippleDuration;
    private int      rippleAlpha;
    private Drawable rippleBackground;

    private float radius;

    private View childView;

    private AnimatorSet    rippleAnimator;
    private ObjectAnimator hoverAnimator;

    private float   eventX;
    private float   eventY;
    private boolean eventCancelled;

    private GestureDetector gestureDetector;

    public MaterialRippleLayout(Context context) {
        this(context, null, 0);
    }

    public MaterialRippleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialRippleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setWillNotDraw(false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapConfirm());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRippleLayout);
        rippleColor = a.getColor(R.styleable.MaterialRippleLayout_rippleColor, DEFAULT_COLOR);
        rippleDiameter = a.getDimensionPixelSize(
            R.styleable.MaterialRippleLayout_rippleDimension,
            (int) dpToPx(getResources(), DEFAULT_DIAMETER_DP)
        );
        rippleOverlay = a.getBoolean(R.styleable.MaterialRippleLayout_rippleOverlay, DEFAULT_RIPPLE_OVERLAY);
        rippleHover = a.getBoolean(R.styleable.MaterialRippleLayout_rippleHover, DEFAULT_HOVER);
        rippleDuration = a.getInt(R.styleable.MaterialRippleLayout_rippleDuration, DEFAULT_DURATION);
        rippleAlpha = (int) (255 * a.getFloat(R.styleable.MaterialRippleLayout_rippleAlpha, DEFAULT_ALPHA));
        rippleBackground = new ColorDrawable(a.getColor(R.styleable.MaterialRippleLayout_rippleBackground, DEFAULT_BACKGROUND));

        a.recycle();

        paint.setColor(rippleColor);
        paint.setAlpha(rippleAlpha);
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T getChildView() {
        return (T) childView;
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("MaterialRippleLayout can host only one child");
        }
        //noinspection unchecked
        childView = child;
        super.addView(child, index, params);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (childView == null) {
            throw new IllegalStateException("MaterialRippleLayout must have a child view to handle clicks");
        }
        childView.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superOnTouchEvent = super.onTouchEvent(event);

        if (!isEnabled() || !childView.isEnabled()) return superOnTouchEvent;

        eventX = event.getX();
        eventY = event.getY();

        boolean isEventInBounds = bounds.contains((int) eventX, (int) eventY);

        if (gestureDetector.onTouchEvent(event)) {
            childView.onTouchEvent(event);
            startRipple();
        } else {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (isEventInBounds) {
                        startRipple();
                    } else {
                        setRadius(0);
                    }
                    childView.onTouchEvent(event);
                    break;
                case MotionEvent.ACTION_DOWN:
                    eventCancelled = false;
                    if (rippleHover) {
                        startHover();
                    }
                    childView.onTouchEvent(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (hoverAnimator != null) {
                        hoverAnimator.cancel();
                    }
                    setRadius(0);
                    childView.onTouchEvent(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (rippleHover) {
                        if (isEventInBounds && !eventCancelled) {
                            setRadius(rippleDiameter);
                        } else {
                            setRadius(0);
                        }
                    }

                    if (!isEventInBounds) {
                        childView.onTouchEvent(event);
                        eventCancelled = true;
                    }
                    break;
            }
        }
        return true;
    }

    private void startHover() {
        if (eventCancelled) return;

        if (hoverAnimator != null) {
            hoverAnimator.cancel();
        }
        hoverAnimator = ObjectAnimator.ofFloat(this, radiusProperty, 0, rippleDiameter)
            .setDuration(HOVER_DURATION);
        hoverAnimator.setInterpolator(new DecelerateInterpolator(3));
        hoverAnimator.start();
    }

    private void startRipple() {
        if (eventCancelled) return;

        final int width = getWidth();
        final int height = getHeight();

        final int halfWidth = width / 2;
        final int halfHeight = height / 2;

        final float radiusX = halfWidth > eventX ? width - eventX : eventX;
        final float radiusY = halfHeight > eventY ? height - eventY : eventY;

        float endRadius = (float) Math.sqrt(Math.pow(radiusX, 2) + Math.pow(radiusY, 2)) * 1.2f;

        if (rippleAnimator != null) {
            rippleAnimator.cancel();
        }

        if (hoverAnimator != null) {
            hoverAnimator.cancel();
        }

        rippleAnimator = new AnimatorSet();
        rippleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                setRadius(0);
                setRippleAlpha(rippleAlpha);
            }
        });

        ObjectAnimator ripple = ObjectAnimator.ofFloat(this, radiusProperty, radius, endRadius);
        ripple.setDuration(rippleDuration);
        ripple.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator fade = ObjectAnimator.ofInt(this, circleAlphaProperty, rippleAlpha, 0);
        fade.setDuration(FADE_DURATION);
        fade.setInterpolator(new AccelerateInterpolator());
        fade.setStartDelay(rippleDuration - FADE_DURATION - FADE_EXTRA_DELAY);

        rippleAnimator.playTogether(ripple, fade);
        rippleAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(0, 0, w, h);
        rippleBackground.setBounds(bounds);
    }

    /*
     * Drawing
     */
    @Override
    public void draw(Canvas canvas) {
        if (rippleOverlay) {
            rippleBackground.draw(canvas);
            super.draw(canvas);
            canvas.drawCircle(eventX, eventY, radius, paint);
        } else {
            rippleBackground.draw(canvas);
            canvas.drawCircle(eventX, eventY, radius, paint);
            super.draw(canvas);
        }
    }

    /*
     * Animations
     */
    private Property<MaterialRippleLayout, Float> radiusProperty
        = new Property<MaterialRippleLayout, Float>(Float.class, "radius") {
        @Override
        public Float get(MaterialRippleLayout object) {
            return object.getRadius();
        }

        @Override
        public void set(MaterialRippleLayout object, Float value) {
            object.setRadius(value);
        }
    };

    private float getRadius() {
        return radius;
    }


    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    private Property<MaterialRippleLayout, Integer> circleAlphaProperty
        = new Property<MaterialRippleLayout, Integer>(Integer.class, "rippleAlpha") {
        @Override
        public Integer get(MaterialRippleLayout object) {
            return object.getRippleAlpha();
        }

        @Override
        public void set(MaterialRippleLayout object, Integer value) {
            object.setRippleAlpha(value);
        }
    };

    public int getRippleAlpha() {
        return paint.getAlpha();
    }

    public void setRippleAlpha(Integer rippleAlpha) {
        paint.setAlpha(rippleAlpha);
        invalidate();
    }

    /*
    * Accessor
     */
    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    public void setRippleOverlay(boolean rippleOverlay) {
        this.rippleOverlay = rippleOverlay;
    }

    public void setRippleDiameter(int rippleDiameter) {
        this.rippleDiameter = rippleDiameter;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public void setRippleBackground(int color) {
        this.rippleBackground = new ColorDrawable(color);
    }

    public void setRippleHover(boolean rippleHover) {
        this.rippleHover = rippleHover;
    }

    /*
     * Helper
     */
    private class SingleTapConfirm extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    static float dpToPx(Resources resources, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
