module zadanie.wzi.wzi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens zadanie.wzi.wzi to javafx.fxml;
    exports zadanie.wzi.wzi;
    exports zadanie.wzi.wzi.Enums;
    opens zadanie.wzi.wzi.Enums to javafx.fxml;
    exports zadanie.wzi.wzi.Parser;
    opens zadanie.wzi.wzi.Parser to javafx.fxml;
    exports zadanie.wzi.wzi.Controler;
    opens zadanie.wzi.wzi.Controler to javafx.fxml;
    exports zadanie.wzi.wzi.Drawings;
    opens zadanie.wzi.wzi.Drawings to javafx.fxml;
    exports zadanie.wzi.wzi.Model;
    opens zadanie.wzi.wzi.Model to javafx.fxml;
//    exports zadanie.wzi.wzi.Animation;
//    opens zadanie.wzi.wzi.Animation to javafx.fxml;
}