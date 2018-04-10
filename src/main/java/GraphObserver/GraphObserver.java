package GraphObserver;

import Graph.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
/*Класс, позволяющий обходить граф в памяти и использовать различные методы его обработки. Во всех случаях реализован обход
в ширину с использованием очереди*/
public class GraphObserver {
    private HashMap<String, Vertex> SortByRating;
    private HashMap<String, Vertex> SortByViews;
    private HashMap<String, Vertex> SortBySaves;
    private HashMap<String, Vertex> ShowAllVertex;
    private HashMap<String, Vertex> ShowLinkedObjects;
//Метод, предназначенный для вывода всех объектов графа
    public ObservableList<Vertex> ShowAll(Graph graph){
        ShowAllVertex=new HashMap<String, Vertex>();
        LinkedList<Vertex>VertexQueue=new LinkedList<Vertex>();
        ObservableList<Vertex> result= FXCollections.observableArrayList();
        //В очередь отправляется первый объект из массива объектов графа
        VertexQueue.addFirst(graph.getGraphArray()[0]);
        /* Пока очередь не пуста, текущий объект в очереди отмечается, как пройденный (переменная passed), затем ссылки
        на URL вершины (в качестве ключа) и сама вершина помещается в HashMap */
        while(VertexQueue.peek()!=null){
            VertexQueue.peek().setPassed(true);
            ShowAllVertex.put(VertexQueue.peek().getURL(),VertexQueue.peek());
            /*Осуществляется проход по всем ссылкам на объеты в поле ссылок текущего объекта, если объект не пройден,
            он помещается в очередь*/
            for(Vertex link: VertexQueue.peek().getLinks()){
                if(!link.getPassed()){
                    VertexQueue.add(link);
                }
            }
            //Первый элемент очереди удаляется
            VertexQueue.removeFirst();
        }
        //Осуществляем проход по всей HashMap и помещаем ее записи в ObservableList
        for(Map.Entry<String,Vertex> map: ShowAllVertex.entrySet()){
            String stringkey=map.getKey();
            for(Vertex vertex: graph.getGraphArray()){
                if(stringkey.equals(vertex.getURL())){
                    result.add(vertex);
                }
            }
        }
        //Очистка всех объектов графа от меток passed, чтобы можно было обходить граф и в дальнейшем
        graph.ClearPassed();
        //Возвращяем ObservableList
        return result;

    }
    /*В методах сортировки алгоритм аналогичен обходу всего графа, добавляется только проверка на соответствие
    хотя бы одного тега объекта заявленной категории сортировки и сама сортировка списка перед его возвращением*/
    public ObservableList<Vertex> SortByRatings(Graph graph, String category){
        SortByRating=new HashMap<String, Vertex>();
        LinkedList<Vertex>VertexQueue=new LinkedList<Vertex>();
        ObservableList<Vertex> result= FXCollections.observableArrayList();
        VertexQueue.addFirst(graph.getGraphArray()[0]);
        while(VertexQueue.peek()!=null){
            VertexQueue.peek().setPassed(true);
            //Проверка категории отдельным методом
            if(CheckCategory(VertexQueue.peek(),category)) {
                SortByRating.put(VertexQueue.peek().getURL(), VertexQueue.peek());
            }
                for (Vertex link : VertexQueue.peek().getLinks()) {
                if (!link.getPassed()) {
                                VertexQueue.add(link);
                    }
               }
                VertexQueue.removeFirst();
        }

        graph.ClearPassed();
        //Возврат отсортированного ObservableList
        result=SortHashbyRating(SortByRating);
        graph.ClearPassed();
        return result;
    }

    public ObservableList<Vertex> SortByViews(Graph graph, String category){
        SortByViews=new HashMap<String, Vertex>();
        LinkedList<Vertex>VertexQueue=new LinkedList<Vertex>();
        ObservableList<Vertex> result= FXCollections.observableArrayList();
        VertexQueue.addFirst(graph.getGraphArray()[0]);
        while(VertexQueue.peek()!=null){
            VertexQueue.peek().setPassed(true);
            if(CheckCategory(VertexQueue.peek(),category)) {
                SortByViews.put(VertexQueue.peek().getURL(), VertexQueue.peek());
            }
            for (Vertex link : VertexQueue.peek().getLinks()) {
                if (!link.getPassed()) {
                    VertexQueue.add(link);
                }
            }
            VertexQueue.removeFirst();
        }

        graph.ClearPassed();
        result=SortHashbyViews(SortByViews);
        graph.ClearPassed();
        return result;
    }

