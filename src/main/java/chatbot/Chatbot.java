package chatbot;

import Services.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Chatbot {

    JsonObject context;
    Service service;
    String userName;

    Club Club;
    Manualidad Manualidad;
    Juego Juego;
    Servicio Servicio;
    Evento Evento;

    public static void main(String[] args) throws IOException {
        Chatbot c = new Chatbot();
        Scanner scanner = new Scanner(System.in);
        String userUtterance;

        do {
            System.out.print("User:");
            userUtterance = scanner.nextLine();

            JsonObject userInput = new JsonObject();
            userInput.add("userUtterance", new JsonPrimitive(userUtterance));
            JsonObject botOutput = c.process(userInput);
            String botUtterance = "";
            if (botOutput != null && botOutput.has("botUtterance")) {
                botUtterance = botOutput.get("botUtterance").getAsString();
            }
            System.out.println("Bot:" + botUtterance);

        } while (!userUtterance.equals("QUIT"));
        scanner.close();
    }

    public Chatbot() {
        context = new JsonObject();
        context.add("currentTask", new JsonPrimitive("none"));
        service = new Service();
        userName = "";
        this.Club = new Club();
        this.Manualidad = new Manualidad();
        this.Juego = new Juego();
        this.Servicio = new Servicio();
        this.Evento = new Evento();
    }

    public JsonObject process(JsonObject userInput) throws IOException {
        System.out.println(userInput.toString());
        //step1: process user input
        JsonObject userAction = processUserInput(userInput);

        //step2: update context
        updateContext(userAction);

        //step3: identify bot intent
        identifyBotIntent();
        System.out.println("context " + context.toString());
        //step4: structure output
        JsonObject out = getBotOutput();
        System.out.println("out " + out.toString());
        return out;
    }

    public String processFB(JsonObject userInput) throws IOException {
        JsonObject out = process(userInput);
        return out.toString();
    }

    public JsonObject processUserInput(JsonObject userInput) throws IOException {
        String userUtterance = null;
        JsonObject userAction = new JsonObject();
        //info usuario 
        if (userInput.has("userName")) {
            userName = userInput.get("userName").getAsString();;
        }
        System.out.println("user: " + userName);
        //default case
        userAction.add("userIntent", new JsonPrimitive(""));
        if (userInput.has("userUtterance")) {
            userUtterance = userInput.get("userUtterance").getAsString();
            userUtterance = userUtterance.replaceAll("%2C", ",");
        }

        if (userUtterance.matches("hola|Hola|buenos dias|Buenos dias|tengo una pregunta")) {
            userAction.add("userIntent", new JsonPrimitive("intentSaludo"));
        } else {
            String userType = " n ";
            if (userInput.has("userType")) {
                userType = userInput.get("userType").getAsString();
                userType = userType.replaceAll("%2C", ",");
            }

            if (userType != null) {
                String[] entrada = userType.split(":");
                if (entrada[0].trim().equals("requestMenuInicial")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuInicial"));
                } else if (entrada[0].trim().equals("requestClubCliente")) {
                    userAction.add("userIntent", new JsonPrimitive("intentClubCliente"));
                } else if (entrada[0].trim().equals("requestMenuCliente")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuCliente"));
                } else if (entrada[0].trim().equals("requestMenuEmpleado")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuEmpleado"));
                } else if (entrada[0].trim().equals("requestClubEmpleado")) {
                    userAction.add("userIntent", new JsonPrimitive("intentClubEmpleado"));
                } else if (entrada[0].trim().equals("requestMenuEmpleadoClub")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuEmpleadoClub"));
                } else if (entrada[0].trim().equals("requestVerJuegos")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerJuegos"));
                } else if (entrada[0].trim().equals("requestVerEventos")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerEventos"));
                } else if (entrada[0].trim().equals("requestVerReglas")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerReglas"));
                } else if (entrada[0].trim().equals("requestVerAlmuerzo")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerAlmuerzo"));
                } else if (entrada[0].trim().equals("requestVerRemision")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerRemision"));
                } else if (entrada[0].trim().equals("requestVerServicios")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerServicios"));
                } else if (entrada[0].trim().equals("requestVerManualidades")) {
                    userAction.add("userIntent", new JsonPrimitive("intentVerManualidades"));
                } else {
                    userAction.add("userIntent", new JsonPrimitive("intentDefault"));
                }
                if (entrada.length > 1) {
                }
            }
        }
        return userAction;
    }

    public void updateContext(JsonObject userAction) {
        //copy userIntent
        context.add("userIntent", userAction.get("userIntent"));
        String userIntent = context.get("userIntent").getAsString();

        if (userIntent.equals("intentDefault")) {
            context.add("currentTask", new JsonPrimitive("taskDefault"));
        } else if (userIntent.equals("intentSaludo")) {
            context.add("currentTask", new JsonPrimitive("taskMenuInicial"));
        } else if (userIntent.equals("intentMenuInicial")) {
            context.add("currentTask", new JsonPrimitive("taskMenuInicial"));
        } else if (userIntent.equals("intentClubCliente")) {
            context.add("currentTask", new JsonPrimitive("taskClubCliente"));
        } else if (userIntent.equals("intentMenuCliente")) {
            context.add("currentTask", new JsonPrimitive("taskMenuCliente"));
        } else if (userIntent.equals("intentMenuEmpleado")) {
            context.add("currentTask", new JsonPrimitive("taskMenuEmpleado"));
        } else if (userIntent.equals("intentClubEmpleado")) {
            context.add("currentTask", new JsonPrimitive("taskClubEmpleado"));
        } else if (userIntent.equals("intentMenuEmpleadoClub")) {
            context.add("currentTask", new JsonPrimitive("taskMenuEmpleadoClub"));
        } else if (userIntent.equals("intentVerJuegos")) {
            context.add("currentTask", new JsonPrimitive("taskVerJuegos"));
        } else if (userIntent.equals("intentVerEventos")) {
            context.add("currentTask", new JsonPrimitive("taskVerEventos"));
        } else if (userIntent.equals("intentVerReglas")) {
            context.add("currentTask", new JsonPrimitive("taskVerReglas"));
        } else if (userIntent.equals("intentVerAlmuerzo")) {
            context.add("currentTask", new JsonPrimitive("taskVerAlmuerzo"));
        } else if (userIntent.equals("intentVerRemision")) {
            context.add("currentTask", new JsonPrimitive("taskVerRemision"));
        } else if (userIntent.equals("intentVerServicios")) {
            context.add("currentTask", new JsonPrimitive("taskVerServicios"));
        } else if (userIntent.equals("intentVerManualidades")) {
            context.add("currentTask", new JsonPrimitive("taskVerManualidades"));
        }
    }

    public void identifyBotIntent() {
        String currentTask = context.get("currentTask").getAsString();
        if (currentTask.equals("taskDefault")) {
            context.add("botIntent", new JsonPrimitive("botDefault"));
        } else if (currentTask.equals("taskSaludo")) {
            context.add("botIntent", new JsonPrimitive("botSaludo"));
        } else if (currentTask.equals("taskMenuInicial")) {
            context.add("botIntent", new JsonPrimitive("botMenuInicial"));
        } else if (currentTask.equals("taskClubCliente")) {
            context.add("botIntent", new JsonPrimitive("botClubCliente"));
        } else if (currentTask.equals("taskMenuCliente")) {
            context.add("botIntent", new JsonPrimitive("botMenuCliente"));
        } else if (currentTask.equals("taskMenuEmpleado")) {
            context.add("botIntent", new JsonPrimitive("botMenuEmpleado"));
        } else if (currentTask.equals("taskClubEmpleado")) {
            context.add("botIntent", new JsonPrimitive("botClubEmpleado"));
        } else if (currentTask.equals("taskMenuEmpleadoClub")) {
            context.add("botIntent", new JsonPrimitive("botMenuEmpleadoClub"));
        } else if (currentTask.equals("taskVerJuegos")) {
            context.add("botIntent", new JsonPrimitive("botVerJuegos"));
        } else if (currentTask.equals("taskVerEventos")) {
            context.add("botIntent", new JsonPrimitive("botVerEventos"));
        } else if (currentTask.equals("taskVerReglas")) {
            context.add("botIntent", new JsonPrimitive("botVerReglas"));
        } else if (currentTask.equals("taskVerAlmuerzo")) {
            context.add("botIntent", new JsonPrimitive("botVerAlmuerzo"));
        } else if (currentTask.equals("taskVerRemision")) {
            context.add("botIntent", new JsonPrimitive("botVerRemision"));
        } else if (currentTask.equals("taskVerServicios")) {
            context.add("botIntent", new JsonPrimitive("botVerServicios"));
        } else if (currentTask.equals("taskVerManualidades")) {
            context.add("botIntent", new JsonPrimitive("botVerManualidades"));
        }
    }

    public JsonObject getBotOutput() throws IOException {

        JsonObject out = new JsonObject();
        String botIntent = context.get("botIntent").getAsString();
        JsonArray buttons = new JsonArray();
        String botUtterance = "";
        String type = "";

        if (botIntent.equals("botDefault")) {
            botUtterance = "Lo siento no puedo ayudarte en tu solicitud ";
            type = "Default";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botSaludo") || botIntent.equals("botMenuInicial")) {
            type = "MenuInicial";
            botUtterance = "Hola, escoje un perfil";
            out = getbotMenuInicial();
        } else if (botIntent.equals("botClubCliente")) {
            type = "ClubCliente";
            botUtterance = "Seleccione Club ";
            out = getbotClubCliente();
        } else if (botIntent.equals("botMenuCliente")) {
            type = "MenuCliente";
            botUtterance = "tus opciones:";
            out = getbotMenuCliente();
        } else if (botIntent.equals("botMenuEmpleado")) {
            type = "MenuEmpleado";
            botUtterance = "sus opciones";
            out = getbotMenuEmpleado();
        } else if (botIntent.equals("botClubEmpleado")) {
            type = "ClubEmpleado";
            botUtterance = "seleccione club";
            out = getbotClubEmpleado();
        } else if (botIntent.equals("botMenuEmpleadoClub")) {
            type = "MenuEmpleadoClub";
            botUtterance = "Club Campestre Guaymaral";
            out = getbotMenuEmpleadoClub();
        } else if (botIntent.equals("botVerJuegos")) {
            type = "VerJuegos";
            botUtterance = "Juegos Disponibles";
            out = getbotVerJuegos();
        } else if (botIntent.equals("botVerEventos")) {
            type = "VerEventos";
            botUtterance = "Eventos Del Año";
            out = getbotVerEventos();
        } else if (botIntent.equals("botVerReglas")) {
            type = "VerReglas";
            botUtterance = "reglas Club Campestre Guaymaral";
            out = getbotVerReglas();
        } else if (botIntent.equals("botVerAlmuerzo")) {
            type = "VerAlmuerzo";
            botUtterance = "Almuerzo Club Campestre Guaymaral";
            out = getbotVerAlmuerzo();
        } else if (botIntent.equals("botVerRemision")) {
            type = "VerRemision";
            botUtterance = "remision Club Campestre Guaymaral";
            out = getbotVerRemision();
        } else if (botIntent.equals("botVerServicios")) {
            type = "VerServicios";
            botUtterance = "Eventos Del Año";
            out = getbotVerServicios();
        } else if (botIntent.equals("botVerManualidades")) {
            type = "VerManualidades";
            botUtterance = "Manualidades disponibles";
            out = getbotVerManualidades();
        }
        out.add("botIntent", context.get("botIntent"));
        out.add("botUtterance", new JsonPrimitive(botUtterance));
        out.add("type", new JsonPrimitive(type));
        System.out.println("context: " + context.toString());
        System.out.println("salida: " + out.toString());
        return out;
    }

    public JsonObject getbotMenuInicial() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Cliente"));
        b.add("respuesta", new JsonPrimitive("requestClubCliente"));
        buttons.add(b);
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Empleado"));
        b.add("respuesta", new JsonPrimitive("requestMenuEmpleado"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotClubCliente() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Club Campestre Guaymaral"));
        b.add("respuesta", new JsonPrimitive("requestMenuCliente"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotMenuCliente() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Ver Servicios"));
        b.add("respuesta", new JsonPrimitive("requestVerServicios"));
        buttons.add(b);
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Ver Eventos"));
        b.add("respuesta", new JsonPrimitive("requestVerEventos"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotMenuEmpleado() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Juegos"));
        b.add("respuesta", new JsonPrimitive("requestVerJuegos"));
        buttons.add(b);
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Informacion Club"));
        b.add("respuesta", new JsonPrimitive("requestClubEmpleado"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotClubEmpleado() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("Club Campestre Guaymaral"));
        b.add("respuesta", new JsonPrimitive("requestMenuEmpleadoClub"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotMenuEmpleadoClub() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        b = new JsonObject();
        b1 = new JsonArray();
        e = new JsonObject();
        b.add("titulo", new JsonPrimitive("Reglas"));
        b.add("respuesta", new JsonPrimitive("requestVerReglas"));
        e.add("titulo", new JsonPrimitive("Reglas"));
        e.add("subtitulo", new JsonPrimitive("opcion 1"));
        b1.add(b);
        e.add("buttons", b1);
        elements.add(e);
        b = new JsonObject();
        b1 = new JsonArray();
        e = new JsonObject();
        b.add("titulo", new JsonPrimitive("Almuerzo"));
        b.add("respuesta", new JsonPrimitive("requestVerAlmuerzo"));
        e.add("titulo", new JsonPrimitive("Almuerzo"));
        e.add("subtitulo", new JsonPrimitive("opcion 2"));
        b1.add(b);
        e.add("buttons", b1);
        elements.add(e);
        b = new JsonObject();
        b1 = new JsonArray();
        e = new JsonObject();
        b.add("titulo", new JsonPrimitive("Remision"));
        b.add("respuesta", new JsonPrimitive("requestVerRemision"));
        e.add("titulo", new JsonPrimitive("Remision"));
        e.add("subtitulo", new JsonPrimitive("opcion 3"));
        b1.add(b);
        e.add("buttons", b1);
        elements.add(e);
        b = new JsonObject();
        b1 = new JsonArray();
        e = new JsonObject();
        b.add("titulo", new JsonPrimitive("Manualidadades"));
        b.add("respuesta", new JsonPrimitive("requestVerManualidades"));
        e.add("titulo", new JsonPrimitive("Manualidadades"));
        e.add("subtitulo", new JsonPrimitive("opcion 4"));
        b1.add(b);
        e.add("buttons", b1);
        elements.add(e);
        b = new JsonObject();
        b1 = new JsonArray();
        e = new JsonObject();
        b.add("titulo", new JsonPrimitive("Eventos"));
        b.add("respuesta", new JsonPrimitive("requestVerEventos"));
        e.add("titulo", new JsonPrimitive("Eventos"));
        e.add("subtitulo", new JsonPrimitive("opcion 5"));
        b1.add(b);
        e.add("buttons", b1);
        elements.add(e);
        out.add("elements", elements);
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotVerJuegos() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerJuegos();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            JsonArray elementosServicio = (JsonArray) servicio.get("Juego").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "" + obj.get("descripcion").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("descripcion").getAsString()));
                e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
                e.add("buttons", new JsonArray());
                elements.add(e);

            }
        } catch (Exception err) {
            e = new JsonObject();
            JsonObject obj = servicio.get("Juego").getAsJsonObject();
            e.add("titulo", new JsonPrimitive("" + "" + obj.get("descripcion").getAsString()));
            e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("descripcion").getAsString()));
            e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
            e.add("buttons", new JsonArray());
            elements.add(e);
        }
        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerEventos() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerEventos();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            JsonArray elementosServicio = (JsonArray) servicio.get("Evento").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("fecha").getAsString()));
                e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
                e.add("buttons", new JsonArray());
                elements.add(e);

            }
        } catch (Exception err) {
            e = new JsonObject();
            JsonObject obj = servicio.get("Evento").getAsJsonObject();
            e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
            e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("fecha").getAsString()));
            e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
            e.add("buttons", new JsonArray());
            elements.add(e);
        }
        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerReglas() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerReglas();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        e = new JsonObject();
        JsonObject obj = (JsonObject) servicio.get("Club").getAsJsonObject();
        e.add("titulo", new JsonPrimitive("" + "" + obj.get("reglas").getAsString()));
        e.add("subtitulo", new JsonPrimitive("Reglas"));       
        e.add("buttons", new JsonArray());
        elements.add(e);

        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerAlmuerzo() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerAlmuerzo();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        e = new JsonObject();
        JsonObject obj = (JsonObject) servicio.get("Club").getAsJsonObject();
        e.add("titulo", new JsonPrimitive("" + "" + obj.get("almuerzo").getAsString()));
        e.add("subtitulo", new JsonPrimitive("Almuerzo"));
        e.add("buttons", new JsonArray());
        elements.add(e);

        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerRemision() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerRemision();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        e = new JsonObject();
        JsonObject obj = (JsonObject) servicio.get("Club").getAsJsonObject();
        e.add("titulo", new JsonPrimitive("" + "" + obj.get("remision").getAsString()));
        e.add("subtitulo", new JsonPrimitive("Remision"));
        e.add("buttons", new JsonArray());
        elements.add(e);

        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerServicios() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerServicios();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            JsonArray elementosServicio = (JsonArray) servicio.get("Servicio").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
                e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
                e.add("buttons", new JsonArray());
                elements.add(e);

            }
        } catch (Exception err) {
            e = new JsonObject();
            JsonObject obj = servicio.get("Servicio").getAsJsonObject();
            e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
            e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
            e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
            e.add("buttons", new JsonArray());
            elements.add(e);
        }
        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }

    public JsonObject getbotVerManualidades() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getVerManualidades();
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            JsonArray elementosServicio = (JsonArray) servicio.get("Manualidad").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
                e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
                e.add("buttons", new JsonArray());
                elements.add(e);

            }
        } catch (Exception err) {
            e = new JsonObject();
            JsonObject obj = servicio.get("Manualidad").getAsJsonObject();
            e.add("titulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
            e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("nombre").getAsString()));
            e.add("url", new JsonPrimitive("" + "" + obj.get("url").getAsString()));
            e.add("buttons", new JsonArray());
            elements.add(e);
        }
        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }
}
