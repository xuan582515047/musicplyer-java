package cn.xyx.media;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserManager {
    private static final String USER_DATA_FILE = "src/users.xml";
    private Map<String, String> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) return;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element userElem = (Element) nodeList.item(i);
                String username = userElem.getElementsByTagName("username").item(0).getTextContent();
                String password = userElem.getElementsByTagName("password").item(0).getTextContent();
                users.put(username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        return password.equals(users.get(username));
    }

    private void saveUsers() {
        try {
            File file = new File(USER_DATA_FILE);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element root = doc.createElement("users");
            doc.appendChild(root);
            for (Map.Entry<String, String> entry : users.entrySet()) {
                Element userElem = doc.createElement("user");
                Element usernameElem = doc.createElement("username");
                usernameElem.setTextContent(entry.getKey());
                Element passwordElem = doc.createElement("password");
                passwordElem.setTextContent(entry.getValue());
                userElem.appendChild(usernameElem);
                userElem.appendChild(passwordElem);
                root.appendChild(userElem);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
