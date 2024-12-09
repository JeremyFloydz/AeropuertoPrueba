package com.example.aeropuerto1.controller;

import com.example.aeropuerto1.dao.aeropuertoPrivadoDao;
import com.example.aeropuerto1.dao.aeropuertoPublicoDao;
import com.example.aeropuerto1.model.Aeropuerto;
import com.example.aeropuerto1.model.AeropuertoPrivado;
import com.example.aeropuerto1.model.AeropuertoPublico;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class aeropuertosController implements Initializable {

    @FXML
    private MenuItem actDesAvion, aniadirAeropuerto, aniadirAvion, borrarAeropuerto, borrarAvion, editarAeropuerto, infoAeropuerto;
    @FXML
    private Menu menuAeropuertos, menuAviones, menuAyuda;
    @FXML
    private RadioButton btPrivados, btPublicos;
    @FXML
    private ToggleGroup rbGroup;
    @FXML
    private Label lblListado, lblNombre;
    @FXML
    private MenuBar barraMenu;
    @FXML
    private TextField txtNombre;
    @FXML
    private HBox panelBotones, panelHueco;
    @FXML
    private TilePane panelCentro;
    @FXML
    private FlowPane panelListado;
    @FXML
    private VBox rootPane;
    @FXML
    private TableView tablaVista;

    private ObservableList lstEntera = FXCollections.observableArrayList();
    private ObservableList lstFiltrada = FXCollections.observableArrayList();

    /**
     * Método de inicialización de JavaFX. Configura los listeners para manejar la selección de elementos,
     * carga de aeropuertos públicos y privados, y la funcionalidad de filtrado.
     * @param url Ubicación usada para resolver rutas relativas para el objeto raíz, o null si no se proporciona.
     * @param resourceBundle Recurso local usado para localizar el objeto raíz, o null si no se proporciona.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tablaVista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
                if (newValue != null) {
                    deshabilitarMenus(false);
                } else {
                    deshabilitarMenus(true);
                }
            }
        });
        cargarPublicos();

        rbGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldBtn, Toggle newBtn) -> {
            if (newBtn.equals(btPublicos)) {
                cargarPublicos();
            } else {
                cargarPrivados();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }

    public void deshabilitarMenus(boolean deshabilitado) {
        editarAeropuerto.setDisable(deshabilitado);
        borrarAeropuerto.setDisable(deshabilitado);
        infoAeropuerto.setDisable(deshabilitado);
    }

    public void cargarPublicos() {
        try {
            tablaVista.getSelectionModel().clearSelection();
            txtNombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

            TableColumn<AeropuertoPublico, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getId()));

            TableColumn<AeropuertoPublico, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getNombre()));

            TableColumn<AeropuertoPublico, String> colPais = new TableColumn<>("País");
            colPais.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getPais()));

            TableColumn<AeropuertoPublico, String> colCiudad = new TableColumn<>("Ciudad");
            colCiudad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCiudad()));

            TableColumn<AeropuertoPublico, String> colCalle = new TableColumn<>("Calle");
            colCalle.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCalle()));

            TableColumn<AeropuertoPublico, Integer> colNumero = new TableColumn<>("Número");
            colNumero.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getNumero()));

            TableColumn<AeropuertoPublico, Integer> colAnio = new TableColumn<>("Año");
            colAnio.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getAnio_inauguracion()));

            TableColumn<AeropuertoPublico, Integer> colCapacidad = new TableColumn<>("Capacidad");
            colCapacidad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getCapacidad()));

            TableColumn<AeropuertoPublico, BigDecimal> colFinanciacion = new TableColumn<>("Financiación");
            colFinanciacion.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getFinanciacion()));

            TableColumn<AeropuertoPublico, Integer> colTrabajadores = new TableColumn<>("Nº Trabajadores");
            colTrabajadores.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getNum_trabajadores()));

            tablaVista.getColumns().addAll(colId, colNombre, colPais, colCiudad, colCalle, colNumero, colAnio, colCapacidad, colFinanciacion, colTrabajadores);
            ObservableList<AeropuertoPublico> aeropuertos = aeropuertoPublicoDao.cargarListado();

            if (aeropuertos != null && !aeropuertos.isEmpty()) {
                lstEntera.setAll(aeropuertos);
                tablaVista.setItems(aeropuertos);
            } else {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("No se encontraron Aeropuertos.");
                alerta(lst);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    public void cargarPrivados() {

        tablaVista.getSelectionModel().clearSelection();
        txtNombre.setText(null);
        lstEntera.clear();
        lstFiltrada.clear();
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear();

        TableColumn<AeropuertoPrivado, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getId()));
        TableColumn<AeropuertoPrivado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getNombre()));
        TableColumn<AeropuertoPrivado, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getPais()));
        TableColumn<AeropuertoPrivado, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCiudad()));
        TableColumn<AeropuertoPrivado, String> colCalle = new TableColumn<>("Calle");
        colCalle.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getCalle()));
        TableColumn<AeropuertoPrivado, Integer> colNumero = new TableColumn<>("Número");
        colNumero.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getDireccion().getNumero()));
        TableColumn<AeropuertoPrivado, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getAnio_inauguracion()));
        TableColumn<AeropuertoPrivado, Integer> colCapacidad = new TableColumn<>("Capacidad");
        colCapacidad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getAeropuerto().getCapacidad()));
        TableColumn<AeropuertoPrivado, Integer> colSocios = new TableColumn<>("Nº Socios");
        colSocios.setCellValueFactory(new PropertyValueFactory("numero_socios"));
        tablaVista.getColumns().addAll(colId, colNombre, colPais, colCiudad, colCalle, colNumero, colAnio, colCapacidad, colSocios);
        ObservableList<AeropuertoPrivado> aeropuertos = aeropuertoPrivadoDao.cargarListado();
        lstEntera.setAll(aeropuertos);
        tablaVista.setItems(aeropuertos);
    }


    public void filtrar() {
        String valor = txtNombre.getText();
        valor = valor.toLowerCase();
        if (valor.isEmpty()) {
            tablaVista.setItems(lstEntera);
        } else {
            lstFiltrada.clear();
            if (lstEntera.getFirst() instanceof AeropuertoPublico) {
                for (Object aeropuerto : lstEntera) {
                    AeropuertoPublico aeropuertoPublico = (AeropuertoPublico) aeropuerto;
                    String nombre = aeropuertoPublico.getAeropuerto().getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.contains(valor)) {
                        lstFiltrada.add(aeropuertoPublico);
                    }
                }
            } else {
                for (Object aeropuerto : lstEntera) {
                    AeropuertoPrivado aeropuertoPrivado = (AeropuertoPrivado) aeropuerto;
                    String nombre = aeropuertoPrivado.getAeropuerto().getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.contains(valor)) {
                        lstFiltrada.add(aeropuertoPrivado);
                    }
                }
            }
            tablaVista.setItems(lstFiltrada);
        }
    }

    /**
     * Muestra una alerta de error con los mensajes proporcionados.
     *
     * @param textos Lista de mensajes a mostrar en la alerta de error.
     */
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }










}