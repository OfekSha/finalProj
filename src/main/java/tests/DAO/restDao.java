package tests.DAO;

import application.controller.dao.DAO;
import application.entities.Restaurant;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class restDao implements DAO<Restaurant> {
    Restaurant rest;
    Gson gson;

    public restDao() {
        this.rest = null;
        gson= new Gson();
    }

    @Override
    public Optional<Restaurant> get(long id) {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get("rest_test_data.json"));
            rest = gson.fromJson(reader,Restaurant.class);
        } catch (IOException e) {
            rest=null;
            e.printStackTrace();
        }
        return Optional.ofNullable(rest);

    }
    @Deprecated // restaurant has only one data.
    @Override
    public List<Restaurant> getAll() {
        return null;
    }

    @Override
    public void save(Restaurant restaurant) {
        rest=restaurant;
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Restaurant restaurant, String[] params) {
        rest=restaurant;
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Restaurant restaurant) {
        rest = null;
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
