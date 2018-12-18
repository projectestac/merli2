package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.type.Identifier;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.voc.Language;
import cat.xtec.merli.domain.voc.Structure;
import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.bind.*;


/**
 * General information that describes a learning object as a whole.
 */
@XmlType(name = "general")
@XmlAccessorType(XmlAccessType.NONE)
public class GeneralDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Catalog entries for the learning object */
    @DucProperty(DucVocabulary.IDENTIFIER)
    @XmlElement(name = "identifier")
    protected List<Identifier> identifiers;

    /** Name of the learning object */
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "title")
    protected List<LangString> titles;

    /** Description of the content */
    @DucProperty(DucVocabulary.DESCRIPTION)
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    protected List<LangString> descriptions;

    /** Languages used within the resource */
    @DucProperty(DucVocabulary.LANGUAGE)
    @XmlElement(name = "language")
    @XmlSchemaType(name = "string")
    protected List<Language> languages;

    /** Organizational structure of the learning object */
    @DucProperty(DucVocabulary.STRUCTURE)
    @XmlElement(name = "structure")
    @XmlJavaTypeAdapter(Structure.Adapter.class)
    protected Structure structure;

    /** Keywords that describe this learning object */
    @DucClass(DucVocabulary.KEYWORD)
    @XmlElement(name = "keyword")
    protected List<Entity> keywords;


    /**
     * Returns this object's structure value.
     *
     * @return          Structure value
     */
    public Structure getStructure() {
        return structure;
    }


    /**
     * Sets this object's structure value.
     *
     * @param value     Structure value
     */
    public void setStructure(Structure value) {
        this.structure = value;
    }


    /**
     * Returns this object's description value.
     *
     * @return          Description value
     */
    public List<LangString> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<LangString>();
        }

        return descriptions;
    }


    /**
     * Returns this object's catalog entries list reference.
     *
     * @return          Catalog entry list reference
     */
    public List<Identifier> getIdentifiers() {
        if (identifiers == null) {
            identifiers = new ArrayList<Identifier>();
        }

        return identifiers;
    }


    /**
     * Returns this object's keywords entity list reference.
     *
     * @return          Entity list reference
     */
    public List<Entity> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<Entity>();
        }

        return this.keywords;
    }


    /**
     * Returns this object's titles value.
     *
     * @return          Titles value
     */
    public List<LangString> getTitles() {
        if (titles == null) {
            titles = new ArrayList<LangString>();
        }

        return titles;
    }


    /**
     * Returns this object's language list reference.
     *
     * @return          Locale tags list reference
     */
    public List<Language> getLanguages() {
        if (languages == null) {
            languages = new ArrayList<Language>();
        }

        return this.languages;
    }

}
