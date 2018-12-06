package screenshot;

import handlers.MqttConnectionHandler;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class SSCapture {
    MqttConnectionHandler mqttConnectionHandler = MqttConnectionHandler.getInstance();


    public boolean doCapture() {
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File("screenshot.png"));
            byte[] fileContent = FileUtils.readFileToByteArray(new File("screenshot.png"));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            System.out.println(encodedString);
            mqttConnectionHandler.publish(encodedString);
            return true;
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
