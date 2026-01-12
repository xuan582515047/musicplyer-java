package cn.xyx.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterView extends Application {
    private UserManager userManager = new UserManager();
    // 用于窗口拖动的变量
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("注册账号");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // 创建顶部标题栏
        HBox titleBar = createTitleBar(primaryStage);

        Label titleLabel = new Label("用户注册");
        titleLabel.setFont(new Font("微软雅黑", 22));
        titleLabel.setStyle("-fx-text-fill: #2e8b57;");

        Separator sep = new Separator();
        sep.setPadding(new Insets(5,0,10,0));

        Label userLabel = new Label("账号:");
        userLabel.setStyle("-fx-text-fill: #2e8b57; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("请输入账号");
        userField.setPrefHeight(35);

        Label passLabel = new Label("密码:");
        passLabel.setStyle("-fx-text-fill: #2e8b57; -fx-font-weight: bold;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("请输入密码");
        passField.setPrefHeight(35);

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #d43f3a;");

        Button registerBtn = new Button("注册");
        registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px;");
        registerBtn.setPrefWidth(150);
        registerBtn.setPrefHeight(40);

        // 鼠标悬停效果
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle("-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px;"));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px;"));

        HBox btnBox = new HBox(registerBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10,0,0,0));

        VBox contentBox = new VBox(15, titleLabel, sep, userLabel, userField, passLabel, passField, btnBox, infoLabel);
        contentBox.setPadding(new Insets(20, 40, 30, 40));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: #f6fff6;");

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(titleBar, contentBox);
        mainLayout.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");

        // 使内容区域可以拖动窗口
        contentBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        contentBox.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(mainLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        registerBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                infoLabel.setText("账号和密码不能为空");
                return;
            }
            String encrypted = UserManager.encryptPassword(password);
            if (userManager.register(username, encrypted)) {
                infoLabel.setStyle("-fx-text-fill: #2e8b57;");
                infoLabel.setText("注册成功，正在返回登录界面...");
                // 自动跳转到登录界面
                primaryStage.close();
                LoginView loginView = new LoginView();
                Stage loginStage = new Stage();
                try {
                    loginView.start(loginStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                infoLabel.setStyle("-fx-text-fill: #d43f3a;");
                infoLabel.setText("账号已存在");
            }
        });
    }

    // 创建标题栏的方法
    private HBox createTitleBar(Stage stage) {
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #2e8b57; -fx-border-radius: 10 10 0 0;");
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setPrefHeight(40);

        // 左侧标题
        Label title = new Label("用户注册");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        // 占位区域，使按钮靠右
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 最小化按钮
        ImageView minimizeIcon = new ImageView(new Image("img/topandbottom/minimizeDark.png"));
        minimizeIcon.setFitWidth(15);
        minimizeIcon.setFitHeight(15);

        Label minimizeBtn = new Label("", minimizeIcon);
        minimizeBtn.setOnMouseEntered(e -> minimizeIcon.setImage(new Image("img/topandbottom/minimize.png")));
        minimizeBtn.setOnMouseExited(e -> minimizeIcon.setImage(new Image("img/topandbottom/minimizeDark.png")));
        minimizeBtn.setOnMouseClicked(e -> stage.setIconified(true));

        // 关闭按钮
        ImageView closeIcon = new ImageView(new Image("img/topandbottom/closeDark.png"));
        closeIcon.setFitWidth(15);
        closeIcon.setFitHeight(15);

        Label closeBtn = new Label("", closeIcon);
        closeBtn.setOnMouseEntered(e -> closeIcon.setImage(new Image("img/topandbottom/close.png")));
        closeBtn.setOnMouseExited(e -> closeIcon.setImage(new Image("img/topandbottom/closeDark.png")));
        closeBtn.setOnMouseClicked(e -> stage.close());

        // 将组件添加到标题栏
        titleBar.getChildren().addAll(title, spacer, minimizeBtn, closeBtn);

        // 使标题栏可以拖动窗口
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        return titleBar;
    }
}