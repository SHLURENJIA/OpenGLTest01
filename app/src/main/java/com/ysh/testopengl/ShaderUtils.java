package com.ysh.testopengl;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：create by @author{ YSH } on 2019/2/21
 * 描述:着色器辅助工具类
 * 修改备注:
 */
public class ShaderUtils {

    /**
     * 创建着色器程序
     *
     * @param vertexShader
     * @param fragmentShader
     * @return
     */
    public static int createProgram(String vertexShader, String fragmentShader) {
        //加载顶点着色器
        int vertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        if (vertexShaderId == 0) {
            return 0;
        }

        //加载片元着色器
        int fragShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        if (fragShaderId == 0) {
            return 0;
        }

        //创建着色器程序
        int program = GLES20.glCreateProgram();
        //在程序中加入顶点着色器和片元着色器
        if (program != 0) {
            //加入顶点着色器
            GLES20.glAttachShader(program, vertexShaderId);
            checkGlError("glAttachShader");
            //加入片元着色器
            GLES20.glAttachShader(program, fragShaderId);
            checkGlError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放成功的程序
            int[] linkStatus = new int[1];
            //获取program链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                //链接失败，删除程序
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                return 0;
            }
        }
        return program;
    }

    /**
     * 加载着色器，编译着色器
     *
     * @param type         着色器类型 GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
     * @param shaderString 着色器程序内容文本字符串
     * @return 着色器程序id
     */
    private static int loadShader(int type, String shaderString) {
        //创建着色器对象
        int shaderid = GLES20.glCreateShader(type);
        if (shaderid != 0) {//创建成功
            //加载着色器代码到着色器对象
            GLES20.glShaderSource(shaderid, shaderString);
            //编译着色器
            GLES20.glCompileShader(shaderid);
            //存放编译成功Shader数量数组
            int[] compileStatus = new int[1];
            //获取shader编译情况
            GLES20.glGetShaderiv(shaderid, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                //编译失败，显示日志并删除该对象
                Log.e("GLES20", "Could not compile shader " + type + ":");
                Log.e("GLES20", GLES20.glGetShaderInfoLog(shaderid));
                GLES20.glDeleteShader(shaderid);
                return 0;
            }
        }
        return shaderid;
    }


    /**
     * 从 assets 资源文件夹中读取着色器内容
     *
     * @param fname 着色器文件名称
     * @param r
     * @return 着色器内容
     */
    public static String loadFromAssetsFile(String fname, Resources r) {
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result.replaceAll("\\r\\n", "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检查每一步操作是否有错误方法
     *
     * @param op
     */
    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
