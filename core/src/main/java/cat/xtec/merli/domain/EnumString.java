package cat.xtec.merli.domain;


/**
 * Interface for string enumerations. Note that this interface is
 * intended to be implemented only by enumerations.
 */
public interface EnumString<E extends Enum<E>> {

    /**
     * Returns this enumeration value.
     *
     * @return  String value
     */
    public String value();


    /**
     * Returns this enumeration name.
     *
     * @return  String value
     */
    public String name();


    /**
     * Returns this enumeration source.
     *
     * The source of this enumeration identifies the authority of
     * the controlled vocabulary defined on the enum. That is, where
     * the vocabulary comes from (i.e. LOMv1.0).
     *
     * @return  Source value
     */
    public EnumSource source();

}
