package com.example.cameratest.Interface;

public interface BrushFragmentListener {
    void onBrushSizeChanged(float size);
    void onBrushOpacityChanged(int opacity);
    void onBrushColorChangeListener(int color);
    void onBrushStateChangeListener(boolean isEraser);
}
