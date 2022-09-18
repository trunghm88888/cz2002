package hrps.entity;

import hrps.entity.enums.IDType;

import java.io.Serializable;

/**
 * This class represents an ID. It could either be a passport or a driving license.
 *
 * @author An Ruyi
 */
public class ID implements Serializable {
    /**
     * This ID's type.
     */
    private final IDType idtype;
    /**
     * This ID's number
     */
    private final String idNumber;

    /**
     * Create an ID.
     *
     * @param type     ID type of the ID.
     * @param idNumber ID number of the ID.
     */
    public ID(IDType type, String idNumber) {
        this.idtype = type;
        this.idNumber = idNumber;
    }

    /**
     * Get the ID type.
     *
     * @return An enum constant represents the ID type of this ID.
     */
    public IDType getIdType() {
        return idtype;
    }

    /**
     * Get the ID type of this ID in the format of a String.
     *
     * @return A String represents this ID type.
     */
    public String getIdTypeName() {
        if (idtype == IDType.PASSPORT) {
            return "Passport";
        } else if (idtype == IDType.DRIVING_LICENSE) {
            return "Driving License";
        }
        return null;
    }

    /**
     * Get the ID number
     *
     * @return A String represents the ID number of this ID.
     */
    public String getIdNumber() {
        return idNumber;
    }
}
