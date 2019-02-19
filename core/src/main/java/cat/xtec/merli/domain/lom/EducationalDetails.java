package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Context;
import cat.xtec.merli.domain.voc.Language;
import cat.xtec.merli.domain.voc.UserRole;
import cat.xtec.merli.domain.voc.ResourceType;
import cat.xtec.merli.bind.*;
import cat.xtec.merli.xml.*;


/**
 * Pedagogical and educational characteristics.
 */
@DucContainer()
@XmlType(name = "educational")
@XmlAccessorType(XmlAccessType.NONE)
public class EducationalDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** How the learning object is to be used */
    @DucAnnotation(DucVocabulary.USAGE)
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    protected List<LangString> descriptions;

    /** User's natural languages */
    @DucAttribute(DucVocabulary.USER_LANGUAGE)
    @XmlElement(name = "language")
    @XmlSchemaType(name = "string")
    protected List<Language> languages;

    /** Normal users of the learning object */
    @DucAttribute(DucVocabulary.USER_ROLE)
    @XmlElement(name = "intendedEndUserRole")
    @XmlJavaTypeAdapter(UserRoleAdapter.class)
    protected List<UserRole> userRoles;

    /** Typical learning environment */
    @DucAttribute(DucVocabulary.CONTEXT)
    @XmlElement(name = "context")
    @XmlJavaTypeAdapter(ContextAdapter.class)
    protected List<Context> contexts;

    /** Specific kinds of resources */
    @DucAttribute(DucVocabulary.LEARNING_TYPE)
    @XmlElement(name = "learningResourceType")
    @XmlJavaTypeAdapter(ResourceTypeAdapter.class)
    protected List<ResourceType> resourceTypes;


    /**
     * Returns this object's resource type list reference.
     *
     * @return          Resource type list reference
     */
    public List<ResourceType> getResourceTypes() {
        if (resourceTypes == null) {
            resourceTypes = new ArrayList<ResourceType>();
        }

        return this.resourceTypes;
    }


    /**
     * Returns this object's user role list reference.
     *
     * @return          User role list reference
     */
    public List<UserRole> getUserRoles() {
        if (userRoles == null) {
            userRoles = new ArrayList<UserRole>();
        }

        return this.userRoles;
    }


    /**
     * Returns this object's context list reference.
     *
     * @return          Context list reference
     */
    public List<Context> getContexts() {
        if (contexts == null) {
            contexts = new ArrayList<Context>();
        }

        return this.contexts;
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
