package model;

public class Dim {
    private int dim;
    private String name;

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dim{" +
                "dim=" + dim +
                ", name='" + name + '\'' +
                '}';
    }
}
