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

public class LoginView extends Application {
    private UserManager userManager = new UserManager();
    // 用于窗口拖动的变量
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("用户登录");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // 创建顶部标题栏
        HBox titleBar = createTitleBar(primaryStage);

        Label titleLabel = new Label("用户登录");
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

        Button loginBtn = new Button("登录");
        loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        loginBtn.setPrefWidth(150);
        loginBtn.setPrefHeight(40);

        // 鼠标悬停效果
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));

        HBox btnBox = new HBox(loginBtn);
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

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            String encrypted = UserManager.encryptPassword(password);
            if (userManager.login(username, encrypted)) {
                infoLabel.setStyle("-fx-text-fill: #2e8b57;");
                infoLabel.setText("登录成功，正在进入主界面...");
                primaryStage.close();
                MainApp.launchApp(new Stage());
            } else {
                infoLabel.setStyle("-fx-text-fill: #d43f3a;");
                infoLabel.setText("账号或密码错误");
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
        Label title = new Label("用户登录");
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