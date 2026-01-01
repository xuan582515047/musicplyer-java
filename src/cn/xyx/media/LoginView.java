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

public class LoginView extends Application {
    private UserManager userManager = new UserManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("用户登录");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Label titleLabel = new Label("用户登录");
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
        Button loginBtn = new Button("登录");
        loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        HBox btnBox = new HBox(loginBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10,0,0,0));
        VBox vbox = new VBox(10, titleLabel, sep, userLabel, userField, passLabel, passField, btnBox, infoLabel);
        vbox.setPadding(new Insets(30, 40, 30, 40));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");
        Scene scene = new Scene(vbox, 350, 320);
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
}
