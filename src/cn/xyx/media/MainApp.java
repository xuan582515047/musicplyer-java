package cn.xyx.media;

import cn.xyx.Utils.ImageUtils;
import cn.xyx.Utils.XMLUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;


import javafx.animation.KeyValue;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


import static javafx.util.Duration.millis;

public class MainApp extends Application {
    public static Stage staticStage;

    private double resetX;
    private double resetY;
    //3.最大化之前的宽度、高度
    private double resetWidth;
    private double resetHeight;

    private double mouseX;
    private double mouseY;

    private VBox groupVBox;//用于存放歌单列表的VBox对象

    private double xOffset;
    private double yOffset;

    private Label labGroupName;

    private int currentIndex;

    //歌单列表的TableView
    private TableView<PlayBean> tableView;

    //当前播放时间的前一秒
    private int prevSecond;
    //当前播放的playbean
    private PlayBean currentPlayBean;

    private Label labTotalTime;

    private ImageView panImageView;

    private Timeline timeline;

    private ImageView backImageView;

    private ImageView butplayImage;

    private Label labplay;


    private int playMode = 1;//1.列表循环 2.顺序播放 3.单曲循环

    private Slider sliderSong;

    private Label labplayTime;

    private Slider sldVolume;

    private ProgressBar volumeprogress;

    private double prevVolumn;

    private VBox lrcVBox;
    //当前歌词索引
    private int currentLrcIndex;

    private ArrayList<BigDecimal> lrcList = new ArrayList<>();

    //示波器
    private SpectrumPane spectrumPane;
    private Timeline spectrumTimeline;


