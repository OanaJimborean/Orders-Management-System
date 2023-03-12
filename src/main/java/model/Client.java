package model;

public class Client {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String dateOfBirth;


    /**
     * Creates a new instance of Client.
     * @param id valid integer.
     * @param name valid name containing only letters and spaces.
     * @param address can be null
     * @param phone can be null, otherwise a String object of length 10 and containing only digits.
     * @param email a valid email.
     * @param dateOfBirth can be null, otherwise a String of the format 'yyyy-mm-dd'
     */

    public Client(int id, String name, String address, String phone, String email, String dateOfBirth)    {
        this.id=id;
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.dateOfBirth=dateOfBirth;
    }


     // Parameterless constructor.

    public Client()
    {
        this.id=0;
        this.name=null;
        this.address=null;
        this.phone=null;
        this.email=null;
        this.dateOfBirth=null;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }


    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return ""+name+", id: "+id;
    }

    public String toStringBill(){
        return "name: "+name+"; id: "+id +"; address: "+address+"; phone: "+phone+"; email: "+email;
    }
}
