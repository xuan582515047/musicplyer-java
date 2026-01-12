package cn.xyx.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 对话框基类，提供通用的窗口功能
 * 包括：无边框窗口、拖拽功能、标题栏创建
 */
public abstract class BaseDialog {

    protected Stage stage;
    protected double xOffset = 0;
    protected double yOffset = 0;

    /**
     * 创建无边框对话框
     * @param owner 父窗口
     * @param title 窗口标题
     * @param width 宽度
     * @param height 高度
     */
    protected void createDialog(Stage owner, String title, double width, double height) {
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        if (owner != null) {
            stage.initOwner(owner);
        }

        VBox rootLayout = new VBox();

        // 创建标题栏
        HBox titleBar = TitleBarUtil.createTitleBar(stage, title);
        rootLayout.getChildren().add(titleBar);

        // 创建内容区域
        VBox contentArea = createContentArea();
        contentArea.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        contentArea.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        rootLayout.getChildren().add(contentArea);
        rootLayout.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene scene = new Scene(rootLayout, width, height);
        stage.setScene(scene);
    }

    /**
     * 子类实现此方法创建对话框内容
     * @return 内容区域的VBox
     */
    protected abstract VBox createContentArea();

    /**
     * 显示对话框
     */
    public void show() {
        if (stage != null) {
            stage.show();
        }
    }

    /**
     * 隐藏对话框
     */
    public void hide() {
        if (stage != null) {
            stage.hide();
        }
    }

    /**
     * 设置对话框标题
     */
    public void setTitle(String title) {
        if (stage != null) {
            stage.setTitle(title);
        }
    }
}
