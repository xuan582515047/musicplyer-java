package cn.xyx.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import cn.xyx.media.UserManager;

public class LoginRegisterView extends Application {
    private UserManager userManager = new UserManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("用户登录/注册");
        Label userLabel = new Label("账号:");
        TextField userField = new TextField();
        Label passLabel = new Label("密码:");
        PasswordField passField = new PasswordField();
        Label infoLabel = new Label();
        Button loginBtn = new Button("登录");
        Button registerBtn = new Button("注册");
        HBox btnBox = new HBox(10, loginBtn, registerBtn);
        VBox vbox = new VBox(10, userLabel, userField, passLabel, passField, btnBox, infoLabel);
        vbox.setPadding(new Insets(20));

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            String encrypted = UserManager.encryptPassword(password);
            if (userManager.login(username, encrypted)) {
                infoLabel.setText("登录成功，正在进入主界面...");
                // 跳转到主界面
                primaryStage.close();
                MainApp.launchApp(new Stage());
            } else {
                infoLabel.setText("账号或密码错误");
            }
        });

        registerBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                infoLabel.setText("账号和密码不能为空");
                return;
            }
            String encrypted = UserManager.encryptPassword(password);
            if (userManager.register(username, encrypted)) {
                infoLabel.setText("注册成功，请登录");
            } else {
                infoLabel.setText("账号已存在");
            }
        });

        Scene scene = new Scene(vbox, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 启动入口
    public static void main(String[] args) {
        launch(args);
    }
}
