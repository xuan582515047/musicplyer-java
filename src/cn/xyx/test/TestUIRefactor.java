package cn.xyx.test;

import cn.xyx.ui.ButtonUtil;
import cn.xyx.ui.TitleBarUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 测试重构后的UI工具类
 */
public class TestUIRefactor extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);

        // 测试标题栏工具类
        javafx.scene.layout.HBox titleBar = TitleBarUtil.createTitleBar(primaryStage, "测试窗口");

        // 测试按钮工具类
        Button btn1 = ButtonUtil.createPrimaryButton("主要按钮", 120, 35);
        Button btn2 = ButtonUtil.createCancelButton("取消按钮", 120, 35);

        btn1.setOnAction(e -> System.out.println("主要按钮被点击"));
        btn2.setOnAction(e -> System.out.println("取消按钮被点击"));

        VBox content = new VBox(15, btn1, btn2);
        content.setStyle("-fx-padding: 20;");

        root.getChildren().addAll(titleBar, content);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
