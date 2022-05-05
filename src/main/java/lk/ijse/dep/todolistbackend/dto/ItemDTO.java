package lk.ijse.dep.todolistbackend.dto;

public class ItemDTO {  private String id;
    private String description;
    private String email;
    private Enum status;

    public ItemDTO() {
    }

    public ItemDTO(String id, String description, String email, Enum status) {
        this.id = id;
        this.description = description;
        this.email = email;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }
}


