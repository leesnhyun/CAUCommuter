package sh.cau.commuter.Main;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by SH on 2015-12-10.
 */
public class RealTimeCanvas extends View {

    public RealTimeCanvas(Context context) {
        super(context);
    }

    public RealTimeCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
