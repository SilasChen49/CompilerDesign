package lexical;

public class State {

    private int state;
    private String value;

    State(){
        state = 0;
        value = new String();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void extend(char c){
        value = value + c;
    }

    public String getValue() {
        return value;
    }
}
