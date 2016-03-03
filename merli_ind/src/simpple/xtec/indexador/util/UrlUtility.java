package simpple.xtec.indexador.util;

import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

/**
 * This simple static class contains
 * several methods that are useful to
 * manipulate URL's.
 * Copyright 2001-2003 by Jeff Heaton (http://www.jeffheaton.com)
 *
 * @author Jeff Heaton
 * @version 1.2
 */
public class UrlUtility {

        // Logger
	static final Logger logger = Logger.getLogger(UrlUtility.class);

	/**
	 * The name that should be used to store any "default documents"
	 */
	public static String indexFile = "index.html";

	/**
	 * Private constructor prevents insanitation.
	 */
	private UrlUtility() {
	}

	/**
	 * Strip the query string from a URL.
	 *
	 * @param url    The URL to examine.
	 * @return The URL with no query string.
	 * @exception MalformedURLException
	 */
	static public URL stripQuery(URL url) throws MalformedURLException {
		String file = url.getFile();
		int i = file.indexOf("?");
		if (i == -1)
			return url;
		file = file.substring(0, i);
		return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
	}

	/**
	* Strip the "#" from a URL.
	*
	* @param url   The URL to examine.
	* @return The URL with no query string.
	* @exception MalformedURLException
	*/
	static public URL stripSharp(URL url) throws MalformedURLException {
		String file = url.getFile();
		int i = file.indexOf("#");
		if (i == -1)
			return url;
		file = file.substring(0, i);
		logger.debug("# removed from:" + file);
		return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
	}

	/**
	 * Strip the anchor tag from the URL.
	 *
	 * @param url    The URL to scan.
	 * @return The URL with no anchor tag.
	 * @exception MalformedURLException
	 */
	static public URL stripAnhcor(URL url) throws MalformedURLException {
		String file = url.getFile();
		return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
	}

	/**
	 * Encodes a string in base64.
	 *
	 * @param s      The string to encode.
	 * @return The encoded string.
	 */
/*	static public String base64Encode(String s) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		Base64OutputStream out = new Base64OutputStream(bout);
		try {
			out.write(s.getBytes());
			out.flush();
		} catch (IOException e) {
		}

		return bout.toString();
	}
*/
	/**
	 * Resolve the base of a URL.
	 *
	 * @param base   The base.
	 * @param rel    The relative path.
	 * @return The combined absolute URL.
	 */
	static public String resolveBase(String base, String rel) {
		String protocol;
		int i = base.indexOf(':');
		if (i != -1) {
			protocol = base.substring(0, i + 1);
			base = "http:" + base.substring(i + 1);
		} else
			protocol = null;

		URL url;

		try {
			url = new URL(new URL(base), rel);
		} catch (MalformedURLException e) {
			return "";
		}

		if (protocol != null) {
			base = url.toString();
			i = base.indexOf(':');
			if (i != -1)
				base = base.substring(i + 1);
			base = protocol + base;
			return base;
		} else
			return url.toString();
	}

	public static String convertFilename(String base, String path) {
		return convertFilename(base, path, true);
	}

	/**
	 * Convert a filename for local storage. Also create the directory
	 * tree.
	 *
	 * @param base   The local path that forms the base of the downloaded web tree.
	 * @param path    The URL path.
	 * @return The resulting local path to store to.
	 */
	public static String convertFilename(String base, String path, boolean mkdir) {
		String result = base;
		int index1;
		int index2;

		// add ending slash if needed
		if (result.charAt(result.length() - 1) != File.separatorChar)
			result = result + File.separator;

		// strip the query if needed

		int queryIndex = path.indexOf("?");
		if (queryIndex != -1)
			path = path.substring(0, queryIndex);

		// see if an ending / is missing from a directory only

		int lastSlash = path.lastIndexOf(File.separatorChar);
		int lastDot = path.lastIndexOf('.');

		if (path.charAt(path.length() - 1) != '/') {
			if (lastSlash > lastDot)
				path += "/" + indexFile;
		}

		// determine actual filename
		lastSlash = path.lastIndexOf('/');

		String filename = "";
		if (lastSlash != -1) {
			filename = path.substring(1 + lastSlash);
			path = path.substring(0, 1 + lastSlash);

			if (filename.equals(""))
				filename = indexFile;
		}
		// create the directory structure, if needed

		index1 = 1;
		do {
			index2 = path.indexOf('/', index1);

			if (index2 != -1) {
				String dirpart = path.substring(index1, index2);
				result += dirpart;
				result += File.separator;

				if (mkdir) {
					File f = new File(result);
					f.mkdir();
				}

				index1 = index2 + 1;

			}
		} while (index2 != -1);

		// attach name
		result += filename;

		return result;
	}
}
