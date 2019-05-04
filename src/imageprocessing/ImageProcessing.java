/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Team Neurun
 */
public class ImageProcessing extends Application {
    
    static int type = 0;
    Image uploadedImage;
    ImageView uploadedImageView;
    
    Image uploadedImageResult;
    ImageView uploadedImageViewResult;
    
    Image uploadedImageGrayscale;
    ImageView uploadedImageViewGrayscale;
    
    Image adjustedContrastImage;
    ImageView adjustedContrastImageView;
    
    Double contrast_amount = 1.0;
    
    boolean picAdded = false;
    Button buttonCurrent;
    
    GridPane grid;
    VBox image_vbox;
    ScrollPane scroll;
    
    BarChart<String,Number> grayHistoOne;
    BarChart<String,Number> grayHistoTwo;
    BarChart<String,Number> grayHistoThree;
    
    BarChart<String,Number> colorHistoOne;
    BarChart<String,Number> colorHistoTwo;
    BarChart<String,Number> colorHistoThree;
    
    StackPane root;
    
    
    Text histo_4;
    Slider slider;
    
    @Override
    public void start(Stage primaryStage) {
        
        uploadedImageView = new ImageView(
            new Image(ImageProcessing.class.getResourceAsStream("graph.png")));
        
        //Button btn = new Button();
        //btn.setText("Say 'Hello World'");
        //btn.setOnAction(new EventHandler<ActionEvent>() {
            
          //  @Override
         //   public void handle(ActionEvent event) {
         //       System.out.println("Hello World!");
         //   }
       // });
        
        root = new StackPane();
        
        Scene scene = new Scene(root, 100, 100);
        
        
        primaryStage.setTitle("Image Processing");
        BorderPane border = new BorderPane();
        HBox hbox = addHBox(primaryStage, border);
        border.setTop(hbox);
        border.setLeft(addVBox(border));
        addStackPane(hbox);         // Add stack to HBox in top region

        border.setCenter(addFlowPane());
        scroll = new ScrollPane();
        scroll.setContent(border);
        scroll.setFitToWidth(true);

        root.getChildren().add(border);
        root.getChildren().add(scroll);


        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setScene(scene);
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.show();
    }
    
    public HBox addHBox(Stage primaryStage, BorderPane border) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        buttonCurrent = new Button("Upload Image");
        //buttonCurrent.setPrefSize(100, 20);
        final FileChooser fileChooser = new FileChooser();

        
        buttonCurrent.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                picAdded = true;
               // openFile(file);

               // where my problem is 
                Image image1 = new Image(file.toURI().toString());
                // what I tried to do
                    // Image image1 = new Image(file);
                ImageView ip = new ImageView(image1);
                
