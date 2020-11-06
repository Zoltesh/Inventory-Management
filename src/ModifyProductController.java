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
import java.util.ResourceBundle;

public class ModifyProductController extends MainController implements Initializable {

    public GridPane ModifyProductUI;

    private int Index;
    private int Id;

    private Scene scene;

    private ModifyProductController modifyProductController;

    @FXML private ObservableList<Part> modifyProductAssociatedPartsList = FXCollections.observableArrayList();
    @FXML private TextField modifyProductSearchText;

    @FXML private TableView<Part> modifyProductTable;
    @FXML private TableView<Part> modifyProductAssociatedTable;

    @FXML private TableColumn<Part, Integer> modifyProductIdColumn;
    @FXML private TableColumn<Part, String> modifyProductNameColumn;
    @FXML private TableColumn<Part, Integer> modifyProductStockColumn;
    @FXML private TableColumn<Part, Double> modifyProductPriceColumn;

    @FXML private TableColumn<Part, Integer> modifyProductAssociatedIdColumn;
    @FXML private TableColumn<Part, String> modifyProductAssociatedNameColumn;
    @FXML private TableColumn<Part, Integer> modifyProductAssociatedStockColumn;
    @FXML private TableColumn<Part, Double> modifyProductAssociatedPriceColumn;

    @FXML private TextField modifyProductNameTextField = new TextField();
    @FXML private TextField modifyProductStockTextField = new TextField();
    @FXML private TextField modifyProductPriceTextField = new TextField();
    @FXML private TextField modifyProductMaxTextField = new TextField();
    @FXML private TextField modifyProductMinTextField = new TextField();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //*********** Sets the add product table to inventory object list of parts  ***********
        modifyProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductTable.setItems(MainController.masterInventory.getAllParts());
        modifyProductTable.getSelectionModel().selectFirst();

        //***********************************************************************************

        //*********** Sets the add product associated parts table  ***********

        modifyProductAssociatedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAssociatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAssociatedStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductAssociatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductAssociatedTable.setItems(modifyProductAssociatedPartsList);

