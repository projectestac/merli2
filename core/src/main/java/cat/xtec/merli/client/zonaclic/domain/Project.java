package cat.xtec.merli.client.zonaclic.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.client.zonaclic.StringDeserializer;


/**
 * Represents a project in the catalog.
 */
public class Project implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Unique project identifier */
    @JsonIgnore
    protected UID id;

    /** Project permalink */
    @JsonIgnore
    protected UID location;

    /** Title of the project */
    @JsonProperty("title")
    protected String title;

    /** Authors of the project */
    @JsonProperty("author")
    protected String author;

    /** Last updated date */
    @JsonProperty("date")
    @JsonFormat(pattern = "dd/MM/yy")
    protected Date date;

    /** Descriptions of the project */
    @JsonProperty("description")
    @JsonDeserialize(using = StringDeserializer.class)
    protected List<LangString> descriptions;

    /** Descriptions of the license */
    @JsonProperty("license")
    @JsonDeserialize(using = StringDeserializer.class)
    protected List<LangString> licenses;

    /** Learning areas classification */
    @JsonProperty("areaCodes")
    protected List<Area> areas;

    /** Learning levels classification */
    @JsonProperty("levelCodes")
    protected List<Level> levels;

    /** Available languages */
    @JsonProperty("langCodes")
    protected List<Locale> locales;

    /** Keywords classification */
    @JsonProperty("descCodes")
    protected List<Integer> keywords;

    /** Related projects paths */
    @JsonProperty("relatedTo")
    protected List<String> relations;


    /**
     * Returns this object's unique identifier.
     *
     * @return          URN value
     */
    public UID getUID() {
        return id;
    }


    /**
     * Sets this object's unique identifier.
     *
     * @param value     URN value
     */
    public void setUID(UID id) {
        this.id = id;
    }


    /**
     * Returns this object's location value.
     *
     * @return          Location value
     */
    public UID getLocation() {
        return location;
    }


    /**
     * Sets this object's location value.
     *
     * @param value     Location value
     */
    public void setLocation(UID location) {
        this.location = location;
    }


    /**
     * Returns this object's title value.
     *
     * @return          Title value
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets this object's title value.
     *
     * @param value     Title value
     */
    public void setTitle(String value) {
        this.title = value;
    }


    /**
     * Returns this object's author value.
     *
     * @return          Author value
     */
    public String getAuthor() {
        return author;
    }


    /**
     * Sets this object's author value.
     *
     * @param value     Author value
     */
    public void setAuthor(String value) {
        this.author = value;
    }


    /**
     * Returns this object's date value.
     *
     * @return          Date value
     */
    public Date getDate() {
        return date;
    }


    /**
     * Sets this object's date value.
     *
     * @param value     Date value
     */
    public void setDate(Date value) {
        this.date = value;
    }


    /**
     * Returns this object's areas list reference.
     *
     * @return          Areas list reference
     */
    public List<Area> getAreas() {
        if (areas == null) {
            areas = new ArrayList<Area>();
        }

        return areas;
    }


    /**
     * Returns this object's descriptions list reference.
     *
     * @return          Descriptions list reference
     */
    public List<LangString> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<LangString>();
        }

        return descriptions;
    }


    /**
     * Returns this object's locales list reference.
     *
     * @return          Locales list reference
     */
    public List<Locale> getLocales() {
        if (locales == null) {
            locales = new ArrayList<Locale>();
        }

        return locales;
    }


    /**
     * Returns this object's levels list reference.
     *
     * @return          Levels list reference
     */
    public List<Level> getLevels() {
        if (levels == null) {
            levels = new ArrayList<Level>();
        }

        return levels;
    }


    /**
     * Returns this object's licenses list reference.
     *
     * @return          Licenses list reference
     */
    public List<LangString> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<LangString>();
        }

        return licenses;
    }


    /**
     * Returns this object's keywords list reference.
     *
     * @return          Keywords list reference
     */
    public List<Integer> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<Integer>();
        }

        return keywords;
    }


    /**
     * Returns this object's relations list reference.
     *
     * @return          Relations list reference
     */
    public List<String> getRelations() {
        if (relations == null) {
            relations = new ArrayList<String>();
        }

        return relations;
    }


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(title);
    }

}
