import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;


public class ModifyPartController implements Initializable{

    public GridPane ModifyPartUI;
    private int Index;
    private int Id;
    private Scene scene;

    @FXML private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    @FXML private TextField modifyProductSearchText;

    @FXML private Label modifyPartMachineIdLabel = new Label("Machine ID");
    @FXML private Label modifyPartCompNameLabel = new Label("Comp Name");

    @FXML private TextField modifyPartNameTextField = new TextField();
    @FXML private TextField modifyPartStockTextField = new TextField();
    @FXML private TextField modifyPartPriceTextField = new TextField();
    @FXML private TextField modifyPartMaxTextField = new TextField();
    @FXML private TextField modifyPartMinTextField = new TextField();
    @FXML private TextField modifyPartMachineIdTextField = new TextField();
    @FXML private TextField modifyPartCompNameTextField = new TextField();


    @FXML private ToggleGroup modifyPartToggleGroup = new ToggleGroup();

    @FXML private RadioButton modifyPartRadioInHouse = new RadioButton();
    @FXML private RadioButton modifyPartRadioOutsourced = new RadioButton();

    public ModifyPartController(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        modifyPartMachineIdTextField.setVisible(false);
        modifyPartMachineIdLabel.setVisible(false);
        modifyPartMachineIdTextField.setDisable(true);

        modifyPartCompNameLabel.setVisible(false);
        modifyPartMachineIdTextField.setVisible(false);
        modifyPartMachineIdTextField.setDisable(true);

        modifyPartRadioInHouse.setToggleGroup(modifyPartToggleGroup);
        modifyPartRadioOutsourced.setToggleGroup(modifyPartToggleGroup);


    }

    public void initPart(int index, String type, Part part){

        setId(part.getId());
        setIndex(index);

        if(type.equals("InHouse")){
            InHouse inHouse = (InHouse)part;

            modifyPartRadioInHouse.setSelected(true);

            modifyPartNameTextField.setText(inHouse.getName());
            modifyPartStockTextField.setText(Integer.toString(inHouse.getStock()));
            modifyPartPriceTextField.setText(Double.toString(inHouse.getPrice()));
            modifyPartMaxTextField.setText(Integer.toString(inHouse.getMax()));
            modifyPartMinTextField.setText(Integer.toString(inHouse.getMin()));
            modifyPartMachineIdTextField.setText(Integer.toString(inHouse.getMachineId()));

            modifyPartCompNameLabel.setVisible(false);
            modifyPartCompNameTextField.setDisable(true);
            modifyPartCompNameTextField.setVisible(false);

            modifyPartMachineIdTextField.setVisible(true);
            modifyPartMachineIdTextField.setDisable(false);
            modifyPartMachineIdLabel.setVisible(true);


        }else if(type.equals("Outsourced")){

            Outsourced outsourced = (Outsourced)part;

            modifyPartRadioOutsourced.setSelected(true);

            modifyPartNameTextField.setText(outsourced.getName());
            modifyPartStockTextField.setText(Integer.toString(outsourced.getStock()));
            modifyPartPriceTextField.setText(Double.toString(outsourced.getPrice()));
            modifyPartMaxTextField.setText(Integer.toString(outsourced.getMax()));
            modifyPartMinTextField.setText(Integer.toString(outsourced.getMin()));
            modifyPartCompNameTextField.setText(outsourced.getCompanyName());

            modifyPartMachineIdLabel.setVisible(false);
            modifyPartMachineIdTextField.setDisable(true);
            modifyPartMachineIdTextField.setVisible(false);

            modifyPartCompNameTextField.setVisible(true);
            modifyPartCompNameLabel.setVisible(true);
            modifyPartCompNameTextField.setDisable(false);

        }

    }

    public void modifyPartCancelButtonClicked(){

        Main.window.setScene(Main.mainWindow);

    }

    public void modifyPartSaveButtonClicked(){

        if(modifyPartRadioInHouse.isSelected()) {

            checkModifyInput(modifyPartNameTextField, modifyPartStockTextField, modifyPartPriceTextField, modifyPartMaxTextField,
                    modifyPartMinTextField, modifyPartMachineIdTextField, getScene());

        }else if(modifyPartRadioOutsourced.isSelected()){

            checkModifyInput(modifyPartNameTextField, modifyPartStockTextField, modifyPartPriceTextField, modifyPartMaxTextField,
                    modifyPartMinTextField, modifyPartCompNameTextField, getScene());

        }

    }

