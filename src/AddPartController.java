import javafx.fxml.FXML;
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
import java.util.Comparator;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    public GridPane AddPartUI;

    private Scene scene;

    @FXML private Label addPartMachineIdLabel = new Label("Machine ID");
    @FXML private Label addPartCompNameLabel = new Label("Comp Name");

    @FXML private TextField addPartNameTextField = new TextField();
    @FXML private TextField addPartStockTextField = new TextField();
    @FXML private TextField addPartPriceTextField = new TextField();
    @FXML private TextField addPartMaxTextField = new TextField();
    @FXML private TextField addPartMinTextField = new TextField();
    @FXML private TextField addPartMachineIdTextField = new TextField();
    @FXML private TextField addPartCompNameTextField = new TextField();


    @FXML private ToggleGroup addPartToggleGroup = new ToggleGroup();

    @FXML private RadioButton addPartRadioInHouse = new RadioButton();
    @FXML private RadioButton addPartRadioOutsourced = new RadioButton();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addPartMachineIdLabel.setVisible(true);
        addPartMachineIdTextField.setPromptText("Mach ID");
        addPartMachineIdTextField.setVisible(true);

        addPartCompNameLabel.setVisible(false);
        addPartCompNameTextField.setPromptText("Comp Name");
        addPartCompNameTextField.setVisible(false);

        addPartRadioInHouse.setToggleGroup(addPartToggleGroup);
        addPartRadioOutsourced.setToggleGroup(addPartToggleGroup);
        addPartRadioInHouse.setSelected(true);



    }

    //*********************************     Core Functions  ************************************************************
    public void addPartRadioInHouseSelected(){

        addPartCompNameLabel.setVisible(false);
        addPartCompNameTextField.setDisable(true);
        addPartCompNameTextField.clear();
        addPartCompNameTextField.setVisible(false);

        addPartMachineIdTextField.setVisible(true);
        addPartMachineIdTextField.setDisable(false);
        addPartMachineIdLabel.setVisible(true);

    }

    public void addPartRadioOutsourcedSelected(){

        addPartMachineIdLabel.setVisible(false);
        addPartMachineIdTextField.setDisable(true);
        addPartMachineIdTextField.clear();
        addPartMachineIdTextField.setVisible(false);


        addPartCompNameTextField.setVisible(true);
        addPartCompNameTextField.setDisable(false);
        addPartCompNameLabel.setVisible(true);

    }

    public void addPartCancelButtonClicked(){

        Main.window.setScene(Main.mainWindow);

    }

    public void addPartSaveButtonClicked(){

        if(addPartRadioInHouse.isSelected()) {

            checkAddInput(addPartNameTextField, addPartStockTextField, addPartPriceTextField, addPartMaxTextField,
                    addPartMinTextField, addPartMachineIdTextField, getScene());

        }else if(addPartRadioOutsourced.isSelected()){

            checkAddInput(addPartNameTextField, addPartStockTextField, addPartPriceTextField, addPartMaxTextField,
                    addPartMinTextField, addPartCompNameTextField, getScene());

        }

    }

    //******************************************************************************************************************

    //*********************************     Error Checking Functions    ************************************************
    public void partError(String message, Scene redoScene){

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

    public void checkAddInput(TextField name, TextField stock, TextField price, TextField max, TextField min,
                              TextField mc, Scene scene){

        int Id = getNextId();

        //Launches error window if text field is empty
        if (name.getText().isEmpty()){
            partError("Name Field cannot be empty", scene);
            return;
        }

        //Launches error window if field is empty or not a whole number
        try{
            Integer.parseInt(stock.getText());
        }catch(Exception e) {
            partError("Stock Field cannot be empty and must be a whole number", scene);
            return;
        }

        //Launches error window if field is empty
        try{
            Double.parseDouble(price.getText());
        }catch(Exception e){
            partError("Price Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Max field is empty/string
        try{
            Integer.parseInt(max.getText());
        }catch(Exception e){
            partError("Max Field cannot be empty/string", scene);
            return;
        }

        //Launches error window if Min field is empty/string
        try{
            Integer.parseInt(min.getText());
        }catch(Exception e){
            partError("Min Field cannot be empty/string", scene);
            return;
        }

        //If InHouse is selected checks that the field is not empty/string
        //Else Outsourced is selected checks that the field is not empty
        if(addPartRadioInHouse.isSelected()){
            try{
                Integer.parseInt(mc.getText());
            }catch(Exception e){
                partError("Machine ID Field cannot be empty/string", scene);
                return;
            }
        }else if(addPartRadioOutsourced.isSelected()){
            if(mc.getText().isEmpty()){
                partError("Company Name Field cannot be empty", scene);
                return;
            }
        }

        //Ensures that the value in the Max field is not less than the value in the Min field
        if(Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())){
            partError("Max cannot be less than Min", scene);
            return;
        }

        //Ensures that the Inventory Level is not less than the Min field or greater than the Max field
        if((Integer.parseInt(stock.getText()) < (Integer.parseInt(min.getText()))) || (Integer.parseInt(stock.getText()) > (Integer.parseInt(max.getText())))){
            partError("Inventory Level cannot be less than Min or greater than Max", scene);
            return;
        }

        //Creates an InHouse Part if the InHouse Radio Button is selected
        if(addPartRadioInHouse.isSelected()){
            String Name = name.getText();
            int Stock = Integer.parseInt(stock.getText());
            double Price = roundPrice(Double.parseDouble(price.getText()));
            int Max = Integer.parseInt(max.getText());
            int Min = Integer.parseInt(min.getText());
            int MachineId = Integer.parseInt(mc.getText());

            InHouse inHouse = new InHouse(Id, Name, Price, Stock, Min, Max, MachineId);
            MainController.masterInventory.addPart(inHouse);

        //Creates an Outsourced Part if the Outsourced Radio Button is selected
        }else if(addPartRadioOutsourced.isSelected()){

            String Name = name.getText();
            int Stock = Integer.parseInt(stock.getText());
            double Price = roundPrice(Double.parseDouble(price.getText()));
            int Max = Integer.parseInt(max.getText());
            int Min = Integer.parseInt(min.getText());
            String CompanyName = mc.getText();

            Outsourced outsourced = new Outsourced(Id, Name, Price, Stock, Min, Max, CompanyName);
            MainController.masterInventory.addPart(outsourced);

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

    //Generates a new ID for the part to add. Goes through list of Parts and gets the next available integer going up.
    public int getNextId(){

        //Will hold the value of the next available ID
        int nextId = 0;

        //Sorts the list of parts in the inventory from lowest to greatest.
        Comparator<Part> comparator = Comparator.comparingInt(Part::getId);
        MainController.masterInventory.getAllParts().sort(comparator);

        //Loops through the list of all parts and finds the next available ID
        for (int i = 0; i < MainController.masterInventory.getAllParts().size(); i++){

            //This condition only happens if the next available number is 1 + the highest ID in the list
            if (i == (MainController.masterInventory.getAllParts().size() - 1)){
                nextId = MainController.masterInventory.getAllParts().get(i).getId() + 1;
                break;

             //This condition happens up until the last index
            }else{

                //If the ID of the current part index + 1 is equal to the ID of the next part's index, continue to the next
                //iteration
                if ((MainController.masterInventory.getAllParts().get(i).getId() + 1) ==
                        (MainController.masterInventory.getAllParts().get(i+1).getId())){
                    continue;

                 //If the i does not equal the ID of the next item in the list, set nextId to i
                }else{
                    nextId = MainController.masterInventory.getAllParts().get(i).getId() + 1;
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
