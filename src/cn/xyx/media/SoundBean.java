package cn.xyx.media;

public class SoundBean {
    private String filePath;
    private String addDate;

    public SoundBean(){

    }
    public SoundBean(String filePage, String addDate){
        this.filePath = filePage;
        this.addDate = addDate;
    }

    public String getfilePath() {
        return filePath;
    }

    public void setfilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}
