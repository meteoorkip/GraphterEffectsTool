package root;

import general.ViewModel;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import screens.idescreen.IDEView;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class RootPresenter implements Initializable {

    public StackPane rootPane;
    @Inject
    private ViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rootPane.setPrefHeight(350);
        viewModel.setMainView(rootPane);

    }

    public void loadIDE() {
        screens.idescreen.IDEView IDEView = new IDEView();
        rootPane.getChildren().add(IDEView.getView());
        //rootPane.wid
}
}
