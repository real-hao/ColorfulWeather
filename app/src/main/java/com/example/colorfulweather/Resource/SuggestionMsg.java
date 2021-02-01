package com.example.colorfulweather.Resource;

public class SuggestionMsg {
    private String text;
    private String category;
    private String name;

    public SuggestionMsg(String text, String category, String name) {
        this.text = text;
        this.category = category;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
