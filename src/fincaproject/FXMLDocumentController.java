/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 *
 * @author sevas
 */
public class FXMLDocumentController implements Initializable {
    private Connection con;
    private PreparedStatement statement;
    private ResultSet result;
    private ObservableList items = FXCollections.observableArrayList();
    private File logFile = new File("Log.txt");
    
    @FXML
    private ComboBox<String> registrosComboBox;
    @FXML
    private ListView<String> listView1;
    @FXML
    private GridPane animalesGridPane;
    @FXML
    private ComboBox<String> tipoComboBox;
    @FXML
    private DatePicker datePickerAnimal;
    @FXML
    private TextField numeroPartos;
    @FXML
    private Label labelPartos;
    @FXML
    private ImageView imageView;
    @FXML
    private Label labelPartos1;
    @FXML
    private TextField corralIdAnimales;
    @FXML
    private Label labelPartos2;
    @FXML
    private TextField ventaTextField;
    @FXML
    private Pane photoPane;
    @FXML
    private GridPane buttonPhotoPane;
    @FXML
    private Label tipoAnimalLabel;
    @FXML
    private TextField precioFinalAnimal;
    @FXML
    private TextField precioActualAnimal;
    @FXML
    private GridPane corralGridPane;
    @FXML
    private TextField ubicacionCorral;
    @FXML
    private TextField numeroAnimales;
    @FXML
    private TextField diasCorral;
    @FXML
    private DatePicker desdeCorral;
    @FXML
    private DatePicker hastaCorral;
    @FXML
    private TableView<AnimalBaja> dadoDeBajaTable;
    @FXML
    private TableColumn<AnimalBaja, String> animalIdColumn;
    @FXML
    private TableColumn<AnimalBaja, String> fNacimientoColumn;
    @FXML
    private TableColumn<AnimalBaja, String> nPartosColumn;
    @FXML
    private TableColumn<AnimalBaja, String> tipoColumn;
    @FXML
    private TableColumn<AnimalBaja, String> fBajaColumn;
    @FXML
    private TableColumn<AnimalBaja, String> lugarColumn;
    @FXML
    private TableColumn<AnimalBaja, String> causaColumn;
    @FXML
    private TableColumn<AnimalBaja, String> precioColumn;
    @FXML
    private TextField byIdDadoDeBaja;
    @FXML
    private Label byIdLabel;
    @FXML
    private GridPane vacunasGridPane;
    @FXML
    private Label vacunaIdTextField;
    @FXML
    private TextField nombreVacunaTextField;
    @FXML
    private ComboBox<String> selectedCorral;
    @FXML
    private GridPane vacunarGridPane;
    @FXML
    private ListView<String> vacunasListView;
    @FXML
    private DatePicker fechaVacuna;
    @FXML
    private ComboBox<String> selectedCorral1;
    @FXML
    private ComboBox<String> selectedVacuna;
    @FXML
    private GridPane hijosGridpane;
    @FXML
    private ListView<String> hijosListView;
    @FXML
    private RadioButton madresActivas;
    @FXML
    private RadioButton madresInactivas;
    @FXML
    private GridPane ventaGridPane;
    @FXML
    private ListView<String> animalesEnVentaListView;
    @FXML
    private Label numeroAnimalesVenta;
    @FXML
    private TextField tipoVenta;
    @FXML
    private TextField precioVenta;
    @FXML
    private Label estadoVenta;
    @FXML
    private Label fechaVentaLabel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeConnetion();
        try {
            loadItems();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadItems() throws SQLException, IOException{
        if (logFile.createNewFile()) {
            System.out.println("File has been created.");
        } else {
        
            System.out.println("File already exists.");
        }
        
        executeQuery("SELECT table_name FROM information_schema.tables\n" +"WHERE table_schema = 'finca';"); 
        while(result.next())
            registrosComboBox.getItems().add(result.getString("TABLE_NAME"));
        result.close();
        
        executeQuery("select tipo from animal group by tipo;");
        tipoComboBox.getItems().add("All");
        while(result.next())       
            tipoComboBox.getItems().add(result.getString("tipo"));
        
        executeQuery("select ubicacionCorral from corral");
        selectedCorral.getItems().add("All");
        selectedCorral1.getItems().add("All");
        while(result.next()){
            selectedCorral1.getItems().add(result.getString("ubicacionCorral"));
            selectedCorral.getItems().add(result.getString("ubicacionCorral"));
        }
        
        executeQuery("select nombre from vacunas");
        selectedVacuna.getItems().add("All");
        
        while(result.next()){
            selectedVacuna.getItems().add(result.getString("nombre"));
        }
        
       
   		animalesGridPane.setVisible(false);
        corralGridPane.setVisible(false);
        listView1.setVisible(false);
        dadoDeBajaTable.setVisible(false);
        byIdDadoDeBaja.setVisible(false);
        byIdLabel.setVisible(false);
        vacunarGridPane.setVisible(false);
        vacunasListView.setVisible(false);
        vacunasGridPane.setVisible(false);
        hijosGridpane.setVisible(false);
        ventaGridPane.setVisible(false);
    }  

    
    private void makeConnetion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/finca","root","Marlon,Rodriguez1"
            );
                
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @FXML
    private void seleccionarTabla(ActionEvent event) throws SQLException, ParseException {
        String selected = registrosComboBox.getSelectionModel().getSelectedItem();
        animalesGridPane.setVisible(false);
        corralGridPane.setVisible(false);
        listView1.setVisible(false);
        dadoDeBajaTable.setVisible(false);
        byIdDadoDeBaja.setVisible(false);
        byIdLabel.setVisible(false);
        vacunarGridPane.setVisible(false);
        vacunasListView.setVisible(false);
        vacunasGridPane.setVisible(false);
        hijosGridpane.setVisible(false);
        ventaGridPane.setVisible(false);
        
        listView1.getItems().clear();
        executeQuery("SELECT * FROM " + selected + ";");
        switch(selected){
            case "animal":
                animalesGridPane.setVisible(true);
                listView1.setVisible(true);
                while(result.next()){
                    String activo = result.getString("estado");
                    
                    if(activo.equals("1")){
                        String id = result.getString("animalId");
                        listView1.getItems().add(id);
                    }
                }  
                
                break;
            case "corral":
                corralGridPane.setVisible(true);
                listView1.setVisible(true);
                while(result.next()){
                    String id = result.getString("corralId");
                    listView1.getItems().add(id);
                }  
                
                break;
            case "dadodebaja":
                dadoDeBajaTable.setVisible(true);
                byIdDadoDeBaja.setVisible(true);
                byIdLabel.setVisible(true);
                items.clear();
                
                animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
                fNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
                nPartosColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPartos"));
                tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
                fBajaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaBaja"));
                lugarColumn.setCellValueFactory(new PropertyValueFactory<>("lugar"));
                causaColumn.setCellValueFactory(new PropertyValueFactory<>("causa"));
                precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
                
                executeQuery("SELECT * FROM dadodebaja;");
                while(result.next()){
                    String animalId = result.getString("animalId");
                    String fechaNacimiento = result.getString("fechaNacimiento");
                    String numeroPartos = result.getString("numeroPartos");
                    String tipo = result.getString("tipo");
                    String fechaBaja = result.getString("fechaBaja");
                    String lugar = result.getString("lugar");
                    String causa = result.getString("causa");
                    String precio = result.getString("precioInicial");
                    dadoDeBajaTable.getSortOrder().add(fBajaColumn);
                    items.add(new AnimalBaja(animalId,fechaNacimiento,numeroPartos,tipo,fechaBaja,lugar,causa,precio));
                    dadoDeBajaTable.setItems(items);
                }
                
                byIdDadoDeBaja.textProperty().addListener((observable, oldValue, newValue) -> {
                    items.clear();
                    try {
                        executeQuery("select * from dadodebaja where animalId like '"+newValue+"%';");
                        while(result.next()){
                            String animalId = result.getString("animalId");
                            String fechaNacimiento = result.getString("fechaNacimiento");
                            String numeroPartos = result.getString("numeroPartos");
                            String tipo = result.getString("tipo");
                            String fechaBaja = result.getString("fechaBaja");
                            String lugar = result.getString("lugar");
                            String causa = result.getString("causa");
                            String precio = result.getString("precioInicial");
                            items.add(new AnimalBaja(animalId,fechaNacimiento,numeroPartos,tipo,fechaBaja,lugar,causa,precio));
                            dadoDeBajaTable.setItems(items);
                        }  
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                         Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                break;
            case "vacunas":
                listView1.setVisible(true);
                vacunasGridPane.setVisible(true);
                
                executeQuery("select * from vacunas");
                
                listView1.getItems().clear();
                while(result.next()){
                    listView1.getItems().add(result.getString("nombre"));
                }
                
                break;
            case "vacunacion":
                vacunarGridPane.setVisible(true);
                vacunasListView.setVisible(true);
                listView1.setVisible(true);
                
                listView1.getItems().clear();
                executeQuery("select animalId from animal where estado = b'1';");
                while(result.next()){
                    listView1.getItems().add(result.getString("animalId"));
                }
                break;
            case "hijos":
                hijosGridpane.setVisible(true);
                listView1.setVisible(true);
                ToggleGroup group = new ToggleGroup();
                madresActivas.setSelected(true);
                madresActivas.setToggleGroup(group);
                madresInactivas.setToggleGroup(group);
                
                listView1.getItems().clear();
                executeQuery("select hijos.animalId from hijos inner join animal\n" +
                    "on hijos.animalId = animal.animalId where animal.estado = b'1' group by hijos.animalId;");
                while(result.next()){
                    listView1.getItems().add(result.getString("animalId"));
                }
                
                hijosListView.getItems().clear();
                break;
            case "venta":
                ventaGridPane.setVisible(true);
                listView1.setVisible(true);
                
                listView1.getItems().clear();
                executeQuery("select ventaId from venta");
                result.next();
                while(result.next()){
                    listView1.getItems().add(result.getString("ventaId"));
                }
                
                
                break;
        }
        
    }

    @FXML
    private void seleccionarItemRegistros(MouseEvent event) throws SQLException, FileNotFoundException, IOException {
        String selectedTable = registrosComboBox.getSelectionModel().getSelectedItem();
        String selected = listView1.getSelectionModel().getSelectedItem();
        
        switch(selectedTable){
            case "animal":
                executeQuery("select * from animal where animalId ="+selected+";");
                result.next();
                String tipo = result.getString("tipo");
                if(!tipo.equals("Vaca")){
                    numeroPartos.setVisible(false);
                    labelPartos.setVisible(false);
                }
                else{
                    numeroPartos.setVisible(true);
                    labelPartos.setVisible(true);
                }
                
                String [] split = result.getString("fechaNacimiento").split("-");     

                Blob blob = result.getBlob("foto");
                if(blob != null){
                    InputStream is = blob.getBinaryStream();
                    Image image = new Image(is);
                    imageView.setImage(image);
                }
                else
                    imageView.setImage(null);
                
                datePickerAnimal.setValue(null);
                if(!result.getString("fechaNacimiento").equals("1111-11-11"))
                    datePickerAnimal.setValue(LocalDate.of(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2])));
                numeroPartos.setText(result.getString("numeroPartos"));
                corralIdAnimales.setText(result.getString("corralId"));
                ventaTextField.setText(result.getString("ventaId"));
                tipoAnimalLabel.setText(result.getString("tipo"));
                precioActualAnimal.setText(result.getString("precioInicial"));
                precioFinalAnimal.setText(result.getString("precioFinal"));
                photoPane.setVisible(true);
                buttonPhotoPane.setVisible(false);
                break;
            case "corral":
                executeQuery("select * from corral where corralId ="+selected+";");
                result.next();
                ubicacionCorral.setText(result.getString("ubicacionCorral"));
                
                numeroAnimales.setText(result.getString("numeroDeAnimales"));
                diasCorral.setText(result.getString("diasEnCorral"));
                
                desdeCorral.setValue(null);
                
                executeQuery("select desde from corral where corralId ="+selected);
                result.next();
                if(result.getString("desde")!= null && !result.getString("desde").equals("1111-11-11")){
                    String [] s = result.getString("desde").split("-");
                    desdeCorral.setValue(LocalDate.of(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2])));
                }
                
                hastaCorral.setValue(null);
                executeQuery("select hasta from corral where corralId ="+selected);
                result.next();
                if(result.getString("hasta")!= null && !result.getString("hasta").equals("1111-11-11")){
                    String [] s = result.getString("hasta").split("-");
                    hastaCorral.setValue(LocalDate.of(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2])));
                }
              
                break;
            case "vacunas":
                executeQuery("SELECT * FROM finca.vacunas where nombre = '" + selected+"';");
                result.next();
                vacunaIdTextField.setText(result.getString("vacunaId"));
                nombreVacunaTextField.setText(result.getString("nombre"));
                break;
            case "vacunacion":
                fechaVacuna.setValue(null);
                executeQuery("select nombre from vacunacion inner join vacunas \n" +
                    "on vacunacion.vacunaId = vacunas.vacunaId where animalId = "+selected+";");
                vacunasListView.getItems().clear();
                if(result.next())
                	do{
                		vacunasListView.getItems().add(result.getString("nombre"));
                	}while(result.next());
            	else
            		message("El animal selecionado no cuenta con ninguna vacuna",1);
                break;
            case "hijos":
                executeQuery("select hijosId from hijos where animalId = "+selected+";");
                hijosListView.getItems().clear();
                while(result.next()){
                    hijosListView.getItems().add(result.getString("hijosId"));
                }
                break;
            case "venta":
                executeQuery("select animalId from animal where ventaId = "+selected+";");
                animalesEnVentaListView.getItems().clear();
                
                while(result.next())
                    animalesEnVentaListView.getItems().add(result.getString("animalId"));
                
                executeQuery("select * from venta where ventaId = " + selected);
                
                result.next();
                
                precioVenta.setText(result.getString("precio"));
                tipoVenta.setText(result.getString("tipo"));
                numeroAnimalesVenta.setText(result.getString("numeroAnimales"));
                fechaVentaLabel.setText(result.getString("fecha"));
                
                executeQuery("select estado from animal where ventaId = "+selected+" and estado = b'1' limit 1;");
                if(result.next()){
                    estadoVenta.setText("No Vendido");
                    estadoVenta.setTextFill(Color.GREEN);
                }
                executeQuery("select estado from animal where ventaId = "+selected+" and estado = b'0' limit 1;");
                if(result.next()){
                    estadoVenta.setText("Vendido");
                    estadoVenta.setTextFill(Color.RED);
                }
                else{
                    estadoVenta.setText("No Vendido");
                    estadoVenta.setTextFill(Color.GREEN);
                }
                    
                
                break;

            }
        
    }
    
    private void message(String message, int valor){
        Alert alert = null;
        if(valor == 1){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
        }
            
        alert.setHeaderText(null);
        alert.setContentText(message);
        

        alert.show();
    }


    @FXML
    private void seleccionarTipo(ActionEvent event) throws SQLException {
        listView1.getItems().clear();
        imageView.setImage(null);
        labelPartos.setVisible(false);
        numeroPartos.setVisible(false);
        
        datePickerAnimal.getEditor().clear();
        String selected_text = tipoComboBox.getSelectionModel().getSelectedItem();
        
        if(selected_text.equals("All")){
            listView1.getItems().clear();
            
            executeQuery("SELECT * FROM animal where estado = b'1';");
   
            while(result.next()){
                String id = result.getString("animalId");
                listView1.getItems().add(id);
            }
        }
        else{
            executeQuery("select animalId from animal where tipo = '"+selected_text+"'and estado = b'1';");

            while(result.next()){
                String id = result.getString("animalId");
                listView1.getItems().add(id);
            }
            
        }

    }
    
    private void executeQuery(String query) throws SQLException{
        statement = con.prepareStatement(query);
        result = statement.executeQuery();        
    }
    private void executeUpdate(String query) throws SQLException{
        statement = con.prepareStatement(query);
        statement.executeUpdate();

        try{
            FileWriter fr = new FileWriter(logFile, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(java.time.LocalDate.now() + "  " + query + "  \n\n");

            br.close();
            fr.close(); 
        }catch(IOException e){
            message("No se pudo escribir en el Log",0);
        }
    }
  

    @FXML
    private void nuevoAnimal(ActionEvent event) throws SQLException, FileNotFoundException {
        String selectedTable = registrosComboBox.getSelectionModel().getSelectedItem();
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Nuevo " + selectedTable);
        dialog.setResizable(true);
        
        ComboBox<String> tipo = new ComboBox();
        DatePicker date = new DatePicker();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        TextField text4 = new TextField();
        TextField text6 = new TextField();
        Button choose = new Button();
        TextField text7 = new TextField("1");
        TextField text8 = new TextField("0");
        TextField text9 = new TextField("0");
        
        choose.setText("Escoge una imagen");
        tipo.setValue("None");
        
        tipo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String selected = tipo.getSelectionModel().getSelectedItem();
                text3.setDisable(true);
                text3.clear();
                if(selected.equals("None"))
                    text3.setDisable(false);
            }
        });
  
        
        choose.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(null);
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

                if (selectedFile != null) {

                    text4.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        Label label1 = new Label("Tipo");
        Label label2 = new Label("Animal ID: ");
        Label label3 = new Label("Fecha de Nacimiento: ");
        Label label4 = new Label("Nuevo Tipo: ");
        Label label5 = new Label("Escoger Imagen: ");
        Label label6 = new Label("Corral ID: ");
        Label label7 = new Label("Numero de Animales: ");
        Label label8 = new Label("Precio Actual: ");
        Label label9 = new Label("Precio Final: ");
        
        
        executeQuery("select tipo from animal group by tipo;");
        tipo.getItems().add("None");
        while(result.next())       
            tipo.getItems().add(result.getString("tipo"));
        
        
        executeQuery("select animalId from animal order by animalId desc limit 1;");
        int animalId = 1;
        int id2 = 0;
        int id1 = 1;
        
        if(result.next())
            id1 = result.getInt("animalId") + 1;
        executeQuery("select animalId from dadodebaja order by animalId desc limit 1;");
        if(result.next())
            id2 = result.getInt("animalId") + 1;
        if(id1 > id2)
            animalId = id1;
        else
            animalId = id2;

        text2.setText(animalId + "");
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

        grid.add(label1, 1, 1);
        grid.add(tipo, 2, 1);
        grid.add(label4,3, 1);
        grid.add(text3, 4,1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(date, 2, 3);
        grid.add(label5,1,4);
        grid.add(text4,2,4);
        grid.add(choose,4,4);
        grid.add(label6,1,5);
        grid.add(text6, 2, 5);
        grid.add(label7,1,7);
        grid.add(text7,2,7);
        grid.add(label8,1,8);
        grid.add(label9,1,9);
        grid.add(text8,2,8);
        grid.add(text9,2,9);
        
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String t = tipo.getSelectionModel().getSelectedItem();
                    if(t.equals("None")){
                        t = text3.getText();
                    }
                    String i = text2.getText();
                    String d = "1111-11-11";
                    if(date.getValue() != null)
                        d = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String p = text4.getText();
                    
                    String pA = text8.getText();
                    String pF = text9.getText();
                    
                    if(p == null || p.equals(""))
                        p =  "noPhoto.png";
                    
                    String c = text6.getText();
                    
                    String [] values = {i,d,p,t,c,pA,pF};
                    
                    return values;
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        String temp = null;
        if (opcion.isPresent()){
            String [] values = opcion.get();
            
            try{
                               
                if(Integer.parseInt(text7.getText()) > 1){
                    int num = Integer.parseInt(values[0])+Integer.parseInt(text7.getText());
                    for(int i = Integer.parseInt(values[0]); num > i;i++){
                        
                        executeQuery("select animalId from dadodebaja where animalId = " + i);
                        if(result.next()){
                            temp = result.getString("animalId");
                            throw new SQLException("Error");
                        }
                        String statement = "INSERT INTO `finca`.`animal` (`animalId`, "
                                + "`fechaNacimiento`, `estado`, `foto`, `tipo`, `corralId`, `ventaId`,precioInicial,precioFinal)"
                        + "VALUES ('"+i+"', '"+values[1]+"', b'1', ?, '"+values[3]+"', '"
                                + values[4]+"', '0','"+values[5]+"','"+values[6]+"');" ;
                        
                        PreparedStatement pstmt = con.prepareStatement(statement);

                        InputStream in = new FileInputStream(values[2]);

                        pstmt.setBlob(1, in);

                        pstmt.execute();
                        
                        try{
                            FileWriter fr = new FileWriter(logFile, true);
                            BufferedWriter br = new BufferedWriter(fr);
                            br.write(java.time.LocalDate.now() + "  " + statement + "  \n\n");

                            br.close();
                            fr.close(); 
                        }catch(IOException e){
                            message("No se pudo escribir en el Log",0);
                        }

                    }   
                }
                else{
                    executeQuery("select animalId from dadodebaja where animalId = " + values[0]);
                    if(result.next()){
                        temp = result.getString("animalId");
                        throw new SQLException("Error");
                    }
                    
                    String statement = "INSERT INTO `finca`.`animal` (`animalId`, `fechaNacimiento`,"
                            + " `estado`, `foto`, `tipo`, `corralId`, `ventaId`,precioInicial,precioFinal)"
                        + "VALUES ('"+values[0]+"', '"+values[1]+"', b'1', ?, '"+values[3]+"', '" + values[4]+"', '0','"+
                            values[5]+"','"+values[6]+"');" ;
                    PreparedStatement pstmt = con.prepareStatement(statement);

                    InputStream in = new FileInputStream(values[2]);

                    pstmt.setBlob(1, in);

                    pstmt.execute();
                    try{
                            FileWriter fr = new FileWriter(logFile, true);
                            BufferedWriter br = new BufferedWriter(fr);
                            br.write(java.time.LocalDate.now() + "  " + statement + "  \n\n");

                            br.close();
                            fr.close(); 
                        }catch(IOException e){
                            message("No se pudo escribir en el Log",0);
                        }

                }
            }catch(FileNotFoundException | NumberFormatException | SQLException e){
                if(temp != null)
                    message("Error: el animal ya existe en dadoDeBaja con el Id = " + temp,0);
                else
                    //message("Error: Datos mal ingresados",0);
                    e.printStackTrace();
            }
            
        }
        listView1.getItems().clear();

        animalesGridPane.setVisible(true);
        executeQuery("SELECT * FROM animal ;");

        while(result.next()){
            String id = result.getString("animalId");
            listView1.getItems().add(id);
        }
        executeQuery("select tipo from animal group by tipo;");
        tipoComboBox.getItems().clear();
        tipoComboBox.getItems().add("All");
        while(result.next())       
            tipoComboBox.getItems().add(result.getString("tipo"));
    }


    @FXML
    private void verPhotoButton(ActionEvent event) {
        ImageView i = new ImageView();
        i.setImage(imageView.getImage());
        i.setFitHeight(768);
        i.setFitWidth(576);
        
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Imagen");
        dialog.setResizable(false);
        GridPane pane = new GridPane();
        
        
        pane.add(i, 1, 1);
        
        dialog.getDialogPane().setContent(pane);
        
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.show();
        buttonPhotoPane.setVisible(false);
        photoPane.setVisible(true);
    }

    @FXML
    private void newPhotoButton(ActionEvent event) throws SQLException, FileNotFoundException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nueva Imagen");
        dialog.setResizable(false);
        GridPane pane = new GridPane();
        
        Label label = new Label("Escoja una Imagen: ");
        TextField text = new TextField();
        Button button = new Button("Escoger");
        
        pane.add(label, 0, 0);
        pane.add(text,1,0);
        pane.add(button, 1, 2);
        
        dialog.getDialogPane().setContent(pane);
        
        button.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(null);
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

                if (selectedFile != null) {

                    text.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType b) {

                if (b == buttonTypeOk) {
                   
                    String i = text.getText();
              
                    return i;
                }else{
                    photoPane.setVisible(true);
                    buttonPhotoPane.setVisible(false);
                }
                return null;
            }
        });
        
        Optional<String> opcion = dialog.showAndWait();
        if (opcion.isPresent()){

            String path = opcion.get();
            String selected = listView1.getSelectionModel().getSelectedItem();
            String preSelected = selected;
            String corral = corralIdAnimales.getText();
            String venta = ventaTextField.getText();

            PreparedStatement pstmt = con.prepareStatement("UPDATE animal SET foto = ? WHERE (animalId = '"+selected+"') "+
                    "and (corralId = '"+corral+"') and (ventaId = '"+venta+"');" );
            
            InputStream in = new FileInputStream(path);
            
            pstmt.setBlob(1, in);
            
            pstmt.execute();
            animalesGridPane.setVisible(true);
            executeQuery("SELECT * FROM animal ;");
            listView1.getItems().clear();
            while(result.next()){
                String id = result.getString("animalId");
                listView1.getItems().add(id);
            } 
            buttonPhotoPane.setVisible(false);
            
            listView1.getSelectionModel().select(preSelected);
            
            executeQuery("select * from animal where animalId ="+preSelected+";");
               result.next();

            Blob blob = result.getBlob("foto");
            if(blob != null){
                InputStream is = blob.getBinaryStream();
                Image image = new Image(is);
                imageView.setImage(image);
            }
            else
                imageView.setImage(null);
            photoPane.setVisible(true);
            
        }
        
        
    }

    @FXML
    private void photoClicked(MouseEvent event) {
        buttonPhotoPane.setVisible(true);
        photoPane.setVisible(false);
    }

    @FXML
    private void eliminarAnimalButton(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Animal");
        alert.setHeaderText("Elimar un animal");
        alert.setContentText("Esta seguro de eliminar el animal con el ID = " + selected + " ?");
        

        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() == ButtonType.OK){
            executeUpdate("DELETE FROM animal WHERE (`animalId` = '"+selected+"');");
            executeQuery("SELECT * FROM animal ;");
            listView1.getItems().clear();
            while(result.next()){
                String id = result.getString("animalId");
                listView1.getItems().add(id);
            }
            imageView.setImage(null);
            datePickerAnimal.setValue(null);
            numeroPartos.setText(null);
            corralIdAnimales.setText(null);
            ventaTextField.setText(null);
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
 
        animalesGridPane.setVisible(true);
        
    }

    @FXML
    private void darDeBajaButton(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dar de Baja");
        dialog.setHeaderText("Dar de baja un animal");
        dialog.setContentText("Esta seguro de dar de baja el animal con el ID = " + selected + " ?");
        
        dialog.setResizable(true);
        
        DatePicker date = new DatePicker();
        TextField text1 = new TextField();
        TextField text2 = new TextField();

        Label label1 = new Label("Fecha: ");
        Label label2= new Label("Lugar: ");
        Label label3 = new Label("Causa: ");
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        
        grid.add(label1, 1, 1);
        grid.add(date,2, 1);
        grid.add(label2, 1, 2);
        grid.add(text1, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text2, 2, 3);
      
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String [] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    
                    String d = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String l = text1.getText();
                    String c = text2.getText();
                    String [] input = {d,l,c};
                    return input;     
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        
        if (opcion.isPresent()){
            
            
            String [] values = opcion.get();
            
            try{
                executeUpdate("call darDeBaja("+selected+", '"+values[0]+"' , '"+values[1]+"', '"+values[2]+"' );");
            }catch(SQLException e){
                message("Error: no se pudo dar de baja",0);
            }
           
        }
 
        animalesGridPane.setVisible(true);
        executeQuery("SELECT * FROM animal where estado = b'1';");
        listView1.getItems().clear();
        while(result.next()){
            String id = result.getString("animalId");
            listView1.getItems().add(id);
        }
        imageView.setImage(null);
        datePickerAnimal.setValue(null);
        numeroPartos.setText(null);
        corralIdAnimales.setText(null);
        ventaTextField.setText(null);
         
    }

    @FXML
    private void actualizarAnimalButton(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Actulizar");
        alert.setContentText("Desea Actualizar el animal con el Id = "+selected+" ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String f = datePickerAnimal.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String np = numeroPartos.getText();
            if(np == null || np.equals(""))
                np = "0";
            String c = corralIdAnimales.getText();
            String v = ventaTextField.getText();
            String pA = precioActualAnimal.getText();
            String pF = precioFinalAnimal.getText();
            
            try{
                executeUpdate("UPDATE animal SET fechaNacimiento = '"+f+"', "
                    + "numeroPartos = '"+np+"', corralId = '"+c+"', ventaId = '"+v+"', precioInicial = "
                            +pA+ ", precioFinal = "+pF+ " WHERE (animalId = '"+selected+"');"); 
            } catch(SQLException e){
                message("Error: Datos mal ingresados",0);
            }
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        
        
        
        
        
    }

    @FXML
    private void nuevoCorral(ActionEvent event) throws ParseException, SQLException {
        Dialog<String []> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Corral");
        dialog.setResizable(false);
        GridPane pane = new GridPane();
        
        Label l1 = new Label("Corral Id: ");
        Label l2 = new Label("Ubicacion del Corral: ");
        Label l3 = new Label("Numero de Dias: ");
        Label l4 = new Label("Desde: ");
        
        executeQuery("select corralId from corral order by corralId desc limit 1;");
        int preId = 1;
        if(result.next())
        	preId = Integer.parseInt(result.getString("corralId")) + 1;
        
        TextField t1 = new TextField(preId + "");
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        DatePicker d = new DatePicker();
        
        pane.add(l1,0,0);
        pane.add(l2,0,1);
        pane.add(l3,0,2);
        pane.add(l4,0,3);
        
        pane.add(t1,1,0);
        pane.add(t2,1,1);
        pane.add(t3,1,2);
        pane.add(d,1,3);
        
        dialog.getDialogPane().setContent(pane);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
         dialog.setResultConverter(new Callback<ButtonType, String []>() {
            @Override
            public String [] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String [4];
                    input[0] = t1.getText();
                    input[1] = t2.getText();
                    input[2] = "0";
                    if(t3.getText() != null && !t3.getText().equals(""))
                        input[2] = t3.getText();
                    input[3] = "1111-11-11";
                    if(d.getValue() != null)
                        input[3] = d.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return input;
                }
                return null;
            }
        });
        
        Optional<String []> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get();
            String finalDate = "1111-11-11";
            
            if(!output[3].equals("1111-11-11") && !output[2].equals("0")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(output[3]));
                c.add(Calendar.DATE, Integer.parseInt(output[2]));

                finalDate =  sdf.format(c.getTime());
            }
            
            try{
                executeUpdate("INSERT INTO corral (corralId, ubicacionCorral, diasEnCorral, desde, hasta) VALUES "
                        + "('"+output[0]+"', '"+output[1]+"', '"+output[2]+"', '"+output[3]+"', '"+finalDate+"');");
                
                listView1.getItems().clear();
                
                executeQuery("select corralId from corral");
                while(result.next()){
                    listView1.getItems().add(result.getString("corralId"));
                }
                ubicacionCorral.setText(null);
                numeroAnimales.setText(null);
                diasCorral.setText(null);
                desdeCorral.setValue(null);
                hastaCorral.setValue(null);
                
                
                selectedCorral.getItems().clear();
                selectedCorral1.getItems().clear();
                
                executeQuery("select ubicacionCorral from corral");
                while(result.next()){
                    selectedCorral.getItems().add(result.getString("ubicacionCorral"));
                    selectedCorral1.getItems().add(result.getString("ubicacionCorral"));
                }
                  
            }catch(SQLException e){
                message("Error: Datos mal ingresados!",0);
            }
        }
        
    }

    @FXML
    private void actualizarCorral(ActionEvent event) throws ParseException, SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Actualizar");
        alert.setContentText("Desea actualizar el corral con Id = "+selected+" ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String u = ubicacionCorral.getText();
            String d = diasCorral.getText();
            String f = desdeCorral.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String finalDate = "1111-11-11";
            
            if(!f.equals("") && !d.equals("0")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(f));
                c.add(Calendar.DATE, Integer.parseInt(d));

                finalDate =  sdf.format(c.getTime());
            }
            
            try{
                executeUpdate("UPDATE `finca`.`corral` SET `ubicacionCorral` = '"+u+"', `diasEnCorral` = '"+d+"', `desde` = '"+f+"', "
                        + "`hasta` = '"+finalDate+"' WHERE (`corralId` = '"+selected+"');");
            }
            catch(SQLException e){
                message("Error: Datos mal ingresados",0);
            }
            
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void eliminarCorral(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setContentText("Desea eliminar el corral con Id = "+selected+" ?");

        Optional<ButtonType> re = alert.showAndWait();
        if (re.get() == ButtonType.OK){
            
            try{
                executeUpdate("delete from `finca`.`corral` WHERE (`corralId` = '"+selected+"');");
                 listView1.getItems().clear();
                
                executeQuery("select corralId from corral");
                while(result.next()){
                    listView1.getItems().add(result.getString("corralId"));
                }
                ubicacionCorral.setText(null);
                numeroAnimales.setText(null);
                diasCorral.setText(null);
                desdeCorral.setValue(null);
                hastaCorral.setValue(null);
            }
            catch(SQLException e){
                message("Error: No se pudo eliminar, tal vez existen animales en este corral",0);
            }
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
                
        
    }

    @FXML
    private void nuevaVacuna(ActionEvent event) throws SQLException {
        Dialog<String []> dialog = new Dialog<>();
        dialog.setTitle("Nueva Vacuna");
        dialog.setResizable(false);
        GridPane pane = new GridPane();
        
        Label l1 = new Label("Vacuna Id: ");
        Label l2 = new Label("Nombre: ");
        
        executeQuery("select vacunaId from finca.vacunas order by vacunaId desc limit 1;");
        int preId = 1;
        if(result.next())
        	preId = Integer.parseInt(result.getString("vacunaId")) + 1;
        
        TextField t1 = new TextField(preId + "");
        TextField t2 = new TextField();
        
        pane.add(l1,0,0);
        pane.add(l2,0,1);
        
        pane.add(t1,1,0);
        pane.add(t2,1,1);
        
        dialog.getDialogPane().setContent(pane);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
         dialog.setResultConverter(new Callback<ButtonType, String []>() {
            @Override
            public String [] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String [2];
                    input[0] = t1.getText();
                    input[1] = t2.getText();
                    
                    return input;
                }
                return null;
            }
        });
        
        Optional<String []> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get();
            
            if(output[0].equals("")||output[1].equals(""))
                throw new SQLException("e");
            
            try{
                executeUpdate("insert into finca.vacunas (vacunaId, nombre) values ('"+output[0]+"','"+output[1]+"')");
                
                listView1.getItems().clear();
                
                executeQuery("select nombre from finca.vacunas");
                while(result.next()){
                    listView1.getItems().add(result.getString("nombre"));
                }
                nombreVacunaTextField.setText(null);
                vacunaIdTextField.setText(null);
                
                selectedVacuna.getItems().clear();
                executeQuery("select nombre from vacunas");
                selectedVacuna.getItems().add("All");

                while(result.next()){
                    selectedVacuna.getItems().add(result.getString("nombre"));
                }
                  
            }catch(SQLException e){
                message("Error: Datos mal ingresados!",0);
            }
        }
    }

    @FXML
    private void actualizarVacuna(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Actualizar");
        
        executeQuery("select vacunaId from finca.vacunas where nombre = '" + selected + "'");
        
        result.next();
        String id = result.getString("vacunaId");
        alert.setContentText("Desea actualizar la vacuna con Id = "+id+" ?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK){
            String n = nombreVacunaTextField.getText();
            
            if(n.equals(""))
                throw new SQLException("e");
            
            try{
                executeUpdate("UPDATE finca.vacunas SET nombre = '"+n+"'WHERE (`vacunaId` = '"+id+"');");
                
                nombreVacunaTextField.setText(null);
                vacunaIdTextField.setText(null);
                
                executeQuery("select * from vacunas");
                
                listView1.getItems().clear();
                while(result.next()){
                    listView1.getItems().add(result.getString("nombre"));
                }
                
                
            }
            catch(SQLException e){
                message("Error: Datos mal ingresados",0);
            }
            
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void eliminarVavuna(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setContentText("Desea eliminar la vacuna con el nombre = "+selected+" ?");
        
        executeQuery("select vacunaId from finca.vacunas where nombre = '" + selected + "'");
        
        result.next();
        String id = result.getString("vacunaId");
        
        Optional<ButtonType> re = alert.showAndWait();
        if (re.get() == ButtonType.OK){
            
            try{
                executeUpdate("delete from finca.vacunas WHERE (`vacunaId` = '"+id+"');");
                 listView1.getItems().clear();
                
                nombreVacunaTextField.setText(null);
                vacunaIdTextField.setText(null);
                
                executeQuery("select * from vacunas");
                
                listView1.getItems().clear();
                while(result.next()){
                    listView1.getItems().add(result.getString("nombre"));
                }
            }
            catch(SQLException e){
                message("Error: No se pudo eliminar, tal vez existen animales con esta vacuna",0);
            }
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void seleccionarCorral(ActionEvent event) throws SQLException {
        String selected;
        if(selectedCorral.getSelectionModel().getSelectedItem() != null)
            selected = selectedCorral.getSelectionModel().getSelectedItem();
        else
            selected = selectedCorral1.getSelectionModel().getSelectedItem();
        
        
        if(selected.equals("All")){
            executeQuery("select animalId from animal where estado = b'1';");
            listView1.getItems().clear();
            while(result.next()){
                listView1.getItems().add(result.getString("animalId"));
            }
        }
        else{
            executeQuery("select animalId from animal inner join corral on animal.corralId = corral.corralId where ubicacionCorral = '"+selected+"' and estado = b'1';");
            listView1.getItems().clear();
            while(result.next()){
                listView1.getItems().add(result.getString("animalId"));
            }
            
        }
        vacunasListView.getItems().clear();
        
    }
    
    
    private void inLog(String sentencia) throws SQLException{
        
    }

    @FXML
    private void vacunasListViewClicled(MouseEvent event) throws SQLException {
        String selectedVacuna = vacunasListView.getSelectionModel().getSelectedItem();
        String selectedAnimal = listView1.getSelectionModel().getSelectedItem();
        
        executeQuery("select fecha from vacunacion inner join vacunas\n" +
        "on vacunacion.vacunaId = vacunas.vacunaId where animalId = '"+selectedAnimal+"'  and nombre = '"+selectedVacuna+"' ;");
        fechaVacuna.setValue(null);
        if(result.next()){
            
            String [] split = result.getString("fecha").split("-");
            
            fechaVacuna.setValue(LocalDate.of(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2])));
        } 
    }

    @FXML
    private void vacunarCorral(ActionEvent event) throws SQLException {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Realizar una vacunacion");
        dialog.setResizable(true);
        
        Label label1 = new Label("Seleccione una vacuna: ");
        Label label2 = new Label("Seleccione un corral: ");
        Label label3 = new Label("Fecha: ");
       
        ComboBox<String> vacuna = new ComboBox();
        ComboBox<String> corral = new ComboBox();
        DatePicker date = new DatePicker();
        
        executeQuery("select nombre from vacunas;");  
        while(result.next())
            vacuna.getItems().add(result.getString("nombre"));
        
        executeQuery("select ubicacionCorral from corral;");
        while(result.next())
            corral.getItems().add(result.getString("ubicacionCorral"));
        
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        
        grid.add(label1, 0,0);
        grid.add(vacuna, 1,0);
        grid.add(label2,0,1);
        grid.add(corral,1,1);
        grid.add(label3,0,2);
        grid.add(date,1,2);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String[3];
                    try {
                        
                        executeQuery("select vacunaId from vacunas where nombre = '"+vacuna.getSelectionModel().getSelectedItem()+"'");
                        result.next();
                        input[0] = result.getString("vacunaId"); 
                        executeQuery("select corralId from corral where ubicacionCorral = '"+corral.getSelectionModel().getSelectedItem()+"'");
                        result.next();
                        input[1] = result.getString("corralId");
                        input[2] = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        
                 
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    return input;
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get();
            
            
            executeQuery("select animalId from animal where estado = b'1' and corralId = '"+output[1]+"'");
            
            while(result.next()){
                try{
                executeUpdate("insert into vacunacion(vacunaId,animalId,fecha)"
                        + "values ('"+output[0]+"','"+result.getString("animalId")+"','"+output[2]+"')");
                }catch(SQLException e){
                    message("No se pudo realizar la vacunacion del animal con id = " + result.getString("animalId"),0 );
                }
            }
            
        }
        
    }

    @FXML
    private void VacunarId(ActionEvent event) throws SQLException {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Realizar una vacunacion");
        dialog.setResizable(false);
        
        Label label1 = new Label("Seleccione una vacuna: ");
        Label label2 = new Label("Introduzca el/los id: ");
        Label label3 = new Label("Fecha: ");
       
        ComboBox<String> vacuna = new ComboBox();
        TextField ids = new TextField();
        ids.setPromptText("1,2,3,....");
        DatePicker date = new DatePicker();
        
        executeQuery("select nombre from vacunas;");  
        while(result.next())
            vacuna.getItems().add(result.getString("nombre"));
        

        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        
        grid.add(label1, 0,0);
        grid.add(vacuna, 1,0);
        grid.add(label2,0,1);
        grid.add(ids,1,1);
        grid.add(label3,0,2);
        grid.add(date,1,2);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String[3];
                    try {
                        
                        executeQuery("select vacunaId from vacunas where nombre = '"+vacuna.getSelectionModel().getSelectedItem()+"'");
                        result.next();
                        input[0] = result.getString("vacunaId"); 
                        input[1] = ids.getText();
                        input[2] = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        
                 
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    return input;
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get();
            
            String [] index = output[1].split(",");
            
            if(index.length == 1){
                try{
                executeUpdate("insert into vacunacion(vacunaId,animalId,fecha)"
                        + "values ('"+output[0]+"','"+output[1]+"','"+output[2]+"')");
                }catch(SQLException e){
                    message("No se pudo realizar la vacunacion del animal con id = " + result.getString("animalId"),0 );
                }
            }
            else{
                for(int i = 0; index.length > i; i++){
                    executeQuery("select animalId from animal where estado = b'1' and animalId = '"+index[i]+"'");
                    if(result.next())
                        try{
                        executeUpdate("insert into vacunacion(vacunaId,animalId,fecha)"
                                + "values ('"+output[0]+"','"+index[i]+"','"+output[2]+"')");
                        }catch(SQLException e){
                            message("No se pudo realizar la vacunacion del animal con id = " + result.getString("animalId"),0 );
                        }
                }                
            }
            
     
            
        }
    }

    @FXML
    private void seleccionarVacuna(ActionEvent event) throws SQLException {
        String selected =  selectedVacuna.getSelectionModel().getSelectedItem();
        
        
        if(selected.equals("All")){
            executeQuery("select animalId from animal where estado = b'1';");
            listView1.getItems().clear();
            while(result.next()){
                listView1.getItems().add(result.getString("animalId"));
            }
        }
        else{
            executeQuery("select vacunacion.animalId from vacunacion inner join vacunas on\n" +
                "vacunacion.vacunaId = vacunas.vacunaId\n" +
                "inner join animal on animal.animalId = vacunacion.animalId\n" +
                " where nombre = '"+selected+"' and estado = b'1';");
            listView1.getItems().clear();
            while(result.next()){
                listView1.getItems().add(result.getString("animalId"));
            }
            
        }
        vacunasListView.getItems().clear();
    }

    @FXML
    private void selectMadresActivas(ActionEvent event) throws SQLException {
        listView1.getItems().clear();
        executeQuery("select hijos.animalId from hijos inner join animal\n" +
            "on hijos.animalId = animal.animalId where animal.estado = b'1' group by hijos.animalId;");
        while(result.next()){
            listView1.getItems().add(result.getString("animalId"));
        }
        hijosListView.getItems().clear();
    }

    @FXML
    private void selectMadresInactivas(ActionEvent event) throws SQLException {
        listView1.getItems().clear();
        executeQuery("select hijos.animalId from hijos inner join animal\n" +
            "on hijos.animalId = animal.animalId where estado = b'0' group by hijos.animalId;");
        while(result.next()){
            listView1.getItems().add(result.getString("animalId"));
        }
        hijosListView.getItems().clear();
    }

    @FXML
    private void nuevoHijo(ActionEvent event) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Hijo");
        dialog.setResizable(false);
        
        Label label1 = new Label("Id de la madre: ");
        Label label2 = new Label("Id del hijo: ");
       
        TextField id1 = new TextField();
        TextField id2 = new TextField();
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        
        grid.add(label1, 0,0);
        grid.add(id1, 1,0);
        grid.add(label2,0,1);
        grid.add(id2,1,1);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String[2];
                    input[0] = id1.getText();
                    input[1] = id2.getText();
                    return input;
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get();
            
            try{
                executeQuery("select animalId from animal where animalId ="+output[1]+" and estado = b'1'");
                if(result.next()){
                    executeUpdate("INSERT INTO `finca`.`hijos` (`hijosId`, `animalId`) VALUES ('"+output[1]+"', '"+output[0]+"');");
                     
                    executeQuery("select hijosId from hijos where animalId = "+output[0]+";");
                    hijosListView.getItems().clear();
                    while(result.next()){
                        hijosListView.getItems().add(result.getString("hijosId"));
                    }
                }
                
            }catch(SQLException e){
                message("El/los id no son correctos",0);
            }
      
        }
    }

    @FXML
    private void eliminarHijo(ActionEvent event) {
        String selectedParent = listView1.getSelectionModel().getSelectedItem();
        String selectedSon = hijosListView.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Hijo");
        alert.setContentText("Desea eliminar la relacion madre hijo con los ids = "+selectedParent+" y "+selectedSon+" ?");
 
        Optional<ButtonType> re = alert.showAndWait();
        if (re.get() == ButtonType.OK){
            
            try{
                executeUpdate("DELETE FROM `finca`.`hijos` WHERE (`hijosId` = '"+selectedSon+"') and (`animalId` = '"+selectedParent+"');");
                listView1.getItems().clear();
                hijosListView.getItems().clear();
            }
            catch(SQLException e){
                message("Error: No se pudo eliminar, tal vez existen animales con esta vacuna",0);
            }
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void cambioDeCorral(ActionEvent event) throws SQLException {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Cambio de Corral");
        dialog.setContentText("Desea cambiar los animales a otro corral?");
        dialog.setResizable(false);
        
        Label label1 = new Label("Corral Actual Id: ");
        Label label2 = new Label("Nuevo Corral Id: ");
       
        TextField id1 = new TextField();
        TextField id2 = new TextField();
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        
        grid.add(label1, 0,0);
        grid.add(id1, 1,0);
        grid.add(label2,0,1);
        grid.add(id2,1,1);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType b) {

                if (b == buttonTypeOk) {
                    String [] input = new String[2];
                    input[0] = id1.getText();
                    input[1] = id2.getText();
                    return input;
                }
                return null;
            }
        });
        
        Optional<String[]> opcion = dialog.showAndWait();
        if (opcion.isPresent()){
            String [] output = opcion.get(); 
            executeQuery("select numeroDeAnimales from corral where corralId ="+output[1]+" ");
            if(result.next()){
                int num = result.getInt("numeroDeAnimales");
                if(num > 0){
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Aumentar animales en corral");
                    alert.setContentText("El corral con Id = "+ output[1] + " ya contiene animales, "
                            + "desea mezclar los animales de ambos corrales en un solo corral?");

                    Optional<ButtonType> re = alert.showAndWait();
                    if (re.get() == ButtonType.OK){

                        try{
                            executeUpdate("call cambioCorral("+output[0]+","+output[1]+")");
                            message("Cambio de corral realizado",1);
                        }
                        catch(SQLException e){
                            message("Error: No se pudo cambiar de corral",0);
                        }

                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }

                }
                else{
                    try{
                            executeUpdate("call cambioCorral("+output[0]+","+output[1]+")");
                            message("Cambio de corral realizado",1);
                        }
                        catch(SQLException e){
                            message("Error: No se pudo cambiar de corral",0);
                        }
                }
            }
                
        
      
        }
    }

    @FXML
    private void nuevaVenta(ActionEvent event) throws SQLException {
	    Dialog<String[]> dialog = new Dialog<>();
	    dialog.setTitle("Nueva venta");
	    dialog.setContentText("Crear un nuevo Id para venta");
	    dialog.setResizable(false);
	    
	    Label label1 = new Label("Venta Id: ");
	    Label label2 = new Label("Precio: ");
	    Label label3 = new Label("Tipo: ");
	   

	    executeQuery("select ventaId from venta order by ventaId desc limit 1;");
	    int i = 0;
	    if(result.next())
	    	i = result.getInt("ventaId") + 1;
	    
	    TextField id = new TextField(i+"");
	    TextField pr = new TextField();
	    TextField ti = new TextField();
	    
	    GridPane grid = new GridPane();
	    grid.setPadding(new Insets(10));
	    
	    grid.add(label1, 0,0);
	    grid.add(id, 1,0);
	    grid.add(label2,0,1);
	    grid.add(pr,1,1);
	    grid.add(label3,0,2);
	    grid.add(ti,1,2);

	    dialog.getDialogPane().setContent(grid);
	    
	    ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
	    ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
	    dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

	    dialog.setResultConverter(new Callback<ButtonType, String[]>() {
	        @Override
	        public String[] call(ButtonType b) {

	            if (b == buttonTypeOk) {
	                String [] input = new String[3];
	                input[0] = id.getText();
	                input[1] = pr.getText();
	                input[2]= ti.getText();
	                return input;
	            }
	            return null;
	        }
	    });
	    
	    Optional<String[]> opcion = dialog.showAndWait();
	    if (opcion.isPresent()){
	        String [] output = opcion.get(); 
	        
	        try{
	            executeUpdate("insert into venta(ventaId,precio,tipo) values ('"+output[0]+"','"+output[1]+"','"+output[2]+"')");
	            listView1.getItems().clear();
	            executeQuery("select ventaId from venta");
	            result.next();
	            while(result.next()){
	                listView1.getItems().add(result.getString("ventaId"));
	            }
	            animalesEnVentaListView.getItems().clear();
	            
	        }catch(SQLException e){
	            message("No se pudo crear la nueva venta",0);
	        }
        }
    }

    @FXML
    private void actualizarVenta(ActionEvent event) {
        String precio = precioVenta.getText();
        String tipo = tipoVenta.getText();
        
        String id = listView1.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Actualizar venta");
        alert.setContentText("Desea actulizar los campos de la venta con el id = "+id+" ?");
 
        Optional<ButtonType> re = alert.showAndWait();
        if (re.get() == ButtonType.OK){
            
            try{
            executeUpdate("UPDATE `finca`.`venta` SET `precio` = '"+precio+"', `tipo` = '"+tipo+"' WHERE (`ventaId` = '"+id+"');");
            }
            catch(SQLException e){
                message("No se pudo actualizar la venta",0);
            }
            
        } else {
            // ... user chose CANCEL or closed the dialog
        }  
    }

    @FXML
    private void eliminarVenta(ActionEvent event) throws SQLException {
        String selected = listView1.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Venta");
        alert.setHeaderText("Elimar una Venta");
        alert.setContentText("Esta seguro de eliminar la venta con el ID = " + selected + " ?");
        

        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() == ButtonType.OK){
            try{
                executeUpdate("DELETE FROM venta WHERE (`ventaId` = '"+selected+"');");
                executeQuery("SELECT ventaId FROM venta ;");
                listView1.getItems().clear();
                animalesEnVentaListView.getItems().clear();
                result.next();
                while(result.next()){
                    String id = result.getString("ventaId");
                    listView1.getItems().add(id);
                }
                precioVenta.clear();
                tipoVenta.clear();   
            }catch(SQLException e){
                message("No se pudo eliminar la venta",0);
            }   
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void realizarVenta(ActionEvent event) {
        String selected = listView1.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Venta");
        alert.setHeaderText("Desea vender a los animales ligados a esta venta?");
        alert.setContentText("Esta seguro de efectuar la venta con el ID = " + selected + " ?");
        
        

        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() == ButtonType.OK){
            try{  
                executeUpdate("UPDATE `finca`.`animal` SET `estado` = b'0' WHERE (`ventaId` = '"+selected+"');");
                executeUpdate("update venta set fecha = '"+java.time.LocalDate.now()+"' where ventaId = '"+selected+"'");
                executeQuery("SELECT ventaId FROM venta ;");
                listView1.getItems().clear();
                animalesEnVentaListView.getItems().clear();
                result.next();
                while(result.next()){
                    String id = result.getString("ventaId");
                    listView1.getItems().add(id);
                }
                precioVenta.clear();
                tipoVenta.clear();   
            }catch(SQLException e){
                message("No se pudo realizar la venta",0);
            }   
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }


}
