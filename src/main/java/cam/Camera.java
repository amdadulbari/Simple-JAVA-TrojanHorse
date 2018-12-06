package cam;

import com.github.sarxos.webcam.Webcam;
import handlers.MqttConnectionHandler;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Camera {
    MqttConnectionHandler mqttConnectionHandler = MqttConnectionHandler.getInstance();

    public boolean doCapture() {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();
        try {
            ImageIO.write(image, "JPG", new File("test.jpg"));
            byte[] fileContent = FileUtils.readFileToByteArray(new File("test.jpg"));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            webcam.close();
            System.out.println(encodedString);
            mqttConnectionHandler.publish(encodedString);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
