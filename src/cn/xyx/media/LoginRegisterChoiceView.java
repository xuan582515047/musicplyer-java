package cn.xyx.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginRegisterChoiceView extends Application {
    // 用于窗口拖动的变量
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("请选择操作");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // 创建顶部标题栏
        HBox titleBar = createTitleBar(primaryStage);

        Label titleLabel = new Label("欢迎使用音乐播放器");
        titleLabel.setFont(new Font("微软雅黑", 22));
        titleLabel.setStyle("-fx-text-fill: #2e8b57;");

        Separator sep = new Separator();
        sep.setPadding(new Insets(5,0,10,0));

        Button loginBtn = new Button("登录");
        Button registerBtn = new Button("注册账号");

        // 设置按钮样式
        loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        loginBtn.setPrefWidth(150);
        loginBtn.setPrefHeight(40);

        registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        registerBtn.setPrefWidth(150);
        registerBtn.setPrefHeight(40);

        // 鼠标悬停效果
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));

        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle("-fx-background-color: #3cb371; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;"));

        VBox contentBox = new VBox(20, titleLabel, sep, loginBtn, registerBtn);
        contentBox.setPadding(new Insets(20, 40, 30, 40));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: #f6fff6;");

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(titleBar, contentBox);

        // 设置VBox背景色
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

        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            primaryStage.close();
            LoginView loginView = new LoginView();
            Stage loginStage = new Stage();
            try {
                loginView.start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        registerBtn.setOnAction(e -> {
            primaryStage.close();
            RegisterView registerView = new RegisterView();
            Stage regStage = new Stage();
            try {
                registerView.start(regStage);
            } catch (Exception ex) {
                ex.printStackTrace();
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
        Label title = new Label("音乐播放器");
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

    public static void main(String[] args) {
        launch(args);
    }
}