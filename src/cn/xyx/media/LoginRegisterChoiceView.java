package cn.xyx.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginRegisterChoiceView extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("请选择操作");
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Label titleLabel = new Label("欢迎使用音乐播放器");
        titleLabel.setFont(new Font("微软雅黑", 22));
        titleLabel.setStyle("-fx-text-fill: #2e8b57;");
        Separator sep = new Separator();
        sep.setPadding(new Insets(5,0,10,0));
        Button loginBtn = new Button("登录");
        Button registerBtn = new Button("注册账号");
        loginBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        registerBtn.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10;");
        VBox vbox = new VBox(20, titleLabel, sep, loginBtn, registerBtn);
        vbox.setPadding(new Insets(30, 40, 30, 40));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f6fff6; -fx-border-radius: 10; -fx-background-radius: 10;");
        Scene scene = new Scene(vbox, 350, 250);
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

    public static void main(String[] args) {
        launch(args);
    }
}
