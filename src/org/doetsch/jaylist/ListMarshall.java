package org.doetsch.jaylist;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class ListMarshall {
	
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
	
	
	
	
	ListModel unmarshall  (URL src) throws IOException, SAXException, ParserConfigurationException  {
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
				ListMarshall.class.getResource("xml/list.xsd").openStream());
		
		docBuilder = docBuilderFactory.newDocumentBuilder();
		docBuilder.setErrorHandler(new ErrorHandlerAdapter());
		doc = docBuilder.parse(src.openStream());
		doc.normalize();
		ListModel newListModel = parse(doc);
		newListModel.setPath(src);
		return newListModel;
	}
	
	private ListModel parse (Node node) {
		ListModel list = new ListModel();
		Node child;
		NodeList grandchildren;
		NamedNodeMap attributes;
		String title = "";
		String desc = "";
		Dimension frameSize = new Dimension(200, 400); //default dimensions if frame config unspecified
		int flag = 0;
		boolean expanded = false;
		
		
		//get the root node (<list>)
		NodeList children = node.getChildNodes();
		node = children.item(0);
		
		//get the root's child notes (<item>s)
		children = node.getChildNodes();
		
		
		for (int i = 0; i < children.getLength(); i++ ) {
			if (children.item(i).getNodeName().equals("header")) {
				//System.out.println("header found");
				list.setHeader(children.item(i).getTextContent());
			}
			
			if (children.item(i).getNodeName().equals("config")) {
				child = children.item(i);
				grandchildren = child.getChildNodes();
				
				for (int j = 0; j < grandchildren.getLength(); j++) {
					if (grandchildren.item(j).getNodeName().equals("frame")) {
						attributes = grandchildren.item(j).getAttributes();
						int width = Integer.valueOf(
								attributes.getNamedItem("width").getNodeValue());
						int height = Integer.valueOf(
								attributes.getNamedItem("height").getNodeValue());
						frameSize = new Dimension(width, height);
					}
				}
			}
				
			
			if (children.item(i).getNodeName().equals("item")) {
				child = children.item(i);
				grandchildren = child.getChildNodes();
				
				
				for (int j = 0; j < grandchildren.getLength(); j++) {
					if (grandchildren.item(j).getNodeName().equals("title")) {
						title = grandchildren.item(j).getTextContent();
					}
					if (grandchildren.item(j).getNodeName().equals("desc")) {
						desc = grandchildren.item(j).getTextContent();
					}
					if (grandchildren.item(j).getNodeName().equals("flag")) {
						flag = Integer.valueOf(grandchildren.item(j).getTextContent());
					}
					if (grandchildren.item(j).getNodeName().equals("expanded")) {
						expanded = Boolean.valueOf(grandchildren.item(j).getTextContent());
					}
					
				}

				
				
				
				list.addItemModels(new ItemModel(title, desc, flag, expanded));
				
			}
		}
		
		list.setFrameSize(frameSize);
		
		return list;
	}
	
	void marshall (ListModel listModel, URL dest) {
		DocumentBuilderFactory docBuilderFactory;
		DocumentBuilder docBuilder;
		Document document;
		Element rootElement;
		Transformer transformer;
		Element configElement;
		Element frameElement;
		
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			return;
		}
		
		document = docBuilder.newDocument();
		rootElement = document.createElement("list");
		
		//set the header
		Element headerElement = document.createElement("header");
		headerElement.setTextContent(listModel.getHeader());
		rootElement.appendChild(headerElement);
		
		//add config details
		configElement = document.createElement("config");
		frameElement = document.createElement("frame");
		frameElement.setAttribute("width", String.valueOf(
				listModel.getFrameSize().width));
		frameElement.setAttribute("height", String.valueOf(
				listModel.getFrameSize().height));
		configElement.appendChild(frameElement);
		rootElement.appendChild(configElement);
		
		
		
		for (ItemModel itemModel : listModel.getItemModels()) {
			Element itemElement = document.createElement("item");
			Element titleElement = document.createElement("title");
			Element descElement = document.createElement("desc");
			Element flagElement = document.createElement("flag");
			Element expandedElement = document.createElement("expanded");
			
			titleElement.setTextContent(itemModel.title);
			descElement.setTextContent(itemModel.desc);
			flagElement.setTextContent(String.valueOf(itemModel.flag));
			expandedElement.setTextContent(String.valueOf(itemModel.expanded));
			
			itemElement.appendChild(titleElement);
			itemElement.appendChild(descElement);
			itemElement.appendChild(flagElement);
			itemElement.appendChild(expandedElement);
			
			rootElement.appendChild(itemElement);		
		}
		
		
		
		
		
		document.appendChild(rootElement);
		
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			try {
				transformer.transform(new DOMSource(document),
						new StreamResult(new File(dest.toURI())));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
				
	}

	
	
}
