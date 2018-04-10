package Graph;

import org.jsoup.nodes.Element;


public class Graph {
    //Массивы, хранящие уже созданные объекты вершин и тегов
    private Vertex[] VertexList;
    private TagBridge[] TagList;
    //Переменные, хрянящие количество тегов, количество элементов графа и максимально возможное количество элементов
    private int tags;
    private int elements;
    private int elementsMax;
    //Для инициализации графа достаточно URL исходной вершины и максимального количества вершин
    public Graph (String URL,int stepsMax){
        //Создание массивов для хранения вершин и тегов нужного размера, определение базовых учетных переменных
        VertexList=new Vertex[stepsMax];
        TagList=new TagBridge[500];
        this.elementsMax=stepsMax;
        tags=0;
        elements=1;
        //Первым объектом массива вершин автоматически создается исходная вершина, затем вызывается метод заполнения графа
        VertexList[0]=new Vertex(URL);
        FillGraph();
    }
    private void FillVertex(Vertex current) {
        //Из этого метода последовательно вызывается метод заполнения списка тегов и метод заполнения списка связей
        FillTags(current);
        FillLinks(current);
    }

    private void FillGraph() {
        //В цикле осуществляется проход по всему массиву, хранящему объекты вершин
            for (int i = 0; i < VertexList.length; i++) {
                /*Проверка на отсутствие объекта в массиве, так как отсутствие возможно, только если количество вершин по связям
                получается меньше заявленного(массив автозаполняется по мере цикла), то проверка нужна только одна*/
                if(VertexList[i]==null&&elements<elementsMax){
                    break;
                }
                //Проверка, заполнены ли ссылки и теги объекта, если нет, вызывается метод заполнения
                if (!VertexList[i].getFilled()) {
                    FillVertex(VertexList[i]);
                }

            }
        }


    public Vertex[] getGraphArray(){
        return VertexList;
    }
    public TagBridge[] getTagArray(){
        return TagList;
    }

private void FillLinks(Vertex current) {
    boolean checked;
    /*Из текущей вершины достается список в котором хранятся все поля с сайта, относящиеся к классу post-info (хранит
    информацию о связанных публикациях). В цикле осуществляется проход по всем объектам списка*/
    for (Element link : current.getInlinks()) {
        //Проверка, если уже такая вершина в массиве вершин
        checked = false;
        //Проверка, ведет ли ссылка другую публикацию. Все ссылки, ведущие в другие места отбрасываются
        if (link.select("a").attr("href").startsWith("/post")) {
            //Осуществляется проход по всему массиву вершин, для всех не пустых элементов сравниваем их URL с URL ссылки из элемента списка links
            for (int i = 0; i < VertexList.length; i++) {
                String post="https://habrahabr.ru"+link.select("a").attr("href");
                if (VertexList[i] != null && post.equals(VertexList[i].getURL())) {
                    //Если в массиве вершин уже есть вершина с таким URL, то передаем в список связей текущего объекта ссылку на нее
                    current.setLinks(VertexList[i]);
                    checked = true;
                }
            }
            //Если вершины не существует
            if (checked == false) {
                //Проверка на переполнение списка вершин, новые вершины будут добавляться только если не превышено их максимальное количество
                if (elements != VertexList.length) {
                    //В массиве вершин создается новая вершина
                    String post="https://habrahabr.ru"+link.select("a").attr("href");
                    VertexList[elements] = new Vertex(post);
                    //В список связей текущего объекта передается ссылка на него, счетчик вершин увеличивается на 1
                    current.setLinks(VertexList[elements]);
                    elements++;
                }
            }
        }
    }
    current.setFilled(true);
}
private void FillTags(Vertex current){
    boolean checked;
    /*Из текущей вершины достается список в котором хранятся все поля с сайта, относящиеся к классу inline-list__item_tag (хранит
    информацию о теге). В цикле осуществляется проход по всем объектам списка*/
    for (Element tag : current.getIntags()) {
        //Проверка, есть ли уже такой тег в массиве тегов
        checked = false;
        //Из класса post_tag вынимается текст названия
        String tagname=tag.select(".post__tag").text();
        //В цикле проходим по всему массиву тегов, сравнивая их имена
            for (int i = 0; i < TagList.length; i++) {
                if (TagList[i]!= null && tagname.equals(TagList[i].getName())) {
                    //Если имена совпадают, то текущий объект получает в свой список тегов ссылку на уже существующий объект из массива тегов
                    current.setTags(TagList[i]);
                    checked = true;
                }
            }
            //Если такого тега еще нет
            if (checked == false) {
                //Проверка массива тегов на переполнение
                if (tags!= TagList.length) {
                    //В массиве тегов создается новый объект тега, текущий объект получает в свой список тегов ссылку на него, счетчик тегов увеличивается на 1
                    TagList[tags] = new TagBridge(tag.select("a").attr("href"),tag.select(".post__tag").text());
                    current.setTags(TagList[tags]);
                    tags++;
                }
            }

    }
}
public void ClearPassed(){
        for(Vertex vertex: VertexList){
            vertex.setPassed(false);
        }
}
}
