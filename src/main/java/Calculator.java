import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Calculator {

    @FXML private Label sumLbl, valuesLbl;
    @FXML private AnchorPane background;

    private int sum = 0;
    private ArrayList<Byte> values = new ArrayList<>();

    @FXML private void sumUp(){

        sumLbl.setText(String.valueOf(sum));

        sum = 0;
        values.clear();
    }

    @FXML private void clear(){

        sum = 0;
        values.clear();

        valuesLbl.setText("");
        sumLbl.setText(String.valueOf(sum));
    }

    private void addAndUpdate(byte value){

        values.add(value);
        valuesLbl.setText(String.join("+",values.toString()));
    }

    @FXML private void zero(){

        byte value = 0;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void one(){

        byte value = 1;

        sum += 1;
        addAndUpdate(value);
    }

    @FXML private void two(){

        byte value = 2;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void three(){

        byte value = 3;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void four(){

        byte value = 4;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void five(){

        byte value = 5;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void six(){

        byte value = 6;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void seven(){

        byte value = 7;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void eight(){

        byte value = 8;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void nine(){

        byte value = 9;

        sum += value;
        addAndUpdate(value);
    }

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }
}
