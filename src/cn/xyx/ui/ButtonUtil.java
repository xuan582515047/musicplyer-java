package cn.xyx.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * 按钮样式工具类，提供统一的按钮样式
 */
public class ButtonUtil {

    private static final String NORMAL_STYLE = "-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;";
    private static final String HOVER_STYLE = "-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;";
    private static final String NORMAL_STYLE_NO_RADIUS = "-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px;";
    private static final String HOVER_STYLE_NO_RADIUS = "-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px;";

    /**
     * 创建标准登录/注册按钮
     */
    public static Button createPrimaryButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setStyle(NORMAL_STYLE);
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        // 鼠标悬停效果
        button.setOnMouseEntered(e -> button.setStyle(HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(NORMAL_STYLE));

        return button;
    }

    /**
     * 创建标准登录/注册按钮（无圆角）
     */
    public static Button createPrimaryButtonNoRadius(String text, double width, double height) {
        Button button = new Button(text);
        button.setStyle(NORMAL_STYLE_NO_RADIUS);
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        // 鼠标悬停效果
        button.setOnMouseEntered(e -> button.setStyle(HOVER_STYLE_NO_RADIUS));
        button.setOnMouseExited(e -> button.setStyle(NORMAL_STYLE_NO_RADIUS));

        return button;
    }

    /**
     * 创建确认按钮
     */
    public static Button createConfirmButton(String text, double width, double height) {
        return createPrimaryButton(text, width, height);
    }

    /**
     * 创建取消按钮
     */
    public static Button createCancelButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #333333; -fx-font-size: 15px; -fx-background-radius: 10;");
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #dddddd; -fx-text-fill: #333333; -fx-font-size: 15px; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #333333; -fx-font-size: 15px; -fx-background-radius: 10;"));

        return button;
    }

    /**
     * 为现有按钮设置标准样式
     */
    public static void setPrimaryButtonStyle(Button button, double width, double height) {
        button.setStyle(NORMAL_STYLE);
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        button.setOnMouseEntered(e -> button.setStyle(HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(NORMAL_STYLE));
    }
}
