package cat.xtec.merli.crawler.storage;

import java.io.File;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import cat.xtec.merli.Application;
import cat.xtec.merli.domain.UID;


/**
 * Crawler storage. Keeps references to the crawled resources into
 * persistent storage.
 */
public class ResourceStore implements AutoCloseable {

    /** Unique name of this store */
    private static final String STORE_NAME = "crawler";

    /** Database entity store */
    private EntityStore store;

    /** Database environment */
    private Environment environment;

    /** Primary index for the store */
    private PrimaryIndex<String, ResourceEntity> entries;


    /**
     * Create a new store instance.
     *
     * @param name      Unique store name
     */
    public ResourceStore() throws DatabaseException {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        StoreConfig storeConfig = new StoreConfig();
        File folder = new File(getStorageFolder());

        folder.mkdirs();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(false);
        storeConfig.setAllowCreate(true);
        storeConfig.setTransactional(false);
        storeConfig.setDeferredWrite(false);

        environment = new Environment(folder, envConfig);
        store = new EntityStore(environment, STORE_NAME, storeConfig);
        entries = store.getPrimaryIndex(String.class, ResourceEntity.class);
    }


    /**
     * Returns the crawler storage folder.
     *
     * @return          Storage folder
     */
    private String getStorageFolder() {
        return Application.getProperty("crawler.storage.path");
    }


    /**
     * Checks if an entity with the given ID was stored.
     *
     * @param urn           Unique resource identifier
     */
    public boolean contains(UID id) throws DatabaseException {
        return entries.contains(id.getString());
    }


    /**
     * Stores an entity or replaces an existing entity.
     *
     * @param urn           Unique resource identifier
     * @param resource      Resource to store
     */
    public void put(UID id) throws DatabaseException {
        entries.putNoReturn(new ResourceEntity(id));
    }


    /**
     * {@inheritDoc}
     */
    public void close() throws DatabaseException {
        store.close();
        environment.close();
    }

}
