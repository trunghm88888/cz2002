package tool;

import java.io.*;

/**
 * SerializeDB is a class that serializes data to a file and deserializes data from a file.
 */
public class SerializeDB {
    /**
     * Deserializes an object from a binary file.
     *
     * @param filename the name of the file to read from
     * @return the deserialized object
     */
    public static Object readSerializedObject(String filename) {
        Object pDetails = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            pDetails = in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        // print out the size
        // System.out.println(" Details Size: " + pDetails.size());
        // System.out.println();
        return pDetails;
    }

    /**
     * Serializes an object to a binary file.
     *
     * @param filename the name of the file to write to
     * @param object  the Serializable object to serialize
     */
    public static void writeSerializedObject(String filename, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.close();
            //	System.out.println("Object Persisted");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
