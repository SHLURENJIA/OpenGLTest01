package com.ysh.testopengl;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 作者：create by @author{ YSH } on 2019/2/21
 * 描述:三角形
 * 修改备注:
 */
public class Triangle {
    /**
     * 具体物体的3D变换矩阵，包括旋转平移缩放
     */
    final static float[] mMatrix = new float[16];
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * 4;
    /**
     * 着色器程序ID
     */
    int mProgramId;
    /**
     * 总变换矩阵引用
     */
    int mMVPMatrixHandle;
    /**
     * 顶点位置属性引用
     */
    int mVertexPositionHandle;
    /**
     * 顶点颜色属性引用
     */
    int mVertexColorHandle;
    /**
     * 顶点着色器代码
     */
    String mVertexShader;
    /**
     * 片元着色器代码
     */
    String mFragmentShader;
    /**
     * 该物体着色器缓冲
     */
    FloatBuffer mTriangleBuffer;

    int vCount = 0;
    /**
     * 绕y轴旋转
     */
    float yAngle = 0;
    /**
     * TODO 绕z轴旋转？
     */
    float xAngle = 0;

    public Triangle(MySurfaceView mv, float z) {
        //初始化顶点
        initVertexData(z);
//        initVertexData2(0.2f, 0.5f,z);
        //初始化着色器
        initShader(mv);
    }

    /**
     * 初始化顶点数据
     */
    private void initVertexData(float z) {
        float[] vertexArray = {
                // x, y, z, r,g,b,a
                0f, -0.5f, z, 1f, 0f, 0f, 0f,
                0.5f, 0.5f, z, 0f, 1f, 0f, 0f,
                -0.5f, 0.5f, z, 0f, 0f, 1f, 0f
        };
        //
        mTriangleBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4)//一个float四个字节
                .order(ByteOrder.nativeOrder())//设置字节顺序与操作系统一致
                .asFloatBuffer()
                .put(vertexArray);
        mTriangleBuffer.position(0);

    }


    /**
     * 初始化着色器
     *
     * @param mv
     */
    private void initShader(MySurfaceView mv) {
        //加载着色器内容
        mVertexShader = ShaderUtils.loadFromAssetsFile("vertex.glsl", mv.getResources());
        mFragmentShader = ShaderUtils.loadFromAssetsFile("frag.glsl", mv.getResources());
        //创建OpenGL程序
        mProgramId = ShaderUtils.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        mVertexPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        //获取程序中顶点颜色属性引用id
        mVertexColorHandle = GLES20.glGetAttribLocation(mProgramId, "aColor");
        //获取程序中总变换矩阵引用id
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "uMVPMatrix");
    }

    public void drawSelf() {
        //使用某套OpenGL程序
        GLES20.glUseProgram(mProgramId);
        //初始化变换矩阵
        Matrix.setRotateM(mMatrix, 0, 0, 0, 1, 0);
        //设置沿z轴正向移动1
        Matrix.translateM(mMatrix, 0, 0, 0, 1);
        //设置绕y轴旋转
        Matrix.rotateM(mMatrix, 0, yAngle, 0, 1, 0);
        //设置绕z轴旋转
        Matrix.rotateM(mMatrix, 0, xAngle, 1, 0, 0);
        //将最终变换矩阵传入OpenGl程序
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMatrix), 0);
        //为画笔指定顶点位置数据
        mTriangleBuffer.position(0);
        GLES20.glVertexAttribPointer(mVertexPositionHandle, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mVertexPositionHandle);
        mTriangleBuffer.position(0);
        //为画笔指定顶点着色数据
        mTriangleBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(mVertexColorHandle, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mVertexColorHandle);
        mTriangleBuffer.position(0);
        //绘制内容
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }


}
