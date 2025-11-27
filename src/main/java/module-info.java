module ca.qc.bdeb.sim.tp2_camelot_a_velo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens ca.qc.bdeb.sim.tp2_camelot_a_velo to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2_camelot_a_velo;
}