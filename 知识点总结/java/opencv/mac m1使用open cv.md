1. 安装opencv（如果编译了源码就不用再安装了）

   ```
   brew install opencv
   ```

   安装完后opencv地址应该位于

   ```
   /opt/homebrew/Cellar/opencv/4.5.3_2
   ```

2. 根据以下博客，获取到项目github地址，拉取项目

   http://byan.xyz/2021/mac-opencv-java/

   https://github.com/baiyan0707/opencv_utils

3. 将项目resource下`libopencv_java451.dylib`复制到`java.library.path`

   获取路径

   ```
   System.out.println(System.getProperty("java.library.path"));
   ```

4. 根据以下博客，下载arm架构jdk，解决以下问题

   ```
   java mach-o, but wrong architecture
   ```

   https://blog.csdn.net/weixin_44225613/article/details/119302187

   https://www.azul.com/downloads/?package=jdk

   下载jdk后将项目jdk换成zuluJava（arm版本jdk）

5. 根据以下博客，更换链接库，解决以下问题

   ```
   Library not loaded … Reason: Image not loaded
   ```

   https://blog.csdn.net/dyx810601/article/details/79933785

   举例：

   ```
   install_name_tool -change  @rpath/libopencv_dnn.4.5.dylib  /opt/homebrew/Cellar/opencv/4.5.3_2/lib/libopencv_dnn.4.5.dylib /Users/jingling/Library/Java/Extensions/libopencv_java451.dylib
   ```

6. 测试代码

   ```
   package xyz.byan.opencv.util;
   
   import org.opencv.core.Core;
   import org.opencv.core.Mat;
   import org.opencv.imgcodecs.Imgcodecs;
   import org.opencv.objdetect.QRCodeDetector;
   
   public class QrCodeDetectUtil {
   
       static QRCodeDetector qrCodeDetector;
       static {
           try {
               System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
               qrCodeDetector = new QRCodeDetector();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
       public static String getQrCode(String fileName) {
           Mat image = Imgcodecs.imread(fileName);
           return qrCodeDetector.detectAndDecode(image);
       }
   
       public static void main(String[] args) throws Exception {
           System.out.println(getQrCode("/Users/jingling/Desktop/2.jpg"));
       }
   }
   ```

   