package cat.xtec.merli.client.zonaclic.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.voc.Catalog;


/**
 * Represents a reference to a project in the catalog.
 */
public class Reference implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Host of the object */
    public static final String HOST = "clic.xtec.cat";

    /** Base location for the object */
    private static final String BASE_PATH = "/repo/?prj=";

    /** Catalog for this project UID */
    private static final String CATALOG = Catalog.MERLI.value();

    /** Unique project identifier */
    @JsonIgnore
    protected UID id;

    /** Last updated date */
    @JsonProperty("date")
    @JsonFormat(pattern = "dd/MM/yy")
    protected Date date;

    /** Relative path to the project */
    @JsonProperty("path")
    protected String path;


    /**
     * Returns this object's unique identifier.
     *
     * @return          URN value
     */
    @JsonIgnore
    public UID getUID() {
        if (id instanceof UID == false) {
            id = Reference.getUUID(path);
        }

        return id;
    }


    /**
     * Returns this object's date object.
     *
     * @return          Date object
     */
    public Date getDate() {
        return date;
    }


    /**
     * Sets this object's date object.
     *
     * @return          Date object
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * Returns this object's path object.
     *
     * @return          Path object
     */
    public String getPath() {
        return path;
    }


    /**
     * Sets this object's path object.
     *
     * @return          Path object
     */
    public void setPath(String path) {
        this.path = path;
    }


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(path);
    }


    /**
     * Returns a unique object identifier given its path.
     *
     * @param path          Project path
     * @return              URN for the project
     */
    @JsonIgnore
    public static UID getUUID(String path) {
        final String id = HOST + ":projects:" + path;
        return UID.from("urn:" + CATALOG + ":" + id);
    }


    /**
     * Returns a unique object URL given its path. That is, the
     * permalink URL of the project.
     *
     * @param path          Project path
     * @return              URN for the project
     */
    @JsonIgnore
    public static UID getLocation(String path) {
        return UID.from("https://" + HOST + BASE_PATH + path);
    }


    /**
     * Returns an object path given its identifier. The object path
     * is its URN part after the scope. This method does not validate
     * the provided URN an thus, may return wrong results if the
     * identifier is malformed.
     *
     * @param id            Project URN
     * @return              Path component of the URN
     */
    @JsonIgnore
    public static String getPath(UID id) {
        return id.getString().split(":projects:")[1];
    }

}
