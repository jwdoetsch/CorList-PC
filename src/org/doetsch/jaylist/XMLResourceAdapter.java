package org.doetsch.jaylist;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XMLResourceAdapter is a helper class that provides
 * a straightforward, albeit limited, XML resource
 * handler.
 *  
 * @author Jacob Wesley Doetsch
 *
 */
public class XMLResourceAdapter {

	/*
	 * ErrorHandlerAdapter forwards the associated exceptions
	 * to within the scope of the parent DocumentBuilder.
	 */
	private class ErrorHandlerAdapter implements ErrorHandler {

		@Override
		public void error(SAXParseException exception) throws SAXException {
			throw new SAXException(exception);
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			throw new SAXException(exception);
		}

		@Override
		public void warning(SAXParseException exception) throws SAXException {
			throw new SAXException(exception);
		}
		
	}
	
	/**
	 * Returns the head element of the XML resource
	 * at the specified URL.
	 * 
	 * [Document (implements Node)]
	 * 
	 * 
	 * @param resourcePath
	 * @return the element, as a Document (Node)
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public Document getRootNode (URL resourcePath, URL schemaPath) 
			throws IOException, SAXException, ParserConfigurationException  {
			Document doc;
			DocumentBuilder docBuilder;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			
			//configure the factory for validation against the supertodo schema
			docBuilderFactory.setNamespaceAware(true);
			docBuilderFactory.setValidating(true);
			docBuilderFactory.setAttribute(
	                "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
	                "http://www.w3.org/2001/XMLSchema");
			docBuilderFactory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					schemaPath.openStream());
			
			docBuilder = docBuilderFactory.newDocumentBuilder();
			docBuilder.setErrorHandler(new ErrorHandlerAdapter());
			doc = docBuilder.parse(resourcePath.openStream());
			doc.normalize();
			return doc;
	}
	
	public boolean exportDocument (Document doc, URL destination) {
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			try {
				transformer.transform(new DOMSource(doc),
						new StreamResult(new File(destination.toURI())));
				return true;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			return false;
		} catch (TransformerFactoryConfigurationError |
				TransformerException e) {
			e.printStackTrace();
			return false;

		}
	}
}
