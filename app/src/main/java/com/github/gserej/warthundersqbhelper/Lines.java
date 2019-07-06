package com.github.gserej.warthundersqbhelper;

public class Lines {

    private String id;
    private String message;

    public Lines(String id, String message) {
        this.id = id;
        this.message = message;
    }


    @Override
    public String toString() {
        return "Lines{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
