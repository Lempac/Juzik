package com.juzik.juzik;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;


import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class MainController {
    @FXML
    protected TextField musicFile;
    @FXML
    protected ImageView coverImage;
    @FXML
    protected VBox musicData;
    @FXML
    protected TextField musicTitle;
    @FXML
    protected TextField musicArtist;
    @FXML
    protected TextField musicAlbum;
    @FXML
    protected TextField musicTrack;
    @FXML
    protected TextField musicGenre;
    @FXML
    protected TextField musicYear;
    @FXML
    protected TextField musicComment;
    @FXML
    protected ListView<String> musicList;
    @FXML
    protected TextField musicDirectory;
    @FXML
    protected ComboBox<String> musicDirectories;

    private AudioFile JaudioFile;

    @FXML
    public void initialize() {
        musicDirectory.setText(System.getProperty("user.home") + '/');
        this.onSubmitButtonClick();
    }

    private String[] getAllMP3(File dir) {
        File[] files = dir.listFiles(file -> file.isFile() && file.getName().endsWith(".mp3"));
        assert files != null;
        return Arrays.stream(files).map(File::getName).toArray(String[]::new);
    }

    private String[] getAllDirs(File dir) {
        File[] dirs = dir.listFiles(File::isDirectory);
        assert dirs != null;
        return Arrays.stream(dirs).map(File::getName).toArray(String[]::new);
    }

    @FXML
    protected void onAutomationButtonClick() {
        Alert auto = new Alert(Alert.AlertType.INFORMATION);
        auto.setHeaderText("Automation doesnt work right now!");
        auto.setContentText(null);
        auto.show();
    }

    @FXML
    protected void onSubmitButtonClick() {
        if (musicDirectory.getText().isEmpty()) return;
        File dir = new File(musicDirectory.getText());
        if (!dir.exists() || !dir.isDirectory()) return;
        musicDirectories.getItems().clear();
        musicDirectories.getItems().addAll(getAllDirs(dir));
        musicList.getItems().clear();
        musicList.getItems().addAll(getAllMP3(dir));
        musicData.setVisible(false);
    }

    @FXML
    protected void onInfoButtonClick() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Tutorial and Instructions");
        info.setContentText("""
        1. Locate the "Music Directory" option in the top bar and add a directory, which is folder with a folder of each
        artist or have a folder of your mp3 files.
        2. Use the "Folders" drop-down menu to select the specific folder you want to work with.
        3. In the listbox, choose the desired song that you want to edit. Begin the editing process,
        making any changes needed. Be sure to save your modifications when done. Note: To update the album cover,
        drag and drop an image file (only supporting jpg, jpeg, and png formats) into the blank space where the cover would
        be displayed.

        IMPORTANT Actions That May Cause Issues: Avoid copying, highlighting text, or using the "Tab" key in the editor,
        as these actions will prevent changes from being saved.

        Bugs:
        In case the cover change doesn't work correctly, try removing the existing cover and then attempt to set it again.

        These guidelines should help you navigate through the program smoothly. If you encounter any other feel free to reach
        out to me on GitHub at M7PAX.
        """);
        info.setHeaderText(null);
        info.setGraphic(null);
        info.setResizable(true);
        info.setWidth(400);
        info.show();
    }

    @FXML
    protected void onMusicDirectorySelected() {
        if (musicDirectories.getSelectionModel().isEmpty()) return;
        File dir = new File(Path.of(musicDirectory.getText(), musicDirectories.getSelectionModel().getSelectedItem()).toString());
        musicList.getItems().clear();
        musicList.getItems().addAll(getAllMP3(dir));
    }

    @FXML
    public void onSelectedMusic() throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        if (musicList.getSelectionModel().isEmpty() || musicList.getSelectionModel().getSelectedItem().isEmpty()) return;
        JaudioFile = AudioFileIO.read(Path.of(musicDirectory.getText(),musicDirectories.getSelectionModel().isEmpty() ? "" : musicDirectories.getSelectionModel().getSelectedItem(), musicList.getSelectionModel().getSelectedItem()).toFile());
        Tag tag = JaudioFile.getTag();

        musicArtist.setText(tag.getFirst(FieldKey.ARTIST));
        musicTitle.setText(tag.getFirst(FieldKey.TITLE));
        musicAlbum.setText(tag.getFirst(FieldKey.ALBUM));
        musicYear.setText(tag.getFirst(FieldKey.YEAR));
        musicTrack.setText(tag.getFirst(FieldKey.TRACK));
        musicGenre.setText(tag.getFirst(FieldKey.GENRE));
        musicComment.setText(tag.getFirst(FieldKey.COMMENT));
        musicFile.setText(AudioFile.getBaseFilename(JaudioFile.getFile()));
        coverImage.setImage(createBlankImage(200, 200, Color.GRAY));
        if (tag.getFirstArtwork() != null){
            coverImage.setPreserveRatio(true);
            coverImage.setSmooth(true);
            coverImage.setImage(convertToFxImage((BufferedImage) tag.getFirstArtwork().getImage()));
            coverImage.fitWidthProperty();
        }
        musicData.setVisible(true);
    }

    @FXML
    protected void onSaveButtonClicked() throws FieldDataInvalidException, CannotWriteException {
        if(!JaudioFile.getFile().exists()) return;
        Tag tags = JaudioFile.getTag();
        if (musicArtist.getText() != null){
            tags.setField(FieldKey.ARTIST, musicArtist.getText());
        }
        if (musicTitle.getText() != null) {
            tags.setField(FieldKey.TITLE, musicTitle.getText());
        }
        if (musicAlbum.getText() != null){
            tags.setField(FieldKey.ALBUM, musicAlbum.getText());
        }
        if(musicYear.getText() != null){
            tags.setField(FieldKey.YEAR, musicYear.getText());
        }
        if(musicTrack.getText() != null){
            tags.setField(FieldKey.TRACK, musicTrack.getText());
        }
        if(musicGenre.getText() != null){
            tags.setField(FieldKey.GENRE, musicGenre.getText());
        }
        if(musicComment.getText() != null){
            tags.setField(FieldKey.COMMENT,  musicComment.getText());
        }
        AudioFileIO.write(JaudioFile);
    }

    private WritableImage convertToFxImage(BufferedImage image) {
        if (image == null) return null;
//        WritableImage wr = new WritableImage(image.getWidth(), image.getHeight());
//        PixelWriter pw = wr.getPixelWriter();
//        for (int x = 0; x < image.getWidth(); x++) {
//            for (int y = 0; y < image.getHeight(); y++) {
//                pw.setArgb(x, y, image.getRGB(x, y));
//            }
//        }
        int width = image.getWidth();
        int height = image.getHeight();
        WritableImage wr = new WritableImage(width, height);
        PixelWriter pw = wr.getPixelWriter();

        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
        return wr;
    }

    private WritableImage createBlankImage(int width, int height, Color color) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        return image;
    }

    @FXML
    protected void onCoverImageDrop(DragEvent dragEvent) {
        System.out.println(dragEvent.getDragboard().getImage());
        coverImage.setImage(dragEvent.getDragboard().getImage());
    }

    @FXML
    protected void onCoverImageDrag(DragEvent dragEvent) {
        System.out.println(dragEvent.getDragboard().getContentTypes());
        if (dragEvent.getDragboard().hasContent(DataFormat.IMAGE)) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    @FXML
    protected void onContentDeleteButtonPressed(ActionEvent actionEvent){
        ((TextField)((Button)actionEvent.getSource()).getParent().getChildrenUnmodifiable().get(1)).clear();
    }
}