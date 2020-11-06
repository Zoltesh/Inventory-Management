import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    public GridPane MainUI;

    static Inventory masterInventory = new Inventory();

    @FXML private TextField mainPartSearchText;
    @FXML private TextField mainProductSearchText;

    @FXML private TableView<Part> mainPartTable;
    @FXML private TableView<Product> mainProductTable;

    @FXML private TableColumn<Part, Integer> mainPartIdColumn;
    @FXML private TableColumn<Part, String> mainPartNameColumn;
    @FXML private TableColumn<Part, Integer> mainPartStockColumn;
    @FXML private TableColumn<Part, Double> mainPartPriceColumn;

    @FXML private TableColumn<Product, Integer> mainProductIdColumn;
    @FXML private TableColumn<Product, String> mainProductNameColumn;
    @FXML private TableColumn<Product, Integer> mainProductStockColumn;
    @FXML private TableColumn<Product, Double> mainProductPriceColumn;

    public MainController(){

    }


    @FXML
    public void initialize(URL location, ResourceBundle resources){
        InHouse part1 = new InHouse(1, "Gnarly", 10.99, 100, 1, 1000, 1234);
        InHouse part2 = new InHouse(2, "Rekt", 100.00, 45, 10, 100, 5678);

        Outsourced part3 = new Outsourced(3, "Yeetness", 10.99, 100, 1, 1000, "Bling");
        Outsourced part4 = new Outsourced(4, "Silly Sauce", 37.99, 29, 25, 100, "China");

        masterInventory.addPart(part1);
        masterInventory.addPart(part2);
        masterInventory.addPart(part3);
        masterInventory.addPart(part4);

        Product product1 = new Product(1, "Poopsicle", 99.99, 84, 50, 120);
        Product product2 = new Product(2, "Kudos", 47.89, 230, 10, 500);

        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);

        product2.addAssociatedPart(part3);
        product2.addAssociatedPart(part4);

        masterInventory.addProduct(product1);
        masterInventory.addProduct(product2);



        //*********** Sets the main part table to inventory object list of parts  ***********
        mainPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainPartTable.setItems(masterInventory.getAllParts());
        mainPartTable.getSelectionModel().selectFirst();

        //***********************************************************************************

        //*********** Sets the main product table to inventory object list of products  ***********
        mainProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductTable.setItems(masterInventory.getAllProducts());

        //***********************************************************************************


    }

    public boolean mainPartSearchButtonClicked(ActionEvent event){

        ObservableList<Part> searchPart = FXCollections.observableArrayList();

        if(mainPartSearchText.getText().isEmpty()){
            mainPartTable.setItems(masterInventory.getAllParts());
            return true;

        }

        try{
            int id = Integer.parseInt(mainPartSearchText.getText());
            searchPart.add(masterInventory.lookupPart(id));
            mainPartTable.setItems(searchPart);
            return true;

        }catch (Exception ignored){
            System.out.println("String detected, could not parseInt");
        }

        searchPart.add(masterInventory.lookupPart(mainPartSearchText.getText()));
        mainPartTable.setItems(searchPart);
        return false;

    }

    public boolean mainProductSearchButtonClicked(ActionEvent event){

        ObservableList<Product> searchProduct = FXCollections.observableArrayList();

        if(mainProductSearchText.getText().isEmpty()){
            mainProductTable.setItems(masterInventory.getAllProducts());
            return true;

        }

        try{
            int id = Integer.parseInt(mainProductSearchText.getText());
            searchProduct.add(masterInventory.lookupProduct(id));
            mainProductTable.setItems(searchProduct);
            return true;

        }catch (Exception ignored){
            System.out.println("String detected, could not parseInt");
        }

        searchProduct.add(masterInventory.lookupProduct(mainProductSearchText.getText()));
        mainProductTable.setItems(searchProduct);
        return false;

    }

    public void mainPartAddButtonClicked(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddPartUI.fxml"));
        Parent addPart = loader.load();

        Scene addPartWindow = new Scene(addPart, 400, 400);

        Main.window.setScene(addPartWindow);

        AddPartController addPartController = loader.getController();

        addPartController.setScene(addPartWindow);

    }

    public void mainProductAddButtonClicked(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddProductUI.fxml"));
        Parent addProduct = loader.load();

        Scene addProductWindow = new Scene(addProduct, 1000, 700);

        Main.window.setScene(addProductWindow);

        AddProductController addProductController = loader.getController();

        addProductController.setScene(addProductWindow);


    }

    public void mainProductModifyButtonClicked(ActionEvent event) throws IOException {

        Product getSelectedProduct = mainProductTable.getSelectionModel().getSelectedItem();
        int getSelectedIndex = mainProductTable.getSelectionModel().getSelectedIndex();


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyProductUI.fxml"));
        Parent modifyProduct = loader.load();

        Scene modifyProductWindow = new Scene(modifyProduct, 1000, 700);

        ModifyProductController modifyProductController = loader.getController();

        modifyProductController.initProducts(getSelectedProduct);

        modifyProductController.setScene(modifyProductWindow);
        modifyProductController.setIndex(getSelectedIndex);
        modifyProductController.setId(getSelectedProduct.getId());

        Main.window.setScene(modifyProductWindow);

    }

    public void mainPartDeleteButtonClicked(){

        masterInventory.deletePart(mainPartTable.getSelectionModel().getSelectedItem());

    }

    public void mainProductDeleteButtonClicked(){

        masterInventory.deleteProduct(mainProductTable.getSelectionModel().getSelectedItem());

    }

    public void exitButtonClicked(){

        Main.window.close();

    }

    public void mainPartModifyButtonClicked(ActionEvent event) throws IOException {

        Part getSelectedPart = mainPartTable.getSelectionModel().getSelectedItem();
        int getSelectedIndex = mainPartTable.getSelectionModel().getSelectedIndex();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyPartUI.fxml"));
        Parent modifyPart = loader.load();

        Scene modifyPartWindow = new Scene(modifyPart, 400, 400);

        ModifyPartController modifyPartController = loader.getController();

        if(getSelectedPart.getClass() == InHouse.class){

            modifyPartController.initPart(getSelectedIndex, "InHouse", getSelectedPart);

        }else if(getSelectedPart.getClass() == Outsourced.class){

            modifyPartController.initPart(getSelectedIndex, "Outsourced", getSelectedPart);

        }

        modifyPartController.setScene(modifyPartWindow);


        Main.window.setScene(modifyPartWindow);


    }
}