    public ObservableList<Vertex> SortBySaves(Graph graph, String category){
        SortBySaves=new HashMap<String, Vertex>();
        LinkedList<Vertex>VertexQueue=new LinkedList<Vertex>();
        ObservableList<Vertex> result= FXCollections.observableArrayList();
        VertexQueue.addFirst(graph.getGraphArray()[0]);
        while(VertexQueue.peek()!=null){
            VertexQueue.peek().setPassed(true);
            if(CheckCategory(VertexQueue.peek(),category)) {
                SortBySaves.put(VertexQueue.peek().getURL(), VertexQueue.peek());
            }
            for (Vertex link : VertexQueue.peek().getLinks()) {
                if (!link.getPassed()) {
                    VertexQueue.add(link);
                }
            }
            VertexQueue.removeFirst();
        }

        graph.ClearPassed();
        result=SortHashbySaves(SortBySaves);
        graph.ClearPassed();
        return result;
    }
    //Проверка соответствия хотя бы одного тега вершины заявленной категории
    private boolean CheckCategory(Vertex vertex, String category){
        //Проходим по всем тегам вершины, если есть хоть одно совпадение с категорией, возвращаем true
        for(TagBridge tag: vertex.getTags()){
            if(tag.getName().equals(category))return true;
        }
        return false;
    }
    //Метод, предназначенный для перевода строки с сайта в тип float, используется для сортировки по рейтингу
    private float ToFloat(String incoming){
        String[] array=incoming.split("");
        float result;
        String finaly="";
        for(String literal:array){
            if (literal.equals(",")){
                literal=".";
            }
            finaly=finaly+literal;
        }
        finaly=finaly+"f";
        result=Float.parseFloat(finaly);
        return result;
    }
    //Метод, предназначенный для перевода строки с сайта в тип int, используется для сортировки по просмотрам и сохранениям
    private int ToInt(String incoming){
        String[] array=incoming.split("");
        int result;
        String finaly="";
        for(String literal:array){
            if (literal.equals(",")){
                literal="";
            }
            if (literal.equals("k")){
                literal="000";
            }
            finaly=finaly+literal;
        }
        result=Integer.parseInt(finaly);
        return result;
    }
    /*Методы сортировки в целом аналогичны. HashMap переводится в ObservableList, после чего он сортируется
    компаратором по нужному значению*/
    private ObservableList<Vertex> SortHashbyRating(HashMap<String, Vertex> incoming){
        ObservableList<Vertex> sortedHash = FXCollections.observableArrayList();
        for(Map.Entry<String,Vertex> map: incoming.entrySet()){
            Vertex value=map.getValue();
            sortedHash.add(value);
        }
        Comparator<Vertex> comparator = new Comparator<Vertex>() {
            @Override
            public int compare(Vertex left, Vertex right) {
                return (int) (ToFloat(right.getRating()) - ToFloat(left.getRating()));
            }
        };
        Collections.sort(sortedHash,comparator);
        return sortedHash;
    }

    private ObservableList<Vertex> SortHashbyViews(HashMap<String, Vertex> incoming){
        ObservableList<Vertex> sortedHash=FXCollections.observableArrayList();
        for(Map.Entry<String,Vertex> map: incoming.entrySet()){
            Vertex value=map.getValue();
            sortedHash.add(value);
        }
        Comparator<Vertex> comparator = new Comparator<Vertex>() {
            @Override
            public int compare(Vertex left, Vertex right) {
                return ToInt(right.getViews()) - ToInt(left.getViews());
            }
        };
        Collections.sort(sortedHash,comparator);
        return sortedHash;
    }

    private ObservableList<Vertex> SortHashbySaves(HashMap<String, Vertex> incoming){
        ObservableList<Vertex> sortedHash=FXCollections.observableArrayList();
        for(Map.Entry<String,Vertex> map: incoming.entrySet()){
            Vertex value=map.getValue();
            sortedHash.add(value);
        }
        Comparator<Vertex> comparator = new Comparator<Vertex>() {
            @Override
            public int compare(Vertex left, Vertex right) {
                return ToInt(right.getSaves()) - ToInt(left.getSaves());
            }
        };
        Collections.sort(sortedHash,comparator);
        return sortedHash;
    }
    //Метод ищущий ссылки на одну вершину и ссылки, ведущие от нее
    public ObservableList<Vertex> ShowLinkedObjects(Graph graph, String URL,String sort){
        ShowLinkedObjects=new HashMap<String, Vertex>();
        LinkedList<Vertex>VertexQueue=new LinkedList<Vertex>();
        ObservableList<Vertex> result= FXCollections.observableArrayList();
        VertexQueue.addFirst(graph.getGraphArray()[0]);
        boolean check=false;
        //Проходим по всему графу и ищем ссылки на указанный обхект
        while(VertexQueue.peek()!=null){
            VertexQueue.peek().setPassed(true);
            //Если найденный объект и есть та самая вершина, в HashMap добавляются все объекты на которые у него есть ссылки
            if(VertexQueue.peek().getURL().equals(URL)){
                check=false;
                for(Vertex link: VertexQueue.peek().getLinks()){
                    ShowLinkedObjects.put(link.getURL(),link);
                }
            }else {
                //Иначе смотрим на ссылки текущей вершины и ищем нужную
                for(Vertex link: VertexQueue.peek().getLinks()){
                    check=false;
                    //Если ссылка найдена, вложенный цикл прерывается, а объект, содержащий ссылку, добавляется в HashMap
                    if(link.getURL().equals(URL)){
                        check=true;
                        break;
                    }
                }
            }
            if(check) {
                ShowLinkedObjects.put(VertexQueue.peek().getURL(), VertexQueue.peek());
            }
            for(Vertex link: VertexQueue.peek().getLinks()){
                if(!link.getPassed()){
                    VertexQueue.add(link);
                }
            }
            VertexQueue.removeFirst();
        }
        //Метод может использовать все три варианта сортировки, в зависимости от переданного ключа
        if(sort.equals("rating")){
            result=SortHashbyRating(ShowLinkedObjects);
        }
        else if(sort.equals("views")){
            result=SortHashbyViews(ShowLinkedObjects);
        }
        else if(sort.equals("saves")){
            result=SortHashbySaves(ShowLinkedObjects);
        }
        graph.ClearPassed();
        return result;
    }
}
