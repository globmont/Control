package input;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikhil on 1/6/2016.
 */
public class ConfigHandler {
    private Control control;
    private String filePath;

    private HashMap<String, Control.Button> mapToButton = new HashMap<>();
    private HashMap<String, Control.Stick> mapToStick = new HashMap<>();
    private HashMap<String, StickState.Mode> mapToStickMode = new HashMap<>();
    private HashMap<String, ButtonState.Mode> mapToButtonMode = new HashMap<>();
    private Wini ini = null;
    Map<String, String> bindingType;
    Map<String, String> keyPressRouting;
    Map<String, String> functionRouting;
    Map<String, String> mouseEventRouting;
    Map<String, String> stickModes;
    Map<String, String> holdStart;
    Map<String, String> pushFrequency;
    Map<String, String> buttonMode;

    public ConfigHandler(Control control, String filePath) {
        this.control = control;
        this.filePath = filePath;

        try {
            ini = new Wini(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not find config file. Reverting to default configuration.");
        }

        populateMaps();

    }

    private void populateMaps() {
        mapToStickMode.put("discrete", StickState.Mode.DISCRETE);
        mapToStickMode.put("continuous", StickState.Mode.CONTINUOUS);

        mapToButtonMode.put("single", ButtonState.Mode.SINGLE);
        mapToButtonMode.put("repeatDelay", ButtonState.Mode.REPEAT_DELAY);
        mapToButtonMode.put("repeatInstant", ButtonState.Mode.REPEAT_INSTANT);

        mapToStick.put("ls", Control.Stick.LS);
        mapToStick.put("rs", Control.Stick.RS);

        mapToButton.put("a", Control.Button.A);
        mapToButton.put("b", Control.Button.B);
        mapToButton.put("x", Control.Button.X);
        mapToButton.put("y", Control.Button.Y);
        mapToButton.put("lb", Control.Button.LB);
        mapToButton.put("rb", Control.Button.RB);
        mapToButton.put("ls", Control.Button.LS);
        mapToButton.put("rs", Control.Button.RS);
        mapToButton.put("back", Control.Button.BACK);
        mapToButton.put("start", Control.Button.START);
        mapToButton.put("du", Control.Button.DU);
        mapToButton.put("dr", Control.Button.DR);
        mapToButton.put("dd", Control.Button.DD);
        mapToButton.put("dl", Control.Button.DL);
        mapToButton.put("adu", Control.Button.ADU);
        mapToButton.put("adr", Control.Button.ADR);
        mapToButton.put("add", Control.Button.ADD);
        mapToButton.put("adl", Control.Button.ADL);
        mapToButton.put("lt", Control.Button.LT);
        mapToButton.put("rt", Control.Button.RT);
        mapToButton.put("lsu", Control.Button.LSU);
        mapToButton.put("lsr", Control.Button.LSR);
        mapToButton.put("lsd", Control.Button.LSD);
        mapToButton.put("lsl", Control.Button.LSL);
        mapToButton.put("rsu", Control.Button.RSU);
        mapToButton.put("rsr", Control.Button.RSR);
        mapToButton.put("rsd", Control.Button.RSD);
        mapToButton.put("rsl", Control.Button.RSL);
        mapToButton.put("mrs", Control.Button.MRS);
        mapToButton.put("mls", Control.Button.MLS);
    }

    public void readConfig() {

        control.clearBindingTypes();
        control.resetListeners();

        bindingType = ini.get("bindingType");
        keyPressRouting = ini.get("keyPressRouting");
        functionRouting = ini.get("functionRouting");
        mouseEventRouting = ini.get("mouseEventRouting");
        stickModes = ini.get("stickModes");


        for(String binding : bindingType.keySet()) {
            Control.Button button = mapToButton.get(binding);
            String[] bindings = bindingType.get(binding).split(" ");
            control.addBindingType(button, bindings);
            for(String listenerType : bindings) {
                switch(listenerType) {
                    case "keyPress":
                        routeKeyPress(binding);
                        break;
                    case "function":
                        routeFunction(binding);
                        break;
                    case "mouseEvent":
                        mouseEventRouting(binding);
                        break;
                }
            }
        }

        for(String stickName : stickModes.keySet()) {
            StickState.Mode stickMode = mapToStickMode.get(stickModes.get(stickName));
            switch(stickMode) {
                case DISCRETE:
                    discreteStick(stickName);
                    break;
                case CONTINUOUS:
                    continuousStick(stickName);
                    break;
            }
        }


        control.bindKeys();
    }

    public void applyButtonStateParameters() {
        holdStart = ini.get("holdStart");
        pushFrequency = ini.get("pushFrequency");
        buttonMode = ini.get("buttonMode");

        for(String buttonName : holdStart.keySet()) {
            Control.Button button = mapToButton.get(buttonName);
            control.setButtonHoldStart(button, Integer.parseInt(holdStart.get(buttonName)));
        }

        for(String buttonName : pushFrequency.keySet()) {
            Control.Button button = mapToButton.get(buttonName);
            control.setButtonPushFrequency(button, Integer.parseInt(pushFrequency.get(buttonName)));
        }

        System.out.println(buttonMode.size());
        for(String buttonName : buttonMode.keySet()) {
            Control.Button button = mapToButton.get(buttonName);
            control.setButtonMode(button, mapToButtonMode.get(buttonMode.get(buttonName)));
            System.out.println("Set button " + buttonName + " to mode " + buttonMode.get(buttonName) + "(" + mapToButtonMode.get(buttonMode.get(buttonName)) + ")");
        }
    }

    private void discreteStick(String stickName) {
        String[] up = bindingType.get(stickName + "u").split(" ");
        String[] right = bindingType.get(stickName + "r").split(" ");
        String[] down = bindingType.get(stickName + "d").split(" ");
        String[] left = bindingType.get(stickName + "l").split(" ");
        String[][] bindingType = new String[][] {up, right, down, left};
        new StickState(StickState.Mode.DISCRETE, mapToStick.get(stickName), control, bindingType);
    }

    private void continuousStick(String stickName) {
        String[] bindings = bindingType.get("m" + stickName).split(" ");
        String[][] bindingType = new String[][] {bindings};
        new StickState(StickState.Mode.CONTINUOUS, mapToStick.get(stickName), control, bindingType);
    }

    private void routeKeyPress(String buttonName) {
        Control.Button button = mapToButton.get(buttonName);
        String[] pressHold = keyPressRouting.get(buttonName).split(",");
        String[] press = pressHold[0].split(" ");
        if(press[0].equals("")) {
            press = new String[0];
        }
        String[] hold = pressHold[1].split(" ");
        if(hold[0].equals("")) {
            hold = new String[0];
        }
        String [][] keyBinding = new String[][] {press, hold};
        control.getKeyPressListener().addMapping(button, keyBinding);
    }

    private void routeFunction(String buttonName) {
        Control.Button button = mapToButton.get(buttonName);
        String[] functionNames = functionRouting.get(buttonName).split(" ");
        control.getFunctionListener().addMapping(button, functionNames);
    }

    private void mouseEventRouting(String buttonName) {
        Control.Button button = mapToButton.get(buttonName);
        String mouseButton = mouseEventRouting.get(buttonName);
        control.getMouseEventListener().addMapping(button, mouseButton);
    }
}
