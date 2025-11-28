package ca.qc.bdeb.sim.tp2_camelot_a_velo;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Input {
    private static final Set<KeyCode> TOUCHES = new HashSet<>();

    public static boolean isKeyPressed(KeyCode keyCode){
        return TOUCHES.contains(keyCode);
    }
    public static void setKeyPressed(KeyCode keyCode, boolean appuie){
        if(appuie){
            TOUCHES.add(keyCode);
        } else TOUCHES.remove(keyCode);
        System.out.println(TOUCHES);
    }
}
