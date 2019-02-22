package com.ysh.testopengl;

import android.opengl.Matrix;

/**
 * 作者：create by @author{ YSH } on 2019/2/21
 * 描述: 存储系统矩阵状态类
 * 修改备注:
 */
public class MatrixState {

    /**
     * 投影矩阵
     */
    private static float[] mProjectMatrix = new float[16];
    /**
     * 摄像机位置朝向9参数矩阵
     */
    private static float[] mVMatrix = new float[16];
    /**
     * 最后起作用的总变换矩阵
     */
    private static float[] mMVPMatrix;

    /**
     * 设置摄像机
     *
     * @param cx  摄像机位置x
     * @param cy  摄像机位置y
     * @param cz  摄像机位置z
     * @param tx  摄像机目标点x
     * @param ty  摄像机目标点y
     * @param tz  摄像机目标点z
     * @param upx 摄像机UP向量X分量
     * @param upy 摄像机UP向量Y分量
     * @param upz 摄像机UP向量Z分量
     */
    public static void setCamera(
            float cx,
            float cy,
            float cz,
            float tx,
            float ty,
            float tz,
            float upx,
            float upy,
            float upz
    ) {
        Matrix.setLookAtM(
                mVMatrix,
                0,
                cx,
                cy,
                cz,
                tx,
                ty,
                tz,
                upx,
                upy,
                upz
        );
    }

    /**
     * 设置正交投影矩阵
     *
     * @param left   near面的left
     * @param right  near面的right
     * @param bottom near面的bottom
     * @param top    near面的top
     * @param near   near面距离
     * @param far    far面距离
     * @return
     */
    public static void setProjectOrtho(
            float left,
            float right,
            float bottom,
            float top,
            float near,
            float far
    ) {
        Matrix.orthoM(mProjectMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 获取具体物体的总变换矩阵
     *
     * @param spec TODO ??这是什么
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;//TODO 第三个参数矩阵是？？
    }

}
