import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by i848075 on 02/09/2015.
 */
public class FileWritingTest {

    private File file;

    @Before
    public void setUp() throws Exception {
        file = new File("test_file");
        file.createNewFile();
    }

    @Test
    public void test() throws Exception {
        ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        Product product = new Product();
        product.setId("Id 1");
        product.setName("Name 1");
        outputStream.writeObject(product);

        product = new Product();
        product.setId("Id 2");
        product.setName("Name 2");
        outputStream.writeObject(product);
        outputStream.flush();

        ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        product = (Product) inputStream.readObject();
        assertEquals("Id 1", product.getId());
        assertEquals("Name 1", product.getName());

        product = (Product) inputStream.readObject();
        assertEquals("Id 2", product.getId());
        assertEquals("Name 2", product.getName());
    }

    @After
    public void tearDown() {file.delete();}
}