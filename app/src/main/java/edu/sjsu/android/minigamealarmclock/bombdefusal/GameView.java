package edu.sjsu.android.minigamealarmclock.bombdefusal;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends View {
    Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
    }
}
