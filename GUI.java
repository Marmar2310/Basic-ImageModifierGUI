
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUI extends Application {
	private VBox root = new VBox();
	private File file = null;
	private Image image = null;
	private Image newImage = null;
	private ImageView imgView = null;

	@Override
	public void start(Stage stage) throws Exception {

		stage.setWidth(500);
		stage.setHeight(500);
		stage.setTitle("Edge Detection Tool");

		Menu toolMenu = new Menu("Tools");
		MenuItem edgeItem = new MenuItem("Edge Detection");
		MenuItem revertItem = new MenuItem("Revert");
		Menu fileMenu = new Menu("File");
		MenuItem openItem = new MenuItem("Open");
		MenuItem closeItem = new MenuItem("Close");

		closeItem.setDisable(true);
		edgeItem.setDisable(true);
		revertItem.setDisable(true);

		openItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent open) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Image File");
				file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					loadImageFile(file);
					closeItem.setDisable(false);
					edgeItem.setDisable(false);
					openItem.setDisable(true);
				}
			}
		});

		closeItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent close) {
				root.getChildren().remove(1);
				closeItem.setDisable(true);
				edgeItem.setDisable(true);
				openItem.setDisable(false);
			}
		});

		edgeItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent edge) {
				revertItem.setDisable(false);
				edgeItem.setDisable(true);
				EdgeDetector edgeting = new EdgeDetector();
				root.getChildren().remove(1);
				newImage = edgeting.filterImage(image);
				ImageView newImgView = new ImageView(newImage);
				root.getChildren().add(newImgView);
			}
		});

		revertItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				revertItem.setDisable(true);
				edgeItem.setDisable(false);
				root.getChildren().remove(1);
				root.getChildren().add(imgView);
			}
		});

		toolMenu.getItems().addAll(edgeItem, revertItem);
		fileMenu.getItems().addAll(openItem, closeItem);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, toolMenu);
		root.getChildren().add(menuBar);

		stage.setScene(new Scene(root, 500, 500));
		stage.show();

	}

	public void loadImageFile(File file) {
		image = new Image("file:" + file.getPath());
		imgView = new ImageView(image);
		root.getChildren().add(imgView);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
