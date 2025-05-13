package game.achievements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of AchievementFile using standard file I/O.
 */
public class FileHandler implements AchievementFile {

    private String fileLocation;

    /**
     * Constructs a FileHandler with the default file location.
     */
    public FileHandler() {
        this.fileLocation = DEFAULT_FILE_LOCATION;
    }

    /**
     * Sets the file location to save to.
     *
     * @param fileLocation the new file location
     */
    @Override
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Gets the location currently being saved to.
     *
     * @return the current file location
     */
    @Override
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Saves the given data to a file followed by a new-line character.
     *
     * @param data the data to be saved
     */
    @Override
    public void save(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    /**
     * Loads and returns all previously saved data as a list of strings.
     *
     * @return a list of saved data entries
     */
    @Override
    public List<String> read() {
        List<String> lines = new ArrayList<>();
        File file = new File(fileLocation);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }

        return lines;
    }
}
