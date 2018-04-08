package Application;

import GraphObserver.GraphObserver;
import au.com.bytecode.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Graph.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    TextField URL;
    @FXML
    TextField VERTEX_NUMBER;
    @FXML
    Button ShowAll;
    @FXML
    Button SortBy;
    @FXML
    Button ShowLinked;
    @FXML
    Button ExportToCSV;
    @FXML
    Label LGraph;
    @FXML
    Label LURL;
    @FXML
    Label LVertex;
    @FXML
    Button CreateGraph;
    @FXML
    private TableView<Vertex> VertexTable;
    @FXML
    private TableColumn<Vertex, String> URLColumn;
    @FXML
    private TableColumn<Vertex, String> NameColumn;
    @FXML
    private TableColumn<Vertex, String> DateColumn;
    @FXML
    private TableColumn<Vertex, String> TagsColumn;
    @FXML
    private TableColumn<Vertex, String> LinksColumn;
    @FXML
    private TableColumn<Vertex, String> RatingColumn;
    @FXML
    private TableColumn<Vertex, String> ViewsColumn;
    @FXML
    private TableColumn<Vertex, String> SavesColumn;

    private boolean created;
    private Graph graph;
    private GraphObserver GO;
    private String chosetag;
    private String choserating;
    private String linkedURL;
    private String filepath;
    List<String> choicerating = new ArrayList<String>();
    private ObservableList<Vertex> TableView= FXCollections.observableArrayList();

    @FXML private void initialize(){
        filepath="ExportData.csv";
        choicerating.add("Рейтинг публикации");
        choicerating.add("Количество просмотров");
        choicerating.add("Количество сохранений");
        URLColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("URL"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("name"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("date"));
        TagsColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("tagtext"));
        LinksColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("linktext"));
        RatingColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("rating"));
        ViewsColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("views"));
        SavesColumn.setCellValueFactory(new PropertyValueFactory<Vertex, String>("saves"));
        URLColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        NameColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        DateColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        TagsColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        LinksColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        RatingColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        ViewsColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        SavesColumn.prefWidthProperty().bind(VertexTable.widthProperty().divide(8));
        LGraph.setVisible(false);
        ExportToCSV.setVisible(false);
        ShowAll.setVisible(false);
        SortBy.setVisible(false);
        ShowLinked.setVisible(false);
        VertexTable.setVisible(false);

    }
    public void createGraph(){
        if(URL.getText().startsWith("https://habrahabr.ru/post/")) {
            Thread CG=new Thread(new Runnable() {
                public void run() {
                    ExportToCSV.setVisible(false);
                    ShowAll.setVisible(false);
                    SortBy.setVisible(false);
                    ShowLinked.setVisible(false);
                    VertexTable.setVisible(false);
                    LURL.setVisible(false);
                    LVertex.setVisible(false);
                    CreateGraph.setVisible(false);
                    LGraph.setVisible(true);
                    URL.setVisible(false);
                    VERTEX_NUMBER.setVisible(false);
                    graph = new Graph(URL.getText(), Integer.parseInt(VERTEX_NUMBER.getText()));
                    LURL.setVisible(true);
                    LVertex.setVisible(true);
                    CreateGraph.setVisible(true);
                    LGraph.setVisible(false);
                    ShowAll.setVisible(true);
                    SortBy.setVisible(true);
                    ExportToCSV.setVisible(true);
                    ShowLinked.setVisible(true);
                    VertexTable.setVisible(true);
                    URL.setVisible(true);
                    VERTEX_NUMBER.setVisible(true);
                }
            });
            CG.start();
        }
        else{
            Alert wrongURL=new Alert(Alert.AlertType.WARNING);
            wrongURL.setTitle("Неверно задан URL");
            wrongURL.setHeaderText("URL должен вести на станицу с публикацией!");
            wrongURL.setContentText("Введите верный адрес");

            wrongURL.showAndWait();
        }
    }

    public void ShowAll(){
        GO=new GraphObserver();
        TableView.removeAll(TableView);
        TableView.addAll(GO.ShowAll(graph));
        VertexTable.setItems(TableView);

    }
    public void ShowLinkedObjects(){
        ChoiseURLDialog();
        ChoiseRatingDialog();
        if(choserating.equals("Рейтинг публикации")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.ShowLinkedObjects(graph,linkedURL,"rating"));
            VertexTable.setItems(TableView);
        }
        else if(choserating.equals("Количество просмотров")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.ShowLinkedObjects(graph,linkedURL,"views"));
            VertexTable.setItems(TableView);
        }
        else if(choserating.equals("Количество сохранений")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.ShowLinkedObjects(graph,linkedURL,"saves"));
            VertexTable.setItems(TableView);
        }
    }

    public void SortBy(){
        ChoiseTagDialog();
        ChoiseRatingDialog();
        if(choserating.equals("Рейтинг публикации")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.SortByRatings(graph,chosetag));
            VertexTable.setItems(TableView);
        }
        else if(choserating.equals("Количество просмотров")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.SortByViews(graph,chosetag));
            VertexTable.setItems(TableView);
        }
        else if(choserating.equals("Количество сохранений")){
            GO=new GraphObserver();
            TableView.removeAll(TableView);
            TableView.addAll(GO.SortBySaves(graph,chosetag));
            VertexTable.setItems(TableView);
        }
    }

    private void ChoiseTagDialog(){
        List<String> choices = new ArrayList<String>();
        for(TagBridge tag: graph.getTagArray()){
            if(tag!=null) {
                choices.add(tag.getName());
            }
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0),choices);
        dialog.setTitle("Выбор тегов");
        dialog.setHeaderText("Выберите тег, по которому будете проводить сортировку");
        dialog.setContentText("Теги:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            chosetag=result.get();
        }
    }

    private void ChoiseURLDialog(){
        List<String> choices = new ArrayList<String>();
        for(Vertex link: graph.getGraphArray()){
            if(link!=null) {
                choices.add(link.getURL());
            }
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0),choices);
        dialog.setTitle("Выбор URL");
        dialog.setHeaderText("Выберите URL, для которого вы хотите найти связанные объекты");
        dialog.setContentText("URL:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            linkedURL=result.get();
        }
    }

    private void ChoiseRatingDialog(){
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(choicerating.get(0),choicerating);
        dialog.setTitle("Выбор рейтинга");
        dialog.setHeaderText("Выберите рейтинг, по которому будете проводить сортировку");
        dialog.setContentText("Выбор:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            choserating=result.get();
        }
    }

    private void FilePathDialog(){
        TextInputDialog dialog = new TextInputDialog(filepath);
        dialog.setTitle("Файл для экспорта");
        dialog.setHeaderText("Введите название файла для экспорта данных");
        dialog.setContentText("Название файла .scv:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            filepath=result.get();
        }
    }

    public void ExportToCSV()throws Exception{
        FilePathDialog();
        File data=new File(filepath);
            Writer stwriter = new OutputStreamWriter(new FileOutputStream(data, false), "Windows-1251");
            CSVWriter writer = new CSVWriter(stwriter, ';');
            String [] columns="Название публикации;Ссылка;Дата;Тэги;Ссылки;Рейтинг;Просмотры;Сохранения".split(";");
            writer.writeNext(columns);
            for (Vertex vertex : TableView) {
                String record =vertex.getName() + ";" + vertex.getURL() + ";" + vertex.getDate() + ";" + vertex.getTagsAsString() + ";" + vertex.getLinksAsString() + ";" + vertex.getRating() + ";" + vertex.getViews() + ";" + vertex.getSaves();
                String[] text = record.split(";");
                writer.writeNext(text);
            }
            writer.flush();
            writer.close();
        }


}