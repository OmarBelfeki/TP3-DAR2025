package clientPackage;

import common.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("✅ Connecté au serveur !");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println((String) in.readObject());

            while (true) {
                System.out.print("Entrez une opération (ex: 5 + 3) ou 'exit' : ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    out.writeObject("exit");
                    out.flush();
                    break;
                }

                String[] parts = input.split(" ");
                if (parts.length != 3) {
                    System.out.println("Format invalide !");
                    continue;
                }

                try {
                    double op1 = Double.parseDouble(parts[0]);
                    double op2 = Double.parseDouble(parts[2]);
                    char operator = parts[1].charAt(0);

                    Operation operation = new Operation(op1, op2, operator);
                    out.writeObject(operation);
                    out.flush();

                    Object response = in.readObject();
                    System.out.println("Serveur : " + response);

                } catch (Exception e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }

            System.out.println("🔌 Déconnexion du serveur...");
        } catch (Exception e) {
            System.out.println("[ERREUR CLIENT] " + e.getMessage());
        }
    }
}
