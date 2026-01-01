package cn.xyx.media;

import cn.xyx.Utils.XMLUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class AddGroup {
    private Stage parentStage;//父窗体
    private VBox groupVBox;//父窗体中显示歌单列表的VBox对象
    private MainApp mainApp;

    private double mouseX;
    private double mouseY;

    private Stage stage;

    public AddGroup(Stage parentStage,VBox groupVBox, MainApp mainApp) {
        this.parentStage = parentStage;
        this.groupVBox = groupVBox;
        this.mainApp = mainApp;

        //新建歌单
        Label lab1 = new Label("新建歌单");
        lab1.setTextFill(Color.WHITE);
        lab1.setPrefWidth(150);
        lab1.setPrefHeight(50);
        lab1.setLayoutX(20);
        lab1.setLayoutY(0);
        lab1.setFont(Font.font(24));

        //2.关闭按钮
        ImageView v1 = new ImageView("img/topandbottom/closeDark.png");
        v1.setFitWidth(13);
        v1.setFitHeight(13);
        Label lab2 = new Label("",v1);
        lab2.setMinWidth(13);
        lab2.setMinHeight(13);
        lab2.setPrefWidth(13);
        lab2.setPrefHeight(13);
        lab2.setLayoutX(270);
        lab2.setLayoutY(10);
        lab2.setOnMouseClicked(e -> {
            stage.hide();
        });

        //文本框
        TextField textGroupName = new TextField();
        textGroupName.setPromptText("请输入歌单名称");
        textGroupName.setPrefWidth(220);
        textGroupName.setPrefHeight(15);
        textGroupName.setLayoutX(20);
        textGroupName.setLayoutY(70);

        //提示标签
        Label labMsg = new Label();
        labMsg.setTextFill(Color.RED);
        labMsg.setPrefWidth(200);
        labMsg.setLayoutX(20);
        labMsg.setLayoutY(100);

        //确认标签
        Button butok = new Button("确 认");
        butok.setPrefWidth(80);
        butok.setPrefHeight(30);
        butok.setLayoutX(50);
        butok.setLayoutY(160);
        butok.setBackground(new Background(new BackgroundFill(Color.rgb(200,200,200),null,null)));
        butok.setOnMouseClicked(e -> {
            String txt = textGroupName.getText().trim();
            //判断
            if(txt == null || txt.length() == 0){
                labMsg.setText("?hello? 为什么不写歌单名称？");
                return ;
            }
            //验证新的歌单名是否重复
            List<String> groupNameList = XMLUtils.readAllGroup();
            for(String s : groupNameList){
                if(txt.equals(s)){
                    labMsg.setText(txt+"歌单已经存在，请更换名称！");
                    return;
                }
            }
            //写入
            XMLUtils.addGroup(txt);

            //更新主窗口上的VBox列表
            ImageView iv1 = new ImageView("img/left/xinyuanDark.png");
            iv1.setFitWidth(15);
            iv1.setPreserveRatio(true);
            Label lab11 = new Label("",iv1);
            lab11.setMinWidth(0);
            lab11.setMinHeight(0);
            lab11.setPrefWidth(15);
            lab11.setPrefHeight(15);
            lab11.setOnMouseEntered(ee->iv1.setImage(new Image("img/left/xinyuan.png")));
            lab11.setOnMouseExited(ee->iv1.setImage(new Image("img/left/xinyuanDark.png")));


            //2.歌单名称:Label
            Label labGroupName = new Label(txt);
            labGroupName.setMinHeight(0);
            labGroupName.setPrefHeight(15);
            labGroupName.setPrefWidth(150);
            labGroupName.setTextFill(Color.rgb(0,0,0));
            labGroupName.setOnMouseEntered(ee->labGroupName.setTextFill(Color.DARKGREEN));
            labGroupName.setOnMouseExited(ee->labGroupName.setTextFill(Color.rgb(0,0,0)));

            //3.播放图片
            ImageView iv2 = new ImageView("img/left/volume_1_Dark.png");
            iv2.setFitWidth(15);
            iv2.setFitHeight(15);
            Label lab22 = new Label("",iv2);
            lab22.setMinWidth(0);
            lab22.setMinHeight(0);
            lab22.setPrefHeight(15);
            lab22.setPrefWidth(15);
            lab22.setOnMouseEntered(ee->iv2.setImage(new Image("img/left/volume_1.png")));
            lab22.setOnMouseExited(ee->iv2.setImage(new Image("img/left/volume_1_Dark.png")));

            //4."+"符号
            ImageView iv3 = new ImageView("img/left/addDark.png");
            iv3.setFitWidth(15);
            iv3.setFitHeight(15);
            Label lab3 = new Label("",iv3);
            lab3.setMinWidth(0);
            lab3.setMinHeight(0);
            lab3.setPrefHeight(15);
            lab3.setPrefWidth(15);
            lab3.setOnMouseEntered(ee->iv3.setImage(new Image("img/left/add.png")));
            lab3.setOnMouseExited(ee->iv3.setImage(new Image("img/left/addDark.png")));


            //5.垃圾桶符号
            ImageView iv4 = new ImageView("img/left/laji_1_Dark.png");
            iv4.setFitWidth(15);
            iv4.setFitHeight(15);
            Label lab4 = new Label("",iv4);
            lab4.setMinWidth(0);
            lab4.setMinHeight(0);
            lab4.setPrefHeight(15);
            lab4.setPrefWidth(15);
            lab4.setOnMouseEntered(ee->iv4.setImage(new Image("img/left/laji_1.png")));
            lab4.setOnMouseExited(ee->iv4.setImage(new Image("img/left/laji_1_Dark.png")));


            HBox hBox1 = new HBox(7);
            hBox1.getChildren().addAll(lab11,labGroupName,lab22,lab3,lab4);
            hBox1.setPadding(new Insets(5,5,5,10));
            this.groupVBox.getChildren().add(hBox1);

            //关闭此舞台
            stage.hide();
        });

        //取消按钮
        Button butcancel = new Button("取 消");
        butcancel.setPrefWidth(80);
        butcancel.setPrefHeight(30);
        butcancel.setLayoutX(150);
        butcancel.setLayoutY(160);
        butcancel.setTextFill(Color.WHITE);
        butcancel.setBackground(new Background(new BackgroundFill(Color.rgb(200,200,200),null,null)));
        butcancel.setOnMouseClicked(e -> {
            stage.hide();
        });


        //创建新舞台
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        //创建新对象
        Group group = new Group();
        group.getChildren().addAll(lab1,lab2,textGroupName,labMsg,butok,butcancel);
        Scene scene = new Scene(group,300,240);
        scene.setFill(Color.rgb(83,184,114));

        scene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        scene.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - mouseX);
            stage.setY(e.getScreenY() - mouseY);
        });

        //设置场景
        stage.setScene(scene);
        //显示舞台
        stage.show();
    }
}
