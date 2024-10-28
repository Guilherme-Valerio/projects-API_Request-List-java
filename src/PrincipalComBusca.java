
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Scanner;
import java.io.FileWriter;
import java.net.http.HttpResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import br.com.alura.screenmatch.modelos.ErroDeConversaoDeAno;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;


//Essa classe tem a mesma funcionalidade da Principal.java, porém aqui vou fazer uma REQUEST API
public class PrincipalComBusca {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String busca = "";

        while (!busca.equalsIgnoreCase("sair")) {
            System.out.println("Digite um filme para buscar");
            busca = leitura.nextLine();

            if (busca.equalsIgnoreCase("sair")) {
                break;
            }

            try {
                String endereco = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=e50cad54";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                String json = response.body();
                
                // Convertendo a resposta para JSON e verificando a chave "Response"
                String json = response.body();
                JSONObject jsonObject = new JSONObject(json);
                boolean responseStatus = jsonObject.optBoolean("Response", false);
                if (!responseStatus) {
                    System.out.println("Título não encontrado ou incorreto.");
                    continue; // Volta para o início do loop para uma nova entrada
                }
                
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

                TituloOmdb tituloJsonOmdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println(tituloJsonOmdb);

                Titulo tituloJson = new Titulo(tituloJsonOmdb);
                System.out.println(tituloJson);

                FileWriter escrita = new FileWriter("filmes.txt");
                escrita.write(tituloJson.toString());
                escrita.close();

            } catch (NumberFormatException e) {
                System.out.println("Aconteceu um erro");
                System.out.println(e.getMessage());

            } catch (IllegalArgumentException e) {
                System.out.println("Algum erro de argumento na busca, verificar");
                System.out.println(e.getMessage());

            } catch (ErroDeConversaoDeAno e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
