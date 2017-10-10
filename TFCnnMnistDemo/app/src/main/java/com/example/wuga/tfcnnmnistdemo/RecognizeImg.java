package com.example.wuga.tfcnnmnistdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by wuga on 2017/9/12.
 */

public class RecognizeImg {
    public static final String MODEL_FILE = "file:///android_asset/CNNModel.pb"; //asserts目录下的pb文件名字
    public static final String INPUT_NODE = "x";       //输入节点的名称
    public static final String OUTPUT_NODE = "predict_layer";  //输出节点的名称
    //public static final String KEEP_PROB_NODE = "keep_prob_placeholder"; // keep_prob节点的名称
    public static final int NUM_CLASSES = 10;   //输出节点的个数，即总的类别数。
    public static final int HEIGHT = 28;       //输入图片的像素高
    public static final int WIDTH = 28;        //输入图片的像素宽
    public TensorFlowInferenceInterface inferenceInterface;
    RecognizeImg(Context context) {
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(),MODEL_FILE);
    }
    public float[] recognize(Bitmap bitmap){
        //为输入节点准备数据
        float[] pixelArray = bitmapToFloatArray(bitmap);
        Log.i(TAG,"pixelArray:"+ Arrays.toString(pixelArray));
        if(pixelArray.length != HEIGHT*WIDTH){
            throw new IllegalArgumentException("输入图片的像素矩阵的大小不对，传入的大小为:"+pixelArray.length +",需要的大小为："+(HEIGHT*WIDTH));
        }

        // 输入数据
        inferenceInterface.feed(INPUT_NODE, pixelArray);

        //进行模型的推理
        inferenceInterface.run(new String[]{OUTPUT_NODE});

        //获取输出节点的输出信息
        float[] outputs = new float[NUM_CLASSES];    //用于存储模型的输出数据
        inferenceInterface.fetch(OUTPUT_NODE, outputs); //获取输出数据

        return outputs;
    }
    //用于上面的recognize函数将bitmap转换成数组
    public static float[] bitmapToFloatArray(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float[] result = new float[height*width];
        Log.i(TAG,"bitmap width:"+width+",height:"+height);
        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());

        int k = 0;

        //行优先
        for(int j = 0;j < height;j++){
            for (int i = 0;i < width;i++){
                int argb = bitmap.getPixel(i,j);

                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);

                //由于是灰度图，所以r,g,b分量是相等的。
                assert(r==g && g==b);

//                Log.i(TAG,i+","+j+" : argb = "+argb+", a="+a+", r="+r+", g="+g+", b="+b);
                result[k++] = r / 255.0f;
            }
        }

        return result;
    }
    public int argmax(float[] prob){
        int result = 0;
        for(int i=0;i<prob.length-1;i++) {
            if (prob[i] < prob[i+1])
                result = i+1;
            else
                result = i;
        }
        return result;
    }
}
