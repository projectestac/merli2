package cat.xtec.ws.proxies.correu.test;

import cat.xtec.ws.proxies.correu.CorreuSender;
import cat.xtec.ws.proxies.correu.types.CorreuException;
import cat.xtec.ws.proxies.correu.types.CorreuResponse;
import cat.xtec.ws.proxies.correu.types.EnviamentResponse;
import java.io.PrintStream;
import java.util.ArrayList;

public class CorreuTester
{
  public static void main(String[] args)
  {
    try
    {
      if (args.length != 4)
      {
        System.out.println("No s'han passat paràmetres correctes. Els parametres que espera el tester son els seguents");
        System.out.println("1) Entorn d'execucio: els valors possibles son int, acc o prod");
        System.out.println("2) Id. de l'aplicació: ha d'estar donada d'alta al sistema");
        System.out.println("3) Adreça origen: l'adreça origen pot ser: apligest@correueducacio.xtec.cat, correus_aplicacions.educacio@xtec.cat o correus_aplicacions.educacio@gencat.cat");
        System.out.println("4) Adreça destí: l'adreça de correu on s'enviarà el test"); return;
      }
      String entorn = args[0];
      if ((!"int".equals(entorn)) && (!"acc".equals(entorn)) && (!"prod".equals(entorn)))
      {
        System.out.println("El valor " + entorn + " del paràmetre 'entorn' no es correspon a cap valor vàlid. Ha de ser: " + "int" + ", " + "acc" + " o " + "prod"); return;
      }
      String app = args[1];
      String origen = args[2];
      if ((!"correus_aplicacions.educacio@gencat.cat".equals(origen)) && (!"correus_aplicacions.educacio@xtec.cat".equals(origen)) && (!"apligest@correueducacio.xtec.cat".equals(origen)))
      {
        System.out.println("El valor " + origen + " del paràmetre 'correu origen' no es correspon a cap valor vàlid. Pot ser: " + "apligest@correueducacio.xtec.cat" + ", " + "correus_aplicacions.educacio@xtec.cat" + " o " + "correus_aplicacions.educacio@gencat.cat"); return;
      }
      String desti = args[3];
      
      CorreuSender sender = new CorreuSender(app, entorn, true);
      String disponible = sender.consultaDisponibilitat(origen);
      if ("OK".equals(disponible))
      {
        EnviamentResponse resposta = sender.enviaCorreuAmbAdjunt(origen, desti, "Assumpte del missatge. Caracters: ÀÁÈÉÍÏÒÓÚÜÇÑàáèéíïòóúüçñ'", "<html><body>Contingut del missatge en HTML<br/>Caracters: <b>ÀÁÈÉÍÏÒÓÚÜÇÑàáèéíïòóúüçñ'</b></body></html>", 1, "Hola, món!".getBytes(), "hola món.txt", "text/plain");
        if (resposta.isOk())
        {
          System.out.println("El seu correu s'ha enviat correctament a la direcció de correu " + desti);
        }
        else if (resposta.unsendedMessages().size() > 0)
        {
          CorreuResponse response = (CorreuResponse)resposta.unsendedMessages().get(0);
          System.out.println("Error associat al correu enviat: " + response.getErrorMessage());
        }
        else
        {
          System.out.println("Error general: " + resposta.getMessage());
        }
      }
      else
      {
        System.out.println("Hi ha problemes de disponibilitat del servei: " + disponible);
      }
    }
    catch (CorreuException localCorreuException) {}catch (Exception localException) {}
    System.out.println();
  }
}
