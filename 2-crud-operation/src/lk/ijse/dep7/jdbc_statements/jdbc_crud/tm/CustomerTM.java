package lk.ijse.dep7.jdbc_statements.jdbc_crud.tm;

public class CustomerTM {
    private String id;
    private String name;
    private String address;
    private String nic;


    public CustomerTM() {
    }

    public CustomerTM(String id, String name, String address, String nic) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.nic = nic;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Override
    public String toString() {
        return "CustomerTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nic='" + nic + '\'' +
                '}';
    }
}
