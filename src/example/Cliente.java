package example;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nome;
    private String nomeContato;
    private List<String> amigos;

    public Cliente(String nome, String nomeContato) {
        this.nome = nome;
        this.nomeContato = nomeContato;
        this.amigos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void adicionarAmigo(String nomeAmigo) {
        amigos.add(nomeAmigo);
        System.out.println(nome + " adicionou " + nomeAmigo + " como amigo.");
    }
}
