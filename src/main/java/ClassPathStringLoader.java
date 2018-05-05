
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class ClassPathStringLoader {

    public String getStringFromClassPathFile(String fileName){

        String result;

        try {

            InputStream inputStream = getStreamForFile(fileName);

            StringWriter  stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter);

            result =  stringWriter.toString();

        } catch (IOException e) {
            String message = e.getMessage();
            throw new RuntimeException(message);
        }

        return result;
    }

    public InputStream getStreamForFile(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            String message = e.getMessage();
            throw new RuntimeException(message);
        }
    }
}
