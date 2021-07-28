package wolox.training.dto;

public class Publisher {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "name='" + name + '\'' +
                '}';
    }
}
