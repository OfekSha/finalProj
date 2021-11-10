package application.entities;


import java.util.Objects;

public class Client  {
    private String name="";
    private String password="";
    private Permission permission=Permission.User;

    public Client(String name, String password, Permission permission) {
        this.name = name;
        this.password = password;
        this.permission = permission;
    }
    public Client(){
        this(" "," ",Permission.User);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getName().equals(client.getName()) && getPassword().equals(client.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword());
    }
}
