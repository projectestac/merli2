package cat.xtec.merli.client;

import cat.xtec.merli.domain.lom.Resource;


/**
 * Interface that all converters must implement.
 */
public interface LomConverter<T> {

    /**
     * Transforms an object into a new LOM resource.
     *
     * @param object        Object containing the data
     * @return              New resource object
     */
    public Resource convert(T object);

}
