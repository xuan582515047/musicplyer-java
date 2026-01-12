package cn.xyx.media;

import cn.xyx.ui.ButtonUtil;
import cn.xyx.ui.TitleBarUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 重构后的登录界面，使用统一的UI工具类
 * 消除了重复代码，提高了可维护性
 */
public class LoginViewRefactored {
    private UserManager userManager = new UserManager();
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("用户登录");

        VBox rootLayout = new VBox();

        // 使用工具类创建标题栏 - 消除重复代码
        HBox titleBar = TitleBarUtil.createTitleBar(primaryStage, "用户登录");
        rootLayout.getChildren().add(titleBar);

        // 创建内容区域
        VBox contentBox = createContentArea();
        rootLayout.getChildren().add(contentBox);

        rootLayout.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene scene = new Scene(rootLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createContentArea() {
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20, 40, 30, 40));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: #f6fff6;");

        // 标题
        Label titleLabel = new Label("用户登录");
        titleLabel.setFont(new Font("微软雅黑", 22));
        titleLabel.setTextFill(Color.web("#2e8b57"));

        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 0, 10, 0));

        // 账号输入
        Label userLabel = new Label("账号:");
        userLabel.setStyle("-fx-text-fill: #2e8b57; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("请输入账号");
        userField.setPrefHeight(35);

        // 密码输入
        Label passLabel = new Label("密码:");
        passLabel.setStyle("-fx-text-fill: #2e8b57; -fx-font-weight: bold;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("请输入密码");
        passField.setPrefHeight(35);

        // 信息提示
        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #d43f3a;");

        // 使用工具类创建按钮 - 消除重复代码
        Button loginBtn = ButtonUtil.createPrimaryButton("登录", 150, 40);

        HBox btnBox = new HBox(loginBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        contentBox.getChildren().addAll(titleLabel, sep, userLabel, userField, passLabel, passField, btnBox, infoLabel);

        // 登录按钮事件
        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            String encrypted = UserManager.encryptPassword(password);
            if (userManager.login(username, encrypted)) {
                infoLabel.setStyle("-fx-text-fill: #2e8b57;");
                infoLabel.setText("登录成功，正在进入主界面...");
                //primaryStage.close();
                MainApp.launchApp(new Stage());
            } else {
                infoLabel.setStyle("-fx-text-fill: #d43f3a;");
                infoLabel.setText("账号或密码错误");
            }
        });

        // 使内容区域可以拖动窗口
        contentBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        contentBox.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        return contentBox;
    }
}
