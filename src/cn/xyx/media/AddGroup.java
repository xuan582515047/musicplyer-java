package cn.xyx.media;

import cn.xyx.Utils.XMLUtils;
import cn.xyx.ui.BaseDialog;
import cn.xyx.ui.ButtonUtil;
import cn.xyx.ui.TitleBarUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

/**
 * 新建歌单对话框，使用重构后的基类和工具类
 */
public class AddGroup extends BaseDialog {
    private VBox groupVBox;//父窗体中显示歌单列表的VBox对象
    private MainApp mainApp;

    public AddGroup(Stage parentStage, VBox groupVBox, MainApp mainApp) {
        this.groupVBox = groupVBox;
        this.mainApp = mainApp;

        // 创建对话框
        createDialog(parentStage, "新建歌单", 300, 240);

        // 显示对话框
        show();
    }

    @Override
    protected VBox createContentArea() {
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20, 20, 20, 20));
        contentBox.setStyle("-fx-background-color: #f6fff6;");

        // 标题
        Label titleLabel = new Label("新建歌单");
        titleLabel.setFont(Font.font("黑体", 24));
        titleLabel.setTextFill(Color.web("#2e8b57"));

        // 文本框
        TextField textGroupName = new TextField();
        textGroupName.setPromptText("请输入歌单名称");
        textGroupName.setPrefWidth(220);
        textGroupName.setPrefHeight(35);

        // 提示标签
        Label labMsg = new Label();
        labMsg.setTextFill(Color.RED);
        labMsg.setPrefWidth(200);

        // 按钮区域
        Button butOk = ButtonUtil.createConfirmButton("确 认", 80, 30);
        Button butCancel = ButtonUtil.createCancelButton("取 消", 80, 30);

        HBox btnBox = new HBox(20, butOk, butCancel);
        btnBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(titleLabel, textGroupName, labMsg, btnBox);

        // 确认按钮事件
        butOk.setOnAction(e -> {
            String txt = textGroupName.getText().trim();
            // 判断
            if (txt.isEmpty()) {
                labMsg.setText("请输入歌单名称！");
                return;
            }
            // 验证新的歌单名是否重复
            List<String> groupNameList = XMLUtils.readAllGroup();
            for (String s : groupNameList) {
                if (txt.equals(s)) {
                    labMsg.setText(txt + " 歌单已经存在，请更换名称！");
                    return;
                }
            }
            // 写入
            XMLUtils.addGroup(txt);

            // 更新主窗口上的VBox列表
            addGroupToList(txt);

            // 关闭此舞台
            hide();
        });

        // 取消按钮事件
        butCancel.setOnAction(e -> hide());

        return contentBox;
    }

    /**
     * 将新建的歌单添加到主界面列表
     */
    private void addGroupToList(String groupName) {
        ImageView iv1 = new ImageView(new Image("img/left/xinyuanDark.png"));
        iv1.setFitWidth(15);
        iv1.setPreserveRatio(true);
        Label lab11 = new Label("", iv1);
        lab11.setMinWidth(0);
        lab11.setMinHeight(0);
        lab11.setPrefWidth(15);
        lab11.setPrefHeight(15);
        lab11.setOnMouseEntered(ee -> iv1.setImage(new Image("img/left/xinyuan.png")));
        lab11.setOnMouseExited(ee -> iv1.setImage(new Image("img/left/xinyuanDark.png")));

        // 2.歌单名称:Label
        Label labGroupName = new Label(groupName);
        labGroupName.setMinHeight(0);
        labGroupName.setPrefHeight(15);
        labGroupName.setPrefWidth(150);
        labGroupName.setTextFill(Color.rgb(255, 255, 255));
        labGroupName.setOnMouseEntered(ee -> labGroupName.setTextFill(Color.web("#2ef770")));
        labGroupName.setOnMouseExited(ee -> labGroupName.setTextFill(Color.rgb(255, 255, 255)));
        labGroupName.setOnMouseClicked(ee -> {
            // 1.设置歌单名称
            this.mainApp.setGroupName(groupName);
            this.mainApp.readAllSoundByGroupName();
        });

        // 3.播放图片
        ImageView iv2 = new ImageView(new Image("img/left/volume_1_Dark.png"));
        iv2.setFitWidth(15);
        iv2.setFitHeight(15);
        Label lab22 = new Label("", iv2);
        lab22.setMinWidth(0);
        lab22.setMinHeight(0);
        lab22.setPrefHeight(15);
        lab22.setPrefWidth(15);
        lab22.setOnMouseEntered(ee -> iv2.setImage(new Image("img/left/volume_1.png")));
        lab22.setOnMouseExited(ee -> iv2.setImage(new Image("img/left/volume_1_Dark.png")));
        lab22.setOnMouseClicked(ee -> {
            // 1.设置歌单名称
            this.mainApp.setGroupName(groupName);
            this.mainApp.readAllSoundByGroupName();
        });

        // 4."+"符号
        ImageView iv3 = new ImageView(new Image("img/left/addDark.png"));
        iv3.setFitWidth(15);
        iv3.setFitHeight(15);
        Label lab3 = new Label("", iv3);
        lab3.setMinWidth(0);
        lab3.setMinHeight(0);
        lab3.setPrefHeight(15);
        lab3.setPrefWidth(15);
        lab3.setOnMouseEntered(ee -> iv3.setImage(new Image("img/left/add.png")));
        lab3.setOnMouseExited(ee -> iv3.setImage(new Image("img/left/addDark.png")));
        lab3.setOnMouseClicked(ee -> {
            // 1.设置歌单名称为目标歌单
            this.mainApp.setGroupName(groupName);
            // 通过工具方法弹出文件选择器并添加歌曲
            XMLUtils.addSongsViaFileChooser(stage, groupName);
            // 立即刷新当前歌单歌曲
            this.mainApp.readAllSoundByGroupName();
        });

        // 5.垃圾桶符号
        ImageView iv4 = new ImageView(new Image("img/left/laji_1_Dark.png"));
        iv4.setFitWidth(15);
        iv4.setFitHeight(15);
        Label lab4 = new Label("", iv4);
        lab4.setMinWidth(0);
        lab4.setMinHeight(0);
        lab4.setPrefHeight(15);
        lab4.setPrefWidth(15);
        lab4.setOnMouseEntered(ee -> iv4.setImage(new Image("img/left/laji_1.png")));
        lab4.setOnMouseExited(ee -> iv4.setImage(new Image("img/left/laji_1_Dark.png")));

        HBox hBox1 = new HBox(7);
        hBox1.getChildren().addAll(lab11, labGroupName, lab22, lab3, lab4);
        hBox1.setPadding(new Insets(5, 5, 5, 10));

        this.groupVBox.getChildren().add(hBox1);

        // 删除功能
        lab4.setOnMouseClicked(e -> {
            // 1.提示
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认删除");
            alert.setHeaderText("你确定要删除歌单[" + groupName + "]吗？");
            java.util.Optional<javafx.scene.control.ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == javafx.scene.control.ButtonType.OK) {
                XMLUtils.deleteGroup(groupName);
                // 从VBox中删除
                this.groupVBox.getChildren().remove(hBox1);
            }
        });
    }
}