    public void modifyPartRadioInHouseSelected(){

        modifyPartCompNameLabel.setVisible(false);
        modifyPartCompNameTextField.setDisable(true);
        modifyPartCompNameTextField.setVisible(false);

        modifyPartMachineIdTextField.setVisible(true);
        modifyPartMachineIdTextField.setDisable(false);
        modifyPartMachineIdLabel.setVisible(true);

    }

    public void modifyPartRadioOutsourcedSelected(){

        modifyPartMachineIdLabel.setVisible(false);
        modifyPartMachineIdTextField.setDisable(true);
        modifyPartMachineIdTextField.setVisible(false);

        modifyPartCompNameTextField.setVisible(true);
        modifyPartCompNameTextField.setDisable(false);
        modifyPartCompNameLabel.setVisible(true);

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

    //*********************************     Error Checking Functions    ************************************************
    public void modifyPartError(String message, Scene redoScene){

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

    public void checkModifyInput(TextField name, TextField stock, TextField price, TextField max, TextField min,
                              TextField mc, Scene scene){

        //Launches error window if text field is empty
        if (name.getText().isEmpty()){
            modifyPartError("Name Field cannot be empty", scene);
            return;
        }

        //Launches error window if field is empty or not a whole number
        try{
            Integer.parseInt(stock.getText());
        }catch(Exception e) {
            modifyPartError("Stock Field cannot be empty and must be a whole number", scene);
            return;
        }

        //Launches error window if field is empty
        try{
            Double.parseDouble(price.getText());
        }catch(Exception e){
            modifyPartError("Price Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Max field is empty/string
        try{
            Integer.parseInt(max.getText());
        }catch(Exception e){
            modifyPartError("Max Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Min field is empty/string
        try{
            Integer.parseInt(min.getText());
        }catch(Exception e){
            modifyPartError("Min Field cannot be empty/string", scene);
            return;
        }

        //If InHouse is selected checks that the field is not empty/string
        //Else Outsourced is selected checks that the field is not empty
        if(modifyPartRadioInHouse.isSelected()){
            try{
                Integer.parseInt(mc.getText());
            }catch(Exception e){
                modifyPartError("Machine ID Field cannot be empty/string", scene);
                return;
            }
        }else if(modifyPartRadioOutsourced.isSelected()){
            if(mc.getText().isEmpty()){
                modifyPartError("Company Name Field cannot be empty", scene);
                return;
            }
        }

        //Ensures that the value in the Max field is not less than the value in the Min field
        if(Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())){
            modifyPartError("Max cannot be less than Min", scene);
            return;
        }

        //Ensures that the Inventory Level is not less than the Min field or greater than the Max field
        if((Integer.parseInt(stock.getText()) < (Integer.parseInt(min.getText()))) || (Integer.parseInt(stock.getText()) > (Integer.parseInt(max.getText())))){
            modifyPartError("Inventory Level cannot be less than Min or greater than Max", scene);
            return;
        }

        //Creates an InHouse Part if the InHouse Radio Button is selected
        if(modifyPartRadioInHouse.isSelected()){
            String Name = name.getText();
            int Stock = Integer.parseInt(stock.getText());
            double Price = roundPrice(Double.parseDouble(price.getText()));
            int Max = Integer.parseInt(max.getText());
            int Min = Integer.parseInt(min.getText());
            int MachineId = Integer.parseInt(mc.getText());

            InHouse inHouse = new InHouse(getId(), Name, Price, Stock, Min, Max, MachineId);
            MainController.masterInventory.updatePart(getIndex(), inHouse);

            //Creates an Outsourced Part if the Outsourced Radio Button is selected
        }else if(modifyPartRadioOutsourced.isSelected()){

            String Name = name.getText();
            int Stock = Integer.parseInt(stock.getText());
            double Price = roundPrice(Double.parseDouble(price.getText()));
            int Max = Integer.parseInt(max.getText());
            int Min = Integer.parseInt(min.getText());
            String CompanyName = mc.getText();

            Outsourced outsourced = new Outsourced(getId(), Name, Price, Stock, Min, Max, CompanyName);
            MainController.masterInventory.updatePart(getIndex(), outsourced);

        }

        //Clears the TextFields
        name.clear();
        stock.clear();
        price.clear();
        max.clear();
        min.clear();
        mc.clear();

        //Sets the scene back to the main window
        Main.window.setScene(Main.mainWindow);

    }

    //******************************************************************************************************************


    //Using doubles has become problematic when adding prices together. This function will use Big Decimal to more
    //accurately calculate prices
    private static double roundPrice(double priceInput){

        BigDecimal bigDecimal = new BigDecimal(Double.toString(priceInput));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();

    }

}

