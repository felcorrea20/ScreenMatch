package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.excecao.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner leitor = new Scanner(System.in);

        String busca;
            List<Titulo> titulos = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (true) {

            System.out.print("Digite um filme para busca: ");
            busca = leitor.nextLine();

            if (busca.equalsIgnoreCase("sair")) {
                break;
            }

            String chave = "eb57dae1";
            String endereco = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=" + chave;

            try {

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);

                TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println(meuTituloOmdb);


                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                System.out.println("====================");
                System.out.println(meuTitulo);
                titulos.add(meuTitulo);

            } catch (NumberFormatException e) {

                System.out.println("\nAcontececu um erro:");
                System.out.println(e.getMessage());

            } catch (IllegalArgumentException erro) {

                System.out.println("\nAcontececu um erro:");
                System.out.println(erro.getMessage());

            } catch (ErroDeConversaoDeAnoException e) {

                System.out.println(e.getMessage());

            }

        }

        System.out.println("Deseja salvar filmes buscados ( [S]im [N]ão ) ?");
        String opcaoSalvamento = leitor.nextLine();

        if (opcaoSalvamento.equalsIgnoreCase("s")) {
            FileWriter escrita = new FileWriter("filmes_buscados.json");
            escrita.write(gson.toJson(titulos));
            escrita.close();
        }

    }

}