        //***********************************************************************************

    }

    public void initProducts(Product product){

        modifyProductNameTextField.setText(product.getName());
        modifyProductStockTextField.setText(Integer.toString(product.getStock()));
        modifyProductPriceTextField.setText(Double.toString(product.getPrice()));
        modifyProductMaxTextField.setText(Integer.toString(product.getMax()));
        modifyProductMinTextField.setText(Integer.toString(product.getMin()));

        //Need to also add these associated parts to the modifyProductAssociatedPartsList so that they don't get
        //overwritten when clicking the add button
        modifyProductAssociatedPartsList.addAll(product.getAllAssociatedParts());

        getUpdatedParts();

        modifyProductAssociatedTable.setItems(modifyProductAssociatedPartsList);

    }

    public boolean modifyProductSearchButtonClicked(){

        ObservableList<Part> searchPart = FXCollections.observableArrayList();

        if(modifyProductSearchText.getText().isEmpty()){
            modifyProductTable.setItems(MainController.masterInventory.getAllParts());
            return true;

        }

        try{
            int id = Integer.parseInt(modifyProductSearchText.getText());
            searchPart.add(MainController.masterInventory.lookupPart(id));
            modifyProductTable.setItems(searchPart);
            return true;

        }catch (Exception ignored){

        }

        searchPart.add(MainController.masterInventory.lookupPart(modifyProductSearchText.getText()));
        modifyProductTable.setItems(searchPart);
        return false;

    }

    public void modifyProductCancelButtonClicked(){

        Main.window.setScene(Main.mainWindow);

    }

    public void modifyProductAddButtonClicked(){

        modifyProductAssociatedPartsList.add(modifyProductTable.getSelectionModel().getSelectedItem());
        modifyProductAssociatedTable.setItems(modifyProductAssociatedPartsList);

    }

    public void modifyProductDeleteButtonClicked(){

        modifyProductAssociatedPartsList.remove(modifyProductAssociatedTable.getSelectionModel().getSelectedItem());
        modifyProductAssociatedTable.setItems(modifyProductAssociatedPartsList);

    }

    public void modifyProductSaveButtonClicked(){

        checkProductInput(modifyProductNameTextField, modifyProductStockTextField, modifyProductPriceTextField,
                modifyProductMaxTextField, modifyProductMinTextField, getScene());

    }

    //*********************************     Error Checking Functions    ************************************************

    public void modifyProductError(String message, Scene redoScene){

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

        //Launches error window if text field is empty
        if (name.getText().isEmpty()){
            modifyProductError("Name Field cannot be empty", scene);
            return;
        }

        //Launches error window if field is empty or not a whole number
        try{
            Integer.parseInt(stock.getText());
        }catch(Exception e) {
            modifyProductError("Stock Field cannot be empty and must be a whole number", scene);
            return;
        }

        //Launches error window if field is empty
        try{
            Double.parseDouble(price.getText());
        }catch(Exception e){
            modifyProductError("Price Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Max field is empty/string
        try{
            Integer.parseInt(max.getText());
        }catch(Exception e){
            modifyProductError("Max Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Min field is empty/string
        try{
            Integer.parseInt(min.getText());
        }catch(Exception e){
            modifyProductError("Min Field cannot be empty/string", scene);
            return;
        }

        //Ensures that the value in the Max field is not less than the value in the Min field
        if(Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())){
            modifyProductError("Max cannot be less than Min", scene);
            return;
        }

        //Ensures that the Inventory Level is not less than the Min field or greater than the Max field
        if((Integer.parseInt(stock.getText()) < (Integer.parseInt(min.getText()))) || (Integer.parseInt(stock.getText()) > (Integer.parseInt(max.getText())))){
            modifyProductError("Inventory Level cannot be less than Min or greater than Max", scene);
            return;
        }

        //Ensures that the total cost of the associated parts does not exceed the cost of the product
        double totalCost = 0;
        for (Part part : modifyProductAssociatedPartsList) {
            totalCost = roundPrice(totalCost + part.getPrice());
        }
        if (roundPrice(Double.parseDouble(price.getText())) < totalCost){
            modifyProductError("Product Price field cannot be less than the total cost of associated parts", scene);
            return;
        }

        String Name = name.getText();
        int Stock = Integer.parseInt(stock.getText());
        double Price = roundPrice(Double.parseDouble(price.getText()));
        int Max = Integer.parseInt(max.getText());
        int Min = Integer.parseInt(min.getText());

        Product product = new Product(getId(), Name, Price, Stock, Min, Max);

        //Add associated parts to the product object
        for (int i = 0; i < modifyProductAssociatedPartsList.size(); i++){
            product.addAssociatedPart(modifyProductAssociatedPartsList.get(i));
        }

        MainController.masterInventory.updateProduct(getIndex(), product);

        //Clears the TextFields
        name.clear();
        stock.clear();
        price.clear();
        max.clear();
        min.clear();

        //Purges associatedPartsList
        modifyProductAssociatedPartsList.clear();

        //Sets the scene back to the main window
        Main.window.setScene(Main.mainWindow);


    }

    //*********************************     Sets/Gets for ID and Index    **********************************************

    public void setIndex(int index){

        this.Index = index;

    }

    public int getIndex(){

        return this.Index;

    }

    public void setId(int id){

        this.Id = id;

    }

    public int getId(){

        return this.Id;

    }

    public void setScene(Scene scene){

        this.scene = scene;

    }

    public Scene getScene(){

        return this.scene;

    }

    //******************************************************************************************************************

    //Using doubles has become problematic when adding prices together. This function will use Big Decimal to more
    //accurately calculate prices
    private static double roundPrice(double priceInput){

        BigDecimal bigDecimal = new BigDecimal(Double.toString(priceInput));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();

    }

    //If a Part that is associated with the selected Product has been modified, the Part in the Product's associated
    //list needs to reflect the update Part
    public void getUpdatedParts(){

        for(int i = 0; i < modifyProductAssociatedPartsList.size(); i++){

            for(int j = 0; j < MainController.masterInventory.getAllParts().size(); j++){

                if(modifyProductAssociatedPartsList.get(i).getId() == MainController.masterInventory.getAllParts().get(j).getId()){

                    modifyProductAssociatedPartsList.remove(i);
                    modifyProductAssociatedPartsList.add(MainController.masterInventory.getAllParts().get(j));

                }

            }

        }

    }

}
