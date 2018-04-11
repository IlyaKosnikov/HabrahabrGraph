package Graph;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class Vertex {
    private boolean filled;
    private boolean passed;
    Document doc;
    private String URL;
    private String name;
    private String date;
    private Elements intags;
    private Elements inlinks;
    private HashSet<Vertex> links = new HashSet<Vertex>();
    private HashSet<String> tags = new HashSet<>();
    private String rating;
    private String views;
    private String saves;
    private String tagtext;
    private String linktext;

    /*Конструктор вершины, берет данные из сети, хэшсеты тегов и ссылок изначально null, заполняются отдельным методом
    чтобы избежать рекурсии*/
    Vertex(String URL) {
        this.URL = URL;
        this.filled = false;
        this.passed=false;

        try {
            doc = Jsoup.connect(URL).get();
            this.date = doc.select(".post__time").first().text();
            this.name = doc.select(".post__title-text").first().text();
            this.views = doc.select(".post-stats__views-count").first().text();
            this.intags = doc.getElementsByClass("post__tag");
            this.inlinks = doc.getElementsByClass("post-info");
            if(doc.hasClass("stacked-counter__value_magenta")) {
                this.rating = doc.select(".stacked-counter__value.stacked-counter__value_magenta").first().text();
            }else  this.rating="0,0";
            this.saves = doc.select(".bookmark__counter.js-favs_count").first().text();
            this.linktext=LinkString();
            this.tagtext=TagString();
            for (Element tag: intags){
                tags.add(tag.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPassed(boolean pass){
        this.passed=pass;
    }

    public boolean getPassed(){
        return passed;
    }
    public String getDate(){
        return date;
    }
    public String getRating(){
        return rating;
    }
    public String getViews(){
        return views;
    }
    public String getSaves(){
        return saves;
    }

    public String getURL() {
        return this.URL;
    }

    public Elements getIntags() {
        return intags;
    }

    public Elements getInlinks() {
        return inlinks;
    }

    public void setTags(String tag) {
        tags.add(tag);
    }

    public HashSet<String> getTags(){
        return tags;
    }

    private String TagString(){
        String result="";
        for (Element tag : intags) {
            result = result+tag.select(".post__tag").text()+"\n";
        }
        return result;
    }
    private String LinkString(){
        String result="";
        for (Element link : inlinks) {
            if (link.select("a").attr("href").startsWith("/post")) {
                String post=link.select(".post-info__title_large").text()+" (https://habrahabr.ru"+link.select("a").attr("href")+")";
            result = result+post+"\n";
            }
        }
        return result;
    }
    public void setLinks (Vertex vertex){
            links.add(vertex);
        }

        public HashSet<Vertex> getLinks () {
            return links;
        }

        public void setFilled ( boolean filled){
            this.filled = filled;
        }

        public boolean getFilled () {
            return filled;
        }

        public String getName () {
            return name;
        }
        public String getTagtext(){
        return tagtext;
        }
        public String getLinktext(){
        return linktext;
        }

    }

