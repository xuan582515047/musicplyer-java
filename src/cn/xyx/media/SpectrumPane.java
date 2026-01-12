package cn.xyx.media;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class SpectrumPane extends StackPane {
    private Canvas canvas;
    private GraphicsContext gc;
    private double[] spectrumData;
    private int bandCount = 128; // 频带数量

    // 统一使用一种颜色 - 绿色渐变
    private Color primaryColor = Color.rgb(5, 237, 44); // 主颜色
    private Color highlightColor = Color.rgb(200, 255, 200); // 高光颜色

    public SpectrumPane(double width, double height) {
        this.setPrefSize(width, height);
        this.setMinSize(width, height);

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // 初始化频谱数据
        spectrumData = new double[bandCount];
        for (int i = 0; i < bandCount; i++) {
            spectrumData[i] = 0.0;
        }

        this.setBackground(javafx.scene.layout.Background.EMPTY);
        this.getChildren().add(canvas);
        this.setMouseTransparent(true);
    }

    // 根据音量模拟频谱，中间高两边低
    public void updateSpectrumWithVolume(double volume, boolean isPlaying) {
        if (!isPlaying) {
            // 暂停时频谱平滑衰减
            for (int i = 0; i < spectrumData.length; i++) {
                spectrumData[i] *= 0.85;
            }
        } else {
            // 计算中间高两边低的权重分布
            for (int i = 0; i < spectrumData.length; i++) {
                // 计算当前频带在总频带中的位置比例 (0-1)
                double position = (double)i / spectrumData.length;

                // 创建中间高两边低的权重分布（高斯分布）
                // 计算距离中心点的偏移量（-0.5到0.5）
                double offset = position - 0.5;

                // 高斯函数，中间高两边低
                double gaussianWeight = Math.exp(-offset * offset * 10); // 调整10可以控制曲线的宽度

                // 基于音量生成基础值，并应用高斯权重
                double baseValue = volume * 0.8;

                // 添加一些随机波动，使频谱更动态
                double randomFactor = volume * 0.4;
                double randomValue = Math.random() * randomFactor;

                // 中间频带权重高，两边权重低
                double weightedValue = (baseValue + randomValue) * gaussianWeight;

                // 平滑更新
                double targetValue = weightedValue;
                spectrumData[i] = spectrumData[i] * 0.7 + targetValue * 0.3;
            }
        }
        drawSpectrum();
    }

    private void drawSpectrum() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // 清除画布
        gc.clearRect(0, 0, width, height);

        // 计算每个频带的宽度
        double barWidth = width / spectrumData.length * 0.95;
        double spacing = width / spectrumData.length * 0.05;

        // 绘制频谱条 - 中间高两边低
        for (int i = 0; i < spectrumData.length; i++) {
            double x = i * (barWidth + spacing) + spacing/2;
            double barHeight = spectrumData[i] * height * 1.2; // 频谱高度

            // 统一使用绿色，但根据高度调整透明度
            double opacity = 0.3 + spectrumData[i] * 0.7; // 高度越高越不透明
            opacity = Math.min(0.9, opacity); // 限制最大透明度

            // 创建垂直渐变 - 单色渐变
            LinearGradient gradient = new LinearGradient(
                    0, 1, 0, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                    new Stop[] {
                            new Stop(0, Color.rgb(5, 237, 44, opacity * 0.8)), // 底部稍暗
                            new Stop(0.7, Color.rgb(5, 237, 44, opacity)),      // 中间
                            new Stop(1, Color.rgb(255, 255, 255, opacity * 0.5)) // 顶部高光
                    }
            );

            // 绘制频谱条
            gc.setFill(gradient);
            gc.fillRect(x, height - barHeight, barWidth, barHeight);

            // 添加顶部高光
            gc.setFill(Color.rgb(255, 255, 255, 0.6));
            gc.fillRect(x, height - barHeight, barWidth * 0.3, barHeight * 0.08);

            // 添加底部反射效果
            gc.setFill(Color.rgb(5, 237, 44, 0.15));
            gc.fillRect(x, height, barWidth, barHeight * 0.1);
        }

        // 添加中心线高光效果
        double centerLineHeight = 0;
        if (spectrumData.length > 0) {
            centerLineHeight = spectrumData[spectrumData.length / 2] * height * 1.2;
        }

        if (centerLineHeight > 5) {
            // 在中心位置添加一条高光线
            double centerX = width / 2;
            gc.setStroke(Color.rgb(255, 255, 255, 0.7));
            gc.setLineWidth(1);
            gc.strokeLine(centerX, height - centerLineHeight, centerX, height);
        }
    }

    // 清除频谱
    public void clear() {
        for (int i = 0; i < spectrumData.length; i++) {
            spectrumData[i] = 0.0;
        }
        drawSpectrum();
    }

    // 调整大小
    public void resizeSpectrum(double width, double height) {
        canvas.setWidth(width);
        canvas.setHeight(height);
        drawSpectrum();
    }
}