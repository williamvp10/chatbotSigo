
import static spark.Spark.*;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import chatbot.Chatbot;
import chatbot.web.utils.ResponseError;
import static chatbot.web.utils.JSONUtil.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {

    public static void main(String[] args) {
        Spark.setPort(getHerokuAssignedPort());
        Spark.staticFileLocation("/public");

        final Chatbot bot = new Chatbot();

        get("/", (req, res) -> "Hello World! I am the BlockChain bot!!");

        //post handle for WeatherMan chatbot
        post("/bot", new Route() {
            public Object handle(Request request, Response response) {

                String body = request.body();

                System.out.println("body: " + body);
                String splitChar = "&";
                String keyValueSplitter = "=";
                String[] params = body.split(splitChar);
                
                String userName = "noneSaid", userUtterance = "noneSaid", userType = "noneSaid", userImagen = "noneSaid";

                for (int i = 0; i < params.length; i++) {
                    System.out.println(params[i]);
                    String[] sv = params[i].split(keyValueSplitter);
                    if (sv[0].equals("userName")) {
                        if (sv.length > 1) {
                            userName = sv[1];
                        } else {
                            userName = "";
                        }
                        userName = userName.replaceAll("%20", " ");
                        userName = userName.replaceAll("%3A", ":");
                    } else if (sv[0].equals("userUtterance")) {
                        if (sv.length > 1) {
                            userUtterance = sv[1];
                        } else {
                            userUtterance = "";
                        }
                        userUtterance = userUtterance.replaceAll("%20", " ");
                        userUtterance = userUtterance.replaceAll("%3A", ":");
                    } else if (sv[0].equals("userType")) {
                        if (sv.length > 1) {
                            userType = sv[1];
                        } else {
                            userType = "";
                        }
                        userType = userType.replaceAll("%20", " ");
                        userType = userType.replaceAll("%3A", ":");
                    } else if (sv[0].equals("userImagen")) {
                        if (sv.length > 1) {
                            userImagen = sv[1];
                        } else {
                            userImagen = "";
                        }
                        userImagen = userImagen.replaceAll("%20", " ");
                        userImagen = userImagen.replaceAll("%3A", ":");
                        userImagen = userImagen.replaceAll("%2F", "/");
                        userImagen = userImagen.replaceAll("%3F", "?");
                        userImagen = userImagen.replaceAll("%3D", "=");
                        userImagen = userImagen.replaceAll("%26", "&");
                        System.out.println("url " + userImagen);
                    }
                }

                if (!userUtterance.equals("noneSaid")) {

                    System.out.println("Main: User says:" + userUtterance);

                    JsonObject userInput = new JsonObject();
                    userInput.add("userUtterance", new JsonPrimitive(userUtterance));
                    if (!userName.equals("noneSaid")) {
                        System.out.println("name:" + userName);
                        userInput.add("userName", new JsonPrimitive(userName));
                    }
                    if (!userType.equals("noneSaid")) {
                        System.out.println("type:" + userType);
                        userInput.add("userType", new JsonPrimitive(userType));
                    }
                    if (!userImagen.equals("noneSaid")) {
                        System.out.println("Image:" + userImagen);
                        userInput.add("userImagen", new JsonPrimitive(userImagen));
                    }
                    String botResponse = null;
                    try {
                        botResponse = bot.processFB(userInput);
                    } catch (IOException ex) {
                        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Main: Bot says:" + botResponse);
                    if (botResponse != null) {
                        return botResponse;
                    }
                } else {
                    return null;
                }
                response.status(400);
                return new ResponseError("Error! POST not handled.");
            }

        }, json());

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}
