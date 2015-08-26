import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by i848075 on 26/08/2015.
 */
public class RegisterOneProduct implements Route{

    private ProductRepository repository;
    String name;
    String description;
    String price;
    String units;
    private ProductInfo productInfo;
    Gson converter = new Gson();

    public RegisterOneProduct(ProductRepository repository) {
        this.repository = repository;
    }

    public Object handle(Request rq, Response rp) throws Exception {
        Receiver receiver = new Receiver();
        getRequestInfo(rq);
        createProductInfo();
        RegisterProduct registerProduct = new RegisterProduct(receiver, productInfo, repository);
        registerProduct.execute();
        return converter.toJson(receiver);
    }

    private void getRequestInfo(Request rq) {
        name = rq.queryParams("name");
        description = rq.queryParams("description");
        price = rq.queryParams("price");
        units = rq.queryParams("units");
    }

    private void createProductInfo() {
        ProductInfo info = new ProductInfo();
        info.name = this.name;
        info.description = this.description;
        info.price = Double.parseDouble(this.price);
        info.unitsInStock = Integer.parseInt(this.units);
        this.productInfo = info;
    }

    private class Receiver implements ProductReceiver {

        public boolean registrationWasSuccessful;
        public boolean savingWasSuccessful;
        public boolean invalidProductInformation;

        @Override
        public boolean productWasRegisteredSuccessfully() {
            return false;
        }

        @Override
        public void registrationFailed() {
            this.registrationWasSuccessful = false;
        }

        @Override
        public void registrationWasSuccessful() {
            this.registrationWasSuccessful = true;
        }

        @Override
        public void productInformationIsInvalid() {
            invalidProductInformation = true;
        }

        @Override
        public void productFound() {

        }

        @Override
        public boolean productIsInRepository() {
            return false;
        }

        @Override
        public void productWasNotSaved() {
            savingWasSuccessful = false;
        }

        @Override
        public boolean productWasSavedSuccessfully() {
            return savingWasSuccessful;
        }

        @Override
        public void productWasSaved() {
            savingWasSuccessful = true;
        }

        @Override
        public void productWasNotFound() {

        }
    }
}