    //总布局
    @Override
    public void start(Stage primaryStage) throws Exception {
        staticStage = primaryStage;

//        XMLUtils.setSongListRefreshCallback(() -> {
//            // 在JavaFX应用线程中刷新歌单列表
//            javafx.application.Platform.runLater(() -> refreshGroupList());
//        });

        //1.创建一个BorderPane对象
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getTopPane());
        borderPane.setLeft(getLeftPane());
        borderPane.setBottom(getBottonPane());
        borderPane.setCenter(getCenterPane());
        borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(26, 26, 26), null, null)));
        //2.创建场景对象
        Scene scene = new Scene(borderPane, 1210, 800);
        //3.设置场景
        primaryStage.setScene(scene);
        //4.去掉标题栏
        primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        primaryStage.setTitle("xyxMusicPlayer");
        primaryStage.show();

    }

    //获取中间面板
    private BorderPane getCenterPane() {
        //1.读取上一次关闭时播放的歌曲
        String[] playInfo = XMLUtils.readPrevPlayInfo();

        //2.歌单；标签
        Label lab1 = new Label("歌单");
        lab1.setTextFill(Color.WHITE);
        lab1.setBorder(new Border(new BorderStroke(
                Color.web("#0aa03c"),
                Color.web("#0aa03c"),
                Color.web("#0aa03c"),
                Color.web("#0aa03c"),
                BorderStrokeStyle.SOLID,
                BorderStrokeStyle.SOLID,
                BorderStrokeStyle.SOLID,
                BorderStrokeStyle.SOLID,
                new CornerRadii(4),
                new BorderWidths(1),
                new Insets(1, 2, 1, 2)
        )));
        lab1.setLayoutX(30);
        lab1.setLayoutY(10);
        lab1.setPrefWidth(50);
        lab1.setPrefHeight(25);
        lab1.setAlignment(Pos.CENTER);

        //歌单名称
        labGroupName = new Label(playInfo == null ? "(无记录）" : playInfo[0]);
        labGroupName.setLayoutX(90);
        labGroupName.setLayoutY(9);
        labGroupName.setTextFill(Color.web("#1ed760"));
        labGroupName.setFont(new Font("黑体", 18));
        labGroupName.setPrefWidth(200);
        labGroupName.setPrefHeight(25);
        labGroupName.setAlignment(Pos.CENTER_LEFT);


        //碟片的图片
        panImageView = new ImageView("img/center/pan_default.jpg");
        panImageView.setFitWidth(200);
        panImageView.setFitHeight(200);
        Label lab2 = new Label("", panImageView);
        lab2.setLayoutX(30);
        lab2.setLayoutY(60);

        Circle circle = new Circle();
        circle.setCenterX(100);
        circle.setCenterY(100);
        circle.setRadius(100);
        panImageView.setClip(circle);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), new KeyValue(panImageView.rotateProperty(), 0)),
                new KeyFrame(new Duration(10 * 1000), new KeyValue(panImageView.rotateProperty(), 360))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        //歌词的VBox容器
        lrcVBox = new VBox(15);
        lrcVBox.setPadding(new Insets(20, 20, 20, 20));
        lrcVBox.setLayoutX(250);
        lrcVBox.setLayoutY(0);


        //歌单列表标签
        Label lab3 = new Label("歌单列表");
        lab3.setPrefWidth(80);
        lab3.setPrefHeight(25);
        lab3.setTextFill(Color.WHITE);
        lab3.setAlignment(Pos.CENTER);
        lab3.setBackground(new Background(new BackgroundFill(
                LinearGradient.valueOf("linear-gradient(to bottom, #0aa03c, #0a8a35)"),
                new CornerRadii(4, 4, 0, 0, false),
                null
        )));
        lab3.setLayoutX(30);
        lab3.setLayoutY(272);

        //模糊背景
        Image image = new Image("img/topandbottom/yanzi-fengmian.png");
        //获取像素读取器
        PixelReader pr = image.getPixelReader();
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight()
        );
        //创建一个”像素写入器“
        PixelWriter pixelWriter = wImage.getPixelWriter();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = pr.getColor(i, j);
                for (int k = 0; k < 4; k++) {
                    color = color.darker();
                }
                pixelWriter.setColor(i, j, color);
            }
        }

        backImageView = new ImageView(wImage);
        backImageView.setFitWidth(300);
        backImageView.setFitHeight(300);
        backImageView.setLayoutX(0);
        backImageView.setLayoutY(0);

        //高斯模糊
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(63);

        backImageView.setEffect(gaussianBlur);

        Label labLine = new Label();
        labLine.setMinHeight(0);
        labLine.setPrefHeight(5);
        labLine.setBackground(new Background(new BackgroundFill(Color.web("#1db954"), null, null)));
        labLine.setLayoutX(0);
        labLine.setLayoutY(lab3.getLayoutY() + lab3.getPrefHeight());


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(30, 30, 30), null, null)));
        anchorPane.getChildren().addAll(backImageView, lab1, labGroupName, lab2, lrcVBox, lab3, labLine);


        //上侧的ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0, 0, 0, 0));
        scrollPane.setContent(anchorPane);
        scrollPane.setPrefHeight(300);
        scrollPane.setMouseTransparent(true);

        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        backImageView.fitWidthProperty().bind(scrollPane.widthProperty());
        backImageView.fitHeightProperty().bind(scrollPane.heightProperty());
        labLine.prefWidthProperty().bind(scrollPane.widthProperty());

        //<-----------------------下侧歌单列表------------------------>
        tableView = new TableView<>();
        tableView.setPrefWidth(960);
        tableView.getStylesheets().add("css/playTable.css");

        TableColumn c1 = new TableColumn("序号");
        c1.setPrefWidth(80);
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn c2 = new TableColumn("歌名");
        c2.setPrefWidth(300);
        c2.setCellValueFactory(new PropertyValueFactory<>("soundName"));

        TableColumn c3 = new TableColumn("歌手");
        c3.setPrefWidth(150);
        c3.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn c4 = new TableColumn("专辑");
        c4.setPrefWidth(150);
        c4.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn c5 = new TableColumn("大小");
        c5.setPrefWidth(100);
        c5.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn c6 = new TableColumn("时间");
        c6.setPrefWidth(100);
        c6.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn c7 = new TableColumn("操作");
        c7.setPrefWidth(80);
        c7.setCellValueFactory(new PropertyValueFactory<>("labDelete"));


        tableView.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7);

        tableView.setRowFactory(tv -> {
            TableRow<PlayBean> row = new TableRow<>();
            row.setOnMouseClicked((event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    //1.获取选中行的索引
                    this.currentIndex = row.getIndex();
                    //2.将前一秒置为0
                    this.prevSecond = 0;
                    if (this.currentPlayBean != null) {
                        this.currentPlayBean.getMediaPlayer().stop();
                    }

                    //4.获取当前的PlayBean
                    this.currentPlayBean = row.getItem();
                    //5.播放音乐
                    play();
                }
            }));
            return row;
        });

        //<--------------------------总的BorderPane------------------------>
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(scrollPane);
        borderPane.setCenter(tableView);
        c7.prefWidthProperty().bind(borderPane.widthProperty());
        return borderPane;
    }

    //获取下册面板
    private BorderPane getBottonPane() {
        //上一首
        ImageView v1 = new ImageView("img/topandbottom/lastDark.png");
        v1.setFitWidth(40);
        v1.setFitHeight(40);
        Label lab1 = new Label("", v1);
        lab1.setOnMouseEntered(e -> {
            v1.setImage(new Image("img/topandbottom/last.png"));
        });
        lab1.setOnMouseExited(e -> {
            v1.setImage(new Image("img/topandbottom/lastDark.png"));
        });
        lab1.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            this.timeline.stop();
            this.currentIndex--;
            if (this.currentIndex < 0) {
                if (this.playMode == 1) {  //列表循环
                    this.currentIndex = this.tableView.getItems().size() - 1;
                } else {
                    this.currentIndex = 0;
                }
            }
            //设置Table的选中
            this.tableView.getSelectionModel().select(currentIndex);
            //设置波荡PlayBean对象
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //开始播放
            play();
        });

        //播放按钮
        butplayImage = new ImageView("img/topandbottom/PlayDark.png");
        butplayImage.setFitWidth(40);
        butplayImage.setFitHeight(40);
        labplay = new Label("", butplayImage);
        labplay.setOnMouseEntered(e -> {
            butplayImage.setImage(new Image("img/topandbottom/Play.png"));
        });
        labplay.setOnMouseExited(e -> {
            butplayImage.setImage(new Image("img/topandbottom/PlayDark.png"));
        });
        labplay.setOnMouseClicked(e -> {
            //如果正在播放：暂停
            if (this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                this.currentPlayBean.getMediaPlayer().pause();

                //设置播放按钮图标
                butplayImage.setImage(new Image("img/topandbottom/PlayDark.png"));
                labplay.setOnMouseEntered(ee -> {
                    butplayImage.setImage(new Image("img/topandbottom/Play.png"));
                });
                labplay.setOnMouseExited(ee -> {
                    butplayImage.setImage(new Image("img/topandbottom/PlayDark.png"));
                });

                if (spectrumPane != null) {
                    spectrumPane.updateSpectrumWithVolume(0, false);
                }
                this.timeline.pause();
            } else if (this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PAUSED) {
                this.currentPlayBean.getMediaPlayer().play();
                this.timeline.play();
                butplayImage.setImage(new Image("img/topandbottom/PauseDark.png"));
                labplay.setOnMouseEntered(ee -> {
                    butplayImage.setImage(new Image("img/topandbottom/Pause.png"));
                });
                labplay.setOnMouseExited(ee -> {
                    butplayImage.setImage(new Image("img/topandbottom/PauseDark.png"));
                });

                if (spectrumTimeline != null) {
                    spectrumTimeline.play();
                }
            }
        });

        //下一首
        ImageView v3 = new ImageView("img/topandbottom/nextDark.png");
        v3.setFitWidth(40);
        v3.setFitHeight(40);
        Label lab3 = new Label("", v3);
        lab3.setOnMouseEntered(e -> {
            v3.setImage(new Image("img/topandbottom/next.png"));
        });
        lab3.setOnMouseExited(e -> {
            v3.setImage(new Image("img/topandbottom/nextDark.png"));
        });

        lab3.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            this.timeline.stop();
            this.currentIndex++;
            if (this.currentIndex >= this.tableView.getItems().size()) {
                if (this.playMode == 1) {  //列表循环
                    this.currentIndex = 0;
                } else {
                    this.currentIndex = this.tableView.getItems().size() - 1;
                }
            }
            //设置Table的选中
            this.tableView.getSelectionModel().select(currentIndex);
            //设置波荡PlayBean对象
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //开始播放
            play();
        });

        HBox hBox1 = new HBox(40);
        hBox1.setPrefWidth(255);
        hBox1.setPadding(new Insets(10, 10, 10, 30));
        hBox1.getChildren().addAll(lab1, labplay, lab3);

        //<--------------------中间滚动条-------------------->
        labplayTime = new Label("00:00");
        labplayTime.setPrefWidth(40);
        labplayTime.setPrefHeight(78);
        labplayTime.setTextFill(Color.web("#1ed760"));


        //滚动条
        sliderSong = new Slider();
        sliderSong.setMinWidth(0);
        sliderSong.setMinHeight(0);
        sliderSong.setPrefHeight(12);
        sliderSong.setPrefWidth(500);
        sliderSong.getStylesheets().add("css/TopAndBottomPage.css");


        //进度条
        ProgressBar pb1 = new ProgressBar(0);
        pb1.setProgress(0);
        pb1.setMinHeight(0);
        pb1.setPrefHeight(10);
        pb1.setMaxWidth(5000);
        pb1.setPrefWidth(300);
        pb1.getStylesheets().add("css/TopAndBottomPage.css");

        // 创建频谱可视化面板
        spectrumPane = new SpectrumPane(500, 60); // 或者使用 BellCurveSpectrumPane

        // 创建频谱更新动画
        spectrumTimeline = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            if (currentPlayBean != null && currentPlayBean.getMediaPlayer() != null) {
                boolean isPlaying = currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING;
                double volume = currentPlayBean.getMediaPlayer().getVolume();

                // 获取播放进度
                double progress = 0;
                if (currentPlayBean.getMediaPlayer().getTotalDuration() != null) {
                    progress = currentPlayBean.getMediaPlayer().getCurrentTime().toSeconds() /
                            currentPlayBean.getMediaPlayer().getTotalDuration().toSeconds();
                }

                // 添加节奏感
                double rhythm = Math.sin(progress * Math.PI * 4) * 0.2 + 1.0;
                spectrumPane.updateSpectrumWithVolume(volume * rhythm, isPlaying);
            } else {
                spectrumPane.updateSpectrumWithVolume(0, false);
            }
        }));
        spectrumTimeline.setCycleCount(Timeline.INDEFINITE);
        spectrumTimeline.play();


        //sliderSong值发生变化时
        sliderSong.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //设置进度条
                if (currentPlayBean != null) {
                    pb1.setProgress((newValue.doubleValue() + 1) / currentPlayBean.getTotalSeconds());
                }
            }
        });

        //Slider的鼠标抬起(进度条调整播放进度)
        sliderSong.setOnMouseReleased(e -> {
            if (currentPlayBean != null) {
                Duration duration = new Duration(sliderSong.getValue() * 1000);
                currentPlayBean.getMediaPlayer().seek(duration);

                //同时设置Label
                Date date = new Date();
                date.setTime((long) currentPlayBean.getMediaPlayer().getCurrentTime().toMillis());
                labplayTime.setText(new SimpleDateFormat("mm:ss").format(date));
                //设置前一秒
                prevSecond = (int) duration.toSeconds() - 1;
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.setPrefHeight(70); // 增加高度以容纳频谱

        // 调整各组件的位置和大小
        pb1.setMaxHeight(8);
        pb1.setMinHeight(8);

        // 频谱面板定位在进度条上方
        spectrumPane.setTranslateY(-25); // 向上偏移

        stackPane.getChildren().addAll(spectrumPane, pb1, sliderSong);

        //总时间标签
        labTotalTime = new Label("00:00");
        labTotalTime.setPrefWidth(40);
        labTotalTime.setPrefHeight(78);
        labTotalTime.setTextFill(Color.web("#1ed760"));
        labTotalTime.setAlignment(Pos.CENTER_RIGHT);


        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(labplayTime);
        borderPane.setRight(labTotalTime);
        borderPane.setCenter(stackPane);

        labplayTime.prefHeightProperty().bind(borderPane.heightProperty());
        sliderSong.prefHeightProperty().bind(borderPane.heightProperty());
        labTotalTime.prefHeightProperty().bind(borderPane.heightProperty());

        //<--------------------右侧音量控制-------------------->
        ImageView v5 = new ImageView("img/topandbottom/volumeDark.png");
        v5.setFitWidth(17);
        v5.setFitHeight(17);
        Label lab5 = new Label("", v5);
        lab5.setOnMouseEntered(e -> {
            v5.setImage(new Image("img/topandbottom/volume.png"));
        });
        lab5.setOnMouseExited(e -> {
            v5.setImage(new Image("img/topandbottom/volumeDark.png"));
        });
        lab5.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                if (this.currentPlayBean.getMediaPlayer().getVolume() != 0) {
                    this.prevVolumn = this.currentPlayBean.getMediaPlayer().getVolume();
                    //设置为静音
                    this.currentPlayBean.getMediaPlayer().setVolume(0);
                    //设置图片
                    v5.setImage(new Image("img/left/volumeZero_1_Dark.png"));
                    lab5.setOnMouseEntered(ee -> {
                        v5.setImage(new Image("img/left/volueZero_1.png"));
                    });
                    lab5.setOnMouseExited(ee -> {
                        v5.setImage(new Image("img/left/volumeZero_1_Dark.png"));
                    });
                    //设置音量滚动条
                    this.sldVolume.setValue(0);
                } else {
                    this.currentPlayBean.getMediaPlayer().setVolume(this.prevVolumn);

                    v5.setImage(new Image("img/left/volume_1_Dark.png"));
                    lab5.setOnMouseEntered(ee -> {
                        v5.setImage(new Image("img/left/volume_1.png"));
                    });
                    lab5.setOnMouseExited(ee -> {
                        v5.setImage(new Image("img/left/volume_1_Dark.png"));
                    });
                    this.sldVolume.setValue(this.prevVolumn * 100);
                }
            }
        });
        //音量滚动条
        sldVolume = new Slider();
        sldVolume.setMax(100);
        sldVolume.setValue(50);
        sldVolume.setMajorTickUnit(1);

        sldVolume.setMinorTickCount(0);
        sldVolume.setPrefHeight(10);
        sldVolume.setPrefWidth(100);
        sldVolume.getStylesheets().add("css/TopAndBottomPage.css");


        //进度条
        volumeprogress = new ProgressBar(0);
        volumeprogress.setMinHeight(0);
        volumeprogress.setProgress(0.5);
        volumeprogress.setPrefWidth(100);
        volumeprogress.setPrefHeight(10);

        volumeprogress.prefWidthProperty().bind(sldVolume.widthProperty());
        volumeprogress.getStylesheets().add("css/TopAndBottomPage.css");

        //监听音量进度条的鼠标调整

        sldVolume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volumeprogress.setProgress(sldVolume.getValue() / 100);

                if (currentPlayBean != null) {
                    currentPlayBean.getMediaPlayer().setVolume(volumeprogress.getProgress());
                }
            }
        });
        StackPane sp2 = new StackPane();
        sp2.getChildren().addAll(volumeprogress, sldVolume);

        //3.播放模式
        ImageView v6 = new ImageView("img/topandbottom/repeatDark.png");
        v6.setFitWidth(25);
        v6.setFitHeight(25);
        Label lab6 = new Label("", v6);
        lab6.setOnMouseClicked(e -> {
            this.playMode++;
            if (this.playMode > 3) {
                this.playMode = 1;
            }
            switch (this.playMode) {
                case 1:
                    v6.setImage(new Image("img/topandbottom/repeatDark.png"));
                    lab6.setOnMouseEntered(ee -> {
                        v6.setImage(new Image("img/topandbottom/repeat.png"));
                    });
                    lab6.setOnMouseExited(ee -> {
                        v6.setImage(new Image("img/topandbottom/repeatDark.png"));
                    });
                    break;
                case 2:
                    v6.setImage(new Image("img/topandbottom/orderPlayDark.png"));
                    lab6.setOnMouseEntered(ee -> {
                        v6.setImage(new Image("img/topandbottom/orderPlay.png"));
                    });
                    lab6.setOnMouseExited(ee -> {
                        v6.setImage(new Image("img/topandbottom/orderPlayDark.png"));
                    });
                    break;
                case 3:
                    v6.setImage(new Image("img/topandbottom/repeatInOneDark.png"));
                    lab6.setOnMouseEntered(ee -> {
                        v6.setImage(new Image("img/topandbottom/repeatInOne.png"));
                    });
                    lab6.setOnMouseExited(ee -> {
                        v6.setImage(new Image("img/topandbottom/repeatInOneDark.png"));
                    });
                    break;
            }
        });

        //歌词图片
        ImageView v7 = new ImageView("img/topandbottom/ciDark.png");
        v7.setFitWidth(25);
        v7.setFitHeight(25);
        Label lab7 = new Label("", v7);

        //拖拽图片
        ImageView v8 = new ImageView("img/topandbottom/right_drag.png");
        v8.setFitWidth(40);
        v8.setFitWidth(40);
        Label lab8 = new Label("", v8);

        lab8.setOnMousePressed(e -> {
            xOffset = e.getScreenX();
            yOffset = e.getScreenY();
        });
        //鼠标移动时
        lab8.setOnMouseMoved(e -> {
            if (e.getY() > 60 && e.getY() < 90 && e.getX() > 10 && e.getX() < 40) {
                //改变鼠标形状
                lab8.setCursor(Cursor.NW_RESIZE);
            } else {
                lab8.setCursor(Cursor.DEFAULT);
            }
        });
        lab8.setOnMouseDragged(e -> {
            if (staticStage.getWidth() + (e.getScreenX() - xOffset) >= 1200) {
                staticStage.setWidth(staticStage.getWidth() + (e.getScreenX() - xOffset));
                xOffset = e.getScreenX();
            }
            if (staticStage.getHeight() + (e.getScreenY() - yOffset) >= 800) {
                staticStage.setHeight(staticStage.getHeight() + (e.getScreenY() - yOffset));
                yOffset = e.getScreenY();
            }
        });
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(0, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(lab5, sp2, lab6, lab7, lab8);


        //总的BOrderPane
        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(hBox1);
        bottomPane.setCenter(borderPane);
        bottomPane.setRight(hBox);
        return bottomPane;

    }

    //获取左册面板
    private BorderPane getLeftPane() {
        //作者图片
        ImageView v1 = new ImageView("img/topandbottom/logo2.0-as.jpg");
        v1.setFitHeight(225);
        v1.setPreserveRatio(true);

        //2.作者：Label
        Label labAuthor = new Label("Author:xyx");
        labAuthor.setPrefWidth(255);
        labAuthor.setTextFill(Color.web("#1ed760"));//设置颜色
        labAuthor.setFont(new javafx.scene.text.Font("黑体", 18));//设置字体
        labAuthor.setAlignment(Pos.CENTER);

        //3.日期:Label
        Label labDate = new Label("日期：2099-01-10");
        labDate.setPrefWidth(255);
        labDate.setTextFill(Color.web("#cccccc"));
        labDate.setFont(new Font("黑体", 18));
        labDate.setAlignment(Pos.CENTER);


        Label labGd = new Label("已创建的歌单");
        labGd.setPrefWidth(220);
        labGd.setPrefHeight(20);
        labGd.setTextFill(Color.rgb(255, 255, 255));

        ImageView v2 = new ImageView("img/left/create_2_Dark.png");
        v2.setFitWidth(15);
        v2.setPreserveRatio(true);

        Label lab = new Label("", v2);
        lab.setPrefWidth(15);
        lab.setPrefHeight(15);
        lab.setOnMouseEntered(e -> v2.setImage(new Image("img/left/create_2.png")));
        lab.setOnMouseExited(e -> v2.setImage(new Image("img/left/create_2_Dark.png")));
        lab.setOnMouseClicked(e -> {
            new AddGroup(staticStage, groupVBox, this);
        });

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(labGd, lab);

        VBox vBox = new VBox(15);
        vBox.setPrefWidth(255);
        vBox.setPrefHeight(300);
        vBox.setPadding(new Insets(5, 5, 5, 10));
        vBox.getChildren().addAll(v1, labAuthor, labDate, hBox);

        //<-----------------读取所有已创建的歌单----------------->
        List<String> groupList = XMLUtils.readAllGroup();
        //将每个歌单名封装
        List<HBox> hBoxList = new ArrayList<>();
        for (String groupName : groupList) {
            ImageView iv1 = new ImageView("img/left/xinyuanDark.png");
            iv1.setFitWidth(15);
            iv1.setPreserveRatio(true);
            Label lab1 = new Label("", iv1);
            lab1.setMinWidth(0);
            lab1.setMinHeight(0);
            lab1.setPrefWidth(15);
            lab1.setPrefHeight(15);
            lab1.setOnMouseEntered(e -> iv1.setImage(new Image("img/left/xinyuan.png")));
            lab1.setOnMouseExited(e -> iv1.setImage(new Image("img/left/xinyuanDark.png")));


            //2.歌单名称:Label
            Label labGroupName = new Label(groupName);
            labGroupName.setMinHeight(0);
            labGroupName.setPrefHeight(15);
            labGroupName.setPrefWidth(150);
            labGroupName.setTextFill(Color.web("#e0e0e0"));
            labGroupName.setOnMouseEntered(e -> {
                labGroupName.setTextFill(Color.web("#2ef770"));
                labGroupName.setCursor(Cursor.HAND);
            });
            labGroupName.setOnMouseExited(e -> {
                labGroupName.setTextFill(Color.rgb(255, 255, 255));
                labGroupName.setCursor(Cursor.DEFAULT);
            });
            labGroupName.setOnMouseClicked(e -> {
                //1.设置歌单名称
                this.labGroupName.setText(labGroupName.getText().trim());
                readAllSoundByGroupName();
            });

            //3.播放图片
            ImageView iv2 = new ImageView("img/left/volume_1_Dark.png");
            iv2.setFitWidth(15);
            iv2.setFitHeight(15);
            Label lab2 = new Label("", iv2);
            lab2.setMinWidth(0);
            lab2.setMinHeight(0);
            lab2.setPrefHeight(15);
            lab2.setPrefWidth(15);
            lab2.setOnMouseEntered(e -> iv2.setImage(new Image("img/left/volume_1.png")));
            lab2.setOnMouseExited(e -> iv2.setImage(new Image("img/left/volume_1_Dark.png")));
            lab2.setOnMouseClicked(e -> {
                //1.设置歌单名称
                this.labGroupName.setText(labGroupName.getText().trim());
                readAllSoundByGroupName();
            });

            //4."+"符号
            ImageView iv3 = new ImageView("img/left/addDark.png");
            iv3.setFitWidth(15);
            iv3.setFitHeight(15);
            Label lab3 = new Label("", iv3);
            lab3.setMinWidth(0);
            lab3.setMinHeight(0);
            lab3.setPrefHeight(15);
            lab3.setPrefWidth(15);
            lab3.setOnMouseEntered(e -> iv3.setImage(new Image("img/left/add.png")));
            lab3.setOnMouseExited(e -> iv3.setImage(new Image("img/left/addDark.png")));
            lab3.setOnMouseClicked(e -> {
                // 先更新当前显示的歌单名称为目标歌单
                this.labGroupName.setText(labGroupName.getText().trim());
                // 通过工具方法弹出文件选择器并添加歌曲
                XMLUtils.addSongsViaFileChooser(staticStage, labGroupName.getText().trim());
                // 立即刷新当前歌单歌曲
                readAllSoundByGroupName();
            });


            //5.垃圾桶符号
            ImageView iv4 = new ImageView("img/left/laji_1_Dark.png");
            iv4.setFitWidth(15);
            iv4.setFitHeight(15);
            Label lab4 = new Label("", iv4);
            lab4.setMinWidth(0);
            lab4.setMinHeight(0);
            lab4.setPrefHeight(15);
            lab4.setPrefWidth(15);
            lab4.setOnMouseEntered(e -> iv4.setImage(new Image("img/left/laji_1.png")));
            lab4.setOnMouseExited(e -> iv4.setImage(new Image("img/left/laji_1_Dark.png")));

            HBox hBox1 = new HBox(7);
            hBox1.getChildren().addAll(lab1, labGroupName, lab2, lab3, lab4);
            hBox1.setPadding(new Insets(5, 5, 5, 10));

            hBoxList.add(hBox1);

            lab4.setOnMouseClicked(e -> {
                //1.提示
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("确认删除");
                alert.setHeaderText("你确定要删除歌单[" + labGroupName.getText().trim() + "]吗？");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    XMLUtils.deleteGroup(labGroupName.getText().trim());
                    //从VBox中删除
                    this.groupVBox.getChildren().remove(hBox1);
                }
            });
        }
        groupVBox = new VBox(10);
        groupVBox.setPrefWidth(255);
        groupVBox.setPadding(new Insets(10, 0, 0, 15));
        for (HBox hb : hBoxList) {
            groupVBox.getChildren().add(hb);
        }

        //总面板
        BorderPane leftPane = new BorderPane();
        leftPane.setTop(vBox);
        leftPane.setCenter(groupVBox);
        return leftPane;
    }

    //获取上侧面板
    private BorderPane getTopPane() {

        //1.左侧的图片
        ImageView imgeView = new ImageView("img/left/musicplyer.png");
        imgeView.setFitHeight(50);
        imgeView.setPreserveRatio(true);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefWidth(600);
        hbox.setPrefHeight(50);
        hbox.setMaxHeight(50);
        hbox.setPadding(new Insets(10, 0, 10, 40));
        hbox.getChildren().add(imgeView);

        //2.右侧最小化按钮
        ImageView v1 = new ImageView("img/topandbottom/minimizeDark.png");
        v1.setFitWidth(13);
        v1.setFitHeight(13);
        javafx.scene.control.Label lab1 = new Label("", v1);
        lab1.setMinWidth(0);
        lab1.setMinHeight(0);
        lab1.setPrefWidth(15);
        lab1.setPrefHeight(15);

        //鼠标移入移出效果
        lab1.setOnMouseEntered(e -> v1.setImage(new Image("img/topandbottom/minimize.png")));
        ;
        lab1.setOnMouseExited(e -> v1.setImage(new Image("img/topandbottom/minimizeDark.png")));

        lab1.setOnMouseClicked(e -> staticStage.setIconified(true));

        //3.右侧最大化按钮
        ImageView v2 = new ImageView("img/topandbottom/maximizeDark.png");
        v2.setFitWidth(15);
        v2.setFitHeight(15);
        javafx.scene.control.Label lab2 = new Label("", v2);
        lab2.setMinWidth(0);
        lab2.setMinHeight(0);
        lab2.setPrefWidth(15);
        lab2.setPrefHeight(15);
        lab2.setOnMouseEntered(e -> v2.setImage(new Image("img/topandbottom/maximize.png")));
        ;
        lab2.setOnMouseExited(e -> v2.setImage(new Image("img/topandbottom/maximizeDark.png")));


        lab2.setOnMouseClicked(e -> {
            if (!staticStage.isMaximized()) {
                resetX = staticStage.getX();
                resetY = staticStage.getY();
                resetWidth = staticStage.getWidth();
                resetHeight = staticStage.getHeight();
                staticStage.setMaximized(true);

                v2.setImage(new Image("img/topandbottom/resetDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("img/topandbottom/reset.png")));
                ;
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("img/topandbottom/resetDark.png")));
            } else {
                staticStage.setX(resetX);
                staticStage.setY(resetY);
                staticStage.setWidth(resetWidth);
                staticStage.setHeight(resetHeight);
                staticStage.setMaximized(false);

                v2.setImage(new Image("img/topandbottom/MaximizeDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("img/topandbottom/Maximize.png")));
                ;
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("img/topandbottom/MaximizeDark.png")));
            }
        });
        //4.关闭按钮
        ImageView v3 = new ImageView("img/topandbottom/closeDark.png");
        v3.setFitWidth(15);
        v3.setFitHeight(15);
        javafx.scene.control.Label lab3 = new Label("", v3);
        lab3.setMinWidth(15);
        lab3.setMinHeight(15);
        lab3.setPrefWidth(15);
        lab3.setPrefHeight(15);
        lab3.setOnMouseEntered(e -> v3.setImage(new Image("img/topandbottom/close.png")));
        ;
        lab3.setOnMouseExited(e -> v3.setImage(new Image("img/topandbottom/closeDark.png")));

        lab3.setOnMouseClicked(e -> {
            System.exit(0);
        });

        HBox hBox2 = new HBox(15);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPrefWidth(150);
        hBox2.setPrefHeight(50);
        hBox2.getChildren().addAll(lab1, lab2, lab3);
        hBox2.setPadding(new Insets(10, 0, 0, 50));


        javafx.scene.shape.Rectangle rct = new Rectangle();
        rct.setX(0);
        rct.setY(0);
        rct.setWidth(100);
        rct.setHeight(2);
        //设置背景色--渐变
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#0a8a35")),
                new Stop(0.5, Color.web("#1ed760")),
                new Stop(1, Color.web("#0a8a35"))
        };
        rct.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops));


        BorderPane topPane = new BorderPane();
        topPane.setLeft(hbox);
        topPane.setRight(hBox2);
        topPane.setBottom(rct);

        rct.widthProperty().bind(staticStage.widthProperty());

        //鼠标按下时
        topPane.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        //鼠标拖拽时
        topPane.setOnMouseDragged(e -> {
            staticStage.setX(e.getScreenX() - mouseX);
            staticStage.setY(e.getScreenY() - mouseY);
        });
        return topPane;
    }

    //读取某个歌单的所有歌曲（供内部调用）
    private void readAllSoundByGroupNameInternal() {
        //1.读取此歌单下所有的歌曲
        List<SoundBean> soundList = XMLUtils.findSoundByGroupName(this.labGroupName.getText().trim());
        //2.将每个歌曲信息封装到PlayBean对象中
        List<PlayBean> playBeanList = new ArrayList<>();
        for (int i = 0; i < soundList.size(); i++) {
            SoundBean soundBean = soundList.get(i);
            PlayBean playBean = new PlayBean();
            playBean.setId(i + 1);

            //读取音频文件
            File file = new File(soundBean.getfilePath());
            //解析文件
            MP3File mp3File = null;
            try {
                mp3File = new MP3File(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }
            //获取mp3文件的头信息

            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            //获取字符串形式的时长
            String strLength = audioHeader.getTrackLengthAsString();
            //转换为int类型的时长
            int intLength = audioHeader.getTrackLength();

            AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
            String songName = null;
            String artist = null;
            String album = null;

            if (id3v2Tag != null && id3v2Tag.frameMap != null) {
                Set<String> keySet = id3v2Tag.frameMap.keySet();

                if (keySet.contains("TIT2")) {
                    songName = id3v2Tag.frameMap.get("TIT2").toString();
                }
                if (keySet.contains("TPE1")) {
                    artist = id3v2Tag.frameMap.get("TPE1").toString();
                }
                if (keySet.contains("TALB")) {
                    album = id3v2Tag.frameMap.get("TALB").toString();
                }
            }
            System.out.println("歌名:" + songName + ",歌手:" + artist + ",专辑:" + album);
            if (songName != null && !songName.equals("null")) {
                songName = songName.substring(songName.indexOf("\"") + 1, songName.lastIndexOf("\""));
            }
            if (artist != null && !artist.equals("null")) {
                artist = artist.substring(artist.indexOf("\"") + 1, artist.lastIndexOf("\""));
            }
            if (album != null && !album.equals("null")) {
                album = album.substring(album.indexOf("\"") + 1, album.lastIndexOf("\""));
            }


            //为PlayBean对象赋值
            playBean.setSoundName(songName);
            playBean.setArtist(artist);
            playBean.setAlbum(album);
            playBean.setFilePath(soundBean.getfilePath());

            URI uri = file.toURI();
            Media media = new Media(uri.toString());
            MediaPlayer mp = new MediaPlayer(media);


            //监听播放器播放时的事件
            mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    //1秒1动
                    int currentSecond = (int) newValue.toSeconds();
                    //设置滚动条
                    if (currentSecond == prevSecond + 1) {
                        sliderSong.setValue(sliderSong.getValue() + 1);
                        //设置前一秒
                        prevSecond++;
                        //设置新的播放时间
                        Date date = new Date();
                        date.setTime((int) sliderSong.getValue() * 1000);
                        labplayTime.setText(new SimpleDateFormat("mm:ss").format(date));
                    }

                    //设置歌词
                    //1.获取当前播放的秒数
                    double millis = newValue.toMillis();

                    double min = 0;
                    double max = 0;
                    //判断此次是否在正常的播放区间
                    if (currentLrcIndex == 0) {
                        min = 0;
                    } else {
                        min = lrcList.get(currentLrcIndex).doubleValue();
                    }
                    if (currentLrcIndex != lrcList.size() - 1) {
                        max = lrcList.get(currentLrcIndex).doubleValue();
                    }
                    if (millis >= min && millis < max) {
                        return;
                    }
                    if (currentLrcIndex < lrcList.size() - 1 && millis >= lrcList.get(currentLrcIndex + 1).doubleValue()) {
                        currentLrcIndex++;
                        Timeline t1 = new Timeline(new KeyFrame(millis(15),
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lrcVBox.setLayoutY(lrcVBox.getLayoutY() - 1);
                                    }
                                }));
                        t1.setCycleCount(50);
                        t1.play();

                        //歌词变色
                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.web("#1ed760"));  // 当前播放歌词

                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;

                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("黑体", startSize++));
                                    }
                                }));
                        t2.setCycleCount(12);
                        t2.play();

                        //前一行变小变灰

                        Label lab_pre_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                        if (lab_pre_1 != null) {
                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 18;

                                        @Override
                                        public void handle(ActionEvent event) {
                                            lab_pre_1.setFont(new Font("黑体", startSize));
                                        }
                                    }));
                            t3.setCycleCount(12);
                            t3.play();
                            t3.setOnFinished(e -> lab_pre_1.setTextFill(Color.rgb(46, 46, 45)));
                        }

                        //前两行
                        if (currentLrcIndex - 2 >= 0) {
                            Label lab_pre_2 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 2);
                            lab_pre_2.setTextFill(Color.rgb(46, 46, 45));
                        }

                        if (currentLrcIndex + 1 < lrcList.size()) {
                            Label lab_next_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab_next_1.setTextFill(Color.WHITE);
                        }
                    } else if (currentLrcIndex > 0 && millis < lrcList.get(currentLrcIndex).doubleValue()) {
                        currentLrcIndex--;
                        Timeline t1 = new Timeline(new KeyFrame(millis(15),
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lrcVBox.setLayoutY(lrcVBox.getLayoutY() + 1);
                                    }
                                }));
                        t1.setCycleCount(50);
                        t1.play();

                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.rgb(80, 228, 29));

                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;

                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("黑体", startSize++));
                                    }
                                }));
                        t2.setCycleCount(12);
                        t2.play();

                        //前一行变小变灰
                        if (currentLrcIndex - 1 >= 0) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                            lab.setTextFill(Color.rgb(46, 46, 45));
                        }
                        if (currentLrcIndex + 1 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab.setTextFill(Color.WHITE);
                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 18;

                                        @Override
                                        public void handle(ActionEvent actionEvent) {
                                            lab.setFont(new Font("黑体", startSize));
                                        }
                                    }));
                            t3.setCycleCount(12);
                            t3.play();

                        }
                        //后两行
                        if (currentLrcIndex + 2 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 2);
                            lab.setTextFill(Color.rgb(46, 46, 45));
                        }
                        if (currentLrcIndex + 2 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 3);
                            lab.setTextFill(Color.rgb(53, 53, 53));
                        }
                    }
                }
            });

            mp.setOnEndOfMedia(() -> {
                //停止当前播放器的播放
                this.currentPlayBean.getMediaPlayer().stop();
                //2.停止光盘的转动
                this.timeline.stop();

                // 停止频谱动画
                if (spectrumTimeline != null) {
                    spectrumTimeline.stop();
                }
                // 清除频谱
                if (spectrumPane != null) {
                    spectrumPane.clear();
                }

                //设置歌词位置
                this.lrcVBox.getChildren().clear();
                this.lrcVBox.setLayoutY(50 * 2 - 10);
                this.lrcList.clear();
                this.currentLrcIndex = 0;

                //3.播放下一首音乐
                switch (this.playMode) {
                    case 1: //列表循环
                        this.currentIndex++;
                        if (this.currentIndex >= this.tableView.getItems().size()) {
                            this.currentIndex = 0;
                        }
                        this.currentPlayBean = tableView.getItems().get(this.currentIndex);
                        break;
                    case 2: //顺序播放
                        this.currentIndex++;
                        if (this.currentIndex >= this.tableView.getItems().size()) {
                            //停止播放
                            return;
                        }
                        this.currentPlayBean = tableView.getItems().get(this.currentIndex);
                        break;
                    case 3: //单曲循环
                        this.currentPlayBean.getMediaPlayer().seek(new Duration(0));
                        break;
                }
                this.tableView.getSelectionModel().select(currentIndex);
                play();
            });
            playBean.setMediaPlayer(mp);
            //计算文件大小
            BigDecimal bigDecimal = new BigDecimal(file.length());
            BigDecimal result = bigDecimal.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_HALF_UP);

            playBean.setLength(result.toString() + "M");

            playBean.setTime(strLength);
            playBean.setTotalSeconds(intLength);

            //设置删除图片
            ImageView iv = new ImageView("img/left/laji_2_Dark.png");
            iv.setFitWidth(15);
            iv.setFitHeight(15);
            //删除歌单中的歌曲
            Label labDelete = new Label("", iv);
            labDelete.setAlignment(Pos.CENTER);
            labDelete.setOnMouseEntered(e -> {
                iv.setImage(new Image("img/left/laji_2.png"));
            });
            labDelete.setOnMouseExited(e -> {
                iv.setImage(new Image("img/left/laji_2_Dark.png"));
            });
            playBean.setLabDelete(labDelete);
            labDelete.setOnMouseClicked(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("确认删除");
                alert.setHeaderText("你确定要删除该歌曲吗？");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    XMLUtils.deleteSound(labGroupName.getText().trim(), playBean.getFilePath());
                    // 删除后刷新整个歌单，确保完整性和ID重新编号
                    readAllSoundByGroupName();
                }
            });


            //设置图像
