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
    void checkCTL(ActionEvent event) {
        String stateName = states.getValue();
        String formulaCtl = formulaInput.getText();
        // fomular chekcing and CTL algorithm
        //parameter:  kripkeStructure, stateName, formulaCtl
        String res = "" ;
        result.setText( fc.getPath() + " "+ stateName + " --- " + formulaCtl );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        states.getItems().addAll("All States","s0","s1","s2","s3","s4","s5","s6","s7","s8","s9","s10");

    }

}