                //root.setBackground(new Background(backgroundImage));
                uploadedImageView = ip;
                uploadedImage = image1;
                refreshUi(border);
            }
        }); 
        
        Button buttonProjected = new Button("Help");
        buttonProjected.setPrefSize(100, 20);
        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }
    
    public void refreshUi(BorderPane border)
    {
        if (picAdded == true) buttonCurrent.setText("Upload new image");
        
        if (type == 1 || type == 2)
        {
            uploadedImageResult = uploadedImageGrayscale;
            uploadedImageViewResult = uploadedImageViewGrayscale;
        }
        
        
        border.setCenter(addFlowPane()); // refreshes the main data section
    }
    
    public VBox addVBox(BorderPane border) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Toolbox");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[] {
            new Hyperlink("Make Grayscale"),
            new Hyperlink("Grayscale Histogram Equilization"),
            new Hyperlink("Histogram Equilization"),
            new Hyperlink("Non-local means"),
            new Hyperlink("Fourier Denoising"),
            new Hyperlink("Bilateral filtering"),
            new Hyperlink("Anti-aliasing")};

        for (int i=0; i<7; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
            
            // Make grayscale
            if (i == 0)
            {
                options[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                    type = 1;
                    System.out.println("Greyscale");
                    // Convert our image to a bufferedimage
                    BufferedImage image_as_buffered = SwingFXUtils.fromFXImage(uploadedImage, null);
                    BufferedImage bufferedGrayscaleImage = Grayscale.MakeImageGrayscale(image_as_buffered);
                    System.out.println("output black and white: " + bufferedGrayscaleImage);
                    uploadedImageGrayscale = SwingFXUtils.toFXImage(bufferedGrayscaleImage, null);
                    uploadedImageViewGrayscale = new ImageView(uploadedImageGrayscale); 
                    refreshUi(border);
                }
                });
            }
            
            // Grayscale histogram equalization
            else if (i == 1)
            {
                options[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                    type = 2;
                    //System.out.println("Greyscale histogram equalization");
                    // Convert our image to a bufferedimage, and convert it to grayscale:
                    BufferedImage image_as_buffered = SwingFXUtils.fromFXImage(uploadedImage, null);
                    BufferedImage bufferedGrayscaleImage = Grayscale.MakeImageGrayscale(image_as_buffered);
                    //System.out.println("output black and white: " + bufferedGrayscaleImage);
                    uploadedImageGrayscale = SwingFXUtils.toFXImage(bufferedGrayscaleImage, null);
                    uploadedImageViewGrayscale = new ImageView(uploadedImageGrayscale); 
                    
                    
                    int[] intensityData = GrayscaleHistogram.GenerateHistogram(image_as_buffered);
                    
                    // Generate histogram

                    final CategoryAxis xAxis = new CategoryAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    grayHistoOne = 
                        new BarChart<>(xAxis,yAxis);
                    grayHistoOne.setCategoryGap(0);
                    grayHistoOne.setBarGap(0);
         
                    xAxis.setLabel("Pixel Intensity");       
                    yAxis.setLabel("Pixel Frequency");
         
                    XYChart.Series series1 = new XYChart.Series();
                    for (int i = 0; i < intensityData.length; i++)
                    {
                        series1.getData().add(new XYChart.Data( Integer.toString(i), intensityData[i]));
                    }
         
                    grayHistoOne.getData().addAll(series1);
                    
                    
                    /////// Probability histogram:
                    
                    double[] intensityDataProbabilities = GrayscaleHistogram.GenerateProbabilityHistogram(image_as_buffered);
                    
                    // Generate histogram

                    final CategoryAxis xAxisTwo = new CategoryAxis();
                    final NumberAxis yAxisTwo = new NumberAxis();
                    grayHistoTwo = 
                        new BarChart<>(xAxisTwo,yAxisTwo);
                    grayHistoTwo.setCategoryGap(0);
                    grayHistoTwo.setBarGap(0);
         
                    xAxisTwo.setLabel("Pixel Intensity");       
                    yAxisTwo.setLabel("Pixel Probability");
         
                    XYChart.Series series2 = new XYChart.Series();
                    for (int i = 0; i < intensityData.length; i++)
                    {
                        series2.getData().add(new XYChart.Data( Integer.toString(i), intensityDataProbabilities[i]));
                    }
         
                    grayHistoTwo.getData().addAll(series2);
                    
                    
                    
                    /////// Cumulative probability histogram:
                    
                    double[] intensityDataCumulativeProbabilities = GrayscaleHistogram.GenerateCumulativeProbabilityHistogram(image_as_buffered);
                    
                    // Generate histogram

                    final CategoryAxis xAxisThree = new CategoryAxis();
                    final NumberAxis yAxisThree = new NumberAxis();
                    grayHistoThree = 
                        new BarChart<>(xAxisThree,yAxisThree);
                    grayHistoThree.setCategoryGap(0);
                    grayHistoThree.setBarGap(0);
         
                    xAxisThree.setLabel("Pixel Intensity");       
                    yAxisThree.setLabel("Cumulative Probability");
         
                    XYChart.Series series3 = new XYChart.Series();
                    for (int i = 0; i < intensityData.length; i++)
                    {
                        series3.getData().add(new XYChart.Data( Integer.toString(i), intensityDataCumulativeProbabilities[i]));
                    }
         
                    grayHistoThree.getData().addAll(series3);
                    
                    
                    
                    
                    
                    
                    slider = new Slider();
         
                    // The minimum value.
                    slider.setMin(0);
         
                    // The maximum value.
                    slider.setMax(500);
         
                    // Current value
                    slider.setValue(80);
         
                    slider.setShowTickLabels(true);
                    slider.setShowTickMarks(true);
         
                    slider.setBlockIncrement(10);
                    
                    slider.valueProperty().addListener(new ChangeListener() {

                        @Override
                        public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                        contrast_amount = slider.getValue();
                        
                        // Take the contrast amount entered and multiply it by the cumulative probability value for each
                        // pixel intensity
                        int[] adjusted_for_contrast = new int [intensityDataCumulativeProbabilities.length];
                        for(int p = 0; p < intensityDataCumulativeProbabilities.length; p++)
                        {
                            //System.out.println("this cumulative: " + intensityDataCumulativeProbabilities[p] + "; " + intensityDataCumulativeProbabilities[p] * contrast_amount);
                            double product_unrounded = intensityDataCumulativeProbabilities[p] * contrast_amount;
                            adjusted_for_contrast[p] = (int) Math.floor(product_unrounded);
                            //System.out.println("this rounded: " + adjusted_for_contrast[p]);

                        }
                        
;
    
                        
        
                        BufferedImage black_and_white_as_buffered = SwingFXUtils.fromFXImage(uploadedImageGrayscale, null);
                        BufferedImage bufferedContrastImage = GrayscaleHistogram.AdjustContrast(black_and_white_as_buffered, adjusted_for_contrast);
                        //GrayscaleHistogram.Compare(black_and_white_as_buffered, bufferedContrastImage);
                        adjustedContrastImage = SwingFXUtils.toFXImage(bufferedContrastImage, null);
                       
                        refreshUi(border);

                    }
                    });

        
                    
                    
               
                    
                    
                    
                    

                    refreshUi(border);
                }
                });
            }
            
            
            // Color histogram equalization
            else if (i == 2)
            {
                options[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                    type = 3;

                    BufferedImage image_as_buffered = SwingFXUtils.fromFXImage(uploadedImage, null);
                    //BufferedImage hsbImage = ColorHistogram.MakeImageHsb(image_as_buffered);
                    //System.out.println("output black and white: " + bufferedGrayscaleImage);
                    //uploadedImageGrayscale = SwingFXUtils.toFXImage(bufferedGrayscaleImage, null);
                    //uploadedImageViewGrayscale = new ImageView(uploadedImageGrayscale); 
                    
                    System.out.println("image as buffered: " + image_as_buffered);
                    int[] brightnessData = ColorHistogram.GenerateColorHistogram(image_as_buffered);
                    System.out.println("made it here, brightness data is " + brightnessData + " which is not null");
                    
                    // Generate color histogram

                    final CategoryAxis xAxis = new CategoryAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    colorHistoOne = 
                        new BarChart<>(xAxis,yAxis);
                    colorHistoOne.setCategoryGap(0);
                    colorHistoOne.setBarGap(0);
         
                    xAxis.setLabel("Pixel Brightness");       
                    yAxis.setLabel("Frequency");
         
                    XYChart.Series series1 = new XYChart.Series();
                    for (int i = 0; i < brightnessData.length; i++)
                    {
                        double double_i = (double) i;
                        double real_value = i / 100.0;
                        series1.getData().add(new XYChart.Data( Double.toString(real_value), brightnessData[i]));
                    }
         
                    colorHistoOne.getData().addAll(series1);
                    
                    
                    /////// Probability color histogram:
                    
                    double[] intensityDataProbabilities = ColorHistogram.GenerateProbabilityHistogram(image_as_buffered);
                    
                    // Generate histogram

                    final CategoryAxis xAxisTwo = new CategoryAxis();
                    final NumberAxis yAxisTwo = new NumberAxis();
                    colorHistoTwo = 
                        new BarChart<>(xAxisTwo,yAxisTwo);
                    colorHistoTwo.setCategoryGap(0);
                    colorHistoTwo.setBarGap(0);
         
                    xAxisTwo.setLabel("Pixel Brightness");       
                    yAxisTwo.setLabel("Probability");
         
                    XYChart.Series series2 = new XYChart.Series();
                    for (int i = 0; i < intensityDataProbabilities.length; i++)
                    {
                        series2.getData().add(new XYChart.Data( Integer.toString(i), intensityDataProbabilities[i]));
                    }
         
                    colorHistoTwo.getData().addAll(series2);
                    
                    
                    
                    /////// Cumulative probability histogram:
                    
                    double[] brightnessDataCumulativeProbabilities = ColorHistogram.GenerateCumulativeProbabilityHistogram(image_as_buffered);
                    
                    // Generate histogram

                    final CategoryAxis xAxisThree = new CategoryAxis();
                    final NumberAxis yAxisThree = new NumberAxis();
                    colorHistoThree = 
                        new BarChart<>(xAxisThree,yAxisThree);
                    colorHistoThree.setCategoryGap(0);
                    colorHistoThree.setBarGap(0);
         
                    xAxisThree.setLabel("Pixel Brightness");       
                    yAxisThree.setLabel("Cumulative Probability");
         
                    XYChart.Series series3 = new XYChart.Series();
                    for (int i = 0; i < brightnessData.length; i++)
                    {
                        series3.getData().add(new XYChart.Data( Integer.toString(i), brightnessDataCumulativeProbabilities[i]));
                    }
         
                    colorHistoThree.getData().addAll(series3);
                    
                    
                    
                    
                    
                    
                    slider = new Slider();
         
                    // The minimum value.
                    slider.setMin(0);
         
                    // The maximum value.
                    slider.setMax(500);
         
                    // Current value
                    slider.setValue(80);
         
                    slider.setShowTickLabels(true);
                    slider.setShowTickMarks(true);
         
                    slider.setBlockIncrement(10);
                    
      /*              slider.valueProperty().addListener(new ChangeListener() {

                        @Override
                        public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                        contrast_amount = slider.getValue();
                        
                        // Take the contrast amount entered and multiply it by the cumulative probability value for each
                        // pixel intensity
                        int[] adjusted_for_contrast = new int [brightnessDataCumulativeProbabilities.length];
                        for(int p = 0; p < brightnessDataCumulativeProbabilities.length; p++)
                        {
                            //System.out.println("this cumulative: " + intensityDataCumulativeProbabilities[p] + "; " + intensityDataCumulativeProbabilities[p] * contrast_amount);
                            double product_unrounded = brightnessDataCumulativeProbabilities[p] * contrast_amount;
                            adjusted_for_contrast[p] = (int) Math.floor(product_unrounded);
                            //System.out.println("this rounded: " + adjusted_for_contrast[p]);

                        }
                        
;
    
                        
        
                        BufferedImage black_and_white_as_buffered = SwingFXUtils.fromFXImage(uploadedImageGrayscale, null);
                        BufferedImage bufferedContrastImage = GrayscaleHistogram.AdjustContrast(black_and_white_as_buffered, adjusted_for_contrast);
                        //GrayscaleHistogram.Compare(black_and_white_as_buffered, bufferedContrastImage);
                        adjustedContrastImage = SwingFXUtils.toFXImage(bufferedContrastImage, null);
                       
                        refreshUi(border);

                    }
                    });

                ***/       
                    
                    
               
                    
                    
                    
                    

                    refreshUi(border);
                }
                });
            }
            
        }

        return vbox;
    }
    
    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
            new Stop[]{
            new Stop(0,Color.web("#4977A3")),
            new Stop(0.5, Color.web("#B0C6DA")),
            new Stop(1,Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0")); 

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }
    
    public VBox addFlowPane() {
        image_vbox = new VBox();
        image_vbox.setPadding(new Insets(10));
        image_vbox.setSpacing(8);
        

        //scroll.setPannable(true);
        
        // input picture title
        Text input_title = new Text("Original Image:");
        input_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        image_vbox.getChildren().add(input_title);
        
        // input picture
        image_vbox.getChildren().add(uploadedImageView);
        
        // output picture title
        Text output_title = new Text("Greyscale Image:");
        output_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        output_title.setVisible(type == 1 || type == 2);
        image_vbox.getChildren().add(output_title);
        
        // output picture
        if (type == 1 || type == 2)
        {
            image_vbox.getChildren().add(uploadedImageViewResult); 
        }
        
        if (type == 2)
        {
            // output picture title
            Text histo_1 = new Text("Pixel Intensities vs Pixel Frequencies Histogram:");
            histo_1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_1.setVisible(type == 2);
            image_vbox.getChildren().add(histo_1);
            image_vbox.getChildren().addAll(grayHistoOne);
            
            // Probability Histogram
            Text histo_2 = new Text("Pixel Intensities vs Pixel Probabilities Histogram:");
            histo_2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_2.setVisible(type == 2);
            image_vbox.getChildren().add(histo_2);
            image_vbox.getChildren().addAll(grayHistoTwo);
            
            // Cumulative Probability Histogram
            Text histo_3 = new Text("Pixel Intensities vs Pixel Cumulative Probabilities Histogram:");
            histo_3.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_3.setVisible(type == 2);
            image_vbox.getChildren().add(histo_3);
            image_vbox.getChildren().addAll(grayHistoThree);
            
            System.out.println("triggered");
            
            
            histo_4 = new Text("Click Slider to Change Contrast:");
            histo_4.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            histo_4.setVisible(type == 2);
            image_vbox.getChildren().add(histo_4);
             
            image_vbox.getChildren().addAll(slider);


                
            // Final image with adjusted contrast
            if(adjustedContrastImage != null )
            {
                
                //image_vbox.getChildren().addAll(grayHistoThree);
                

                Text histo_5 = new Text("Image with Adjusted Contrast:");
                histo_5.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                histo_5.setVisible(type == 2);
                image_vbox.getChildren().add(histo_5);
            
                adjustedContrastImageView = new ImageView(adjustedContrastImage);
                image_vbox.getChildren().addAll(adjustedContrastImageView);
            }
            else
            {
                adjustedContrastImageView = new ImageView(uploadedImageGrayscale);
                image_vbox.getChildren().addAll(adjustedContrastImageView);
            }
            
                        
        }
        
        
        
        if (type == 3)
        {
            // output picture title
            Text histo_1 = new Text("Pixel Intensities vs Pixel Frequencies Histogram:");
            histo_1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_1.setVisible(type == 2);
            image_vbox.getChildren().add(histo_1);
            image_vbox.getChildren().addAll(colorHistoOne);
            
            // Probability Histogram
            Text histo_2 = new Text("Pixel Intensities vs Pixel Probabilities Histogram:");
            histo_2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_2.setVisible(type == 2);
            image_vbox.getChildren().add(histo_2);
            image_vbox.getChildren().addAll(colorHistoTwo);
            
            // Cumulative Probability Histogram
            Text histo_3 = new Text("Pixel Intensities vs Pixel Cumulative Probabilities Histogram:");
            histo_3.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            histo_3.setVisible(type == 2);
            image_vbox.getChildren().add(histo_3);
            image_vbox.getChildren().addAll(colorHistoThree);
            
            System.out.println("triggered");
            
            
            histo_4 = new Text("Click Slider to Change Contrast:");
            histo_4.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            histo_4.setVisible(type == 2);
            image_vbox.getChildren().add(histo_4);
             
            image_vbox.getChildren().addAll(slider);
/*

                
            // Final image with adjusted contrast
            if(adjustedContrastImage != null )
            {
                
                //image_vbox.getChildren().addAll(grayHistoThree);
                

                Text histo_5 = new Text("Image with Adjusted Contrast:");
                histo_5.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                histo_5.setVisible(type == 2);
                image_vbox.getChildren().add(histo_5);
            
                adjustedContrastImageView = new ImageView(adjustedContrastImage);
                image_vbox.getChildren().addAll(adjustedContrastImageView);
            }
            else
            {
                adjustedContrastImageView = new ImageView(uploadedImageGrayscale);
                image_vbox.getChildren().addAll(adjustedContrastImageView);
            }
   **/         
                        
        }
        
        
        
        
        


       // root.getChildren().add(scroll);

        return image_vbox;
        
    }
    
    /*
    public GridPane addGridPane() {
        System.out.println("Making new grid pane");
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        // Category in column 2, row 1
        Text category = new Text("Original Image:");
        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(category, 1, 0); 

        // Title in column 3, row 1 
        /**
        Text chartTitle = new Text("Current Year");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(chartTitle, 2, 0);
        **/

        // Subtitle in columns 2-3, row 2
        /**
        Text chartSubtitle = new Text("Goods and Services");
        grid.add(chartSubtitle, 1, 1, 2, 1);
        **/

        // House icon in column 1, rows 1-2
    /**
        ImageView imageHouse = uploadedImageView;
        grid.add(imageHouse, 1, 1, 1, 2); 
        
        if (type == 1)
        {
            grid.add(uploadedImageViewResult, 1, 3, 1, 2); 
        }

        // Left label in column 1 (bottom), row 3
        //Text goodsPercent = new Text("Goods\n80%");
        //GridPane.setValignment(goodsPercent, VPos.BOTTOM);
        //grid.add(goodsPercent, 0, 2); 

        // Chart in columns 2-3, row 3
        //ImageView imageChart = uploadedImage;
        //grid.add(imageChart, 1, 2, 2, 1); 

        // Right label in column 4 (top), row 3
        //Text servicesPercent = new Text("Services\n20%");
        //GridPane.setValignment(servicesPercent, VPos.TOP);
        //grid.add(servicesPercent, 3, 2);

        return grid;
    }
    **/
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
