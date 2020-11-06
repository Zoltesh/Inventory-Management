import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    public GridPane AddProductUI;
    private Scene scene;

    @FXML private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    @FXML private TextField addProductSearchText;

    @FXML private TableView<Part> addProductTable;
    @FXML private TableView<Part> addProductAssociatedTable;

    @FXML private TableColumn<Part, Integer> addProductIdColumn;
    @FXML private TableColumn<Part, String> addProductNameColumn;
    @FXML private TableColumn<Part, Integer> addProductStockColumn;
    @FXML private TableColumn<Part, Double> addProductPriceColumn;

    @FXML private TableColumn<Part, Integer> addProductAssociatedIdColumn;
    @FXML private TableColumn<Part, String> addProductAssociatedNameColumn;
    @FXML private TableColumn<Part, Integer> addProductAssociatedStockColumn;
    @FXML private TableColumn<Part, Double> addProductAssociatedPriceColumn;

    @FXML private TextField addProductNameTextField = new TextField();
    @FXML private TextField addProductStockTextField = new TextField();
    @FXML private TextField addProductPriceTextField = new TextField();
    @FXML private TextField addProductMaxTextField = new TextField();
    @FXML private TextField addProductMinTextField = new TextField();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //*********** Sets the add product table to inventory object list of parts  ***********
        addProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductTable.setItems(MainController.masterInventory.getAllParts());
        addProductTable.getSelectionModel().selectFirst();

        //***********************************************************************************

        //*********** Sets the add product associated parts table  ***********

        addProductAssociatedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAssociatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAssociatedStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAssociatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductAssociatedTable.setItems(associatedPartsList);

        //***********************************************************************************

    }

    //*********************************     Core Functions      ********************************************************

    public boolean addProductSearchButtonClicked(){

        ObservableList<Part> searchPart = FXCollections.observableArrayList();

        if(addProductSearchText.getText().isEmpty()){
            addProductTable.setItems(MainController.masterInventory.getAllParts());
            return true;

        }

        try{
            int id = Integer.parseInt(addProductSearchText.getText());
            searchPart.add(MainController.masterInventory.lookupPart(id));
            addProductTable.setItems(searchPart);
            return true;

        }catch (Exception ignored){

        }

        searchPart.add(MainController.masterInventory.lookupPart(addProductSearchText.getText()));
        addProductTable.setItems(searchPart);
        return false;

    }

    public boolean addProductDeleteButtonClicked(){

        associatedPartsList.remove(addProductAssociatedTable.getSelectionModel().getSelectedItem());

        return true;

    }

    public void addProductSaveButtonClicked(){

        checkProductInput(addProductNameTextField, addProductStockTextField, addProductPriceTextField,
                addProductMaxTextField, addProductMinTextField, getScene());

    }

    public void addProductCancelButtonClicked(){

        associatedPartsList.clear();
        Main.window.setScene(Main.mainWindow);

    }

    public void addProductAddButtonClicked(){

        associatedPartsList.add(addProductTable.getSelectionModel().getSelectedItem());
        addProductAssociatedTable.setItems(associatedPartsList);

    }

    //******************************************************************************************************************

    //*********************************     Error Checking Functions    ************************************************

    public void productError(String message, Scene redoScene){

        Stage errorWindow = new Stage();
        errorWindow.initModality(Modality.APPLICATION_MODAL);
        errorWindow.setTitle("Invalid Input");
        errorWindow.setMinWidth(250);

        Text text = new Text();
        text.setText(message);
        Button closeButton = new Button("Try Again");
        closeButton.setOnAction(e -> errorWindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        errorWindow.setScene(scene);
        errorWindow.showAndWait();
        Main.window.setScene(redoScene);

    }

    public void checkProductInput(TextField name, TextField stock, TextField price, TextField max, TextField min,
                                  Scene scene){

        int Id = getNextProductId();

        //Launches error window if text field is empty
        if (name.getText().isEmpty()){
            productError("Name Field cannot be empty", scene);
            return;
        }

        //Launches error window if field is empty or not a whole number
        try{
            Integer.parseInt(stock.getText());
        }catch(Exception e) {
            productError("Stock Field cannot be empty and must be a whole number", scene);
            return;
        }

        //Launches error window if field is empty
        try{
            Double.parseDouble(price.getText());
        }catch(Exception e){
            productError("Price Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Max field is empty/string
        try{
            Integer.parseInt(max.getText());
        }catch(Exception e){
            productError("Max Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Min field is empty/string
        try{
            Integer.parseInt(min.getText());
        }catch(Exception e){
            productError("Min Field cannot be empty/string", scene);
            return;
        }

        //Ensures that the value in the Max field is not less than the value in the Min field
        if(Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())){
            productError("Max cannot be less than Min", scene);
            return;
        }

        //Ensures that the Inventory Level is not less than the Min field or greater than the Max field
        if((Integer.parseInt(stock.getText()) < (Integer.parseInt(min.getText()))) || (Integer.parseInt(stock.getText()) > (Integer.parseInt(max.getText())))){
            productError("Inventory Level cannot be less than Min or greater than Max", scene);
            return;
        }

        //Ensures that the total cost of the associated parts does not exceed the cost of the product
        double totalCost = 0;
        for (Part part : associatedPartsList) {
            totalCost = roundPrice(totalCost + part.getPrice());
        }
        if (roundPrice(Double.parseDouble(price.getText())) < totalCost){
            productError("Product Price field cannot be less than the total cost of associated parts", scene);
            return;
        }


        String Name = name.getText();
        int Stock = Integer.parseInt(stock.getText());
        double Price = roundPrice(Double.parseDouble(price.getText()));
        int Max = Integer.parseInt(max.getText());
        int Min = Integer.parseInt(min.getText());

        Product product = new Product(Id, Name, Price, Stock, Min, Max);

        //Add associated parts to the product object
        for (int i = 0; i < associatedPartsList.size(); i++){
            product.addAssociatedPart(associatedPartsList.get(i));
        }

        MainController.masterInventory.addProduct(product);

        //Clears the TextFields
        name.clear();
        stock.clear();
        price.clear();
        max.clear();
        min.clear();

        //Purges associatedPartsList
        associatedPartsList.clear();

        //Sets the scene back to the main window
        Main.window.setScene(Main.mainWindow);


    }

    public int getNextProductId(){

        //Will hold the value of the next available ID
        int nextId = 0;

        //Sorts the list of parts in the inventory from lowest to greatest.
        Comparator<Product> comparator = Comparator.comparingInt(Product::getId);
        MainController.masterInventory.getAllProducts().sort(comparator);

        //Loops through the list of all parts and finds the next available ID
        for (int i = 0; i < MainController.masterInventory.getAllProducts().size(); i++){

            //This condition only happens if the next available number is 1 + the highest ID in the list
            if (i == (MainController.masterInventory.getAllProducts().size() - 1)){
                nextId = MainController.masterInventory.getAllProducts().get(i).getId() + 1;
                break;

                //This condition happens up until the last index
            }else{

                //If the ID of the current part index + 1 is equal to the ID of the next part's index, continue to the next
                //iteration
                if ((MainController.masterInventory.getAllProducts().get(i).getId() + 1) ==
                        (MainController.masterInventory.getAllProducts().get(i+1).getId())){
                    continue;

                    //If the i does not equal the ID of the next item in the list, set nextId to i
                }else{
                    nextId = MainController.masterInventory.getAllProducts().get(i).getId() + 1;
                }
            }
        }

        return nextId;
    }

    //******************************************************************************************************************


    public void setScene(Scene scene){

        this.scene = scene;

    }

    public Scene getScene(){

        return this.scene;

    }

    //Using doubles has become problematic when adding prices together. This function will use Big Decimal to more
    //accurately calculate prices
    private static double roundPrice(double priceInput){

        BigDecimal bigDecimal = new BigDecimal(Double.toString(priceInput));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();

    }

}
