package com.example.leonl.presentationdemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class WhimSurface extends GLSurfaceView {
    private float myDensity;
    private WhimRenderer myWhimRenderer;

    public WhimSurface(Context context) {
        super(context);
    }


    public WhimSurface(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setRenderer(WhimRenderer myRenderer, float density){
        this.myWhimRenderer = myRenderer;
        this.myDensity = density;

        super.setRenderer(myRenderer);
    }
}
