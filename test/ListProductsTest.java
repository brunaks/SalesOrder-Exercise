import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by i848075 on 28/08/2015.
 */
public class ListProductsTest {

    ListProducts list = new ListProducts();

    @Test
    public void thereAreNoProductsRegistered_ListIsEmpty() {
        ArrayList<ProductInfo> listOfProducts = list.returnsAllProducts();
    }

}
