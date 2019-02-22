package com.ysh.testopengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 作者：create by @author{ YSH } on 2019/2/21
 * 描述:
 * 修改备注:
 */
public class MySurfaceView extends GLSurfaceView {
    /**
     * 角度缩放比例
     */
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    /**
     * 场景渲染器
     */
    private SceneRenderer mRenderer;
    /**
     * 上次触碰x
     */
    private float mPreviousX;
    /**
     * 上次触碰Y
     */
    private float mPreviousY;

    public MySurfaceView(Context context) {
        super(context);
        //使用OpenGL2.0
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        //主动渲染模式
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                for (Triangle triangle : mRenderer.triangles) {
                    triangle.yAngle += dy * TOUCH_SCALE_FACTOR;//设置绕y轴旋转角度
                    triangle.xAngle += dx * TOUCH_SCALE_FACTOR;//设置绕x轴旋转角度
                }
                break;
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class SceneRenderer implements Renderer {
        Triangle[] triangles = new Triangle[3];

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //绘制物体
            for (Triangle triangle : triangles) {
                triangle.drawSelf();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置窗口大小和位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView宽高比
            float ratio = (float) width / (float) height;
            //设置平行投影
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
            //调用此方法产生摄像机9参矩阵
            MatrixState.setCamera(0, 0, 3f,
                    0, 0, 0f,
                    0, 1.0f, 0f);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            //创建各个对象
            for (int i = 0; i < triangles.length; i++) {
                triangles[i] = new Triangle(MySurfaceView.this, -0.3f * i);
            }
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }


    }

}
