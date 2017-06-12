package ru.ifmo.server;

import org.junit.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static junit.framework.TestCase.*;

/**
 * Created by Тарас on 08.06.2017.
 */
public class ConfigParserTest {

    @Test
    public void testProperties() throws Exception {
        checkConfig(loadFile("web-server.properties", ".properties"));
        checkConfig(null);
    }

    @Test
    public void testXml() throws Exception {
        checkConfig(loadFile("web-server.xml", ".xml"));
    }

    private File loadFile(String name, String suffix) throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream(name);

        assertNotNull(in);

        File configFile = File.createTempFile("web-server", suffix);

        Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return configFile;
    }

    private void checkConfig(File configFile) {
        ServerConfig config = new ConfigLoader().load(configFile != null ? configFile.getAbsolutePath() : null);

        Handler failHandler0 = config.getHandlers().get("/info.html");
        Handler failHandler1 = config.getHandlers().get("/info");

        assertNotNull(failHandler0);
        assertNotNull(failHandler1);

        assert failHandler0 == failHandler1;

        assertEquals("ru.ifmo.server.FailHandler", failHandler0.getClass().getName());
        assertEquals(8888, config.getPort());
        assertEquals(5, config.getSocketTimeout());
    }

}
