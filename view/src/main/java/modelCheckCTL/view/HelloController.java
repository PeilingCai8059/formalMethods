package modelCheckCTL.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import modelCheckCTL.controller.*;
import modelCheckCTL.model.KripkeStructure;

public class HelloController implements Initializable {

    @FXML
    private Button modelFile;

    @FXML
    private TextField formulaInput;

    @FXML
    private TextArea result;

    @FXML
    private Button startCheck;

    @FXML
    private ChoiceBox<String> states;

    private FileChooser file = new FileChooser();
    public File fc;
    public String fileContent;
    public KripkeStructure kripkeStructure;

    @FXML
    void chooseModelFile(ActionEvent event) {
        file.setTitle("Choose Model File");
        fc = file.showOpenDialog(null);
        fileContent = FileParser.fileToString(fc);
        try {
            kripkeStructure = new KripkeStructure(fileContent);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Broken Model ! \n" + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void checkCTL(ActionEvent event) throws Exception {
        String stateName = states.getValue();
        // check if the input state is valid in the kripke structure
        if(!kripkeStructure.containsState(stateName)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("The Kripke Structure doesn't contains state : " + stateName );
            alert.showAndWait();
            throw new Exception("Invalid State");
        }

        String formulaCtl = formulaInput.getText();
        // fomular chekcing and CTL algorithm
        CTL_Checker checker = new CTL_Checker(kripkeStructure, formulaCtl,stateName);
        boolean ifHold  = checker.ifHold() ;
        String res = "";
        if(ifHold) {
            res = formulaCtl + " holds";
        }else {
            res = formulaCtl + " doesn't hold";
        }
        result.setText( "In model " + fc.getName() + "\n"+ stateName + ": " +res );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        states.getItems().addAll("All States","s0","s1","s2","s3","s4","s5","s6","s7","s8","s9","s10");

    }

}