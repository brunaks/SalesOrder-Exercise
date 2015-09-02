import java.io.*;
import java.util.ArrayList;

/**
 * Created by i848075 on 01/09/2015.
 */
public class FileProductRepository extends FakeProductRepository {

    File file = new File("products.csv");

    public FileProductRepository() {
        loadAll();
    }

    private void loadAll() {

        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            while (true) {
                try {
                    productsSaved.add((Product) inputStream.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {

        }
    }

    public void saveProduct(Product product) {

        super.saveProduct(product);
        file.delete();
        try {
            file.createNewFile();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);

            for (int i = 0; i < productsSaved.size(); i++) {
                objectStream.writeObject(productsSaved.get(i));
            }

        } catch (Exception e) {

        }
    }
}
