package cat.xtec.merli.parser;

import cat.xtec.merli.bind.DucVocabulary;


/**
 * Main class for the parser module. Provides a command line interface
 * to debug the generated domains.
 */
public final class Parser {

    /** Prevent the instantiation */
    private Parser() {}


    /**
     * Main method of the class.
     *
     * @param args          Command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: parser [class_name…]");
            System.exit(1);
        }

        Class[] types = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            types[i] = Class.forName(args[i]);
        }

        printContext(types);
    }


    /**
     * Builds a context for the given types and prints it.
     *
     * @param types         Types to parse
     */
    public static void printContext(Class[] types) {
        DucContext context = DucContext.newInstance(types);

        for (Class type : context.domains.keySet()) {
            DucDomain domain = context.domains.get(type);
            System.out.printf("\n%s:\n", type);

            for (DucVocabulary voc : domain.keySet()) {
                DucProperty property = domain.get(voc);
                System.out.printf("  %s = %s\n", voc, property);
            }
        }
    }

}
