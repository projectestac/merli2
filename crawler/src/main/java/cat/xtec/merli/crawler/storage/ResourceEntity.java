package cat.xtec.merli.crawler.storage;

import java.io.Serializable;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import cat.xtec.merli.domain.UID;


/**
 * Persistent entity that contains information about a resource.
 * Currently, only the unique resource identifier is stored.
 */
@Entity
public class ResourceEntity implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Unique identifier */
    @PrimaryKey
    protected String id;


    /** Empty constructor */
    public ResourceEntity() {}


    /**
     * Intantiates a new resource entity.
     *
     * @param id            Unique identifier
     */
    public ResourceEntity(UID id) {
        this.id = id.getString();
    }

}
