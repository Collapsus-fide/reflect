package reflect;

import org.apache.commons.cli.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;


public class App2 {
    private static final Logger LOG = Logger.getLogger(App.class.getName());
    public static void main(String[] args) {
        String classe = null;
        String methode = null;

        Options options = new Options();
        Option c = new Option("c","class",true,"nom de la classe à i");
        c.setRequired(true);
        options.addOption(c);
        Option m = new Option("m","class",true,"nom de la methode à invoquer");
        m.setRequired(true);
        options.addOption(m);
        CommandLineParser parser = new DefaultParser();
        try{
            CommandLine line = parser.parse(options, args);
            classe = line.getOptionValue("c");
            methode = line.getOptionValue("m");
        } catch (ParseException exp) {
            LOG.severe("Erreur dans la ligne de commande");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("inspect", options);
            System.exit(1);
        }

        Class<?> cl = null;
        try{
            cl = Class.forName(classe);
        }catch(ClassNotFoundException e){
            LOG.severe("classe introuvable !");
            System.exit(1);
        }
        Class<?>[] parameterTypes = new Class[]{int.class,String.class,String.class};
        try{
            Constructor<?> constructor = cl.getConstructor(parameterTypes);
            Object obj = constructor.newInstance(1,"Collapsus","Fide");

            Method meth = cl.getMethod(methode);
            String res = (String) meth.invoke(obj);
            System.out.printf("Classe: %s%n",classe);
            System.out.printf("Methode: %s%n",methode);
            System.out.printf("Resultat: %s%n",res);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.printf("Inspection de la classe: %s %n", cl.getName());
        System.out.printf("héritée de : %s %n", cl.getSuperclass());
        Field[] lf = cl.getDeclaredFields();
        System.out.printf("Atttributs :%n");
        for(Field f : lf) {
            System.out.printf("\t -%s %n", f.getName());
        }
        System.out.printf("Méthodes :%n");
        Method[] lm = cl.getDeclaredMethods();
        for(Method me : lm){
            System.out.printf("\t -%s %n", me.getName());
        }
    }
}

