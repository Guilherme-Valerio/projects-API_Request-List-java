package busca;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import java.io.IOException;

public class ConsultaCep {
    public Endereco buscaEndereco(String cep) {
        URI endereco = URI.create("https://viacep.com.br/ws/" + cep + "/json");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(endereco)
            .build();

        try {
            HttpResponse<String>response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
                return new Gson().fromJson(response.body(), Endereco.class);
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            throw new RuntimeException("Não conseguir obter o enderoço a partir desse CEP");
        }
    }
}
