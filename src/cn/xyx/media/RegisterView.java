package cn.xyx.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterView extends Application {
    private UserManager userManager = new UserManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("注册账号");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Label titleLabel = new Label("用户注册");
        titleLabel.setFont(new Font("微软雅黑", 22));
        titleLabel.setStyle("-fx-text-fill: #2e8b57;");
        Separator sep = new Separator();
        sep.setPadding(new Insets(5,0,10,0));
        Label userLabel = new Label("账号:");
        TextField userField = new TextField();
        userField.setPromptText("请输入账号");
        Label passLabel = new Label("密码:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("请输入密码");
        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #d43f3a;");
        Button registerBtn = new Button("注册");
        registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px;");
        HBox btnBox = new HBox(registerBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10,0,0,0));
        VBox vbox = new VBox(10, titleLabel, sep, userLabel, userField, passLabel, passField, btnBox, infoLabel);
        vbox.setPadding(new Insets(30, 40, 30, 40));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");
        Scene scene = new Scene(vbox, 350, 320);
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
}
