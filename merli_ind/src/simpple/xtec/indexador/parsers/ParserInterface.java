package simpple.xtec.indexador.parsers;

// import simpple.sise.db.DocumentToIndex;

/*
 * Created on 21/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author pere
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ParserInterface  {
    public String getText ( );
    public String getUrl ( );
    public String getTitle (  );
    public String getAutor (  );
    public String getKeywords (  );
    public String getDate (  );
    // public DocumentToIndex getDocument ();
    
    // Falta description?
}
