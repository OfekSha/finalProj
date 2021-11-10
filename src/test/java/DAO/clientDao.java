package DAO;

import application.controller.dao.DAO;
import application.entities.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class clientDao implements DAO<Client> {
    Set<Client> clients=new HashSet<Client>();
    Gson gson;
    @Override
    public Optional<Client> get(String id) {
        return Optional.empty();
    }

    @Override
    public List<Client> getAll() {
        return null;
    }

    @Override
    public void save(Client client) {
        clients.clear();

        Reader reader = null;

            try {
                reader = Files.newBufferedReader(Paths.get("client_test_data.json"));
                Type setType = new TypeToken<HashSet<Client>>(){}.getType();
                HashSet<Client> temp = gson.fromJson(reader,setType);
                if(!temp.isEmpty()){
                    clients.addAll(temp);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        clients.add(client);
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("client_test_data.json"));
            gson.toJson(clients,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Client client, String[] params) {

    }

    @Override
    public void delete(Client client) {

    }
}
