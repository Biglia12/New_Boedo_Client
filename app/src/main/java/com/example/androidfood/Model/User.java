package com.example.androidfood.Model;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String Apellido;
    private String email;
    private String IsStaff;
    //private String homeAdress;
    //private String entrecalles;
    //private String Pisoydepartamento;
    //private String Localidad;


    public User() {

    }



    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff="false";



    }
    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }



    public String getName() {
        return Name;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }
    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*public String getHomeAdress() {
        return homeAdress;
    }

    public void setHomeAdress(String homeAdress) {
        this.homeAdress = homeAdress;
    }

    public String getEntrecalles() {
        return entrecalles;
    }//

    public void setEntrecalles(String entrecalles) {
        this.entrecalles = entrecalles;
    }//

    public String getPisoydepartamento() {
        return Pisoydepartamento;
    }//

    public void setPisoydepartamento(String pisoydepartamento) {
        Pisoydepartamento = pisoydepartamento;
    }//

    public String getLocalidad() {
        return Localidad;
    }//

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }//*/
}