//            AbstractID3v2Tag tag = mp3File.getID3v2Tag();
//            AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
//            if(frame != null){
//                FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
//                byte[]  imageData = body.getImageData();
//                //将字符串转化为Image对象
//                java.awt.Image image = Toolkit.getDefaultToolkit().createImage(imageData,0,imageData.length);
//                BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);
//                WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage,null);
//                playBean.setImage(writableImage);
//            }else {
//                Image img = new Image("img/topandbottom/yanzi-fengmian.png");
//                WritableImage writableImage = new WritableImage(img.getPixelReader(), (int)img.getWidth(), (int)img.getHeight());
//                playBean.setImage(writableImage);
//            }

            // 读取图像
            AbstractID3v2Tag tag = mp3File.getID3v2Tag();
            AbstractID3v2Frame frame = null;
            if (tag != null) {
                frame = (AbstractID3v2Frame) tag.getFrame("APIC");
            }
            if (frame != null) {
                FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
                byte[] imageData = body.getImageData();
                java.awt.Image image = Toolkit.getDefaultToolkit().createImage(imageData, 0, imageData.length);
                BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);
                WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                playBean.setImage(writableImage);
            } else {
                Image img = new Image("img/topandbottom/yanzi-fengmian.png");
                WritableImage writableImage = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                playBean.setImage(writableImage);
            }

            //playBean对象添加到集合中
            playBeanList.add(playBean);
        }
        //将集合中的数据设置到TableView中显示
        ObservableList<PlayBean> data = FXCollections.observableArrayList(playBeanList);
        this.tableView.getItems().clear();
        this.tableView.setItems(data);
        this.tableView.refresh(); // 强制刷新TableView
    }

    //播放音乐
    private void play() {
        if (spectrumTimeline != null) {
            spectrumTimeline.stop();
            spectrumTimeline.play();
        }
        if (spectrumPane != null) {
            spectrumPane.clear();
        }
        //读取歌词
        loadLrc();
        //1.设置总时间
        this.labTotalTime.setText(this.currentPlayBean.getTime());

        //设置滚动条最大值

        this.sliderSong.setMax(this.currentPlayBean.getTotalSeconds());
        this.sliderSong.setMajorTickUnit(1);//每次前进一格
        this.sliderSong.setValue(0);
        prevSecond = 0;

        //设置初始音量
        this.currentPlayBean.getMediaPlayer().setVolume(this.volumeprogress.getProgress());

        //2.播放音乐
        new Thread() {
            @Override
            public void run() {
                currentPlayBean.getMediaPlayer().play();
            }
        }.start();
//        labplay.setOnMouseClicked(e->{
//            if(this.currentPlayBean == null || this.currentPlayBean.getMediaPlayer() == null){
//                return;
//            }
//        });

        //设置碟片
        if (this.currentPlayBean.getImage() != null) {
            this.panImageView.setImage(this.currentPlayBean.getImage());
        } else {
            this.panImageView.setImage(new Image("img/center/pan_default.jpg"));
        }
        //设置旋转
        this.timeline.stop();
        this.timeline.play();

        //设置背景
        WritableImage wImage = this.currentPlayBean.getImage();
        if (wImage != null) {
            WritableImage newWritableImage = new WritableImage(
                    (int) wImage.getWidth(),
                    (int) wImage.getHeight()
            );
            PixelReader pr = wImage.getPixelReader();
            PixelWriter pw = newWritableImage.getPixelWriter();
            for (int i = 0; i < wImage.getHeight(); i++) {
                for (int j = 0; j < wImage.getWidth(); j++) {
                    Color color = pr.getColor(i, j);
                    for (int k = 0; k < 4; k++) {
                        color = color.darker();
                    }
                    pw.setColor(i, j, color);
                }
            }
            this.backImageView.setImage(newWritableImage);
        } else {
            Image img = new Image("img/center/pan_default.jpg");
            PixelReader pr = img.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) img.getWidth(),
                    (int) img.getHeight()
            );
            PixelWriter pw = writableImage.getPixelWriter();
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    Color color = pr.getColor(i, j);
                    for (int k = 0; k < 4; k++) {
                        color = color.darker();
                    }
                    pw.setColor(i, j, color);
                }
            }
            this.backImageView.setImage(writableImage);
        }

        //设置播放按钮
        this.butplayImage.setImage(new Image("img/topandbottom/pauseDark.png"));
        this.labplay.setOnMouseEntered(e -> {
            butplayImage.setImage(new Image("img/topandbottom/pause.png"));
        });
        this.butplayImage.setOnMouseExited(e -> {
            butplayImage.setImage(new Image("img/topandbottom/pauseDark.png"));
        });
    }

    //加载正在播放的lrc文件
    private void loadLrc() {
        if (this.currentPlayBean == null) {
            return;
        }
        //初始化lrcVBox
        this.lrcVBox.getChildren().clear();
        this.lrcVBox.setLayoutY(50 * 2 - 10);
        this.lrcList.clear();
        this.currentLrcIndex = 0;

        File mp3File = new File(this.currentPlayBean.getFilePath());

        File lrcFile = new File(mp3File.getParent(), mp3File.getName().substring(0, mp3File.getName().indexOf(".")) + ".lrc");
        if (!lrcFile.exists()) {
            return;
        }

        //读取每一行歌词
        try {
            BufferedReader bufIn = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), "UTF-8"));
            String row = null;
            while ((row = bufIn.readLine()) != null) {
                if (row.indexOf("[") == -1 || row.indexOf("]") == -1) {
                    continue;
                }
                if (row.charAt(1) < '0' || row.charAt(1) > '9') {
                    continue;
                }
                //解析时间和歌词
                String strTime = row.substring(1, row.indexOf("]"));
                String strMinute = strTime.substring(0, strTime.indexOf(":"));
                String strSecond = strTime.substring(strTime.indexOf(":") + 1);
                //转换为分钟
                int intMinute = Integer.parseInt(strMinute);
                //转换为秒钟
                BigDecimal totalMilli = new BigDecimal(intMinute * 60).add(new BigDecimal(strSecond)).multiply(new BigDecimal("1000"));
                this.lrcList.add(totalMilli);

                //常见歌词
                Label lab = new Label(row.trim().substring(row.indexOf("]") + 1));
                lab.setMinWidth(550);
                lab.setMinHeight(35);
                lab.setMaxHeight(35);

                lab.setPrefWidth(55);
                lab.setPrefHeight(35);
                lab.setTextFill(Color.rgb(46, 46, 45));
                lab.setFont(new Font("黑体", 18));
                lab.setAlignment(Pos.CENTER);

                if (this.lrcVBox.getChildren().size() == 0) {
                    lab.setTextFill(Color.web("#666666"));  // 未播放歌词
                    lab.setFont(new Font("黑体", 30));
                }
                if (this.lrcVBox.getChildren().size() == 1) {
                    lab.setTextFill(Color.WHITE);
                }
                this.lrcVBox.getChildren().add(lab);

            }
            if (this.currentPlayBean.getMediaPlayer().getTotalDuration().toMillis() -
                    this.lrcList.get(this.lrcList.size() - 1).doubleValue() > 0) {
                Label lab = new Label("...");
                lab.setMinWidth(550);
                lab.setMinHeight(35);
                lab.setMaxHeight(35);

                lab.setPrefWidth(55);
                lab.setPrefHeight(35);
                lab.setTextFill(Color.rgb(46, 46, 45));
                lab.setFont(new Font("黑体", 18));
                lab.setAlignment(Pos.CENTER);
                this.lrcList.add(new BigDecimal(this.currentPlayBean.getMediaPlayer().getTotalDuration().toMillis()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        // 启动登录/注册选择界面
        javafx.application.Application.launch(LoginRegisterChoiceView.class, args);
//        javafx.application.Application.launch(MainApp.class, args);
    }

    public static void launchApp(Stage primaryStage) {
        MainApp app = new MainApp();
        try {
            app.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置当前歌单名称
     *
     * @param groupName 歌单名称
     */
    public void setGroupName(String groupName) {
        if (labGroupName != null) {
            labGroupName.setText(groupName);
        }
    }

    /**
     * 根据当前歌单名称读取所有歌曲（带延迟刷新，确保文件写入完成）
     */
    public void readAllSoundByGroupName() {
        // 使用JavaFX的Timeline添加短暂延迟，确保XML文件写入完成
        Timeline refreshTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    readAllSoundByGroupNameInternal();
                })
        );
        refreshTimeline.setCycleCount(1);
        refreshTimeline.play();
    }

    /**
     * 强制立即刷新歌单（不等待）
     */
    public void refreshGroupImmediately() {
        readAllSoundByGroupNameInternal();
    }

}


