/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurante.fx;

import Restaurante.bl.SeguridadServicio;
import Restaurante.bl.Usuario;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crher
 */
public class FormLoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    JFXTextField txtUsuario;
    
    @FXML
    JFXPasswordField txtContrasena;
    
    SeguridadServicio seguridadServicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        seguridadServicio = new SeguridadServicio();
    }    
    
    public void ingresar() throws IOException {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        
        Usuario usuarioExistente = seguridadServicio.autenticar(usuario, contrasena);
        
        if (usuarioExistente != null) {
            RestauranteFX.setUsuarioAutenticado(usuarioExistente);
            
            Stage stage = RestauranteFX.getStage();

            RestauranteFX.stage = stage;
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/Restaurante/fx/Menu/main.fxml"));

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Punto de Venta");
            stage.show();   
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "Usuario o Contraseña incorrectas");
            alert.showAndWait();      
            
            txtUsuario.clear();
            txtContrasena.clear();
            txtUsuario.requestFocus();
        }            
    }
}

