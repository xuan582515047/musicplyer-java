package cn.xyx.media;

import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.media.MediaPlayer;

public class PlayBean {
    private int id;//序号
    private String soundName;//音乐标题
    private String artist;//演唱家
    private String album;//专辑名称
    private String length;//大小
    private String time;//时长
    private WritableImage image;
    private int totalSeconds;//总秒数
    private String filePath;//文件路径
    private MediaPlayer mediaPlayer;//播放器
    private String lrcPath;//歌词路径
    private Label labDelete;//删除图标的标签


    public PlayBean(){
    }

    public PlayBean(int id, String soundName, String artist, String album, String length, String time, String filePath, WritableImage image, int totalSeconds, MediaPlayer mediaPlayer, String lrcPath, Label labDelete){
        this.id = id;
        this.soundName = soundName;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.time = time;
        this.filePath = filePath;
        this.image = image;
        this.totalSeconds = totalSeconds;
        this.mediaPlayer = mediaPlayer;
        this.lrcPath = lrcPath;
        this.labDelete = labDelete;


    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public WritableImage getImage() {
        return image;
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getLrcPath() {
        return lrcPath;
    }

    public void setLrcPath(String lrcPath) {
        this.lrcPath = lrcPath;
    }

    public Label getLabDelete() {
        return labDelete;
    }

    public void setLabDelete(Label labDelete) {
        this.labDelete = labDelete;
    }



}
