package com.example.androidfood.Model;

public class User {
    private String name;
    private String pass;
    private String phone;
    private String apellido;
    private String email;
    private String isstaff;
    //private String homeAdress;
    //private String entrecalles;
    //private String Pisoydepartamento;
    //private String Localidad;

    public User() {
    }

    public User(String name, String pass, String phone, String apellido, String email, String isstaff) {
        this.name = name;
        this.pass = pass;
        this.phone = phone;
        this.apellido = apellido;
        this.email = email;
        this.isstaff = isstaff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsstaff() {
        return isstaff;
    }

    public void setIsstaff(String isstaff) {
        this.isstaff = isstaff;
    }

    /*public User(String email, String pass, String name, String phone, String cliente) {
    }



    public User(String password, String name) {
        //Email = email;//
        //Apellido=apellido;//
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
        return Email;//
    }

    public void setEmail(String email) {
        this.Email = email;
    }*/

    /////////////////////////////

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
