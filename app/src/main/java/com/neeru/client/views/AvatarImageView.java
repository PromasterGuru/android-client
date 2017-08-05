package com.neeru.client.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.neeru.client.R;

/**
 * Created by brajendra on 11/07/17.
 */

public class AvatarImageView extends BezelImageView {
    private final Paint paint = new Paint();

    public AvatarImageView(Context context) {
        super(context);
        init();

    }

    public AvatarImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AvatarImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();

    }

    private void init() {
        this.paint.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        this.paint.setAntiAlias(true);
        if (isInEditMode()) {
            setImageResource(R.drawable.ic_avatar_default);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas(canvas);
    }

    private void canvas(Canvas canvas) {
        if (!isEnabled()) {
            canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, ((float) getWidth()) / 2.0f, this.paint);
        }
    }

}