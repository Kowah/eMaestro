import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Telecommande {

	public static void main(String[] args) {

		try {

			Socket socketServeur = new Socket("127.0.0.1", 15555);

			PrintWriter printer = new PrintWriter(socketServeur.getOutputStream());

			String message = lireMessageAuClavier();

			while (!message.equals("QUIT")) {
				envoyerMessage(printer, message);
				message = lireMessageAuClavier();
			}

			socketServeur.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String lireMessageAuClavier() throws IOException {
		// lit un message au clavier sur System.in
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		return buff.readLine();
	}

	public static void envoyerMessage(PrintWriter printer, String message) throws IOException {
		// Envoyer le message vers le client

		printer.write(message + "\n");
		printer.flush();

	}

}
