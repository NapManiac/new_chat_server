package entity;



public class Contacts {


    private String id;

    private String name;
    private String temp;//聊天的最后一句话
    private String motto;


    public Contacts() {

    }


    public Contacts(String id, String name, String motto) {
        this.id = id;
        this.name = name;
        this.motto = motto;
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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

}
