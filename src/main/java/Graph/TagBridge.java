package Graph;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
/*Отдельный объект для тега, отличается от обхекта вершины тем, что содержит только ссылки, имя и URL.
Ссылки используются вершинами для получения дополнительных связей, имя для сортировки по тегам*/
public class TagBridge {
    Document doc;
    private String URL;
    private String name;
    private Elements inlinks;
    private HashSet<String> links=new HashSet<String>();
    TagBridge(String URL,String name){
        this.URL=URL;
        this.name=name;
        try {
            doc = Jsoup.connect(URL).get();
            this.inlinks=doc.getElementsByClass("post__title");
            fillHashLinks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void fillHashLinks(){
        for (Element link : inlinks) {
            if (link.select("a").attr("href").startsWith("https://habrahabr.ru/post")) {
                links.add(link.select("a").attr("href"));
                }
            }
        }

    public Elements getInlinks(){
        return inlinks;
    }
    public HashSet<String> getLinks(){
        return links;
    }
    public String getName(){
        return name;
    }
}
