package cat.xtec.merli.duc.client;

import org.semanticweb.owlapi.model.IRI;


/**
 * Base concepts of the ontology hierachy.
 */
public final class DucConcepts {

    /** Root ontology identifier */
    public static final IRI ROOT = IRI.create(
        "http://merli.xtec.cat/DUC");

    /** Thesaurus ontology identifier */
    public static final IRI THESAURUS = IRI.create(
        "http://merli.xtec.cat/Thesaurus");

    /** Curriculum ontology identifier */
    public static final IRI CURRICULUM = IRI.create(
        "http://merli.xtec.cat/Curriculum");

    /** Root class of the thesaurus vocabulary */
    public static final IRI CONCEPT = IRI.create(
        "http://merli.xtec.cat/DUC#Concept");

    /** Root class of the curriculum categories */
    public static final IRI CONTENT = IRI.create(
        "http://merli.xtec.cat/DUC#Content");

}
