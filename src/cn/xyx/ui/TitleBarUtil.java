package cn.xyx.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * 标题栏工具类，提供统一的标题栏创建功能
 */
public class TitleBarUtil {

    // 缓存图片资源，避免重复创建
    private static final Image MINIMIZE_DARK_IMAGE = new Image("img/topandbottom/minimizeDark.png");
    private static final Image MINIMIZE_IMAGE = new Image("img/topandbottom/minimize.png");
    private static final Image CLOSE_DARK_IMAGE = new Image("img/topandbottom/closeDark.png");
    private static final Image CLOSE_IMAGE = new Image("img/topandbottom/close.png");

    /**
     * 创建标准标题栏
     * @param stage 所属窗口
     * @param title 标题文本
     * @return 标题栏HBox
     */
    public static HBox createTitleBar(Stage stage, String title) {
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #2e8b57; -fx-border-radius: 10 10 0 0;");
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setPrefHeight(40);

        // 左侧标题
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        // 占位区域，使按钮靠右
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 最小化按钮
        ImageView minimizeIcon = new ImageView(MINIMIZE_DARK_IMAGE);
        minimizeIcon.setFitWidth(15);
        minimizeIcon.setFitHeight(15);

        Label minimizeBtn = new Label("", minimizeIcon);
        minimizeBtn.setOnMouseEntered(e -> minimizeIcon.setImage(MINIMIZE_IMAGE));
        minimizeBtn.setOnMouseExited(e -> minimizeIcon.setImage(MINIMIZE_DARK_IMAGE));
        minimizeBtn.setOnMouseClicked(e -> stage.setIconified(true));

        // 关闭按钮
        ImageView closeIcon = new ImageView(CLOSE_DARK_IMAGE);
        closeIcon.setFitWidth(15);
        closeIcon.setFitHeight(15);

        Label closeBtn = new Label("", closeIcon);
        closeBtn.setOnMouseEntered(e -> closeIcon.setImage(CLOSE_IMAGE));
        closeBtn.setOnMouseExited(e -> closeIcon.setImage(CLOSE_DARK_IMAGE));
        closeBtn.setOnMouseClicked(e -> stage.close());

        // 将组件添加到标题栏
        titleBar.getChildren().addAll(titleLabel, spacer, minimizeBtn, closeBtn);

        // 使标题栏可以拖动窗口
        double[] xOffset = new double[1];
        double[] yOffset = new double[1];

        titleBar.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
        });

        return titleBar;
    }

    /**
     * 创建标题栏（无最小化按钮）
     */
    public static HBox createTitleBarWithoutMinimize(Stage stage, String title) {
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #2e8b57; -fx-border-radius: 10 10 0 0;");
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setPrefHeight(40);

        // 左侧标题
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        // 占位区域
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 关闭按钮
        ImageView closeIcon = new ImageView(CLOSE_DARK_IMAGE);
        closeIcon.setFitWidth(15);
        closeIcon.setFitHeight(15);

        Label closeBtn = new Label("", closeIcon);
        closeBtn.setOnMouseEntered(e -> closeIcon.setImage(CLOSE_IMAGE));
        closeBtn.setOnMouseExited(e -> closeIcon.setImage(CLOSE_DARK_IMAGE));
        closeBtn.setOnMouseClicked(e -> stage.close());

        titleBar.getChildren().addAll(titleLabel, spacer, closeBtn);

        return titleBar;
    }
}
