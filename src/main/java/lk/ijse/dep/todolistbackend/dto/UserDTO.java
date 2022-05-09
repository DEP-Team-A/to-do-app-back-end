package lk.ijse.dep.todolistbackend.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String ID;
    private String name;
    private String password;
    private String email;

    public UserDTO(String ID, String name, String password, String email) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserDTO() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
