package cn.xyx.Utils;

import cn.xyx.media.SoundBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLUtils {


    private static Runnable refreshCallback;

    public static void setSongListRefreshCallback(Runnable callback) {
        refreshCallback = callback;
    }

    public static void notifySongListChanged() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }

    // 弹出文件选择器并将所选歌曲添加到指定歌单
    public static void addSongsViaFileChooser(javafx.stage.Stage stage, String groupName) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("打开音乐文件");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new javafx.stage.FileChooser.ExtensionFilter("flac", "*.flac"),
                new javafx.stage.FileChooser.ExtensionFilter("所有文件", "*.*")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files != null && !files.isEmpty()) {
            insertSounds(groupName, files);
            // 显式通知刷新，参考 deleteSound 的刷新行为
            notifySongListChanged();
        }
    }

    public static List<String> readAllGroup() {
        //1.创建一个List<Stirng>集合对象
        List<String> groupList = new ArrayList<>();
        //2.创建一个SAXReader对象
        SAXReader reader = new SAXReader();
        //3.读取Document对象
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            //4.读取根元素
            Element root = dom.getRootElement();

            if (root == null) {
                return groupList;
            }
            List<Element> groupEleList = root.elements("group");
            if (groupEleList == null || groupEleList.size() == 0) {
                return groupList;
            }

            //6.遍历groupEleList集合，获取name属性的值
            for (Element ele : groupEleList) {
                groupList.add(ele.attributeValue("name"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    //向MusicGroup.xml中添加一个“新歌单”
    public static void addGroup(String groupName) {
        SAXReader reader = new SAXReader();
        try {
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            //读取根元素
            Element root = dom.getRootElement();
            //向根元素下添加一个新的<group>元素
            Element groupEle = root.addElement("group");
            groupEle.addAttribute("name", groupName);
            groupEle.addAttribute("addDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true); //生成完整<group></group>标签，而不是<group/>

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicPlyerMoudle/src/MusicGroup.xml")),outputFormat);
            xmlWriter.write(dom);
            xmlWriter.close();
            notifySongListChanged();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向MusicGroup.xml中写入”某个歌单“中的”歌曲“
    public static void insertSounds(String groupName,List<File> fileList) {
        SAXReader reader = new SAXReader();
        try{
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> groupEleList = root.elements("group");
            for(Element ele : groupEleList){
                if(ele.attributeValue("name").equals(groupName)){//找到要添加的歌单
                    for(File file : fileList){
                        Element soundEle = ele.addElement("sound");
                        Element filePathEle = soundEle.addElement("filePath");
                        Element addDate = soundEle.addElement("addDate");

                        filePathEle.setText(file.getAbsolutePath());
                        addDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    }
                    break;
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true); //生成完整<group></group>标签，而不是<group/>

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicPlyerMoudle/src/MusicGroup.xml")),outputFormat);
            xmlWriter.write(dom);
            xmlWriter.close();


        }catch(DocumentException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除分组
    public static void deleteGroup(String groupName) {
        SAXReader reader = new SAXReader();
        try{
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> groupEleList = root.elements("group");
            for(Element ele : groupEleList){
                if(ele.attributeValue("name").equals(groupName)){//找到要添加的歌单
                    //调用父元素删除方法
                    root.remove(ele);
                    break;
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true); //生成完整<group></group>标签，而不是<group/>

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicPlyerMoudle/src/MusicGroup.xml")),outputFormat);
            xmlWriter.write(dom);
            xmlWriter.close();
            notifySongListChanged();
        }catch(DocumentException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取上次播放信息
    public static  String[] readPrevPlayInfo(){
        SAXReader reader = new SAXReader();
        try{
            Document dom = reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("PlayInfo.xml"));
            Element root = dom.getRootElement();
            if(root == null){
                return null;
            }
            //读取子元素
            Element ele = root.element("currrentGroup");
            if(ele == null){
                return null;
            }
            String groupName = ele.attributeValue("name");
            Element indexEle = root.element("currentIndex");
            if(indexEle == null){
                return null;
            }
            String index = indexEle.getText();

            String[] strArray = new String[2];
            strArray[0] = groupName;
            strArray[1] = index;
            return strArray;

        }catch(DocumentException e){
            e.printStackTrace();
        }
        return null;
    }

    //读取某个歌单的所有歌曲
    public static  List<SoundBean> findSoundByGroupName(String groupName){
        SAXReader reader = new SAXReader();
        List<SoundBean> soundList = new ArrayList<>();
        try{
            Document dom =  reader.read(XMLUtils.class.getClassLoader().getResourceAsStream("MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> eleList = root.elements("group");
            for(Element ele:eleList){
                if(ele.attributeValue("name").equals(groupName)){
                    List<Element> soundEleList = ele.elements("sound");
                    for(Element soundEle: soundEleList){
                        SoundBean soundBean = new SoundBean();
                        soundBean.setfilePath(soundEle.elementText("filePath"));
                        soundBean.setAddDate(soundEle.elementText("addDate"));
                        soundList.add(soundBean);
                    }
                    return soundList;
                }
            }

        }catch(DocumentException e){
            e.printStackTrace();
        }
        return soundList;
    }

    //删除某个歌单中的某首歌曲
    public static  void deleteSound(String groupName, String filePath) {
        SAXReader reader = new SAXReader();
        try {
            Document dom = reader.read(new FileInputStream("MusicPlyerMoudle/src/MusicGroup.xml"));
            Element root = dom.getRootElement();
            List<Element> groupEleList = root.elements("group");
            for (Element ele : groupEleList) {
                if (ele.attributeValue("name").equals(groupName)) {
                    List<Element> soundEleList = ele.elements("sound");
                    for (Element soundEle : soundEleList) {
                        if (soundEle.elementText("filePath").equals(filePath)) {
                            ele.remove(soundEle);
                            break;
                        }
                    }
                    break;
                }
            }
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setExpandEmptyElements(true); //生成完整<group></group>标签，而不是<group/>

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File("MusicPlyerMoudle/src/MusicGroup.xml")), outputFormat);
            xmlWriter.write(dom);
            xmlWriter.close();
            notifySongListChanged();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}